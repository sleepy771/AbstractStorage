package com.gmail.sleepy771.storage_interface.misc;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.gmail.sleepy771.storage_interface.collections.ExtendedByteBuffer;
import com.gmail.sleepy771.storage_interface.collections.HashSetWithImmutablePart;
import com.gmail.sleepy771.storage_interface.exceptions.UnchecableException;

public final class WritableReference<T> implements Writable<T> {
    public static final HashSetWithImmutablePart<Class<? extends Object>> VALID_WRITABLE_CLASSES = new HashSetWithImmutablePart<Class<? extends Object>>(
	    Arrays.<Class<? extends Object>> asList(Boolean.class, Byte.class,
		    Character.class, Short.class, Integer.class, Long.class,
		    BigInteger.class, Float.class, Double.class,
		    BigDecimal.class, String.class,
		    ByteArrayOutputStream.class, Map.class, ExtendedByteBuffer.class));

    private T obj = null;

    public WritableReference(T writable) throws UnchecableException {
	set(writable);
    }

    public WritableReference() {
    }

    public T get() throws NullPointerException {
	if(isNull())
	    throw new NullPointerException();
	return this.obj;
    }

    public void set(T obj) throws UnchecableException {
	if (!isNull())
	    throw new IllegalArgumentException("Reference alredy in use");
	if (isValid(obj)) {
	    this.obj = obj;
	} else {
	    throw new IllegalArgumentException("Unsupported type");
	}
    }

    private boolean isValid(Object obj) throws UnchecableException {
	if(obj == null)
	    return false;
	if (!obj.getClass().equals(Map.class))
	    return VALID_WRITABLE_CLASSES.contains(obj.getClass());
	else {
	    return validateMapElementTypes(Map.class.cast(obj));
	}
    }

    private boolean validateMapElementTypes(Map<?, ?> map)
	    throws UnchecableException {
	if (map.isEmpty())
	    throw new UnchecableException();
	boolean isValidMap = true;
	Object key = null;
	for (Iterator<?> iterator = map.keySet().iterator(); iterator.hasNext()
		&& isValidMap; key = iterator.next()) {
	    // is needed to check whether key type is wirtable and comparable at
	    // the same time
	    isValidMap &= Comparable.class.isInstance(key.getClass())
		    && WritableReference.class.isInstance(key.getClass());
	    Object value = map.get(key);
	    if (value.getClass().equals(Map.class)) {
		isValidMap &= validateMapElementTypes(Map.class.cast(value));
	    } else {
		isValidMap &= VALID_WRITABLE_CLASSES.contains(value.getClass());
	    }
	}
	return isValidMap;
    }
    
    @Override
    public boolean isNull() {
	return obj == null;
    }

    @Override
    public Class<?> getInnerClass() throws NullPointerException {
	if (isNull())
	    throw new NullPointerException();
	return obj.getClass();
    }

}
