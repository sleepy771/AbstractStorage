package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Collection;
import java.util.HashSet;

import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;

public class StorageObservableImpl<T> implements StorageObservable<T> {
    
    private final Collection<StorageListener<T>> listeners;
    
    public StorageObservableImpl() {
	this(new HashSet<StorageListener<T>>());
    }
    
    public StorageObservableImpl(Collection<StorageListener<T>> listeners) {
	this.listeners = listeners;
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
    }

}
