package com.gmail.sleepy771.prod_cons;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskHeap {
    private final Map<Future<?>, Callable<?>> futureTasks;
    private final Condition isEmpty;
    private final ExecutorService service;
    private final Lock taskLock;

    public TaskHeap(final ExecutorService srv) {
	service = srv;
	taskLock = new ReentrantLock();
	isEmpty = taskLock.newCondition();
	futureTasks = new HashMap<>();
    }

}
