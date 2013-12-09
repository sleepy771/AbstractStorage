package com.gmail.sleepy771.storage_interface.utils;

import java.util.List;

public interface Storage extends Readable, Writable {
    public List<Path> listPaths();

    public long capacity();

    public void go(String name);

    public Data remove(String name);
    
    public List<String> list(Path p);
}
