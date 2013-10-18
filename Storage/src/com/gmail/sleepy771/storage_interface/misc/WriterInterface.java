package com.gmail.sleepy771.storage_interface.misc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gmail.sleepy771.storage_interface.exceptions.PathException;
import com.gmail.sleepy771.storage_interface.navigation.Path;

public interface WriterInterface {
 // Serial for determining which data wern't saved
    public Result<Serial> save(Path<?> path, Data data) throws IOException;

    public Result<Serial> save(Data data) throws IOException, PathException;

    public List<Result<Serial>> saveAll(Map<Path<?>, Data> dataMap)
	    throws IOException;

    public List<Result<Serial>> saveAll(Collection<Data> data)
	    throws IOException, PathException;
    
    public Result<Void> add(Path<?> path, Writable<?> writable)
	    throws IOException;

    public List<Result<Void>> addAll(Path<?> path, Collection<Writable<?>> writable)
	    throws IOException;

    public Result<Writable<?>> set(Path<?> path, Writable<?> writable)
	    throws IOException, PathException;
}
