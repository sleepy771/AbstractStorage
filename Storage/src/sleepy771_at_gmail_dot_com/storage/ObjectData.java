package sleepy771_at_gmail_dot_com.storage;

import java.util.HashMap;

public class ObjectData extends HashMap<String, Object> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7828220738368160620L;
	
	public static final String CLASS = "a2f2ed4f8ebc2cbb4c21a29dc40ab61d";
	
	public ObjectData(Class rep){
		/*
		 * key: "a2f2ed4f8ebc2cbb4c21a29dc40ab61d" is md5 of "class".
		 * The key is chosen not to collide with other variable names
		 */
		this.put(CLASS, rep.getName());
	}
	
	public Class getObjectClass(){
		return StorageHandler.classMap.get(get(CLASS));
	}
	
	public Class getVariableClass(String name){
		exists(name);
		return get(name).getClass();
	}
	
	// TODO RenameTo: ValidateKey
	private void exists(String name){
		if(!containsKey(name))
			throw new IllegalArgumentException("Object not found!");
	}
	
	public String getString(String name){
		exists(name);
		return get(name).toString();
	}
	
	public Integer getInteger(String name){
		exists(name);
		Object o = get(name);
		
		if(o.getClass().equals(String.class)){
			String strO = String.class.cast(o);
			try {
				o = Integer.valueOf(strO);
			} catch (NumberFormatException nfe){
				return null;
			}
		}
			
		//Generovat exception
		if(!o.getClass().equals(Integer.class))
			return null;
		return Integer.class.cast(o);
	}
	
	public Short getShort(String name){
		exists(name);
		Object o = get(name);
		if(!o.getClass().equals(Short.class))
			return null;
		return Short.class.cast(o);
	}
	
	public Byte getByte(String name){
		exists(name);
		Object o = get(name);
		if(!o.getClass().equals(Byte.class))
			return null;
		return Byte.class.cast(o);
	}
	
	public Boolean getBoolean(String name){
		exists(name);
		Object o = get(name);
		if(!o.getClass().equals(Boolean.class))
			return null;
		return Boolean.class.cast(o);
	}
	
	public Double getDouble(String name){
		exists(name);
		Object o = get(name);
		if(!o.getClass().equals(Double.class))
			return null;
		return Double.class.cast(o);
	}
	
	public Float getFloat(String name){
		exists(name);
		Object o = get(name);
		if(!o.getClass().equals(Float.class))
			return null;
		return Float.class.cast(o);
	}
	
	
}
