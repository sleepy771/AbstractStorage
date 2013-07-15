package sleepy771_at_gmail_dot_com.storage;

import java.util.Map;


/**
 * Object that extends data storing in unified form for all types of storage.
 * @author filip
 *
 */
public abstract class AbstractStorage {
	
	public AbstractStorage(){
		
	}
	
	public abstract boolean store(ObjectData object);
	
	public abstract boolean load(Identificator ident);
	
	public abstract ObjectData getBy(Identificator id);
	
	public abstract ObjectData find(StorageQuery q);
	
	public abstract boolean move();
	
	public abstract boolean backup();
	
	public abstract long capacity();
	
	public abstract long size();
	
	public abstract long availibleSpace();
}
