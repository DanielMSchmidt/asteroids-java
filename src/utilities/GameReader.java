package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.Game;

public class GameReader extends Reader {

	public GameReader(String location) {
		super(location);
	}

	public GameReader() {
		super("game.txt");
	}

	public boolean saveGame(Game game) {
		try {
			FileOutputStream fileOut = new FileOutputStream(defaultDataName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(game);
			out.close();
			fileOut.close();
			return true;
		}
		catch (IOException i) {
			return false;
		}

	}

	public Game loadGame() throws IOException {
		try {
			FileInputStream fileIn = new FileInputStream(defaultDataName);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			Game game = (Game) in.readObject();

			in.close();
			fileIn.close();
			return game;
		}
		catch (ClassNotFoundException e) {
			throw new IOException("Class not Found: " + e.getMessage());
		}
	}
}
