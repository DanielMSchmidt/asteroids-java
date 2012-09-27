package utilities;

import java.awt.Dimension;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

public class OptionsReader extends Reader {

	public OptionsReader() {
		super("options.txt");
		initIfInvalid();
	}

	public OptionsReader(String location) {
		super(location);
		initIfInvalid();
	}

	private void initIfInvalid() {
		if (!validate(this.readData())) {
			initialize();
		}
	}

	public String getPlayerName() {
		return readData().get(0);
	}

	public Dimension getDimension() {
		ArrayList<String> arrList = readData();
		Dimension out = new Dimension(Integer.valueOf(arrList.get(1)),
				Integer.valueOf(arrList.get(2)));
		return out;
	}

	public int getMaxPairs() {
		return Integer.valueOf(readData().get(3));
	}

	boolean validate(ArrayList<String> options) {
		/**
		 * Expects input to be in form playername, dimensionX, dimensionY
		 */
		if (options.size() != 4) {
			return false;
		}
		for (int i = 1; i < 4; i++) {
			// Value 1 - 3 should be int
			if (!isInt(options.get(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean isInt(String a) {
		/**
		 * returns true if string can be interpreted as integer Uses regular
		 * expression to do this (\\d+)
		 */
		try {
			Integer.parseInt(a);
		} catch (Exception ex) {
			// Return false if a isn't int
			return false;
		}
		// else return true
		return true;

	}

	public void save(ArrayList<String> options) {
		if (validate(options)) {
			try {
				writeData(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	void initialize() {
		/**
		 * creates default values for all available options
		 */
		ArrayList<String> input = new ArrayList<String>();
		input.add("Player 1");
		input.add("500");
		input.add("500");
		input.add("10");
		try {
			save(input);
		} catch (Exception e) {
			// can't happen due to tests
		}
	}
}
