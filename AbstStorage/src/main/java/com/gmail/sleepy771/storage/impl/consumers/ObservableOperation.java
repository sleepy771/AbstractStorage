package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Collection;
import java.util.HashSet;

import com.gmail.sleepy771.storage.interfaces.consumers.Operation;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageListener;
import com.gmail.sleepy771.storage.interfaces.consumers.StorageObservable;

public class ObservableOperation<T> extends StorageObservableImpl<T> implements
		StorageObservable<T>, Operation<T> {

	public ObservableOperation() {
		super(new HashSet<StorageListener<T>>());
	}

	public ObservableOperation(Collection<StorageListener<T>> listeners) {
		super(listeners);
	}

	@Override
	public synchronized void excute(T object) {
		notifyListeners(object);
	}

}
