package com.gmail.sleepy771.storage_interface.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gmail.sleepy771.storage_interface.misc.Result;

public class ResultQueue implements Queue<Future<Result<?>>> {
    private LinkedList<Future<Result<?>>> futureList = new LinkedList<Future<Result<?>>>();
    private final Lock resLock;

    public ResultQueue() {
	resLock = new ReentrantLock();

    }

    @Override
    public int size() {
	try {
	    resLock.lock();
	    return futureList.size();
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean isEmpty() {
	try {
	    resLock.lock();
	    return futureList.isEmpty();

	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean contains(Object o) {
	try {
	    resLock.lock();
	    boolean contains = false;
	    for (Future<Result<?>> future : futureList) {
		if (contains)
		    break;
		if (o.getClass().equals(Future.class)) {
		    contains |= o.equals(future);
		} else if (o.getClass().equals(Result.class) && future.isDone()) {
		    try {
			contains |= o.equals(future.get());
		    } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		    }
		}
	    }
	    return contains;
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    // not thread safe
    public Iterator<Future<Result<?>>> iterator() {
	throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
	try{
	    resLock.lock();
	    return futureList.toArray();
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public <T> T[] toArray(T[] a) {
	try{
	    resLock.lock();
	    return futureList.<T> toArray(a);
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean remove(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
	try {
	    boolean containsAll = true;
	    resLock.lock();
	    for (Object o : c) {
		containsAll &= contains(o);
	    }
	    return containsAll;
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean addAll(Collection<? extends Future<Result<?>>> c) {
	try{
	    resLock.lock();
	    return futureList.addAll(c);
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean removeAll(Collection<?> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
	try{
	    resLock.lock();
	    futureList.clear();
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean add(Future<Result<?>> e) {
	try {
	    resLock.lock();
	    return futureList.add(e);
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public boolean offer(Future<Result<?>> e) {
	return add(e);
    }

    @Override
    public Future<Result<?>> remove() {
	try{
	    resLock.lock();
	    Future<Result<?>> res = futureList.remove();
	    int count = 1;
	    while(!(res.isDone() || count <= size())) {
		add(res);
		res = futureList.remove();
		count++;
	    }
	    if(!res.isDone()){
		add(res);
		throw new NoSuchElementException();
	    }
	    return res;
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public Future<Result<?>> poll() {
	try{
	    resLock.lock();
	    Future<Result<?>> res = futureList.poll();
	    int count = 1;
	    while(!(res==null || res.isDone() || count <= size())) {
		add(res);
		res = futureList.poll();
		count++;
	    }
	    if(!res.isDone()){
		add(res);
		return null;
	    }
	    return res;
	} finally {
	    resLock.unlock();
	}
    }

    @Override
    public Future<Result<?>> element() {
	try{
	    resLock.lock();
	    Future<Result<?>> res = futureList.element();
	    int count = 1;
	    while(!(res.isDone() || count <= size())) {
		add(res);
		res = futureList.poll();
		count++;
	    }
	    if(!res.isDone()){
		add(res);
		throw new NoSuchElementException();
	    } else {
		futureList.addFirst(res);
	    }
	    return res;
	} finally {
	    resLock.unlock();
	}
	
    }

    @Override
    public Future<Result<?>> peek() {
	try{
	    resLock.lock();
	    Future<Result<?>> res = futureList.peekFirst();
	    int count = 1;
	    while(!(res==null || res.isDone() || count <= size())) {
		add(res);
		res = futureList.poll();
		count++;
	    }
	    if(!res.isDone()){
		add(res);
		return null;
	    } else {
		futureList.addFirst(res);
	    }
	    return res;
	} finally {
	    resLock.unlock();
	}
    }

}
