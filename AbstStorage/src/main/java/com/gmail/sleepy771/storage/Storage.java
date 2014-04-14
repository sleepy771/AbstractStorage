package com.gmail.sleepy771.storage;

import java.util.List;

import com.gmail.sleepy771.datastructures.Data;

public interface Storage {

    public void clear();

    public void close();

    public void flush();

    public Object get(Path p);

    public List<String> listObjects(Path p);

    public Data load(Path p);

    public Data remove(Path p);

    public Object set(Path p, Object replacement);

    public void start();

    public void store(Path p, Data d);

    public void validate(Path p);
}
