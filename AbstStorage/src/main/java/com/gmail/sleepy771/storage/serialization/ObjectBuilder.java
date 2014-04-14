package com.gmail.sleepy771.storage.serialization;

import com.gmail.sleepy771.datastructures.DataInt;
import com.gmail.sleepy771.storage.Buildable;

public interface ObjectBuilder<T> extends Buildable<T> {
    public ObjectBuilder<T> inject(DataInt d);

    public ObjectBuilder<T> validate();
}
