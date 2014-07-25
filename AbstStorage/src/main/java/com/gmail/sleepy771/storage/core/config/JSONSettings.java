package com.gmail.sleepy771.storage.core.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gmail.sleepy771.storage.interfaces.configs.SettingsReader;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsWriter;
import com.google.gson.Gson;

public class JSONSettings implements SettingsReader, SettingsWriter {

	private File file;
	private final Gson gson;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gmail.sleepy771.storage.interfaces.configs.SettingsReader#readSettings
	 * () json file: [ { "name": "settingName", "value": "value", }, { "name":
	 * "settingName2", "value": "value2", } ]
	 */

	public JSONSettings() {
		this(new Gson(), new File("globalsettings.json"));
	}

	public JSONSettings(final Gson gson, final File settingsFile) {
		this.gson = gson;
		file = settingsFile;
	}

	@Override
	public SettingsSet readSettings() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		SettingsSet set = gson.fromJson(br, SettingsSet.class);
		return set;
	}

	@Override
	public void setSettingsFile(final File settingsFile) {
		file = settingsFile;
	}

	@Override
	public void writeSettings(final SettingsSet settings) throws IOException {
		FileWriter fw = new FileWriter(file);
		fw.write(gson.toJson(settings));
		fw.close();
	}

}
