package com.gmail.sleepy771.storage.base;

public interface AssociationHandler {
	public Object associate(Object object);
	
	public Object revAssociate(Object associatedObject, Class<?> cls);
}
