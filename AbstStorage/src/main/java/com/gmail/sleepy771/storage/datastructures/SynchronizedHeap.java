package com.gmail.sleepy771.storage.datastructures;

import java.util.Comparator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage.exceptions.HeapException;

public class SynchronizedHeap<T extends Comparable<T>> implements Heap<T> {
	
	private final Heap<T> innerHeap;
	private final Lock heapLock;
	private final Condition isFull;
	private final Condition isEmpty;
	
	public SynchronizedHeap(Heap<T> innerHeap, Comparator<T> comp) {
		this.innerHeap = innerHeap;
		this.setComparator(comp);
		heapLock = new ReentrantLock();
		isEmpty = heapLock.newCondition();
		if (!innerHeap.isResizable()) {
			isFull = heapLock.newCondition();
		} else {
			isFull = null;
		}
	}
	
	public SynchronizedHeap(Heap<T> innerHeap) {
		this(innerHeap, null);
	}

	@Override
	public void push(T element) throws HeapException{
		heapLock.lock();
		try {
			while (innerHeap.isResizable() && innerHeap.capacity() == innerHeap.size()) {
				isFull.await();
			}
			innerHeap.push(element);
			isEmpty.signal();
		} catch(InterruptedException ie){
			throw new HeapException(ie);
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public T pull() throws HeapException {
		heapLock.lock();
		try {
			while(innerHeap.isEmpty()) {
				isEmpty.await();
			}
			return innerHeap.pull();
		} catch(InterruptedException ie) {
			throw new HeapException(ie);
		} finally {
			isFull.signal();
			heapLock.unlock();
		}
	}

	@Override
	public T pick() throws HeapException {
		heapLock.lock();
		try {
			while(innerHeap.isEmpty()) {
				isEmpty.await();
			}
			return innerHeap.pick();
		} catch(InterruptedException ie) {
			throw new HeapException(ie);
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public boolean remove(T element) {
		heapLock.lock();
		try {
			return innerHeap.remove(element);
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public int size() {
		heapLock.lock();
		try {
			return innerHeap.size();
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public int capacity() {
		heapLock.lock();
		try {
			return innerHeap.capacity();
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public void forceHeapRebuild() {
		heapLock.lock();
		try {
			innerHeap.forceHeapRebuild();
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public void setComparator(Comparator<T> comp) {
		heapLock.lock();
		try {
			this.innerHeap.setComparator(comp);
			this.innerHeap.forceHeapRebuild();
		} finally {
			heapLock.unlock();
		}
	}

	@Override
	public Comparator<T> getComparator() {
		return this.innerHeap.getComparator();
	}

	@Override
	public boolean isResizable() {
		return innerHeap.isResizable();
	}

	@Override
	public boolean isEmpty() {
		heapLock.lock();
		try {
			return innerHeap.isEmpty();
		} finally {
			heapLock.unlock();
		}
	}

}
