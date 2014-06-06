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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.impl.AtomicImpl;
import com.gmail.sleepy771.storage.interfaces.consumers.Collector;
import com.gmail.sleepy771.storage.interfaces.consumers.Operation;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;
import com.gmail.sleepy771.storage.interfaces.datastructures.Atomic;

public class CollectorImpl<T> implements Collector<T>, Runnable {

    private Operation<Atomic<String, T>> defaultOperation;
    private Map<String, Operation<Atomic<String, T>>> futureOperations;
    private LinkedList<Atomic<String, Future<T>>> awaitingFutureObjects = new LinkedList<Atomic<String, Future<T>>>();
    private final Lock collectorLock = new ReentrantLock();
    private final Condition isEmpty = collectorLock.newCondition();
    private final Condition isProcessed = collectorLock.newCondition();
    private final Condition isCollecting = collectorLock.newCondition();
    private Thread collectorThread = new Thread(this);
    private Thread silencerThread = null;
    private final Runnable silencer = new Runnable() {

	    @Override
	    public void run() {
		silence(false);
	    }

	};
    private boolean running = false;
    private StorageObservable<Exception> exceptionObservable;
    private boolean autosilence = false;

    public CollectorImpl(Operation<Atomic<String, T>> o,
	    StorageObservable<Exception> eo) {
	this.defaultOperation = o;
	this.exceptionObservable = eo;
    }

    @Override
    public final void addFutureObject(String name, Future<T> f) {
	collectorLock.lock();
	try {
	    this.awaitingFutureObjects.add(new AtomicImpl<>(name, f));
	    startCollecting();
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
		this.awaitingFutureObjects.add(new AtomicImpl<>(entry));
	    }
	    startCollecting();
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void removeFutureObject(String name) {
	collectorLock.lock();
	try {
	    for (Iterator<Atomic<String, Future<T>>> iter = this.awaitingFutureObjects
		    .iterator(); iter.hasNext();) {
		Atomic<String, Future<T>> dup = iter.next();
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
	    startCollecting();
	} finally {
	    collectorLock.unlock();
	}
    }

    private void startCollecting() {
	if (!running) {
	    running = true;
	    getCollectorThread().start();
	}
    }

    private Thread getCollectorThread() {
	if (collectorThread == null)
	    collectorThread = new Thread(this);
	return collectorThread;
    }

    public final List<Atomic<String, Future<T>>> silence(boolean forced) {
	collectorLock.lock();
	if (!running)
	    return new ArrayList<>(1);
	try {
	    if (!forced || isEmpty())
		isCollecting.await();
	    running = false;
	    isEmpty.signal();
	    isProcessed.signal();
	    return new ArrayList<>(this.awaitingFutureObjects);
	} catch (InterruptedException ie) {
	    ie.printStackTrace(); // TODO log it
	    collectorThread.interrupt();
	    running = false;
	    return new ArrayList<>(this.awaitingFutureObjects);
	} finally {
	    this.awaitingFutureObjects.clear();
	    this.futureOperations.clear();
	    try {
		this.collectorThread.join();
	    } catch (InterruptedException ie) {
		ie.printStackTrace(); // TODO Log
		this.collectorThread.interrupt();
	    }
	    this.collectorThread = null;
	    collectorLock.unlock();
	}
    }
    
    public void silenceAfterTask() {
	collectorLock.lock();
	try {
	    runSilencerThread();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final List<Atomic<String, Future<T>>> close() {
	collectorLock.lock();
	try {
	    running = false;
	    this.isEmpty.signal();
	    this.isProcessed.signal();
	    return new ArrayList<>(this.awaitingFutureObjects);
	} finally {
	    this.defaultOperation = null;
	    this.awaitingFutureObjects.clear();
	    this.awaitingFutureObjects = null;
	    this.exceptionObservable.dispose();
	    this.exceptionObservable = null;
	    try {
		if (collectorThread != null && collectorThread.isAlive())
		    this.collectorThread.join();
		if (silencerThread != null && silencerThread.isAlive())
		    this.silencerThread.join();
	    } catch (InterruptedException ie) {
		ie.printStackTrace(); // TODO Log
		this.collectorThread.interrupt();
		this.silencerThread.interrupt();
	    }
	    this.collectorThread = null;
	    this.silencerThread = null;
	    collectorLock.unlock();
	}
    }

    @Override
    public final void setOnRecieveOperation(Operation<Atomic<String, T>> o) {
	collectorLock.lock();
	try {
	    this.defaultOperation = o;
	} finally {
	    collectorLock.unlock();
	}
    }

    public final Operation<Atomic<String, T>> getOnRecieveOperation() {
	collectorLock.lock();
	try {
	    return this.defaultOperation;
	} finally {
	    collectorLock.unlock();
	}
    }

    private void runSilencerThread() {
	if(silencerThread == null || !silencerThread.isAlive()) {
	    silencerThread = new Thread(silencer);
	    silencerThread.start();
	}
    }

    @Override
    public final void run() {
	collectorLock.lock();
	try {
	    while (running) {
		if (this.awaitingFutureObjects.isEmpty()) {
		    isCollecting.signal();
		    if (autosilence) {
			isEmpty.await(4, TimeUnit.SECONDS);
			if (this.awaitingFutureObjects.isEmpty()) {
			    runSilencerThread();
			}
		    } else {
			isEmpty.await();
		    }
		}
		isProcessed.await();
		List<Atomic<String, Future<T>>> toRemove = new LinkedList<>();
		for (Atomic<String, Future<T>> dup : this.awaitingFutureObjects) {
		    try {
			if (dup.getSecond().isDone()) {
			    if (futureOperations.containsKey(dup.getFirst())) {
				futureOperations.get(dup.getFirst()).excute(
					new AtomicImpl<>(dup.getFirst(), dup
						.getSecond().get()));
				futureOperations.remove(dup.getFirst());
			    } else {
				this.defaultOperation
					.excute(new AtomicImpl<String, T>(dup
						.getFirst(), dup.getSecond()
						.get()));
			    }
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

    public StorageObservable<Exception> getObservale() {
	collectorLock.lock();
	try {
	    return this.exceptionObservable;
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public final void onNotificationPerform(StorageObservable<Void> obs,
	    Void object) {
	collectorLock.lock();
	try {
	    isProcessed.signal();
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

    @Override
    public void addOperationForFuture(String name,
	    Operation<Atomic<String, T>> o) {
	collectorLock.lock();
	try {
	    this.futureOperations.put(name, o);
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void removeOperationFromFuture(String name) {
	collectorLock.lock();
	try {
	    this.futureOperations.remove(name);
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void addFutureWithOperation(String name, Future<T> f,
	    Operation<Atomic<String, T>> o) {
	collectorLock.lock();
	try {
	    awaitingFutureObjects.add(new AtomicImpl<>(name, f));
	    futureOperations.put(name, o);
	    startCollecting();
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void addAllFutureObjectsWithOperations(
	    Map<String, Atomic<Future<T>, Operation<Atomic<String, T>>>> map) {
	collectorLock.lock();
	try {
	    for (Entry<String, Atomic<Future<T>, Operation<Atomic<String, T>>>> entry : map
		    .entrySet()) {
		awaitingFutureObjects.add(new AtomicImpl<>(entry.getKey(),
			entry.getValue().getFirst()));
		futureOperations.put(entry.getKey(), entry.getValue()
			.getSecond());
	    }
	    startCollecting();
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void removeOperations(Collection<String> names) {
	collectorLock.lock();
	try {
	    for (String futureName : names)
		futureOperations.remove(futureName);
	} finally {
	    collectorLock.unlock();
	}
    }

    public void setAutoSilence(boolean autosilence) {
	collectorLock.lock();
	try {
	    this.autosilence = autosilence;
	} finally {
	    collectorLock.unlock();
	}
    }

    public boolean getAutoSilence() {
	collectorLock.lock();
	try {
	    return this.autosilence;
	} finally {
	    collectorLock.unlock();
	}
    }

    @Override
    public void addAllFutureObjectsWithOperation(Map<String, Future<T>> m,
	    Operation<Atomic<String, T>> o) {
	collectorLock.lock();
	try {
	    for(Entry<String, Future<T>> entry : m.entrySet()) {
		awaitingFutureObjects.add(new AtomicImpl<>(entry));
		futureOperations.put(entry.getKey(), o);
	    }
	    startCollecting();
	    isEmpty.signal();
	} finally {
	    collectorLock.unlock();
	}
    }

}
