package com.gmail.sleepy771.storage_interface.settings;

public enum XMLTags {
    SETTING("setting"), OPTION("option"), VALUE("value");
    
    private String tag;
    
    XMLTags(String tag) {
	this.tag = tag;
    }
    
    public String getTag() {
	return this.tag;
    }
}
