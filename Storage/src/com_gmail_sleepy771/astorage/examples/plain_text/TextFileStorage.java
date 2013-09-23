package com_gmail_sleepy771.astorage.examples.plain_text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com_gmail_sleepy771.astorage.AbstractStorage;
import com_gmail_sleepy771.astorage.StorageControl;
import com_gmail_sleepy771.astorage.exceptions.StorageException;
import com_gmail_sleepy771.astorage.utilities.ObjectData;
import com_gmail_sleepy771.astorage.utilities.Query;
import com_gmail_sleepy771.astorage.utilities.UDID;

public class TextFileStorage extends AbstractStorage {
	private File textFile;

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
		
		writer.println("Data: " + data.getSerialNumber() + " {");
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			writer.println(entry.getKey() + ": " + entry.getValue().toString());
		}
		writer.println("References:");
		for (Map.Entry<String, UDID> entry : data.getReferenceSerials()
				.entrySet()) {
			writer.println(entry.getKey() + ": " + entry.getValue().toString());
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
		// TODO dokoncit
		TreeSet<ObjectData> set = new TreeSet<ObjectData>();
		FileReader fr = new FileReader(textFile);
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
		LinkedList<String> sb = null;
		boolean isInteresting = false;
		Map.Entry<String, Object> first = q.poll();
		while ((line = reader.readLine()) != null) {
			if (line.contains("Data")) {
				sb = new LinkedList<String>();
				sb.add(line);
				
			}
			if (sb != null) {
				if (line.contains(first.getKey())
						&& line.split(": ")[1].equals(first.getValue()
								.toString())) {
					isInteresting = true;
				}
				if (line.contains("}")) {
					if (isInteresting) {
						UDID serial = null;
						String classType = null;
						boolean references = false;
						TreeMap<String, String> varmap = new TreeMap<String, String>();
						TreeMap<String, UDID> refMap = new TreeMap<String, UDID>();
						for (String l : sb) {
							if (l.contains("Data")) {
								Pattern p = Pattern.compile("[0-9a-f]+");
								Matcher m = p.matcher(line);
								if (m.find())
									serial = new UDID(m.group());
							} else if (l.contains(ObjectData.CLASS)) {
								classType = l.split(": ")[1];
							} else if (l.contains("References")) {
								references = true;
								continue;
							} else if (!references) {
								String[] kv = l.split(": ");
								varmap.put(kv[0], kv[1]);
							} else {
								String[] kv = l.split(": ");
								refMap.put(kv[0], new UDID(kv[1]));
							}
						}
						ObjectData od = new ObjectData(classType, serial);
						od.putAll(varmap);
						od.putAllReferenceSerials(refMap);
						set.add(od);
					}
					sb = null;
					isInteresting = false;
				}
			}
		}
		reader.close();

		while (q.hasCriteria()) {
			Map.Entry<String, Object> subSelectionCriteria = q.poll();
			TreeSet<ObjectData> subset = new TreeSet<ObjectData>();
			for (ObjectData data : set) {
				if (data.containsKey(subSelectionCriteria.getKey())
						&& data.get(subSelectionCriteria.getKey()).equals(
								subSelectionCriteria.getValue().toString())) {
					subset.add(data);
				}
			}
			set = subset;
		}

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

	public static void main(String arg[]) throws StorageException {
		Table myNewTable = new Table("Office Desk", new Table.Top(1.2f, 2.5f),
				new Table.Leg(1.1f));

		AbstractStorage storage = new TextFileStorage(new File("data.txt"));
		StorageControl.init(storage);

		StorageControl.store(myNewTable);

		// TODO flush method
		Query q = new Query();
		StorageControl.stop();
	}

}
