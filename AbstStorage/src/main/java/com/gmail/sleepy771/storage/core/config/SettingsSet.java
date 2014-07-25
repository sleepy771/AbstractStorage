package com.gmail.sleepy771.storage.core.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SettingsSet implements Iterable<Setting> {
	private final TreeSet<Setting> settings;

	public SettingsSet() {
		settings = new TreeSet<>();
	}

	public SettingsSet(final Map<String, Object> settings) {
		this.settings = new TreeSet<>();
		fromMap(settings);
	}

	public SettingsSet(final Set<Setting> settings) {
		this.settings = new TreeSet<>(settings);
	}

	public void addSetting(final String name, final Object value) {
		settings.add(new Setting(name, value));
	}

	public void addSettingsFromMap(final Map<String, Object> settingsMap) {
		for (Map.Entry<String, Object> entry : settingsMap.entrySet())
			settings.add(new Setting(entry.getKey(), entry.getValue()));
	}

	public void fromMap(final Map<String, Object> settingsMap) {
		settings.clear();
		for (Map.Entry<String, Object> entry : settingsMap.entrySet())
			settings.add(new Setting(entry.getKey(), entry.getValue()));
	}

	public Map<String, Object> getMap() {
		Map<String, Object> settingsMap = new TreeMap<>();
		for (Setting s : settings)
			settingsMap.put(s.getName(), s.getValue());
		return settingsMap;
	}

	@Override
	public Iterator<Setting> iterator() {
		return settings.iterator();
	}

}
