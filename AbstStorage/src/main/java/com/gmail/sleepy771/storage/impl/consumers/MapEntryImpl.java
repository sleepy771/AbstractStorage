package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Map.Entry;

public class MapEntryImpl<K, V> implements Entry<K, V> {
    
    private final K key;
    private final V value;
    
    public MapEntryImpl(K key, V value) {
	this.key = key;
	this.value = value;
    }
    
    @Override
    public K getKey() {
	return this.key;
    }

    @Override
    public V getValue() {
	return this.value;
    }

    @Override
    public V setValue(V value) {
	throw new UnsupportedOperationException("Unmodifiable value");
    }

}
