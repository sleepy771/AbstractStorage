package com.gmail.sleepy771.storage_interface.misc;

import com.gmail.sleepy771.storage_interface.collections.ExtendedByteBuffer;

public interface Serial extends Comparable<Serial>, Writable<Serial>{
    public ExtendedByteBuffer asByteArray();
}
