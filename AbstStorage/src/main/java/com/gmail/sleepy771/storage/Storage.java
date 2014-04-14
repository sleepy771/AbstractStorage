package com.gmail.sleepy771.storage;

import java.util.List;

import com.gmail.sleepy771.datastructures.DataInt;

public interface Storage {

    public void clear();

    public void close();

    public void flush();

    public Object get(Path p);

    public List<String> listObjects(Path p);

    public DataInt load(Path p);

    public DataInt remove(Path p);

    public Object set(Path p, Object replacement);

    public void start();

    public void store(Path p, DataInt d);

    public void validate(Path p);
}
