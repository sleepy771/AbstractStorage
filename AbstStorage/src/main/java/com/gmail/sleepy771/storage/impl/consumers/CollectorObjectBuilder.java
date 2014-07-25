package com.gmail.sleepy771.storage.impl.consumers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.exceptions.NotBuildableException;
import com.gmail.sleepy771.storage.interfaces.Buildable;
import com.gmail.sleepy771.storage.interfaces.consumers.Collector;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;

public class CollectorObjectBuilder<T> implements Buildable<T>,
		StorageListener<Exception> {

	private Collector<?> collector;
	private boolean canntComplete;
	private final boolean needAll;
	private LinkedList<Exception> dueException;
	private Lock collectorObjectBuilderLock;
	private Condition isComplete;
	private Buildable<T> builder;

	public CollectorObjectBuilder(Collector<?> collector, Buildable<T> builder,
			boolean needAll) {
		this.collector = collector;
		this.collector.getExceptionHandler().addListener(this);
		canntComplete = false;
		collectorObjectBuilderLock = new ReentrantLock();
		isComplete = collectorObjectBuilderLock.newCondition();
		this.needAll = needAll;
	}

	public boolean isComplete() {
		return collector.isEmpty();
	}

	@Override
	public T build() throws NotBuildableException {
		collectorObjectBuilderLock.lock();
		try {
			if (this.needAll) {
				while (!isComplete() && !this.canntComplete)
					isComplete.await();
				if (this.canntComplete)
					throw new NotBuildableException(); // TODO due Exception
				return this.builder.build();
			} else {
				while (!isComplete())
					isComplete.await();
				return this.builder.build();
			}
		} catch (InterruptedException ie) {
			// TODO dueException
			throw new NotBuildableException();
		} finally {
			collectorObjectBuilderLock.unlock();
		}
	}

	public List<Exception> listExceptions() throws InterruptedException {
		collectorObjectBuilderLock.lock();
		try {
			while (!isComplete() && !(needAll && this.canntComplete))
				isComplete.await();
			return new ArrayList<Exception>(this.dueException);
		} finally {
			collectorObjectBuilderLock.unlock();
		}
	}

	public void dipose() {
		this.collector.close();

	}

	@Override
	public void onNotificationPerform(StorageObservable<Exception> obs,
			Exception exception) {
		collectorObjectBuilderLock.lock();
		try {
			this.canntComplete = true;
			this.dueException.add(exception);
			this.isComplete.signal();
		} finally {
			collectorObjectBuilderLock.unlock();
		}
	}

}
