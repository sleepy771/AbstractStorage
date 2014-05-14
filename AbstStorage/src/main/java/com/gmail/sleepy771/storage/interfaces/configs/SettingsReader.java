package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;
import java.util.Map;

public interface SettingsReader {
    public void setUp(File settingsFile);
    
    public Map<String, Object> readSettings();
}
