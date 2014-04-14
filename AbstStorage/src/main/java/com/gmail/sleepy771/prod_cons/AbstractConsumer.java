package com.gmail.sleepy771.prod_cons;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConsumer<T> implements Consumer<T>, Runnable {
    private Lock lock;
    private Condition notRecieved;
    private T obj = null;

    public AbstractConsumer() {
	lock = new ReentrantLock();
	notRecieved = lock.newCondition();

    }

    @Override
    public final void injectObject(final T obj) {
	lock.lock();
	try {
	    this.obj = obj;
	    notRecieved.signal();
	} finally {
	    lock.unlock();
	}
    }

    @Override
    public final void run() {
	lock.lock();
	try {
	    notRecieved.await();
	    onRecieve(this.obj);
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	} finally {
	    lock.unlock();
	}
	notRecieved = null;
	lock = null;
    }
}
