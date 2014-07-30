package com.gmail.sleepy771.storage.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import com.gmail.sleepy771.storage.base.AssociationHandler;
import com.gmail.sleepy771.storage.base.StorableObjectsManager;

public class SerializationHandler {
	private final StorableObjectsManager storables = null;
	private final Stack<UnserializedObject> unserializedObjects = null;

	public Map<String, Object> serializeFields(UUID uuid, SelfSerializable ser) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = ser.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object fieldValue = null;
			String fieldName = field.getName();
			if (Modifier.isStatic(field.getModifiers()) && !(fieldName.equals("serialVersionUID") || field.isAnnotationPresent(ImportantStatic.class)))
				continue;
			if(!field.isAccessible()) {
				if(field.getAnnotation(GrantAccessForSerializer.class)==null) {
					throw new IllegalAccessException("Field "+fieldName+" is not accessible. Try to set GrantAcessForSerializer annotation for field.");
				}
				field.setAccessible(true);
				fieldValue = field.get(ser);
				field.setAccessible(false);
			} else {
				fieldValue = field.get(ser);
			}
			if (fieldValue == null && field.getAnnotation(NotNull.class) != null) {
				throw new NullPointerException("Null pointer spoted on field "+fieldName+", where NotNull annotation was set.");
			}
			
			if(storables.isStorable(fieldValue.getClass())) {
				map.put(fieldName, fieldValue);
			} else if(storables.canBeAssociatedWithStorable(fieldValue.getClass())) {
				AssociationHandler ah = storables.getAssociationHandler(fieldValue.getClass());
				fieldValue = ah.associate(fieldValue);
				map.put(fieldName, fieldValue);
			} else {
				map.put(fieldName, uuid);
				unserializedObjects.add(new UnserializedObject(uuid, uuid, fieldValue));
			}
		}
		return map;
	}
}
