package com.gmail.sleepy771.storage.core;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.naming.NameNotFoundException;

import com.gmail.sleepy771.storage.core.serialization.DeserializerFactory;
import com.gmail.sleepy771.storage.core.serialization.SerilaizerFactory;
import com.gmail.sleepy771.storage.core.storages.StorageMapping;
import com.gmail.sleepy771.storage.interfaces.datastructures.Data;
import com.gmail.sleepy771.storage.interfaces.path.GlobalPath;
import com.gmail.sleepy771.storage.interfaces.path.Path;
import com.gmail.sleepy771.storage.interfaces.serialization.Deserializer;
import com.gmail.sleepy771.storage.interfaces.serialization.Serializer;
import com.gmail.sleepy771.storage.interfaces.storages.Storage;
import com.gmail.sleepy771.storage.interfaces.configs.Configuration;

public class Controller {

    /*
     * Central class that provides communication between client and storages.
     */
    private ExecutorService srv;
    private final StorageMapping mapping;
    private final DeserializerFactory deserializers;
    private final SerilaizerFactory serializers;
    private final Configuration configuration;

    public Controller(StorageMapping mapping, SerilaizerFactory serializers,
	    DeserializerFactory deserializers, Configuration config) {
	this.mapping = mapping;
	this.serializers = serializers;
	this.deserializers = deserializers;
	this.configuration = config;
    }

    public void copy(final Path pOrig, final Path pNew) {

    }

    public void start() {
	String poolType = configuration.getSettingTypeDef("threadPoolType");
	switch (poolType) {
	case "fixed":
	    this.srv = Executors.newFixedThreadPool(configuration
		    .<Integer> getSettingTypeDef("threadsCount"));
	    break;
	case "catched":
	    this.srv = Executors.newCachedThreadPool();
	    break;
	case "single":
	    this.srv = Executors.newSingleThreadExecutor();
	    break;
	default:
	    this.srv = Executors.newFixedThreadPool(4);
	}
    }

    public void stop() {
	try {
	    this.srv.awaitTermination(
		    configuration
			    .<Integer> getSettingTypeDef("awaitTermiantionTimeout"),
		    TimeUnit.valueOf(configuration
			    .<String> getSettingTypeDef("awaitTermiantionTimeUnit")));
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    }

    public void restart() throws InterruptedException {
	stop();
	start();
    }

    public void close() {
	stop();
	this.configuration.write(new File(configuration
		.<String> getSettingTypeDef("defaultSettingsFile")));
	this.configuration.dispose();
	this.deserializers.dispose();
	this.serializers.dispose();
	this.mapping.dispose();
    }

    public Storage getStorageByName(final String name)
	    throws NameNotFoundException {
	return mapping.getStorage(name);
    }

    public Object load(final GlobalPath p) throws NameNotFoundException,
	    InterruptedException, ExecutionException {
	Callable<Object> call = new Callable<Object>() {
	    @Override
	    public Object call() throws Exception {
		Storage stor = Controller.this.mapping.getStorage(p
			.getStorage());
		Data dat = stor.load(p.getLocal());
		Class<?> clazz = dat.getRepresentedClass();
		Deserializer des = Controller.this.deserializers
			.getDeserializer(clazz);
		return des.deserialize(dat).asObject();
	    }

	};
	Future<Object> fut = this.srv.submit(call);
	return fut.get();
    }

    public <T> T load(final Class<T> clazz, final Path p) {
	return null;
    }

    public Map<String, Object> loadMore(final Collection<Path> paths) {
	return null;
    }

    public <T> Future<T> loadFutureTypedef(final Class<T> clazz, final Path p) {
	return null;
    }

    public void move(final Path pOrig, final Path pNew) {

    }

    public StorageMapping getStorages() {
	return this.mapping;
    }

    public SerilaizerFactory getSerializers() {
	return serializers;
    }

    public DeserializerFactory getDeserializers() {
	return deserializers;
    }

    public void save(final GlobalPath p, final Object o) {
	Callable<Void> task = new Callable<Void>() {
	    @Override
	    public Void call() throws Exception {
		Storage s = Controller.this.mapping.getStorage(p.getStorage());
		Serializer ser = Controller.this.serializers
			.instantiateSerializerFor(o.getClass());
		Data dat = ser.serialize(o);
		// ser.dispose();
		s.store(p.getLocal(), dat);
		return null;
	    }
	};
	this.srv.submit(task);
    }

    public Future<Void> saveFuture(final Path p, final Object o) {
	return null;
    }

    public void silentSave(final Path p, final Object o) {

    }

    public Object set(final Path p, final Object o) {
	return null;
    }

    public void validatePath(final Path p) throws InvalidPathException {

    }

}
