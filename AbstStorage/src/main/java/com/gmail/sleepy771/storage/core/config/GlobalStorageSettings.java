package com.gmail.sleepy771.storage.core.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gmail.sleepy771.storage.interfaces.configs.Configuration;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsReader;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsWriter;

public class GlobalStorageSettings implements Configuration {
    private static GlobalStorageSettings INSTANCE;

    public static GlobalStorageSettings getInstance() {
	// inject as dependency
	// if (GlobalStorageSettings.INSTANCE == null)
	// GlobalStorageSettings.INSTANCE = new GlobalStorageSettings();
	return GlobalStorageSettings.INSTANCE;
    }

    private Map<String, Object> settings = new HashMap<>();
    private SettingsReader reader;

    private SettingsWriter writer;

    private GlobalStorageSettings(final SettingsReader reader,
	    final SettingsWriter writer) {
	this.reader = reader;
	this.writer = writer;
    }

    @Override
    public synchronized void dispose() {
	settings.clear();
	settings = null;
    }

    @Override
    public synchronized Object getSetting(final String settingName) {
	return settings.get(settingName);
    }

    @Override
    public synchronized SettingsReader getSettingsReader() {
	return reader;
    }

    @Override
    public synchronized SettingsWriter getSettingsWriter() {
	return writer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> T getSettingTypeDef(final String settingName) {
	return (T) settings.get(settingName);
    }

    @Override
    public synchronized void load() {
	try {
	    settings.clear();
	    SettingsSet settingsSet = reader.readSettings();
	    settings.putAll(settingsSet.getMap());
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	}
    }

    @Override
    public synchronized void reloadSettings() {
	try {
	    SettingsSet settingsSet = reader.readSettings();
	    Map<String, Object> settingsMap = settingsSet.getMap();
	    if (!settingsMap.isEmpty()) {
		settings.clear();
		settings.putAll(settingsMap);
	    }
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	    // TODO Load Exception
	}
    }

    @Override
    public synchronized void save() {
	try {
	    if (!settings.isEmpty()) {
		SettingsSet settingsSet = new SettingsSet(settings);
		writer.writeSettings(settingsSet);
	    }
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    // TODO log and notify
	}
    }

    @Override
    public synchronized void setSetting(final String settingName,
	    final Object value) {
	settings.put(settingName, value);
    }

    @Override
    public synchronized void setSettingsFile(final File file) {
	reader.setSettingsFile(file);
	writer.setSettingsFile(file);
    }

    @Override
    public synchronized void setSettingsReader(final SettingsReader reader) {
	if (reader != null)
	    this.reader = reader;
    }

    @Override
    public synchronized void setSettingsWriter(final SettingsWriter writer) {
	if (writer != null)
	    this.writer = writer;
    }
}
