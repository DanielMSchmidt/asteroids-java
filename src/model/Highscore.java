package model;

import java.util.ArrayList;

import utilities.HighscoreReader;

public class Highscore {

    private HighscoreReader scoreReader;
    private String[] caption;
    private String[][] scores;

    public Highscore() {
        this.scoreReader = new HighscoreReader();
        this.caption = new String[2];
        caption[0] = "Score";
        caption[1] = "Player";
        this.scores = scoreReader.getFormattedHighscore();
    }

    public void update() {
        this.scores = scoreReader.getFormattedHighscore();
    }

    public void saveScore(ArrayList<String> newScore) {
        scoreReader.addScore(newScore);
    }
    
    public void resetScore(){
    	scoreReader.deleteFile();    
    	update();
    }

    public String[] getCaption() {
        return caption;
    }

    public String[][] getScores() {
        return scores;
    }

	public void addScore(String name, int score) {
		ArrayList<String> newScore = new ArrayList<String>();
		newScore.add(name);
		newScore.add(String.valueOf(score));
		scoreReader.addScore(newScore);
	    update();
    }
}
