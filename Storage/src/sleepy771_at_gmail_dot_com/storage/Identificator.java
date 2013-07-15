package sleepy771_at_gmail_dot_com.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public abstract class Identificator {
	HashSet<Condition> cond;
	
	public Set<Condition> getConditions(){
		return this.cond;
	}
	
	public boolean meetConditions(HashMap<String, String> dataMap){
		return false;
	}
}
