package com.gmail.sleepy771.storage_interface.fromat;

import java.nio.ByteBuffer;

public class StringFormat implements Format<String>{
    String str;
    
    @Override
    public String get() {
	return this.str;
    }

    @Override
    public void set(String type) {
	this.str = type;
    }

    @Override
    public <R> Format<R> changeFormat(Formater<String, R> formater) {
	return null;
    }

}
