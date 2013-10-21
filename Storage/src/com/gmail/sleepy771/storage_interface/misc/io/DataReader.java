package com.gmail.sleepy771.storage_interface.misc.io;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage_interface.Storage;
import com.gmail.sleepy771.storage_interface.misc.Data;
import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.misc.StorageInterface;
import com.gmail.sleepy771.storage_interface.misc.Writable;
import com.gmail.sleepy771.storage_interface.navigation.DataKey;
import com.gmail.sleepy771.storage_interface.navigation.Path;

public class DataReader {
    private ExecutorService globalService;

    public Future<List<Result<Data>>> load(final Path<?> path) {
	// TODO check if path is defined
	Callable<List<Result<Data>>> call = new Callable<List<Result<Data>>>() {
	    @Override
	    public List<Result<Data>> call() throws Exception {
		return loadRecursively(path);
	    }
	};
	return globalService.submit(call);
    }
    
    private List<Result<Data>> loadRecursively(Path<?> path) throws NoSuchElementException, IOException {
	StorageInterface si = Storage.INSTANCE.getStorage(path.getStorageKey());
	List<Result<Data>> dataList = si.load(path);
	for(Result<Data> dataResult : dataList) {
	    if (dataResult.isIncomplete()) {
		List<Path<?>> paths = dataResult.get().getMissingData();
		Map<DataKey<String>, List<Path<?>>> pathsMap = new TreeMap<DataKey<String>, List<Path<?>>>();
		for (Path<?> p : paths) {
		    if (!pathsMap.containsKey(p.getStorageKey())) {
			pathsMap.put(p.getStorageKey(), new LinkedList<Path<?>>());
		    }
		    pathsMap.get(p.getStorageKey()).add(p);
		}
		for (Map.Entry<DataKey<String>, List<Path<?>>> entr : pathsMap.entrySet()) {
		    Collection<List<Result<Data>>> col = loadAllRecursively(entr.getKey().get(), entr.getValue()).values();
		    for (List<Result<Data>> lrd : col)
			   dataList.addAll(lrd);
		}
		//for(Path<?> necessaryDataPath : paths)
		//    dataList.addAll(loadRecursively(necessaryDataPath));
	    }
	}
	return dataList;
    }
    
    public Future<Map<Path<?>, List<Result<Data>>>> loadAll(final String storageKey, final Collection<Path<?>> paths) {
	Callable<Map<Path<?>, List<Result<Data>>>> call = new Callable<Map<Path<?>, List<Result<Data>>>>() {

	    @Override
	    public Map<Path<?>, List<Result<Data>>> call() throws Exception {
		return loadAllRecursively(storageKey, paths);
	    }
	    
	};
	return globalService.submit(call);
    }
    
    private Map<Path<?>, List<Result<Data>>> loadAllRecursively(String storageKey, Collection<Path<?>> paths) throws NoSuchElementException, IOException {
	StorageInterface si = Storage.INSTANCE.getStorage(storageKey);
	Map<Path<?>, List<Result<Data>>> dataMaps = si.loadAll(paths);
	for (Map.Entry<Path<?>, List<Result<Data>>> dataMapEntry : dataMaps.entrySet()) {
	    Map<DataKey<String>, List<Path<?>>> pathsByStorages = new TreeMap<DataKey<String>, List<Path<?>>>();
	    for (Result<Data> partResult : dataMapEntry.getValue()) {
		if (partResult.isIncomplete()) { 
		    for (Path<?> p : partResult.get().getMissingData()) {
			if(!pathsByStorages.containsKey(p.getStorageKey())){
			    pathsByStorages.put(p.getStorageKey(), new LinkedList<Path<?>>());
			}
			pathsByStorages.get(p.getStorageKey()).add(p);
		    }
		}
	    }
	    for (Map.Entry<DataKey<String>, List<Path<?>>> entr : pathsByStorages.entrySet()) {
		Collection<List<Result<Data>>> col = loadAllRecursively(entr.getKey().get(), entr.getValue()).values();
		for (List<Result<Data>> lrd : col)
		    dataMapEntry.getValue().addAll(lrd);
	    }
	}
	return dataMaps;
    }
    
    public Future<Result<Writable<?>>> get(final Path<?> path) {
	Callable<Result<Writable<?>>> call = new Callable<Result<Writable<?>>>() {
	    @Override
	    public Result<Writable<?>> call() throws Exception {
		StorageInterface si = Storage.INSTANCE.getStorage(path.getStorageKey());
		return si.get(path);
	    }
	};
	return globalService.submit(call);
    }
}
