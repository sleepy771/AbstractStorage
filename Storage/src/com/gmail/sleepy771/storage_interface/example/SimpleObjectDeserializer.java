package com.gmail.sleepy771.storage_interface.example;

import com.gmail.sleepy771.storage_interface.utils.Data;
import com.gmail.sleepy771.storage_interface.utils.Deserializer;

public class SimpleObjectDeserializer implements Deserializer {

    @Override
    public Object deserialize(final Data d) {
	SimpleObject obj = new SimpleObject();
	obj.setDecimal((float) d.get("decNumber"));
	obj.setNumber((int) d.get("number"));
	obj.setName((String) d.get("name"));
	return obj;
    }

}
