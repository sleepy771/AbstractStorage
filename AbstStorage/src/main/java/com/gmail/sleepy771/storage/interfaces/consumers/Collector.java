package com.gmail.sleepy771.storage.interfaces.consumers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage.interfaces.datastructures.Duplet;

public interface Collector<T> extends StorageListener<Void> {
    public void addFutureObject(String name, Future<T> f);
    public void addAllFutureObjects(Map<String, Future<T>> c);
    public void removeFutureObject(String name);
    public void removeAllFutureObjects(Collection<String> c);
    public List<Duplet<String, Future<T>>> close();
    public void setExceptionHandler(StorageObservable<Exception> handler);
    public StorageObservable<Exception> getExceptionHandler();
    public void collect();
    public boolean isEmpty();
    public void setOnRecieveOperation(Operation<Duplet<String, T>> o);
}
