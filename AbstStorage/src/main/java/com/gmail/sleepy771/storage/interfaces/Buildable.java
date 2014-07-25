package com.gmail.sleepy771.storage.interfaces;

import com.gmail.sleepy771.storage.exceptions.NotBuildableException;

public interface Buildable<T> {
	public T build() throws NotBuildableException;
}
