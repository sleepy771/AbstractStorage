package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;

public interface Configuration {
    
    public void setSettingsReader(SettingsReader reader);
    
    public void setSettingsWriter(SettingsWriter writer);
    
    public Object getSetting(String settingName);
    
    public <T> T getSettingTypeDef(String settingName);
    
    public void setSetting(String settingName, Object value);
    
    public void read(File settingsFile);
    
    public void dispose();
    
    public void write(File settingsFile);
}
