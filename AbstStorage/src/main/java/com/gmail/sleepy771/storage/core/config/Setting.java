package com.gmail.sleepy771.storage.core.config;

import com.gmail.sleepy771.storage.interfaces.configs.ValueParserManager;

public class Setting {

	private final String settingName;
	private Object settingValue;
	private ValueParserManager valueParsers;

	public Setting(final String name, final Object value) {
		settingName = name;
		settingValue = value;
	}

	public String getName() {
		return settingName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getTypedefValue() {
		return (T) settingValue;
	}

	public Object getValue() {
		return settingValue;
	}

	public void setValue(final Object newValue) {
		if (!newValue.getClass().equals(settingValue))
			throw new IllegalArgumentException("Value types are not equal");
		settingValue = newValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Setting: {\n");
		sb.append("\tname: ").append(settingName).append("\n");
		sb.append("\ttype: ").append(settingValue.getClass().toString())
				.append("\n");
		sb.append("\tvalue: ").append(settingValue.toString()).append("\n}");
		return sb.toString();
	}
}
