package utilities;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

public class HighscoreReader extends Reader {

	private int maxValues;

	public HighscoreReader() {
		super("highscore.txt");
		OptionsReader options = new OptionsReader();
		this.maxValues = options.getMaxPairs() * 2;
	}

	public HighscoreReader(String location) {
		super(location);
		OptionsReader options = new OptionsReader();
		this.maxValues = options.getMaxPairs() * 2;
	}

	public HighscoreReader(String location, String optionsLocation) {
		super(location);
		OptionsReader options = new OptionsReader(optionsLocation);
		this.maxValues = options.getMaxPairs() * 2;
	}

	public void addScore(ArrayList<String> data) {
		/**
		 * Adds a score into the highscore (sorts it in)
		 */
		ArrayList<String> oldData = readData();
		try {
			if (validate(data)) {
				writeData(merge(oldData, data));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getHighscore() {
		/**
		 * gets the Highscore as ArrayList<String> and validates it before
		 * returning it
		 */
		ArrayList<String> a = readData();
		if (validate(a)) {
			return a;
		}
		return new ArrayList<String>();

	}

	public ArrayList<String> getShortHighscore() {
		ArrayList<String> a = getHighscore();
		if (a.size() > this.maxValues) {
			ArrayList<String> b = new ArrayList<String>();
			b.addAll(a.subList(0, this.maxValues));
			return b;
		}
		return a;

	}

	public String[][] getFormattedHighscore() {
		ArrayList<String> score = getShortHighscore();
		int length = score.size();
		String[][] a = new String[(length / 2)][2];
		int j = 0;
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) {
				a[j][0] = score.get(i);
			} else {
				a[j][1] = score.get(i) + " Points";
				j++;
			}
		}

		return a;
	}

	boolean validate(ArrayList<String> score) {
		if (score.size() % 2 == 1) {
			// if odd number it can't be right
			return false;
		}
		for (int i = 0; i < score.size(); i++) {

			if (i % 2 == 1) {
				// if odd it should be a number
				try {
					Integer.parseInt(score.get(i));
				} catch (NumberFormatException nfe) {
					// returns false if no number
					return false;
				}

			}
		}
		return true;

	}

	ArrayList<String> merge(ArrayList<String> data, ArrayList<String> newInput) {
		ArrayList<String> a = new ArrayList<String>();
		insertionSort(newInput, a);
		insertionSort(data, a);
		return a;
	}

	private void insertionSort(ArrayList<String> oldList,
			ArrayList<String> newList) {
		for (int i = 1; i < oldList.size(); i = i + 2) {
			int j = getPositionOfSortedScore(oldList, newList, i);
			if (j == -1) {
				j = newList.size() + 1;
			}
			newList.add(j - 1, oldList.get(i - 1));
			newList.add(j, oldList.get(i));
		}
	}

	private int getPositionOfSortedScore(ArrayList<String> newInput,
			ArrayList<String> a, int i) {
		for (int j = 1; j < a.size(); j = j + 2) {
			if (Integer.valueOf(newInput.get(i)) > Integer.valueOf(a.get(j))) {

				return j;
			}
		}
		return -1;
	}
}
