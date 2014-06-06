package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;
import java.io.FileNotFoundException;

import com.gmail.sleepy771.storage.core.config.SettingsSet;

public interface SettingsReader {
    public SettingsSet readSettings() throws FileNotFoundException;

    public void setSettingsFile(File settingsFile);
}
