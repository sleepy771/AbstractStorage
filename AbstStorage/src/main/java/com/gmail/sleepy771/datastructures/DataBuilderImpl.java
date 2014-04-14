package com.gmail.sleepy771.datastructures;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gmail.sleepy771.exceptions.NotBuildableException;
import com.gmail.sleepy771.storage.Path;

public class DataBuilderImpl implements DataBuilder {
    private static final Set<Class<?>> WRITABLE_TYPES = Collections.unmodifiableSet(new HashSet<Class<?>>(
	    Arrays.asList(Byte.class, Character.class, Short.class,
		    Integer.class, Long.class, Float.class, Double.class,
		    BigDecimal.class, BigInteger.class, ByteBuffer.class, String.class))); 
    private Map<String, Object> data = new HashMap<>();
    private String objName;
    private Class<?> repClass;
    
    public DataBuilderImpl(DataInt d) {
	this.objName = d.getName();
	this.repClass = d.getRepresentedClass();
	for(Entry<String, Object> entry : d.getRawEntrySet()) {
	    if(!DataInt.class.isInstance(entry.getValue())) {
		this.data.put(entry.getKey(), entry.getValue());
	    } else {
		DataInt di = DataInt.class.cast(entry.getValue());
		DataBuilderImpl db = new DataBuilderImpl(di);
		this.data.put(entry.getKey(), db);
	    }
	}
    }
    
    public DataBuilderImpl(DataBuilderImpl db) {
	this.objName = db.objName;
	this.repClass = db.repClass;
	this.data.putAll(db.data);
    }
    
    public DataBuilderImpl(String objName, Class<?> repClazz) {
	this.objName = objName;
	this.repClass = repClazz;
    }
    
    public DataBuilderImpl() {
	this(null, null);
    }
    
    @Override
    public DataInt build() throws NotBuildableException {
	if (objName == null || repClass == null || data.isEmpty()) {
	    throw new NotBuildableException();
	}
	DataInt d = new DataInt() {
	    private final Map<String, Object> data = DataBuilderImpl.this.data;
	    private final String objName = DataBuilderImpl.this.objName;
	    private final Class<?> repClass = DataBuilderImpl.this.repClass;
	    
	    @Override
	    public Iterator<Entry<String, Object>> iterator() {
		
		Iterator<Entry<String,Object>> iterator = new Iterator<Entry<String,Object>>() {
		    
		    LinkedList<Iterator<Entry<String, Object>>> iteratorStack = new LinkedList<>();
		    Iterator<Entry<String, Object>> currentIterator = data.entrySet().iterator();
		    StringBuilder pathBuilder = new StringBuilder();
		    @Override
		    public boolean hasNext() {
			// TODO Auto-generated method stub
			return currentIterator.hasNext() || !iteratorStack.isEmpty();
		    }

		    @Override
		    public Entry<String, Object> next() {
			Entry<String, Object> obj = currentIterator.next();
			
			while(obj.getValue() instanceof DataInt) {
			    if(currentIterator.hasNext()) {
				iteratorStack.addFirst(currentIterator);
			    }
			    currentIterator = DataInt.class.cast(obj.getValue()).iterator();
			    pathBuilder.append('.').append(obj.getKey());
			    if(!currentIterator.hasNext()) {
				currentIterator = iteratorStack.removeFirst();
			    }
			    obj = currentIterator.next();
			}
			final StringBuilder path = new StringBuilder(pathBuilder);
			final Object val = obj.getValue();
			path.append(obj.getKey());
			Entry<String, Object> entry = new Entry<String, Object>(){
			    private final String key = path.toString();
			    private final Object value = val;
			    @Override
			    public String getKey() {
				return this.key;
			    }

			    @Override
			    public Object getValue() {
				return this.value;
			    }

			    @Override
			    public Object setValue(Object value) {
				throw new UnsupportedOperationException();
			    }
			    
			};
			return entry;
		    }

		    @Override
		    public void remove() {
			throw new UnsupportedOperationException();
		    }
		    
		};
		return iterator;
	    }

	    @Override
	    public Object get(String name) {
		return this.data.get(name);
	    }

	    @Override
	    public Object get(Path p) {
		Object obj = null;
		DataInt di = this;
		Iterator<String> place = p.iterator();
		while(place.hasNext()) {
		    obj = di.get(place.next());
		    if(place.hasNext()) {
			if(!DataInt.class.isInstance(obj)) {
			    throw new InvalidPathException(p.toString(), obj.toString() + "is not data type");
			} else {
			    di = DataInt.class.cast(obj);
			}
		    }
		}
		return obj;
	    }

	    @Override
	    public String getName() {
		return this.objName;
	    }

	    @Override
	    public Map<String, Object> getByClass(Class<?> clazz) {
		Map<String, Object> objs = new HashMap<>();
		for(Entry<String, Object> entry : this.data.entrySet()) {
		    if(clazz.isInstance(entry.getValue())) {
			objs.put(entry.getKey(), entry.getValue());
		    }
		}
		return objs;
	    }

	    @Override
	    public Class<?> getRepresentedClass() {
		return this.repClass;
	    }

	    @Override
	    public Set<Entry<String, Object>> getRawEntrySet() {
		Set<Entry<String, Object>> raw = new HashSet<>(this.data.entrySet());
		return raw;
	    }
	    
	};
	return d;
    }

    @Override
    public Object get(String name) {
	return this.data.get(name);
    }

    @Override
    public Object get(Path p) {
	Object obj = null;
	DataInt di = this;
	Iterator<String> place = p.iterator();
	while(place.hasNext()) {
	    obj = di.get(place.next());
	    if(place.hasNext()) {
		if(!DataInt.class.isInstance(obj)) {
		    throw new InvalidPathException(p.toString(), obj.toString() + "is not data type");
		} else {
		    di = DataInt.class.cast(obj);
		}
	    }
	}
	return obj;
    }

    @Override
    public String getName() {
	return this.objName;
    }

    @Override
    public Map<String, Object> getByClass(Class<?> clazz) {
	Map<String, Object> objs = new HashMap<>();
	for(Entry<String, Object> entry : this.data.entrySet()) {
	    if(clazz.isInstance(entry.getValue()))
		objs.put(entry.getKey(), entry.getValue());
	}
	return objs;
    }

    @Override
    public Class<?> getRepresentedClass() {
	return this.repClass;
    }

    @Override
    public DataBuilder put(String name, Object obj) {
	if(WRITABLE_TYPES.contains(obj.getClass()) || DataInt.class.isInstance(obj)) {
	    this.data.put(name, obj);
	}
	return this;
    }

    @Override
    public DataBuilder putAll(Map<String, Object> data) {
	for (Entry<String, Object> entry : data.entrySet()) {
	    put(entry.getKey(), entry.getValue());
	}
	return this;
    }

    @Override
    public DataBuilder putAll(DataInt d) {
	putAll(new DataBuilderImpl(d));
	return this;
    }

    @Override
    public DataBuilder putAll(DataBuilder d) {
	this.data.putAll(d.getRaw());
	return this;
    }

    @Override
    public DataBuilder remove(String name) {
	this.data.remove(name);
	return this;
    }

    @Override
    public DataBuilder removeAll(Collection<String> names) {
	for(String name : names) {
	    this.data.remove(name);
	}
	return this;
    }

    @Override
    public DataBuilder clear() {
	this.objName = null;
	this.repClass = null;
	this.data.clear();
	return this;
    }

    @Override
    public DataBuilder setName(String name) {
	this.objName = name;
	return this;
    }

    @Override
    public DataBuilder setClass(Class<?> clazz) {
	this.repClass = clazz;
	return this;
    }

    @Override
    public Iterator<Entry<String, Object>> iterator() {
	return this.data.entrySet().iterator();
    }

    @Override
    public Map<String, Object> getRaw() {
	return new HashMap<>(this.data);
    }

    @Override
    public Set<Entry<String, Object>> getRawEntrySet() {
	return this.data.entrySet();
    }

}
