package com.gmail.sleepy771.storage.interfaces.datastructures;

public interface MutableAtomic<U, W> extends Atomic<U, W> {
    public U setFirst(U u);
    public W setSecond(W w);
}
