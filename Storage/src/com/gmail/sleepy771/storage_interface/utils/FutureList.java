package com.gmail.sleepy771.storage_interface.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureList implements Future<List<Object>> {
    private final List<Future<?>> innerList = new ArrayList<>();

    public void addFuture(final Future<?> f) {
	innerList.add(f);
    }

    public void addFutures(final Collection<Future<?>> futures) {
	innerList.addAll(futures);
    }

    public void addFutures(final Future<?>...futures ) {
	for (Future<?> future : futures) {
	    innerList.add(future);
	}
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
	boolean canceled = true;
	for (Future<?> f : innerList) {
	    canceled &= f.cancel(mayInterruptIfRunning);
	}
	return canceled;
    }

    @Override
    public List<Object> get() throws InterruptedException, ExecutionException {
	List<Object> output = new ArrayList<Object>(innerList.size());
	for (Future<?> f : innerList) {
	    output.add(f.get());
	}
	return output;
    }

    @Override
    public List<Object> get(long timeout, final TimeUnit unit)
	    throws InterruptedException, ExecutionException, TimeoutException {
	List<Object> output = new ArrayList<Object>(innerList.size());
	for (Future<?> f : innerList) {
	    long dt = System.currentTimeMillis();
	    output.add(f.get(timeout, unit));
	    dt = System.currentTimeMillis() - dt;
	    if (timeout > 0) {
		timeout -= dt;
	    } else {
		timeout = 0;
	    }
	}
	return output;
    }

    @Override
    public boolean isCancelled() {
	for (Future<?> f : innerList) {
	    if (f.isCancelled())
		return true;
	}
	return false;
    }

    @Override
    public boolean isDone() {
	for (Future<?> f : innerList) {
	    if (!f.isDone())
		return false;
	}
	return true;
    }

}
