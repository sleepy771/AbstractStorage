package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;
import java.io.IOException;

import com.gmail.sleepy771.storage.core.config.SettingsSet;

public interface SettingsWriter {
	public void setSettingsFile(File f);

	public void writeSettings(SettingsSet settings) throws IOException;
}
