package utilities;

import java.io.*;
import java.util.ArrayList;

public class Reader {

	private String defaultDataName;

	public Reader(String location) {
		super();
		this.defaultDataName = location;
	}

	String flat(ArrayList<String> list) {
		if (list.isEmpty())
			return "";

		String output = list.get(0);
		if (list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				output = output + "," + list.get(i);
			}
		}

		return output;
	}
	public void writeData(ArrayList<String> arrayList) throws IOException{
		writeData(arrayList, defaultDataName);
	}

	public void writeData(ArrayList<String> arrayList, String location)
			throws IOException {
		/**
		 * Writes the arrList as a single line into the dataName file, seperated
		 * by comma
		 */
		String data = flat(arrayList);
		File file = new File(location);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		bw.write(data);

		bw.flush();
		bw.close();
	}

	String readRawData(String location) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(location));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				data.add(zeile);
			}
		} catch (IOException e) {
			return "";
		}

		return flat(data);	}
	
	ArrayList<String> readData() {
		return readData(defaultDataName);

	}

	public ArrayList<String> readData(String location) {
		String data = readRawData(location);
		if (data == "")
			return new ArrayList<String>();
		
		String[] array = data.split(",");
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < array.length; i++) {
			out.add(array[i]);
		}
		return out;
	}
	public void deleteFile() {
		deleteFile(defaultDataName);
	}
	
	public void deleteFile(String location) {
		ArrayList<String> arrList = new ArrayList<String>();
		try {
			writeData(arrList, location);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void addData(ArrayList<String> addition) {
		addData(addition, defaultDataName);
	}
	public void addData(ArrayList<String> addition, String location) {
		ArrayList<String> lastInput = readData(location);
		if (lastInput.isEmpty() || lastInput.get(0) == "") {
			lastInput = addition;
		} else {
			lastInput.addAll(addition);
		}

		try {
			writeData(lastInput, location);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



}
