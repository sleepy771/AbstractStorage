package com.gmail.sleepy771.storage.serialization;

import java.util.UUID;

public class UnserializedObject {
	private final Object o;
	private final UUID uuid, parentUuid;
	
	public UnserializedObject(UUID uuid, UUID parentUuid, Object o) {
		this.uuid = uuid;
		this.parentUuid = parentUuid;
		this.o = o;
	}
	
	public Object get() {
		return o;
	}
	
	public UUID getUUID() {
		return uuid;
	}
}
