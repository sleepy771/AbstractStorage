package com.gmail.sleepy771.storage.impl;

import java.util.Map;
import java.util.Map.Entry;

import com.gmail.sleepy771.storage.interfaces.datastructures.Duplet;

public class DupletImpl<U, W> implements Duplet<U, W> {
    
    private final U u;
    private final W w;
    
    public DupletImpl(U u, W w) {
	this.u = u;
	this.w = w;
    }
    
    public DupletImpl(Entry<U, W> entry) {
	this(entry.getKey(), entry.getValue());
    }
    
    @Override
    public U getFirst() {
	return this.u;
    }

    @Override
    public W getSecond() {
	return this.w;
    }

}
