package com.gmail.sleepy771.storage_interface.utils;

import java.io.InvalidClassException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DataBuilder implements Builder<Data>{
    public static final String CLASS = "9bd81329febf6efe22788e03ddeaf0af";
    public static final String NAME = "49ee3087348e8d44e1feda1917443987";
    private final Map<String, Object> map = new HashMap<>();

    public DataBuilder(final String name, final Class<?> cls) {
	map.put(NAME, name);
	map.put(CLASS, cls.getName());
    }

    public DataBuilder addObject(final String name, final Object obj) {
	map.put(name, obj);
	return this;
    }

    @Override
    public Data build() {
	return new Data() {
	    private final Map<String, Object> map = DataBuilder.this.map;

	    @Override
	    public void clear() {
		throw new UnsupportedOperationException();
	    }

	    @Override
	    public boolean containsKey(final Object key) {
		return map.containsKey(key);
	    }

	    @Override
	    public boolean containsValue(final Object value) {
		return map.containsValue(value);
	    }

	    @Override
	    public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	    }

	    @Override
	    public Object get(final Object key) {
		return map.get(key);
	    }

	    @Override
	    public Object get(final Path inner) throws IncompleteDataException, UnknownPathException {
		if(getStatus().isComplete()) {
		    Map<String, Object> map = DataBuilder.this.map;
		    Object dataObj = null;
		    String name = null;
		    for(Iterator<String> nameIter = inner.iterator(); nameIter.hasNext(); name = nameIter.next()) {
			dataObj = map.get(name);
			if(dataObj == null)
			    throw new IllegalArgumentException("Field with name "+name+" doesn't exist in this map.");
			else if (!(dataObj instanceof Data) && nameIter.hasNext())
			    throw new UnknownPathException();
			else if(nameIter.hasNext()) {
			    map = (Data) dataObj;
			}
		    }
		    return dataObj;
		} else
		    throw new IncompleteDataException();
	    }

	    @Override
	    public Object get(final String str) {
		return get((Object) str);
	    }

	    @Override
	    public Class<?> getObjectClass() throws ClassNotFoundException {
		Object cls = map.get(CLASS);
		return Class.forName((String) cls);
	    }

	    @Override
	    public Status getStatus() {
		// TODO Auto-generated method stub
		return new Status() {

		};
	    }

	    @Override
	    public boolean isEmpty() {
		return map.isEmpty();
	    }

	    @Override
	    public Set<String> keySet() {
		return map.keySet();
	    }

	    @Override
	    public Map<String, Path> listNeeded() {
		Map<String, Path> paths = new HashMap<>();
		for(Map.Entry<String, Object> vars : map.entrySet()) {
		    if(vars instanceof Path) {
			paths.put(vars.getKey(), (Path) vars.getValue());
		    }
		}
		return paths;
	    }

	    @Override
	    public Object put(final String key, final Object value) {
		throw new UnsupportedOperationException();
	    }

	    @Override
	    public void putAll(final Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	    }

	    @Override
	    public Object remove(final Object key) {
		throw new UnsupportedOperationException();
	    }

	    @Override
	    public int size() {
		return map.size();
	    }

	    @Override
	    public Data substituteData(final Path p) throws InvalidClassException {
		String fName = p.getFieldName();
		Object obj = map.get(fName);
		if(obj instanceof Data)
		    return (Data) map.put(fName, p);
		else
		    throw new InvalidClassException(obj.getClass().getName()+"is not instance of Data");
	    }

	    @Override
	    public void substitutePath(final String name, final Data d) {
		Object obj = map.get(name);
		if(obj != null && obj instanceof Path) {
		    map.put(name, d);
		} else
		    throw new IllegalArgumentException();
	    }

	    @Override
	    public String toString() {
		return map.toString();
	    }

	    @Override
	    public Collection<Object> values() {
		return map.values();
	    }

	};
    }

}
