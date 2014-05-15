package com.gmail.sleepy771.storage.interfaces.consumers;

import java.util.Collection;

public interface StorageObservable<T> {
    public void addListener(StorageListener<T> listener);
    
    public void addAllListeners(Collection<StorageListener<T>> listeners);
    
    public void removeListener(StorageListener<T> listener);
    
    public void removeAllListenres(Collection<StorageListener<T>> listeners);
    
    public void dispose();
    
    public int countListeners();
    
    public void notifyListeners(T obj);
}
