package com.gmail.sleepy771.storage_interface.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StorageInterface {
    public static final StorageInterface INSTANCE = new StorageInterface();

    private final DeserializerHandler deserializer;
    private final Map<String, Deserializer> deserializers = new HashMap<>();
    private final ExecutorService executor;
    private final Reader reader;

    private final SerializerHandler serializer;
    private final Map<Class<?>, ExternalSerializable> serializers = new HashMap<>();
    private final Map<String, Storage> storages = new HashMap<>();
    private final Writer writer;

    private StorageInterface() {
	executor = Executors.newFixedThreadPool(4);
	deserializer = new DeserializerHandler(executor);
	reader = new Reader(executor);
	serializer = new SerializerHandler(executor);
	writer = new Writer(executor);
    }

    public Deserializer getDeserializer(final String clsName) {
	return deserializers.get(clsName);
    }

    public ExternalSerializable getSerializers(final Class<?> cls) {
	return serializers.get(cls);
    }

    public Storage getStorageByName(final String name)
	    throws UndefinedStorageException {
	Storage st = storages.get(name);
	if (st == null)
	    throw new UndefinedStorageException();
	return st;
    }

    public Object load(final Path p) throws InterruptedException,
	    ExecutionException {
	Future<Data> futd = reader.readData(p);
	Future<Object> fo = deserializer.deserialize(futd);
	return fo.get();
    }

    public Future<Object> loadFuture(final Path p) throws InterruptedException,
	    ExecutionException {
	return deserializer.deserialize(reader.readData(p));
    }

    public void putDesrializer(final String clsName, final Deserializer des) {
	deserializers.put(clsName, des);
    }

    public Data read(final Path p) throws InterruptedException, ExecutionException {
	return reader.readData(p).get();
    }

    public void register(final String name, final Storage storage) {
	storages.put(name, storage);
    }

    public void save(final Object o) throws UndefinedLocationException {

    }

    public void save(final Path p, final Object obj)
	    throws InterruptedException, ExecutionException {
	Future<Data> fd = serializer.serialize(obj);
	writer.write(p, fd);
    }

    public void saveLater(final Path p, final Object obj) {
	Future<Data> data = serializer.serialize(obj);
	writer.writeAsync(p, data);
    }

    public void write(final Path p, final Data d) {
	writer.write(p, d);
    }
}
