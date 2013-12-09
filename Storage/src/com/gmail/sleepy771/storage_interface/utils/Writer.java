package com.gmail.sleepy771.storage_interface.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Writer {
    ExecutorService service;
    TaskHeap tasks;

    public Writer(final ExecutorService service) {
	this.service = service;
	tasks = new TaskHeap(service);
    }

    public Map<Path, Data> splitInPaths(final Path p, final Data d) {
	Map<Path, Data> splitData = new HashMap<>();
	splitData.put(p, d);
	for (Map.Entry<String, Object> dataEntry : d.entrySet()) {
	    if (dataEntry.getValue() instanceof Data) {
		Data subData = (Data) dataEntry.getValue();
		Path subPath = p.subPath(dataEntry.getKey());
		dataEntry.setValue(subPath);
		splitData.putAll(splitInPaths(subPath, subData));
	    }
	}
	System.out.println(splitData);
	return splitData;
    }

    public Future<Void> write(final Path p, final Data d) {
	Callable<Void> call = new Callable<Void>() {
	    @Override
	    public Void call() throws Exception {
		Map<Path, Data> rawDataMap = splitInPaths(p, d);
		for (Map.Entry<Path, Data> rawEntry : rawDataMap.entrySet()) {
		    Storage st = StorageInterface.INSTANCE.getStorageByName(rawEntry
			    .getKey().getStorage());
		    st.write(rawEntry.getKey().innerPath(), rawEntry.getValue());
		}
		return null;
	    }
	};
	return service.submit(call);
    }

    public Future<Void> write(final Path p, final Future<Data> fd)
	    throws InterruptedException, ExecutionException {
	return write(p, fd.get());
    }

    public void writeAsync(final Path p, final Future<Data> d) {
	Callable<Void> call = new Callable<Void>() {

	    @Override
	    public Void call() throws Exception {
		Map<Path, Data> rawDataMap = splitInPaths(p, d.get());
		for (Map.Entry<Path, Data> rawEntry : rawDataMap.entrySet()) {
		    Storage st = StorageInterface.INSTANCE
			    .getStorageByName(rawEntry.getKey().getStorage());
		    st.write(rawEntry.getKey().innerPath(), rawEntry.getValue());
		}
		return null;
	    }

	};
	System.out.println("async task pushed");
	tasks.push(d, call);
    }
}
