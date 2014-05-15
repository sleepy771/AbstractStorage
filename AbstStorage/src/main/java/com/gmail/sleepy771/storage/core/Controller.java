package com.gmail.sleepy771.storage.core;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import com.gmail.sleepy771.storage.exceptions.NotBuildableException;
import com.gmail.sleepy771.storage.exceptions.SerializerNotFoundException;
import com.gmail.sleepy771.storage.exceptions.UnserializableObjectException;
import com.gmail.sleepy771.storage.impl.consumers.CollectorImpl;
import com.gmail.sleepy771.storage.impl.consumers.CollectorObjectBuilder;
import com.gmail.sleepy771.storage.impl.consumers.StorageObservableImpl;
import com.gmail.sleepy771.storage.interfaces.Buildable;
import com.gmail.sleepy771.storage.interfaces.datastructures.Data;
import com.gmail.sleepy771.storage.interfaces.datastructures.Duplet;
import com.gmail.sleepy771.storage.interfaces.path.GlobalPath;
import com.gmail.sleepy771.storage.interfaces.path.Path;
import com.gmail.sleepy771.storage.interfaces.serialization.Deserializer;
import com.gmail.sleepy771.storage.interfaces.serialization.Serializer;
import com.gmail.sleepy771.storage.interfaces.simpleobservermodel.CallableObservable;
import com.gmail.sleepy771.storage.interfaces.storages.Storage;
import com.gmail.sleepy771.storage.interfaces.configs.Configuration;
import com.gmail.sleepy771.storage.interfaces.consumers.Operation;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;

public class Controller {

    /*
     * Central class that provides communication between client and storages.
     */
    private ExecutorService srv;
    private final StorageMapping mapping;
    private final DeserializerFactory deserializers;
    private final SerilaizerFactory serializers;
    private final Configuration configuration;
    private final StorageObservable<Exception> exceptionHandler;

    public Controller(StorageMapping mapping, SerilaizerFactory serializers,
	    DeserializerFactory deserializers, Configuration config) {
	this.mapping = mapping;
	this.serializers = serializers;
	this.deserializers = deserializers;
	this.configuration = config;
	this.exceptionHandler = new StorageObservableImpl<Exception>();
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
	this.exceptionHandler.dispose();
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

    public <T> T loadType(final GlobalPath p) throws InterruptedException, ExecutionException {
	Callable<T> loadTask = new Callable<T>() {

	    @Override
	    public T call() throws Exception {
		Storage storage = mapping.getStorage(p.getStorage());
		Data data = storage.load(p.getLocal());
		Class<?> clazz = data.getRepresentedClass();
		Deserializer des = deserializers.getDeserializer(clazz);
		return des.deserialize(data).asTypedObject();
	    }
	    
	};
	return srv.submit(loadTask).get();
    }

    public Map<String, Object> loadAll(final Collection<GlobalPath> paths) throws NotBuildableException, NameNotFoundException, InterruptedException, ExecutionException {
	Map<String, Object> map = new TreeMap<String, Object>();
	for(GlobalPath path : paths) {
	    map.put(path.getObjectName(), load(path));
	}
	return map;
    }
    
    public <T> Map<String, T> loadAllTypeDefined(final Collection<GlobalPath> paths) throws NotBuildableException, InterruptedException, ExecutionException {
	final Map<String, T> map = new HashMap<String, T>();
	for(GlobalPath path : paths) {
	    map.put(path.getObjectName(), this.<T>loadType(path));
	}
	return map;
    }
    
    

    public <T> Future<T> loadFutureTypedef(final GlobalPath p) {
	Callable<T> loadTask = new Callable<T>() {

	    @Override
	    public T call() throws Exception {
		Storage storage = mapping.getStorage(p.getStorage());
		Data data = storage.load(p.getLocal());
		Class<?> clazz = data.getRepresentedClass();
		Deserializer des = deserializers.getDeserializer(clazz);
		return des.deserialize(data).asTypedObject();
	    }
	    
	};
	return this.srv.submit(loadTask);
    }
    
    public Future<Object> loadFuture(GlobalPath p) {
	return this.<Object>loadFutureTypedef(p);
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

    public void saveAndWait(final GlobalPath p, final Object o) throws InterruptedException, ExecutionException {
	Callable<Void> task = new Callable<Void>() {
	    @Override
	    public Void call() throws Exception {
		Storage s = Controller.this.mapping.getStorage(p.getStorage());
		Serializer ser = Controller.this.serializers
			.instantiateSerializerFor(o.getClass());
		Data dat = ser.serialize(o, p.getObjectName());
		ser.dispose();
		s.store(p.getLocal(), dat);
		return null;
	    }
	};
	Future<Void> fut = this.srv.submit(task);
	fut.get();
    }
    
    public void saveSilent(final GlobalPath p, final Object o) {
	Runnable silentSaveRunnable = new Runnable() {

	    @Override
	    public void run() {
		Serializer serializer = null;
		Storage storage = null;
		try {
		    serializer = serializers.instantiateSerializerFor(o.getClass());
		    Data data = serializer.serialize(o, p.getObjectName());
		    storage = mapping.getStorage(p.getStorage());
		    storage.store(p.getLocal(), data);
		} catch (InstantiationException | IllegalAccessException
			| SerializerNotFoundException | UnserializableObjectException | NameNotFoundException e) {
		    exceptionHandler.notifyListeners(e);
		} finally {
		    if(serializer != null)
			serializer.dispose();
		    storage = null;
		    serializer = null;
		}
		
	    }
	    
	};
	this.srv.submit(silentSaveRunnable);
    }
    
    public void saveAllSilent(List<Duplet<GlobalPath, Object>> list) {
	for (Duplet<GlobalPath, Object> dup : list) {
	    saveSilent(dup.getFirst(), dup.getSecond());
	}
    }
    
    public Data serialize(Object o, String name) throws InterruptedException, ExecutionException {
	return serializeFuture(o, name).get();
    }
    
    public void save(GlobalPath p, Data d) throws InterruptedException, ExecutionException {
	saveFuture(p, d).get();
    }
    
    private Future<Void> saveFuture(final GlobalPath p, final Data d) {
	Callable<Void> saveTask = new Callable<Void> () {

	    @Override
	    public Void call() throws Exception {
		Storage s = mapping.getStorage(p.getStorage());
		s.store(p.getLocal(), d);
		return null;
	    }
	};
	
	return srv.submit(saveTask);
    }
    
    private Future<Void> saveFutureWithNotify(final GlobalPath p, final Data d, final StorageListener<Void> obs) {
	Callable<Void> saveTask = new Callable<Void> () {
	    CallableObservable calObs = new CallableObservable(obs);
	    @Override
	    public Void call() throws Exception {
		Storage s = mapping.getStorage(p.getStorage());
		try {
		    s.store(p.getLocal(), d);
		    return null;
		} finally {
		    calObs.notifyObserver();
		}
	    }
	};
	
	return srv.submit(saveTask);
    }
    
    private Future<Data> serializeFuture(final Object o, final String name) {
	Callable<Data> serialTask = new Callable<Data>() {

	    @Override
	    public Data call() throws Exception {
		Serializer s = serializers.instantiateSerializerFor(o.getClass());
		return s.serialize(o, name);
	    }
	    
	};
	return srv.submit(serialTask);
    }
    
    private Future<Data> serializeFutureWithNotify(final Object o, final String name, final StorageListener<Void> obs) {
	Callable<Data> serialTaskNotifiable = new Callable<Data>() {
	    CallableObservable calObs = new CallableObservable(obs);
	    @Override
	    public Data call() throws Exception {
		Serializer s = serializers.instantiateSerializerFor(o.getClass());
		try {
		    return s.serialize(o, name);
		} finally {
		    s.dispose();
		    calObs.notifyObserver();
		}
	    }
	    
	};
	return srv.submit(serialTaskNotifiable);
    }

    public Object set(final Path p, final Object o) {
	return null;
    }

    public void validatePath(final Path p) throws InvalidPathException {

    }

}
