package com.gmail.sleepy771.storage.base;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface StorableObjectsManager extends Manager{
	
	public static Set<Class<?>> BASIC_STORABLE_OBJECTS = Collections.unmodifiableSet(new HashSet<Class<?>>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 769738574993975295L;

		{
			add(Byte.class);
			add(Character.class);
			add(Boolean.class);
			add(Short.class);
			add(Integer.class);
			add(Long.class);
			add(Float.class);
			add(Double.class);
			add(String.class);
			add(List.class);
			add(UUID.class);
		}
	});
	
	public boolean isStorable(Class<?> cls);
	
	public boolean canBeAssociatedWithStorable(Class<?> cls);
	
	public AssociationHandler getAssociationHandler(Class<?> cls);
	
	public void registerAssociationHandlers(AssociationHandler h);
}
