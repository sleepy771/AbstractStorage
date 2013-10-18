package com.gmail.sleepy771.storage_interface.settings;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.sleepy771.storage_interface.exceptions.UnchecableException;
import com.gmail.sleepy771.storage_interface.exceptions.UnknownStorageException;
import com.gmail.sleepy771.storage_interface.misc.StorageInterface;
import com.gmail.sleepy771.storage_interface.navigation.DataKey;

public class Settings {
    private static HashMap<String, DataKey<String>> typePlacements;
    // Change to be able to find configs
    private static File configFolder = new File("/home/filip/.si_config");
    // currently running storage interfaces
    private static HashMap<String, StorageInterface> storages;

    // TODO parse xml

    public static DataKey<String> determinePlacement(String type) {
	if (typePlacements == null || typePlacements.isEmpty()) {
	    readTypePlacements(configFolder, "storage_settings.xml");
	}
	return typePlacements.get(type);
    }

    public static void init(File settingsFolder) {
	if (settingsFolder == null || !settingsFolder.isDirectory()) {

	}
	File[] settingsFiles = settingsFolder.listFiles(new FilenameFilter() {
	    @Override
	    public boolean accept(File dir, String name) {
		int idx = name.lastIndexOf('.');
		String postfix = name.substring(idx + 1);
		return postfix.toLowerCase().equals("xml");
	    }
	});
	XMLReader xmlreader = new XMLReader();
	for (File file : settingsFiles) {
	    xmlreader.setXMLFile(file);
	    // TODO parsovat settings
	    xmlreader.getRules();
	}
    }

    private static void readTypePlacements(File folder, String fileName) {
	File xmlFile = new File(folder.getPath() + File.pathSeparator
		+ fileName);
	if (typePlacements == null)
	    typePlacements = new HashMap<String, DataKey<String>>();
	if (!(xmlFile.exists() && xmlFile.canRead())) {
	    return;
	}
	XMLReader xmlreader = new XMLReader();
	xmlreader.setXMLFile(xmlFile);
	Map<String, Map<String, List<String>>> inp = xmlreader.getRules();
	Map<String, List<String>> predefinedPlaces = inp.get("type_placement");
	for (Map.Entry<String, List<String>> entry : predefinedPlaces
		.entrySet()) {
	    try {
		if (storages.containsKey(new DataKey<String>(entry.getKey()))) {
		    for (String typeName : entry.getValue()) {
			typePlacements.put(typeName,
				new DataKey<String>(entry.getKey()));
		    }
		}
	    } catch (UnchecableException ue) {
		// this exception will newer occur
		ue.printStackTrace();
	    }
	}
    }
    
    // Tento throws je od veci TODO treba prerobit
    public static StorageInterface getStorage(String storageName) throws UnchecableException, UnknownStorageException {
	return getStorage(new DataKey<String>(storageName));
    }
    
    public static StorageInterface getStorage(DataKey<String> storageKey) throws UnknownStorageException {
	if(!storages.containsKey(storageKey))
	    throw new UnknownStorageException();
	return storages.get(storageKey);
    }

    public static DataKey<String> defaultSotrageKey() {
	// TODO Auto-generated method stub
	return null;
    }
    
    //TODO later create DataKey<?> () + method to transform String to ? in DataKey

}
