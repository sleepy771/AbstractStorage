package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.exceptions.ElementsNotFullyCollectedException;
import com.gmail.sleepy771.storage.interfaces.consumers.Collector;

public abstract class ExceptionCollector implements Collector {
    private final Map<String, Future<Object>> futureObjects;
    private final Lock exceptionCollectorLock;
    private final Condition isEmpty;
    private boolean upNRunning = false;

    public ExceptionCollector() {
	this.futureObjects = new TreeMap<>();
	this.exceptionCollectorLock = new ReentrantLock();
	this.isEmpty = this.exceptionCollectorLock.newCondition();
    }

    @Override
    public final void addFutureObject(String name, Future<Object> f) {
	this.exceptionCollectorLock.lock();
	try {
	    this.futureObjects.put(name, f);
	    this.isEmpty.signal();
	} finally {
	    this.exceptionCollectorLock.unlock();
	}
    }

    @Override
    public final void addAllFutureObjects(Map<String, Future<Object>> c) {
	this.exceptionCollectorLock.lock();
	try {
	    this.futureObjects.putAll(c);
	    this.isEmpty.signal();
	} finally {
	    this.exceptionCollectorLock.unlock();
	}
    }

    @Override
    public final void removeFutureObject(String name) {
	throw new UnsupportedOperationException();
    }

    @Override
    public final void removeAllFutureObjects(Collection<String> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void collect() {
	upNRunning = true;
	Runnable collector = new Runnable() {

	    @Override
	    public void run() {
		try {
		    while (upNRunning) {
			if (ExceptionCollector.this.futureObjects.isEmpty()) {
			    ExceptionCollector.this.isEmpty.await();
			}
			for (Entry<String, Future<Object>> entry : ExceptionCollector.this.futureObjects
				.entrySet()) {
			    if (entry.getValue().isDone()) {
				try {
				    entry.getValue().get();
				} catch (InterruptedException
					| ExecutionException e) {
				    handleExceptions(e);
				}
			    }
			}
		    }
		} catch (InterruptedException ie) {
		    handleExceptions(ie);
		}
	    }

	};
    }

    public abstract void handleExceptions(Exception e);

}
