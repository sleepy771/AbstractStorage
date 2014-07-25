package com.gmail.sleepy771.storage.impl;

import java.util.Map;
import java.util.Map.Entry;

import com.gmail.sleepy771.storage.interfaces.datastructures.Atomic;

public class AtomicImpl<U, W> implements Atomic<U, W> {

	private final U u;
	private final W w;

	public AtomicImpl(U u, W w) {
		this.u = u;
		this.w = w;
	}

	public AtomicImpl(Entry<U, W> entry) {
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
