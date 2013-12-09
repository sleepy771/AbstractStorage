package com.gmail.sleepy771.storage_interface.utils;

import java.util.EnumSet;

public class Status {

    enum Flag {
	COMPLETE, ERROR, INCOMPLETE, PREPARING, WAITING, WARNING;
    }

    public EnumSet<Flag> getEnumSet() {
	return null;
    }

    public boolean isComplete() {
	return true;
    }
}
