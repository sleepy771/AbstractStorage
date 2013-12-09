package com.gmail.sleepy771.storage_interface.utils;

import com.gmail.sleepy771.storage_interface.utils.excetions.UncompletableException;

public interface Result {
    public void force() throws Exception;

    public Object get() throws UncompletableException;

    public String getMessage();

    public Status getStatus();
}
