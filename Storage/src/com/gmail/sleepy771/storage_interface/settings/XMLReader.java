package com.gmail.sleepy771.storage_interface.settings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {
    /*
     * Syntax: grant only 2 levels third are values <SETTING
     * name="some_setting_name"> <OPTION
     * name="some_option_name"><VALUE></VALUE></OPTION> <OPTION
     * name="other_option_name"><VALUE>value_2</VALUE></OPTION> <OPTION
     * name="more_options_name"><VALUE>value_3</VALUE> ... <VALUE>value_n</VALUE></OPTION> </SETTING> <SETTING
     * name="other_setting_name"> <OPTION
     * name="some_option_name">value_1</OPTION> <OPTION
     * name="other_option_name">value_2</OPTION> </SETTING>
     */
    private File xmlSettingsFile;
    private Map<String, Map<String, List<String>>> output;
    
    public void setXMLFile(File xmlFile) {
	if(xmlFile != null && xmlFile.canRead()) {
	    this.xmlSettingsFile = xmlFile;
	}
    }

    private void readXML() throws ParserConfigurationException, SAXException, IOException {
	if(this.xmlSettingsFile == null)
	    throw new IllegalArgumentException("Set path to XML config file");
	DocumentBuilderFactory docBuilderFacotry = DocumentBuilderFactory
		.newInstance();
	DocumentBuilder documentBuilder = docBuilderFacotry
		.newDocumentBuilder();
	Document document = documentBuilder.parse(xmlSettingsFile);
	document.getDocumentElement().normalize();
	NodeList nList = document.getElementsByTagName(XMLTags.SETTING.getTag());
	output = new HashMap<String, Map<String, List<String>>>(nList.getLength());
	
	for (int i = 0; i < nList.getLength(); i++) {
	    Node node = nList.item(i);

	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		Element element = Element.class.cast(node);
		String settingName = element.getAttribute("name");
		
		

		NodeList oList = element.getElementsByTagName(XMLTags.OPTION.getTag());
		
		Map<String, List<String>> options = new HashMap<String, List<String>>();
		
		output.put(settingName, options);
		
		for (int j = 0; j < oList.getLength(); j++) {
		    Node oNode = oList.item(j);
		    if (oNode.getNodeType() == Node.ELEMENT_NODE) {
			
			Element oElement = Element.class.cast(oNode);
			String optionName = element.getAttribute("name");
			
			NodeList vList = oElement.getElementsByTagName(XMLTags.VALUE.getTag());
			List<String> values = new ArrayList<String>(vList.getLength());
			options.put(optionName, values);
			for(int k=0; k< vList.getLength(); k++) {
			    values.add(vList.item(k).getTextContent());
			}
		    }
		}
	    }
	}
    }
    
    public Map<String, Map<String, List<String>>> getRules() {
	try{
	    readXML();
	} catch (IOException ioe) {
	    //Write logs
	    ioe.printStackTrace();
	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Map<String, Map<String, List<String>>> output = this.output;
	this.output.clear();
	this.output = null;
	return output;
    }
}
