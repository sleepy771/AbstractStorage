package com.gmail.sleepy771.storage.interfaces.datastructures;

public interface MutableDuplet<U, W> extends Duplet<U, W> {
    public U setFirst(U u);
    public W setSecond(W w);
}
