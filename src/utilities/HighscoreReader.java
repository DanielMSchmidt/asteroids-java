package utilities;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A Writer/Reader which handles the highscores and formats it for the
 * highscore
 * 
 * @author danielschmidt
 * 
 */
public class HighscoreReader extends Reader {

	protected static final int MAXVALUES = 20;

	/**
	 * Default constructor, which writes in the highscore.txt
	 */
	public HighscoreReader() {
		super("highscore.txt");
	}

	/**
	 * Extended constructor, which writes in the location
	 * 
	 * @param location
	 *            Destination the data should be stored
	 */
	public HighscoreReader(String location) {
		super(location);
	}

	/**
	 * Adds a score into the highscore (sorts it in)
	 * 
	 * @param score
	 *            Score to be added
	 */
	public void addScore(ArrayList<String> score) {
		ArrayList<String> oldData = readData();
		try {
			if (validate(score)) {
				writeData(mergeScores(oldData, score));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * gets the complete highscore as ArrayList and validates it
	 * 
	 * @return a validated ArrayList of all scores
	 */
	ArrayList<String> getHighscore() {
		ArrayList<String> a = readData();
		if (validate(a)) {
			return a;
		}
		return new ArrayList<String>();

	}

	/**
	 * gets the shortened highscore, which has max. maxPairs scores in it
	 * 
	 * @return a shortened highscore
	 */
	ArrayList<String> getShortHighscore() {
		ArrayList<String> completeHighscore = getHighscore();
		if (completeHighscore.size() > HighscoreReader.MAXVALUES) {
			ArrayList<String> shortHighscore = new ArrayList<String>();
			shortHighscore.addAll(completeHighscore.subList(0, HighscoreReader.MAXVALUES));
			return shortHighscore;
		}
		return completeHighscore;

	}

	/**
	 * Formats the highscore in a 2D Array and ready to display on a table
	 * element
	 * 
	 * @return a 2D Stringarray of names and scores
	 */
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

	/**
	 * Validates the score
	 * 
	 * @param score
	 *            the score to be validated
	 * @return <code>true</code> if it is valid, else <code>false</code>
	 */
	boolean validate(ArrayList<String> score) {
		if (score.size() % 2 == 1) {
			// if odd numbercount it can't be right
			return false;
		}
		for (int i = 0; i < score.size(); i++) {

			if (i % 2 == 1) {
				// if odd it should be a number
				try {
					Integer.parseInt(score.get(i));
				}
				catch (NumberFormatException nfe) {
					// returns false if no number
					return false;
				}

			}
		}
		return true;

	}

	/**
	 * merges two arraylist of scores into one
	 * 
	 * @param data
	 *            an arraylist of scores
	 * @param newInput
	 *            an arraylist of scores
	 * @return a combined and sorted arraylist of scores
	 */
	protected ArrayList<String> mergeScores(ArrayList<String> data, ArrayList<String> newInput) {
		ArrayList<String> a = new ArrayList<String>();
		sortScores(newInput, a);
		sortScores(data, a);
		return a;
	}

	/**
	 * sorts the scores from highest to lowest
	 * 
	 * @param newScores
	 *            an arraylist of scores
	 * @param oldScores
	 *            an arraylist of scores
	 */
	private void sortScores(ArrayList<String> newScores, ArrayList<String> oldScores) {
		for (int i = 1; i < newScores.size(); i = i + 2) {
			int j = getPositionOfSortedScore(newScores, oldScores, i);
			oldScores.add(j - 1, newScores.get(i - 1));
			oldScores.add(j, newScores.get(i));
		}
	}

	/**
	 * gets the position were the new score should be added
	 * 
	 * @param newScores
	 *            an arraylist of scores
	 * @param oldScores
	 *            an arraylist of scores
	 * @param i
	 *            the position of the new element which should be added
	 * @return the position the new score should be added
	 */
	private int getPositionOfSortedScore(ArrayList<String> newScores, ArrayList<String> oldScores, int i) {
		for (int j = 1; j < oldScores.size(); j = j + 2) {
			if (Integer.valueOf(newScores.get(i)) > Integer.valueOf(oldScores.get(j))) return j;
		}
		return oldScores.size() + 1;
	}
}
