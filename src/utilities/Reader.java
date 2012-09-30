package utilities;

import java.io.*;
import java.util.ArrayList;

/**
 * A Writer/Reader which provides basic IO functionallity in saving data to a
 * file
 * 
 * @author danielschmidt
 * 
 */
public class Reader {

	private String defaultDataName;

	/**
	 * Constructor for Reader
	 * 
	 * @param location
	 *            The location were the data should be stored, e.g.
	 *            "example.txt" for saving under CURRENT_DIR/example.txt
	 */
	public Reader(String location) {
		super();
		this.defaultDataName = location;
	}

	/**
	 * Transforms an ArrayList into a commaseperated string
	 * 
	 * @param list
	 *            ArrayList of Strings
	 * @return A String with the other strings commaseperated
	 */
	String flat(ArrayList<String> list) {
		if (list.isEmpty()) return "";

		String output = list.get(0);
		if (list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				output = output + "," + list.get(i);
			}
		}

		return output;
	}

	/**
	 * writes data to the default location
	 * 
	 * @param arrayList
	 *            data which should be saved
	 * @throws IOException
	 */
	public void writeData(ArrayList<String> arrayList) throws IOException {
		writeData(arrayList, defaultDataName);
	}

	/**
	 * Writes the arrayList as a single line into the dataName file, seperated
	 * by comma
	 * 
	 * @param arrayList
	 *            the input, which should be saved
	 * @param location
	 *            the location, where the data should be stored
	 * @throws IOException
	 */
	public void writeData(ArrayList<String> arrayList, String location) throws IOException {
		String data = flat(arrayList);
		File file = new File(location);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		bw.write(data);

		bw.flush();
		bw.close();
	}

	/**
	 * returns a string of saved elements, seperated by comma
	 * 
	 * @param location
	 *            the location, from where the data should be read
	 * @return a comma seperated string, which was read from the file
	 */
	String readRawData(String location) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(location));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				data.add(zeile);
			}
		}
		catch (IOException e) {
			return "";
		}

		return flat(data);
	}

	/**
	 * Reads data from default File
	 * 
	 * @return an ArrayList of elements which were given in
	 */
	ArrayList<String> readData() {
		return readData(defaultDataName);

	}

	/**
	 * Reads data into an ArrayList
	 * 
	 * @param location
	 *            location from which the data should be read
	 * @return an ArrayList of Objects
	 */
	public ArrayList<String> readData(String location) {
		String data = readRawData(location);
		if (data == "") return new ArrayList<String>();

		String[] array = data.split(",");
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < array.length; i++) {
			out.add(array[i]);
		}
		return out;
	}

	/**
	 * deletes all content in the default file
	 */
	public void deleteFile() {
		deleteFile(defaultDataName);
	}

	/**
	 * deletes all content in the location
	 * 
	 * @param location
	 *            were the content should be deleted
	 */
	public void deleteFile(String location) {
		ArrayList<String> arrList = new ArrayList<String>();
		try {
			writeData(arrList, location);
		}
		catch (IOException e) {
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
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
