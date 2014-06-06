package com.gmail.sleepy771.storage.interfaces.configs;

import java.io.File;
import java.io.FileNotFoundException;

public interface Configuration {

    public void dispose();

    public Object getSetting(String settingName);

    public SettingsReader getSettingsReader();

    public SettingsWriter getSettingsWriter();

    public <T> T getSettingTypeDef(String settingName);

    public void load() throws FileNotFoundException;

    public void reloadSettings();

    public void save();

    public void setSetting(String settingName, Object value);

    public void setSettingsFile(File file);

    public void setSettingsReader(SettingsReader reader);

    public void setSettingsWriter(SettingsWriter writer);
}
