package com.gmail.sleepy771.storage.impl.consumers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.impl.DupletImpl;
import com.gmail.sleepy771.storage.interfaces.consumers.Collector;
import com.gmail.sleepy771.storage.interfaces.consumers.Operation;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;
import com.gmail.sleepy771.storage.interfaces.datastructures.Duplet;

public class CollectorImpl<T> implements Collector<T>, Runnable {

    private Operation<Duplet<String, T>> operation;
    private LinkedList<Duplet<String, Future<T>>> awaitingFutureObjects = new LinkedList<Duplet<String, Future<T>>>();
    private final Lock collectorLock = new ReentrantLock();
    private final Condition isEmpty = collectorLock.newCondition();
    private final Condition processed = collectorLock.newCondition();
    private Thread collectorThread = new Thread(this);
    private boolean running = false;
    private StorageObservable<Exception> exceptionObservable;

    public CollectorImpl(Operation<Duplet<String, T>> o, StorageObservable<Exception> eo) {
	this.operation = o;
	this.exceptionObservable = eo;
    }

    @Override
    public final void addFutureObject(String name, Future<T> f) {
	collectorLock.lock();
	try {
	    this.awaitingFutureObjects.add(new DupletImpl<>(name, f));
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void addAllFutureObjects(Map<String, Future<T>> c) {
	collectorLock.lock();
	try {
	    for (Entry<String, Future<T>> entry : c.entrySet()) {
		this.awaitingFutureObjects.add(new DupletImpl<>(entry));
	    }
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void removeFutureObject(String name) {
	collectorLock.lock();
	try {
	    for (Iterator<Duplet<String, Future<T>>> iter = this.awaitingFutureObjects
		    .iterator(); iter.hasNext();) {
		Duplet<String, Future<T>> dup = iter.next();
		if (dup.getFirst().equals(name))
		    iter.remove();
	    }
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void removeAllFutureObjects(Collection<String> c) {
	collectorLock.lock();
	try {
	    for (String name : c)
		removeFutureObject(name);
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void collect() {
	collectorLock.lock();
	try {
	    if (!running) {
		running = true;
		this.collectorThread.start();
	    }
	} finally {
	    collectorLock.unlock();
	}
    }
    
    @Override
    public final List<Duplet<String, Future<T>>> close() {
	collectorLock.lock();
	try {
	    this.isEmpty.signal();
	    this.processed.signal();
	    running = false;
	    return new ArrayList<>(this.awaitingFutureObjects);
	} finally {
	    this.operation = null;
	    this.awaitingFutureObjects.clear();
	    this.awaitingFutureObjects = null;
	    this.exceptionObservable.dispose();
	    this.exceptionObservable = null;
	    try {
		this.collectorThread.join();
		this.collectorThread = null;
		collectorLock.unlock();
	    } catch (InterruptedException ie) {
		ie.printStackTrace(); // TODO Log
		this.collectorThread.interrupt();
		this.collectorThread = null;
		collectorLock.unlock();
	    }
	}
    }

    @Override
    public final void setOnRecieveOperation(Operation<Duplet<String, T>> o) {
	collectorLock.lock();
	try {
	    this.operation = o;
	} finally {
	    collectorLock.unlock();
	}
    }

    public final Operation<Duplet<String, T>> getOnRecieveOperation() {
	collectorLock.lock();
	try {
	    return this.operation;
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void run() {
	collectorLock.lock();
	try {
	    while (running) {
		if (this.awaitingFutureObjects.isEmpty()) {
		    isEmpty.await();
		}
		processed.await();
		List<Duplet<String, Future<T>>> toRemove = new LinkedList<>();
		for (Duplet<String, Future<T>> dup : this.awaitingFutureObjects) {
		    try {
			if (dup.getSecond().isDone()) {
			    this.operation.excute(new DupletImpl<String, T>(dup
				    .getFirst(), dup.getSecond().get()));
			    toRemove.add(dup);
			}
		    } catch (ExecutionException e) {
			exceptionObservable.notifyListeners(e);
		    }
		}
		this.awaitingFutureObjects.removeAll(toRemove);
	    }
	} catch (InterruptedException ie) {
	    // TODO Just log this exception
	    ie.printStackTrace();
	} finally {
	    collectorLock.unlock();
	}
    }

    public void addExceptionListener(StorageListener<Exception> listener) {
	this.exceptionObservable.addListener(listener);
    }

    public void addAllExceptionListeners(
	    Collection<StorageListener<Exception>> listeners) {
	this.exceptionObservable.addAllListeners(listeners);
    }

    public void removeExceptionListener(StorageListener<Exception> listener) {
	this.exceptionObservable.removeListener(listener);
    }

    public void removeAllExceptionListenres(
	    Collection<StorageListener<Exception>> listeners) {
	this.exceptionObservable.removeAllListenres(listeners);
    }

    public int countListeners() {
	return this.exceptionObservable.countListeners();
    }

    public StorageObservable<Exception> getObservale() {
	return this.exceptionObservable;
    }

    @Override
    public final void onNotificationPerform(StorageObservable<Void> obs, Void object) {
	collectorLock.lock();
	try {
	    processed.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public boolean isEmpty() {
	collectorLock.lock();
	try {
	    return this.awaitingFutureObjects.isEmpty();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void setExceptionHandler(StorageObservable<Exception> handler) {
	collectorLock.lock();
	try {
	    if (handler != null) {
		this.exceptionObservable = handler;
	    }
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public StorageObservable<Exception> getExceptionHandler() {
	collectorLock.lock();
	try {
	    return this.exceptionObservable;
	} finally {
	    collectorLock.unlock();
	}
    }

}
