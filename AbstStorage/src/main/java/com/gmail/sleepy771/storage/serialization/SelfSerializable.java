package com.gmail.sleepy771.storage.serialization;

import java.io.Serializable;

public interface SelfSerializable extends Serializable {
	public static final String INSTANCE_UUID = "INSTANCE_UUID";
	public static final String DEPENDENCIES = "DEPENDENCIES";
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String PARENT_OBJECT = "PARENT";
}
