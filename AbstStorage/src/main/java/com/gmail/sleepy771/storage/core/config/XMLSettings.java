package com.gmail.sleepy771.storage.core.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gmail.sleepy771.storage.impl.exceptions.ParserNotFoundException;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsReader;
import com.gmail.sleepy771.storage.interfaces.configs.SettingsWriter;
import com.gmail.sleepy771.storage.interfaces.configs.ValueParserManager;

public class XMLSettings implements SettingsReader, SettingsWriter {
	enum Tags{
		SETTING("setting"), NAME("name"), TYPE("type"), VALUE("value");
		
		public final String tagName;
		
		Tags(String tagName) {
			this.tagName = tagName;
		}
		
		public String toString() {
			return this.tagName;
		}
	}
	
	private File xmlSettingsFile;
	private Map<String, Object> innerSettingsMap; 
	private ValueParserManager valueParsers;

	@Override
	public SettingsSet readSettings() {
		return null;
	}
	
	/*
	 * <settings>
	 * 	<setting type="[setting-type]" name="[setting-name]">
	 * 		[value]
	 * 	</setting>
	 * </settings>
	 */
	
	private void parseFile() throws SAXException, IOException, ParserConfigurationException, ParserNotFoundException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(xmlSettingsFile);
		
		NodeList nList = document.getElementsByTagName(Tags.SETTING.tagName);
		
		for (int idx=0; idx<nList.getLength(); idx++) {
			Node node = nList.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String settingName = tagReader(Tags.NAME.tagName, element);
				String settingType = tagReader(Tags.NAME.tagName, element);
				String settingValueUnparsed = tagReader(Tags.NAME.tagName, element);
				Object value = valueParsers.parseValue(settingType, settingValueUnparsed);
				this.innerSettingsMap.put(settingName, value);
			}
		}
	}
	
	private static String tagReader(String tagName, Element element) {
		String tagContainment = element.getAttribute(tagName);
		if (tagContainment == null) {
			Node nSettingName = element.getElementsByTagName(Tags.NAME.tagName).item(0);
			tagContainment = ((Element) nSettingName).getTextContent();
		}
		if (tagContainment == null) {
			throw new DOMException(DOMException.NOT_FOUND_ERR, "Can not find "+tagName+" inside of element "+element.getNodeName());
		}
		return tagContainment;
	}
	
	@Override
	public void setSettingsFile(final File settingsFile) {
		this.xmlSettingsFile = settingsFile;
	}

	@Override
	public void writeSettings(final SettingsSet settings) {
		// TODO Auto-generated method stub

	}

}
