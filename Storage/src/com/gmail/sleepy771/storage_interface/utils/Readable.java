package com.gmail.sleepy771.storage_interface.utils;

import java.io.IOException;

public interface Readable {
    public Data read(Path path) throws IOException, UnknownPathException;

    public Result read(Path path, String name) throws IOException,
	    UnknownPathException;
}
