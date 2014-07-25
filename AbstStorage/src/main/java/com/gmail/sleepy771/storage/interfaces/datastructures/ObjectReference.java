package com.gmail.sleepy771.storage.interfaces.datastructures;

public interface ObjectReference<T> {
	public T get();

	public void set(T object);

	public boolean isChanged();
}
