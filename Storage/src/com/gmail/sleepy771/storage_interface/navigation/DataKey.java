package com.gmail.sleepy771.storage_interface.navigation;

import com.gmail.sleepy771.storage_interface.exceptions.UnchecableException;
import com.gmail.sleepy771.storage_interface.misc.Writable;
import com.gmail.sleepy771.storage_interface.misc.WritableReference;


public class DataKey<T extends Comparable<T>> implements Writable<T>, Comparable<DataKey<T>> {
    WritableReference<T> reference;
    
    public DataKey() {
	//TODO auto-generated
	reference = new WritableReference<T>();
    }
    
    public DataKey(T object) throws UnchecableException {
	reference = new WritableReference<T>();
	if(object != null)
	    set(object);
    }
    
    @Override
    public int compareTo(DataKey<T> o) throws NullPointerException {
	return reference.get().compareTo(o.get());
    }

    @Override
    public T get() throws NullPointerException {
	return reference.get();
    }

    @Override
    public void set(T object) throws UnchecableException {
	reference.set(object);
    }

    @Override
    public Class<?> getInnerClass() throws NullPointerException {
	return reference.getInnerClass();
    }

    @Override
    public boolean isNull() {
	return reference.isNull();
    }

}
