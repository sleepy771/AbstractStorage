package com.gmail.sleepy771.storage.core;

import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

public class Controller {

    /*
     * Central class that provides communication between client and storages.
     */
    private final ExecutorService srv;
    private final StorageMapping mapping;
    private final DeserializerFactory deserializers;
    private final SerilaizerFactory serializers;

    public Controller(StorageMapping mapping, SerilaizerFactory serializers, DeserializerFactory deserializers) {
	// set from config file if notdef n = 4
	srv = Executors.newFixedThreadPool(4);
	this.mapping = mapping;
	this.serializers = serializers;
	this.deserializers = deserializers;
    }

    public void copy(final Path pOrig, final Path pNew) {

    }

    public Storage getStorageByName(final String name) throws NameNotFoundException{
	return mapping.getStorage(name);
    }

    public Object load(final GlobalPath p) throws NameNotFoundException, InterruptedException, ExecutionException {
	Callable<Object> call = new Callable<Object>() {
	    @Override
	    public Object call() throws Exception {
		Storage stor = Controller.this.mapping.getStorage(p.getStorage());
		Data dat = stor.load(p.getLocal());
		Class<?> clazz = dat.getRepresentedClass();
		Deserializer des = Controller.this.deserializers.getDeserializer(clazz);
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

    public <T> Future<T> loadFutureTypedef( final Class<T> clazz, final Path p) {
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
	Callable<Void> task = new Callable<Void>(){
	    @Override
	    public Void call() throws Exception {
		Storage s = Controller.this.mapping.getStorage(p.getStorage());
		Serializer ser = Controller.this.serializers.instantiateSerializerFor(o.getClass());
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
