package utilities;

import java.awt.Dimension;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Writer/Reader which handles the Options, which can be saved through
 * the menu
 * 
 * @author danielschmidt
 * 
 */
public class OptionsReader extends Reader {

	/**
	 * Constructor for OptionsReader
	 */
	public OptionsReader() {
		super("options.txt");
		initIfInvalid();
	}

	/**
	 * Constructor for OptionsReader
	 * 
	 * @param location
	 *            were the file should be saved
	 */
	public OptionsReader(String location) {
		super(location);
		initIfInvalid();
	}

	/**
	 * initilizes the file if somehow there is invalid information (e.g. through
	 * direct access)
	 */
	private void initIfInvalid() {
		if (!validate(this.readData())) {
			initialize();
		}
	}

	/**
	 * returns the saved playername
	 * 
	 * @return the saved playername
	 */
	public String getPlayerName() {
		return readData().get(0);
	}

	public Dimension getDimension() {
		ArrayList<String> arrList = readData();
		Dimension out = new Dimension(Integer.valueOf(arrList.get(1)), Integer.valueOf(arrList.get(2)));
		return out;
	}

	public int getMaxPairs() {
		return Integer.valueOf(readData().get(3));
	}

	/**
	 * Validates the options, if there are the right datatypes on each position
	 * Expects input to be in form playername, dimensionX, dimensionY
	 * 
	 * @param options
	 *            the options which should be validated
	 * @return <CODE> True </CODE> if valid, else <CODE>False</CODE>
	 */
	protected boolean validate(ArrayList<String> options) {
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

	/**
	 * returns true if string can be interpreted as integer
	 * 
	 * @param a
	 *            string, to be tested
	 * @return <code> true </code> if a is an integer
	 */
	private boolean isInt(String a) {
		try {
			Integer.parseInt(a);
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * saves the options into a file
	 * 
	 * @param options
	 *            the options which should be saved
	 * @throws Exception
	 *             if the file can't be found, wirtten or if the options are
	 *             invalid
	 */
	public void save(ArrayList<String> options) throws Exception {
		if (validate(options)) {
			writeData(options);
		} else {
			throw new Exception("The arguments are invalid");
		}

	}

	/**
	 * creates default values for all available options
	 */
	void initialize() {
		ArrayList<String> input = new ArrayList<String>();
		input.add("Player 1");
		input.add("500");
		input.add("500");
		input.add("10");
		try {
			save(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
