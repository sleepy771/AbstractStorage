package com.gmail.sleepy771.storage_interface.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Reader {
    private final ExecutorService service;

    public Reader(final ExecutorService service) {
	this.service = service;
    }

    private Data rawData(final Path p) throws UndefinedStorageException, IOException,
	    UnknownPathException {
	Storage st = StorageInterface.INSTANCE.getStorageByName(p.getStorage());
	return st.read(p.innerPath());
    }

    public Future<Data> readData(final Path p) {
	Callable<Data> call = new Callable<Data>() {
	    @Override
	    public Data call() throws Exception {
		Data raw = rawData(p);
		substituteNeeded(raw);
		return raw;
	    }
	};
	return service.submit(call);
    }

    public void substituteNeeded(final Data d) throws UndefinedStorageException,
	    IOException, UnknownPathException {
	if (!d.getStatus().isComplete()) {
	    Map<String, Path> needed = d.listNeeded();
	    for (Map.Entry<String, Path> pathEntry : needed.entrySet()) {
		Data raw = rawData(pathEntry.getValue());
		d.substitutePath(pathEntry.getKey(), raw);
		substituteNeeded(raw);
	    }
	}
    }
}
