package com.gmail.sleepy771.storage_interface.misc;

import com.gmail.sleepy771.storage_interface.exceptions.UnchecableException;



public interface Writable<T> {
	public T get() throws NullPointerException;
	public void set(T object) throws UnchecableException;
	public Class<?> getInnerClass() throws NullPointerException;
	public boolean isNull();
	//TODO format
}
