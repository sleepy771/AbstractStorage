package com_gmail_sleepy771.astorage.examples.plain_text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com_gmail_sleepy771.astorage.AbstractStorage;
import com_gmail_sleepy771.astorage.StorageControl;
import com_gmail_sleepy771.astorage.exceptions.StorageException;
import com_gmail_sleepy771.astorage.parser.ValueParser;
import com_gmail_sleepy771.astorage.utilities.ObjectData;
import com_gmail_sleepy771.astorage.utilities.Query;
import com_gmail_sleepy771.astorage.utilities.UDID;

public class TextFileStorage extends AbstractStorage {
	private File textFile;
	public static final String DATA = "Data";
	public static final String REFERENCES = "References";
	public static final String SEPARATOR = ": ";

	public TextFileStorage(File file) {
		this.textFile = file;
		if (!file.exists())
			try {
				textFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void store(ObjectData data) throws IOException {
		FileWriter fw = new FileWriter(textFile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter writer = new PrintWriter(bw);
		
		writer.println(DATA+SEPARATOR + data.getSerialNumber() + " {");
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			writer.println(entry.getKey() + SEPARATOR + entry.getValue().toString());
		}
		writer.println(REFERENCES);
		for (Map.Entry<String, UDID> entry : data.getReferenceSerials()
				.entrySet()) {
			writer.println(entry.getKey() + SEPARATOR + entry.getValue().toString());
		}
		writer.println("}");
		writer.flush();
		writer.close();
	}

	@Override
	public void storeAll(Set<ObjectData> dataCollection) throws IOException {
		for (ObjectData data : dataCollection)
			store(data);

	}

	@Override
	public Set<ObjectData> load(Query q) throws IOException {
		Set<ObjectData> set = new HashSet<ObjectData>();
		FileReader fr = new FileReader(textFile);
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
		ObjectData od = null;
		boolean references = false;
		int i = 0;
		while((line = reader.readLine())!=null){
			System.out.println("Line: "+i++);
			if(line.contains(DATA)){
				references = false;
				Pattern headPattern = Pattern.compile("\\p{XDigit}+");
				Matcher serialMatcher = headPattern.matcher(line.split(SEPARATOR)[1]);
				serialMatcher.find();
				String serialFound = serialMatcher.group();
				System.out.println(serialFound);
				od = new ObjectData(new UDID(serialFound.toLowerCase()));
				continue;
			} 
			
			if(od != null){
				if (line.contains("}")){
					set.add(od);
					od = null;
					continue;
				}
				if(line.contains(ObjectData.CLASS)){
					od.setClassType(line.split(SEPARATOR)[1]);
				} else {
					if(line.contains(REFERENCES)){
						references = true;
						continue;
					}
					String[] var = line.split(SEPARATOR);
					if(!references){
						od.put(var[0], ValueParser.parseValue(var[1]));
					} else {
						System.out.println(var[1]);
						od.putReferenceSerial(var[0], new UDID(var[1].toLowerCase()));
					}
				}
			}
			
		}
		reader.close();
		// selection
		return set;
	}

	@Override
	public boolean contains(Query q) throws IOException {
		return count(q) != 0;
	}

	@Override
	public long count(Query q) throws IOException {
		return load(q).size();
	}

	@Override
	public long size() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long capacity() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object backup() throws IOException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean restore(Object backup) throws IOException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public static void main(String arg[]) throws StorageException, IOException {
		Table myNewTable = new Table("Office Desk", new Table.Top(1.2f, 2.5f),
				new Table.Leg(1.1f));

		AbstractStorage storage = new TextFileStorage(new File("data.txt"));
		StorageControl.init(storage);

		//StorageControl.store(myNewTable);
		// TODO flush method
		Query q = new Query();
		q.addEqualityCriteria("width", 1.2);
		ObjectData od = StorageControl.loadData(q).get(0);
		System.out.println(od);
		StorageControl.stop();
		//System.out.println(new BigInteger("A1",16));
	}

}
