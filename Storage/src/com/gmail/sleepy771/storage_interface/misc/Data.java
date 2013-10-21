package com.gmail.sleepy771.storage_interface.misc;
import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Map;

import com.gmail.sleepy771.storage_interface.navigation.DataKey;
import com.gmail.sleepy771.storage_interface.navigation.Path;


public interface Data extends Map<DataKey<?>, Writable<?>>{
	public Serial getSerial();
	public Writable<?> get(Path<DataKey<?>> path) throws NoSuchFieldException;
	public Path<DataKey<?>> whereIs(Writable<?> writable) throws NoSuchObjectException;
	public String getType();
	public boolean isComplete();
	public List<Path<?>> getMissingData();
}
