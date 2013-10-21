package com.gmail.sleepy771.storage_interface.misc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gmail.sleepy771.storage_interface.navigation.Path;

public interface ReaderInterface {
    public List<Result<Data>> load(Path<?> path) throws NoSuchElementException,
    IOException;

    public Map<Path<?>,List<Result<Data>>> loadAll(Collection<Path<?>> paths)
    throws NoSuchElementException, IOException;
    
    public Result<Writable<?>> get(Path<?> path) throws IOException,
    NoSuchElementException;
}
