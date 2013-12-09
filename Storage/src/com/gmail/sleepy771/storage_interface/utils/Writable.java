package com.gmail.sleepy771.storage_interface.utils;

public interface Writable {
    public Result write(Data data);

    public Result write(Path path, Data data);

    public Result writeRel(Path path, Data data);

    public Result writeRel(String name, Data data);
}
