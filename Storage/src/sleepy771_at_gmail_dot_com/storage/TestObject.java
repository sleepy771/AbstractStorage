package sleepy771_at_gmail_dot_com.storage;

import java.io.Serializable;

public class TestObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -972987132650837896L;
	
	int a;
	
	public TestObject(int a){
		this.a = a;
	}
	
	public TestObject multiply(int b){
		return new TestObject(this.a * b);
	}
	
	public TestObject multiply(TestObject objB){
		return multiply(objB.getValue());
	}
	
	public int getValue(){
		return this.a;
	}
	
	public void setValue(int a){
		this.a = a;
	}
}
