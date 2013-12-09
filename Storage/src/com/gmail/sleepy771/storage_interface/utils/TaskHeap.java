package com.gmail.sleepy771.storage_interface.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TaskHeap implements Runnable {

    private final Map<Future<?>, Callable<?>> futureTasks;
    private final Condition isEmpty;
    private final ExecutorService service;
    private final Lock taskLock;

    public TaskHeap(final ExecutorService srv) {
	service = srv;
	taskLock = new ReentrantLock();
	isEmpty = taskLock.newCondition();
	futureTasks = new HashMap<>();
	Thread t = new Thread(this);
	t.start();
    }

    public void push(final Future<?> fut, final Callable<?> task) {
	taskLock.lock();
	try {
	    futureTasks.put(fut, task);
	    System.out.println("Task put in map");
	    isEmpty.signal();
	} finally {
	    taskLock.unlock();
	}

    }

    @Override
    public void run() {
	while (!service.isShutdown()) {
	    taskLock.lock();
	    try {
		if (futureTasks.isEmpty()) {
		    isEmpty.await();
		}
		for (Future<?> f : futureTasks.keySet()) {
		    if (f.isDone()) {
			System.out.println("Task done!");
			service.submit(futureTasks.get(f));
			futureTasks.remove(f);
		    }
		}
		service.awaitTermination(1, TimeUnit.SECONDS);

	    } catch (InterruptedException ie) {
		ie.printStackTrace();
	    } finally {
		taskLock.unlock();
	    }
	}
    }

}
