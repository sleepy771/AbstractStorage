package com.gmail.sleepy771.storage_interface.utils;

import java.io.InvalidClassException;
import java.util.Map;

public interface Data extends Map<String, Object> {
    public Object get(Path inner) throws IncompleteDataException, UnknownPathException;

    public Object get(String str);

    public Status getStatus();

    public Class<?> getObjectClass() throws ClassNotFoundException;

    public Map<String, Path> listNeeded();

    public Data substituteData(Path p) throws InvalidClassException;

    public void substitutePath(String name, Data d);
}
