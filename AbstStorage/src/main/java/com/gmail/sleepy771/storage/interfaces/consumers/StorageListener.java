package com.gmail.sleepy771.storage.interfaces.consumers;

public interface StorageListener<T> {
    public void onNotificationPerform(StorageObservable<T> obs,T object);
}
