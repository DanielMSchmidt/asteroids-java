package model;

public class GameOverException extends RuntimeException {

	private static final long serialVersionUID = 4487120996910509334L;

	
	//TODO SCORE wird hier nicht mehr benötigt!
	public GameOverException(int score) {
		super(String.valueOf(score));
	}

}
