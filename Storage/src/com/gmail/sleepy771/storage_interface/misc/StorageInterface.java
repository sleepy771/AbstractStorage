package com.gmail.sleepy771.storage_interface.misc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gmail.sleepy771.storage_interface.navigation.Path;

public interface StorageInterface extends WriterInterface, ReaderInterface{

    public Result<Void> flush();

    public Result<Void> close();  

    public Result<Void> remove(Path<?> path) throws IOException,
	    NoSuchElementException;

    public List<Result<Void>> removeAll(Collection<Path<?>> paths)
	    throws IOException, NoSuchElementException;

    public Result<Long> size();

    public Result<Long> size(Path<?> path);
}
