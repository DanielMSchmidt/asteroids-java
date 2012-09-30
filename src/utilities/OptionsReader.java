package utilities;

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
	 * Validates the options, if there are the right datatypes on each position
	 * Expects input to be in form playername, dimensionX, dimensionY
	 * 
	 * @param options
	 *            the options which should be validated
	 * @return <CODE>True</CODE> if valid, else <CODE>False</CODE>
	 */
	protected boolean validate(ArrayList<String> options) {
		if (options.size() != 3) {
			return false;
		}

		// Value 1 + 2 should be int
		if (!isInt(options.get(1))) {
			return false;
		}

		if (!isInt(options.get(2))) {
			return false;
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
		input.add("5");
		input.add("1");
		try {
			save(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns the saved playername
	 * 
	 * @return the saved playername
	 */
	public String getPlayerName() {
		String a = readData().get(0);
		return a;
	}

	/**
	 * returns the startlevel of the game
	 * 
	 * @return the startlevel of the game
	 */
	public int getStartLevel() {
		return Integer.valueOf(readData().get(1));
	}
	
	/**
	 * returns the speed of the player
	 * 
	 * @return the speed of the player
	 */
	public int getSpeed() {
		return Integer.valueOf(readData().get(2));
	}
}
