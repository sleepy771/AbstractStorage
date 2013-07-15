package sleepy771_at_gmail_dot_com.storage;

import java.util.HashMap;

// Musi byt preveditelna na SQL querry resp. prikaz: WHERE
public abstract class Condition {
	
	public abstract boolean meetCondition(ObjectData data);
	
	
}
