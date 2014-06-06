package com.gmail.sleepy771.storage.core.config;

import java.io.File;

import com.gmail.sleepy771.storage.interfaces.configs.SettingsReader;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsWriter;

public class XMLSettings implements SettingsReader, SettingsWriter {
    private File settingsFile;

    @Override
    public SettingsSet readSettings() {
	return null;
    }

    @Override
    public void setSettingsFile(final File settingsFile) {
	this.settingsFile = settingsFile;
    }

    @Override
    public void writeSettings(final SettingsSet settings) {
	// TODO Auto-generated method stub

    }

}
