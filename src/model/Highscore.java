package model;

import java.util.ArrayList;

import utilities.HighscoreReader;

/**
 * Handles the highscores
 * 
 * @author danielschmidt
 * 
 */
public class Highscore {

	private HighscoreReader scoreReader;
	private String[][] scores;

	/**
	 * the highscore constructor
	 */
	public Highscore() {
		this.scoreReader = new HighscoreReader();
		this.scores = scoreReader.getFormattedHighscore();
	}

	/**
	 * updates the score
	 */
	public void update() {
		this.scores = scoreReader.getFormattedHighscore();
	}

	/**
	 * resets the score
	 */
	public void resetScore() {
		scoreReader.deleteFile();
		update();
	}

	/**
	 * gets the scores
	 * 
	 * @return the scores
	 */
	public String[][] getScores() {
		return scores;
	}

	/**
	 * adds a new score
	 * 
	 * @param name
	 *            playersname
	 * @param score
	 *            score which was scored
	 */
	public void addScore(String name, int score) {
		ArrayList<String> newScore = new ArrayList<String>();
		newScore.add(name);
		newScore.add(String.valueOf(score));
		scoreReader.addScore(newScore);
		update();
	}
}
