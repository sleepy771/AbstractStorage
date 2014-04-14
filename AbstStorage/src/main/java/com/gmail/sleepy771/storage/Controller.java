package com.gmail.sleepy771.storage;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage.serialization.ObjectBuilder;
import com.gmail.sleepy771.storage.serialization.Serializable;

public class Controller {

    /*
     * Central class that provides communication between client and storages.
     */

    private final Map<String, ObjectBuilder<?>> builders;
    private final ExecutorService srv;
    private final Map<String, Storage> storages;

    public Controller(final File confFile) {
	// set from config file if notdef n = 4
	srv = Executors.newFixedThreadPool(4);
	storages = new HashMap<>();
	builders = new HashMap<>();
    }

    public void addStorage(final String name, final Storage storage) {
	// Perform storage check
	storages.put(name, storage);
    }

    public void copy(final Path pOrig, final Path pNew) {

    }

    public Storage getStorageByName(final String name) {
	return null;
    }

    public Object load(final Path p) {
	return null;
    }

    public Future<Object> loadFuture(final Path p) {
	return null;
    }

    public <T> Future<T> loadFutureTypedef(final Path p, final T testObj) {
	return null;
    }

    public <T> T loadTypedef(final Path p, final T testObj) {
	return null;
    }

    public void move(final Path pOrig, final Path pNew) {

    }

    public void removeStorage(final String storageName) {
	// remove after all task were done on this sotrage
	// awareness of connections between paths
	storages.remove(storageName);
    }

    public boolean save(final Path p, final Object o) {
	return false;
    }

    public void saveFuture(final Path p, final Object o) {
	if (o instanceof Serializable) {
	    Serializable so = Serializable.class.cast(o);

	    //so.serialize();
	}
    }

    public Object set(final Path p, final Object o) {
	return null;
    }

    public void validatePath(final Path p) throws InvalidPathException {

    }

}
