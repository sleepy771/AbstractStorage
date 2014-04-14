package com.gmail.sleepy771.storage;

import com.gmail.sleepy771.exceptions.NotBuildableException;

public interface Buildable<T> {
    public T build() throws NotBuildableException;
}
