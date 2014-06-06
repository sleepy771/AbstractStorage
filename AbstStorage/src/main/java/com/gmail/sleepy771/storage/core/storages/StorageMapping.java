package com.gmail.sleepy771.storage.core.storages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import com.gmail.sleepy771.storage.interfaces.path.GlobalPath;
import com.gmail.sleepy771.storage.interfaces.storages.Storage;

public class StorageMapping {
    private final Map<String, Storage> stroages = new HashMap<>();

    public void appendStorage(final String name, final Storage storage)
	    throws NameAlreadyBoundException {
	if (stroages.containsKey(stroages))
	    throw new NameAlreadyBoundException();
	stroages.put(name, storage);
    }

    public void dispose() {

    }

    // TODO create StorageNotFoundException
    public Storage getStorage(final GlobalPath path)
	    throws NameNotFoundException {
	return getStorage(path.getStorage());
    }

    public Storage getStorage(final String name) throws NameNotFoundException {
	if (!stroages.containsKey(name))
	    throw new NameNotFoundException();
	return stroages.get(name);
    }

    public Collection<String> listStorages() {
	return new ArrayList<String>(stroages.keySet());
    }

    public void removeStorage(final String name) {
	stroages.remove(name);
    }
}
