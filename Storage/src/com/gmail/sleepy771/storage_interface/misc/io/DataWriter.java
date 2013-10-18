package com.gmail.sleepy771.storage_interface.misc.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage_interface.Storage;
import com.gmail.sleepy771.storage_interface.exceptions.EmptyDataMapException;
import com.gmail.sleepy771.storage_interface.misc.Data;
import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.misc.Serial;
import com.gmail.sleepy771.storage_interface.misc.StorageInterface;
import com.gmail.sleepy771.storage_interface.misc.Writable;
import com.gmail.sleepy771.storage_interface.navigation.Path;

public class DataWriter {
    private ExecutorService globalExecutors;

    public DataWriter(ExecutorService threadPool) {
	if (threadPool == null)
	    throw new NullPointerException();
	this.globalExecutors = threadPool;
    }

    public Future<Result<Serial>> put(final Path<?> path, final Data data)
	    throws NullPointerException {
	if (path == null || data == null)
	    throw new NullPointerException();
	Callable<Result<Serial>> writeDataCall = new Callable<Result<Serial>>() {
	    @Override
	    public Result<Serial> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(path
			.pollStorageKey());
		Result<Serial> res = si.save(path, data);
		// TODO make storage results and convert it to interface result
		// for more compatibility
		return res;
	    }
	};
	return globalExecutors.submit(writeDataCall);
    }

    public List<Future<Result<Serial>>> putAllToDifferentStorages(
	    Map<Path<?>, Data> dataMap) throws NullPointerException,
	    EmptyDataMapException, IOException {
	if (dataMap == null)
	    throw new NullPointerException();
	if (dataMap.isEmpty())
	    throw new EmptyDataMapException();
	List<Future<Result<Serial>>> futureResults = new ArrayList<Future<Result<Serial>>>();
	for (Map.Entry<Path<?>, Data> dataEntry : dataMap.entrySet()) {
	    try {
		futureResults
			.add(put(dataEntry.getKey(), dataEntry.getValue()));
	    } catch (NullPointerException npe) {
		// TODO npe log
		if (dataEntry.getKey() == null) {
		    // TODO Sotre data to some stack
		}
	    }
	}
	return futureResults;
    }

    public Future<List<Result<Serial>>> putAllToSameStorage(
	    final String storageKey, final Map<Path<?>, Data> dataMap)
	    throws NullPointerException, EmptyDataMapException, IOException {
	if (dataMap == null)
	    throw new NullPointerException();
	if (dataMap.isEmpty())
	    throw new EmptyDataMapException();
	Callable<List<Result<Serial>>> writeDataCollectionCall = new Callable<List<Result<Serial>>>() {
	    @Override
	    public List<Result<Serial>> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(storageKey);
		return si.saveAll(dataMap);
	    }
	};
	return globalExecutors.submit(writeDataCollectionCall);
    }

    public Future<Result<Serial>> putWithoutPath(final String sorageKey,
	    final Data data) {
	if (data == null)
	    throw new NullPointerException();
	Callable<Result<Serial>> writeCall = new Callable<Result<Serial>>() {
	    @Override
	    public Result<Serial> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(sorageKey);
		return si.save(data);
	    }
	};
	return globalExecutors.submit(writeCall);
    }

    public Future<List<Result<Serial>>> putAllWithoutPath(
	    final String sorageKey, final Collection<Data> dataCollection)
	    throws EmptyDataMapException, NullPointerException {
	if (dataCollection == null)
	    throw new NullPointerException();
	if (dataCollection.isEmpty())
	    throw new EmptyDataMapException();
	Callable<List<Result<Serial>>> writeCall = new Callable<List<Result<Serial>>>() {
	    @Override
	    public List<Result<Serial>> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(sorageKey);
		return si.saveAll(dataCollection);
	    }
	};
	return globalExecutors.submit(writeCall);
    }
    
    public Future<Result<Void>> addWritable(final Path<?> path, final Writable<?> writable) {
	if(path == null || writable == null || writable.isNull())
	    throw new NullPointerException();
	Callable<Result<Void>> writerCall = new Callable<Result<Void>>(){
	    @Override
	    public Result<Void> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(path.pollStorageKey());
		return si.add(path, writable);
	    }    
	};
	return globalExecutors.submit(writerCall);
    }
    
    public Future<List<Result<Void>>> addAllWritable(final Path<?> path, final Collection<Writable<?>> writableCollection) {
	if(path == null || writableCollection == null)
	    throw new NullPointerException();
	Callable<List<Result<Void>>> call = new Callable<List<Result<Void>>>() {
	    @Override
	    public List<Result<Void>> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(path.pollStorageKey());
		return si.addAll(path, writableCollection);
	    }	    
	};
	return globalExecutors.submit(call);
    }
    
    public Future<Result<Writable<?>>> setWritable(final Path<?> path, final Writable<?> writable) {
	if(path == null || writable == null || writable.isNull())
	    throw new NullPointerException();
	Callable<Result<Writable<?>>> writerCall = new Callable<Result<Writable<?>>>(){
	    @Override
	    public Result<Writable<?>> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(path.pollStorageKey());
		return si.set(path, writable);
	    }    
	};
	return globalExecutors.submit(writerCall);
    }

    
}
