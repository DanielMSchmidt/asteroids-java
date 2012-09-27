package model;

public class GameOverException extends RuntimeException {

	private static final long serialVersionUID = 4487120996910509334L;

	public GameOverException(int score) {
		super(String.valueOf(score));
	}

}
