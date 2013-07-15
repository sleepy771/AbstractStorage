package sleepy771_at_gmail_dot_com.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;

public class StorageHandler implements Runnable{
	
	AbstractStorage storage;
	
	HashMap<Class, ObjectSerializer> serializersMap = new HashMap<Class, ObjectSerializer>();
	
	public static HashMap<String,Class> classMap = new HashMap<String,Class>();
	static{
		classMap.put(String.class.getName(), String.class);
	}
	
	public boolean store(Object object) throws SerializationException{
		if(!serializersMap.containsKey(object.getClass())){
			throw new SerializationException();
		}
		
		ObjectSerializer os = serializersMap.get(object.getClass());
		ObjectData data = os.serialize(object);
		storage.store(data);
		
		return false;
	}
	
	public Object load(Identificator id){
		ObjectData dat = storage.getBy(id);
		//TODO kontrola ci existuje
		ObjectSerializer os = serializersMap.get(classMap.get(dat.get(ObjectData.CLASS)));
		Object object = os.deserialize(dat);
		return object;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
