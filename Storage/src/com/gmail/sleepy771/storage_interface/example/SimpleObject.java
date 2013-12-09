package com.gmail.sleepy771.storage_interface.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gmail.sleepy771.storage_interface.utils.Data;
import com.gmail.sleepy771.storage_interface.utils.DataBuilder;
import com.gmail.sleepy771.storage_interface.utils.Serializable;

public class SimpleObject implements Serializable {
    private static final List<String> FIELD_NAMES = Collections
	    .unmodifiableList(Arrays.asList("name", "number", "decNumber"));

    private float decNumber;
    private String name;
    private int number;

    @Override
    public Collection<String> fieldNames() {
	return FIELD_NAMES;
    }

    public float getDecimal() {
	return decNumber;
    }

    public String getName() {
	return name;
    }

    public int getNumber() {
	return number;
    }

    @Override
    public Data serialize() {
	return new DataBuilder("SimpleObject", SimpleObject.class)
		.addObject("name", name).addObject("decNumber", decNumber)
		.addObject("number", number).build();
    }

    public void setDecimal(final float f) {
	decNumber = f;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public void setNumber(final int n) {
	number = n;
    }
}
