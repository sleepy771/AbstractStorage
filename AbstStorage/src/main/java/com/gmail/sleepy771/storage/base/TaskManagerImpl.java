package com.gmail.sleepy771.storage.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.datastructures.Heap;
import com.gmail.sleepy771.storage.exceptions.HeapException;

public class TaskManagerImpl implements TaskManager, Runnable {
	
	private final Heap<Task> taskHeap;
	private final Map<Task, ResponseListener> responseMap;
	private final Lock tmLock;
	private final Condition isProcessed;
	private final Condition poolFull;
	private Thread taskManagerThread;
	private ExecutorService threadPool;
	private int maxTasksInPool;
	private int tasksInPool;
	private boolean shutdownSignal;
	private boolean updateSignal;
	
	public TaskManagerImpl(Heap<Task> taskHeap, Map<Task, ResponseListener> responseMap) {
		this.taskHeap = taskHeap;
		this.responseMap = new HashMap<>();
		this.tmLock = new ReentrantLock();
		this.isProcessed = this.tmLock.newCondition();
		this.poolFull = tmLock.newCondition();
		threadPool = Executors.newCachedThreadPool();
		maxTasksInPool = 16;
		tasksInPool = 0;
	}

	@Override
	public void updateHeap() {
		tmLock.lock();
		try {
			updateSignal = true;
		} finally {
			tmLock.unlock();
		}
	}

	@Override
	public void registrateTask(Task t, ResponseListener l) {
		tmLock.lock();
		try {
			taskHeap.push(t);
			responseMap.put(t, l);
		} catch(HeapException h) {
			h.printStackTrace();
			// TODO send to exception handler
		} finally {
			tmLock.unlock();
		}
	}

	@Override
	public void removeTask(Task t) {
		tmLock.lock();
		try {
			taskHeap.remove(t);
			responseMap.remove(t);
		} finally {
			tmLock.unlock();
		}
	}
	
	private void provideResponse(Object o, Task t) {
		tmLock.lock();
		try {
			//TODO create Response and send it
			poolFull.signal();
			isProcessed.signal();
			tasksInPool--;
		} finally {
			tmLock.unlock();
		}
	}
	
	private void sendToPool(Task t) {
		tmLock.lock();
		try {
			threadPool.execute(constructRunnableFromTask(t));
			tasksInPool++;
		} finally {
			tmLock.unlock();
		}
	}

	@Override
	public void run() {
		while(!shutdownSignal) {
			try {
				if (tasksInPool == maxTasksInPool)
					poolFull.await();
				sendToPool(taskHeap.pull());
			} catch (HeapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	
	private Runnable constructRunnableFromTask(final Task t) {
		return new Runnable() {
			@Override
			public void run() {
				provideResponse(t.taskExecutable().execute(), t);
			}
		};
	}
	
	private Thread getManagerThread() {
		if (this.taskManagerThread == null) {
			this.taskManagerThread = new Thread(this);
		}
		return this.taskManagerThread;
	}

	@Override
	public void init() {
		getManagerThread().start();
	}

	@Override
	public void close() {
		// TODO create shutdown
	}
	
}
