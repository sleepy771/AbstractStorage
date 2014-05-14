package com.gmail.sleepy771.storage.core.storages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import com.gmail.sleepy771.storage.interfaces.storages.Storage;

public class StorageMapping {
    private Map<String, Storage> stroages = new HashMap<>();
    
    public void appendStorage(String name, Storage storage) throws NameAlreadyBoundException {
	if (stroages.containsKey(stroages)) {
	    throw new NameAlreadyBoundException();
	}
	this.stroages.put(name, storage);
    }
    
    public void removeStorage(String name) {
	this.stroages.remove(name);
    }
    
    public Storage getStorage(String name) throws NameNotFoundException {
	if (!stroages.containsKey(name))
	    throw new NameNotFoundException();
	return stroages.get(name);
    }
    
    public Collection<String> listStorages() {
	return new ArrayList<String>(stroages.keySet());
    }
}
