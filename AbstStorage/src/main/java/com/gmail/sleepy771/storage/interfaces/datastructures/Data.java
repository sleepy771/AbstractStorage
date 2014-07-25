package com.gmail.sleepy771.storage.interfaces.datastructures;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.gmail.sleepy771.storage.interfaces.path.Path;

public interface Data extends Iterable<Entry<Path, Object>> {

	public Object get(String name);

	public Object get(Path p);
	
	public UUID getObjectUUID();

	public String getName();

	public Set<Entry<String, Object>> getRawEntrySet();

	public Map<String, Object> getByClass(Class<?> clazz);

	public String getClassName();
}
