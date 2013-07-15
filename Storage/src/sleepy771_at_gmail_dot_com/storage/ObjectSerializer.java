package sleepy771_at_gmail_dot_com.storage;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

public abstract class ObjectSerializer {
	
	private TreeSet<String> variablesSet = new TreeSet<String>();
	private Class objectClass;
	
	public ObjectSerializer(Collection<String> varNames){
		this.variablesSet.addAll(varNames);
	}
	
	public boolean validateKeys(ObjectData data){
		return data.keySet().containsAll(variablesSet);
	}
	
	public boolean validateDataType(ObjectData data){
		String clsStr = data.getString(ObjectData.CLASS);
		Class cls = StorageHandler.classMap.get(clsStr);
		return cls.equals(objectClass);
	}
	
	public abstract ObjectData serialize(Object object);
	
	
	public abstract Object deserialize(ObjectData serializationMap);
}
