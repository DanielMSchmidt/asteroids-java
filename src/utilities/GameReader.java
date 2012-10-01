package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Game;

/**
 * A Writer/Reader which handles the saves and loads the Game
 * 
 * @author danielschmidt
 * 
 */
public class GameReader extends Reader {
	/**
	 * basic constructor
	 */
	public GameReader() {
		super("game");
	}

	/**
	 * extended constructor
	 * 
	 * @param location
	 *            location were it should be saved (without .ser at the end)
	 */
	public GameReader(String location) {
		super(location);
	}

	/**
	 * saves the Game into the defaultFilename
	 * 
	 * @param game
	 *            the game, which should be saved
	 * @return <code>true</code> if the game was saved successfully
	 */
	public boolean saveGame(Game game) {
		try {
			FileOutputStream fileOut = new FileOutputStream(defaultDataName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(game);

			out.close();
			fileOut.close();
			return true;
		}
		catch (IOException i) {
			i.printStackTrace();
			return false;
		}

	}

	/**
	 * loads the game from a file
	 * 
	 * @return the loaded game
	 * @throws IOException
	 *             if the savegame wasn't found
	 */
	public Game loadGame() throws IOException {
		Game game = null;
		try {
			FileInputStream fileIn = new FileInputStream(defaultDataName + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);

			game = (Game) in.readObject();

			in.close();
			fileIn.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IOException("Class not Found: " + e.getMessage());
		}
		return game;
	}
}
