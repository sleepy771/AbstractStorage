package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Collection;
import java.util.HashSet;

import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;

public class ExtendedStorageObservable<T> implements StorageObservable<T> {
    
    private final StorageObservable<T> inerImmutableObservable;
    private final Collection<StorageListener<T>> listeners;
    
    public ExtendedStorageObservable(StorageObservable<T> observable, Collection<StorageListener<T>> listenerCollection) {
	this.inerImmutableObservable = observable;
	this.listeners = listenerCollection;
    }
    
    public ExtendedStorageObservable(StorageObservable<T> observable) {
	this(observable, new HashSet<StorageListener<T>>());
    }
    
    @Override
    public synchronized final void addListener(StorageListener<T> listener) {
	this.listeners.add(listener);
    }

    @Override
    public synchronized final void addAllListeners(Collection<StorageListener<T>> listeners) {
	this.listeners.addAll(listeners);
    }

    @Override
    public synchronized final void removeListener(StorageListener<T> listener) {
	this.listeners.remove(listener);
    }

    @Override
    public synchronized final void removeAllListenres(Collection<StorageListener<T>> listeners) {
	this.listeners.removeAll(listeners);
    }

    @Override
    public synchronized final void dispose() {
	this.listeners.clear();
    }

    @Override
    public synchronized final int countListeners() {
	return this.listeners.size();
    }

    @Override
    public synchronized final void notifyListeners(T obj) {
	for(StorageListener<T> listener : listeners) {
	    listener.onNotificationPerform(this, obj);
	}
	inerImmutableObservable.notifyListeners(obj);
    }

}
