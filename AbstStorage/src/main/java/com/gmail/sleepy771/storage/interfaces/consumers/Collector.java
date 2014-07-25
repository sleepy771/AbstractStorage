package com.gmail.sleepy771.storage.interfaces.consumers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage.interfaces.datastructures.Atomic;

public interface Collector<T> extends StorageListener<Void> {
	public void addFutureObject(String name, Future<T> f);

	public void addAllFutureObjects(Map<String, Future<T>> c);

	public void addAllFutureObjectsWithOperation(Map<String, Future<T>> m,
			Operation<Atomic<String, T>> o);

	public void addOperationForFuture(String name,
			Operation<Atomic<String, T>> o);

	public void removeOperationFromFuture(String name);

	public void addFutureWithOperation(String name, Future<T> f,
			Operation<Atomic<String, T>> o);

	public void addAllFutureObjectsWithOperations(
			Map<String, Atomic<Future<T>, Operation<Atomic<String, T>>>> map);

	public void removeOperations(Collection<String> name);

	public void removeFutureObject(String name);

	public void removeAllFutureObjects(Collection<String> c);

	public List<Atomic<String, Future<T>>> close();

	public void setExceptionHandler(StorageObservable<Exception> handler);

	public StorageObservable<Exception> getExceptionHandler();

	public void collect();

	public boolean isEmpty();

	public void setOnRecieveOperation(Operation<Atomic<String, T>> o);
}
