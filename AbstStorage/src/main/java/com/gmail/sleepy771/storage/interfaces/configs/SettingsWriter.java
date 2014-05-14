package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;

public interface SettingsWriter {
    public void setUp(File f);
    
    public void writeSettings();
}
