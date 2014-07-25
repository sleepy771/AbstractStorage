package com.gmail.sleepy771.storage.interfaces.simpleobservermodel;

import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;

public class CallableObservable {
	private final StorageListener<Void> observer;

	public CallableObservable(StorageListener<Void> observer) {
		this.observer = observer;
	}

	public void notifyObserver() {
		this.observer.onNotificationPerform(null, null);
	}
}
