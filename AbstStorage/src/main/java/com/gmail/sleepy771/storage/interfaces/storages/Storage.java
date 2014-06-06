package com.gmail.sleepy771.storage.interfaces.storages;

import java.util.List;

import com.gmail.sleepy771.storage.interfaces.datastructures.Data;
import com.gmail.sleepy771.storage.interfaces.path.Path;

public interface Storage {

    public void clear();

    public void close();

    public Object get(Path p);

    public Class<?> getObjectClass(Path p);

    public List<String> listObjects(Path p);

    public Data load(Path p);

    public Data remove(Path p);

    public Object set(Path p, Object replacement);

    public void store(Path p, Data d);

    public Object update(Path p, Object o);

    public void validate(Path p);
}
