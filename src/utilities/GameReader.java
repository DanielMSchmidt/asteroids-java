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
		super("game");
	}

	public boolean saveGame(Game game) {
		try {
			FileOutputStream fileOut = new FileOutputStream(defaultDataName+".ser");
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

	public Game loadGame() throws IOException {
		Game game = null;
		try {
			FileInputStream fileIn = new FileInputStream(defaultDataName+".ser");
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
