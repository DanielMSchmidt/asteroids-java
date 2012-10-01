package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import model.*;
import model.Game;
import utilities.GameReader;
import utilities.OptionsReader;
import view.*;

/**
 * Maincontroller, which handles the model and the user I/O
 * 
 * @author danielschmidt
 * 
 */
public class UniverseController {
	public static final Dimension RESOLUTION = new Dimension(800, 600);

	Timer timer;

	protected view.Game gView;
	protected view.Menu mView;
	protected view.Settings sView;
	protected view.Highscore hView;

	protected model.Game gModel;
	protected model.Highscore hModel;

	protected utilities.OptionsReader oReader;
	protected utilities.GameReader gReader;

	boolean gameEnd;
	boolean forward;
	boolean left;
	boolean right;
	boolean shot;
	boolean fast;
	boolean help;
	boolean loadingError;

	/**
	 * Contructor
	 */
	protected UniverseController() {
		super();

		loadingError = false;
		oReader = new OptionsReader();
		gReader = new GameReader();
		constructGame();
		constructMenu();
		constructHighscore();
		constructSettings();
	}

	/**
	 * Constructs game Model and View
	 */
	private void constructGame() {
		String playername = oReader.getPlayerName();
		int speed = oReader.getSpeed();
		int level = oReader.getStartLevel();
		this.gModel = new model.Game(playername, RESOLUTION, speed, level);
		this.gView = new view.Game(gModel.getResolution());
		this.gView.setKeyListener(new GameListener());
	}

	/**
	 * Constructs the menu
	 */
	private void constructMenu() {
		this.mView = new view.Menu(gModel.getResolution());
		this.mView.setListener(new MenuListener());
	}

	/**
	 * constructs the settings view
	 */
	private void constructSettings() {
		this.sView = new view.Settings(gModel.getResolution());
		this.sView.setListener(new SettingsListener());
	}

	/**
	 * constructs the highscore model and view
	 */
	private void constructHighscore() {
		this.hModel = new model.Highscore();
		this.hView = new view.Highscore(hModel.getScores(), gModel.getResolution());
		this.hView.setListener(new HighscoreListener());
	}

	/**
	 * shows the menu
	 */
	public void showMenu() {
		mView.setVisible(true);
	}

	/**
	 * starts the game
	 */
	private void startGame() {
		this.gameEnd = false;

		forward = false;
		left = false;
		right = false;
		shot = false;
		fast = false;
		help = false;

		timer = new Timer();

		timer.schedule(new GameTask(), 0, 10);
	}

	/**
	 * loads the game from a file
	 */
	private void reloadGame() {
		try {
			gModel = gReader.loadGame();
			startGame();
		}
		catch (Exception e) {
			gView.printError("Es konnte kein Spiel geladen werden!", e.getMessage());
			loadingError = true;
		}
	}

	/**
	 * Task which runs the Game
	 * 
	 * @author danielschmidt
	 * 
	 */
	class GameTask extends TimerTask {
		/**
		 * gets and prints Printables from model, runs the next round of the
		 * model and ends the game
		 */
		public void run() {
			ArrayList<Printable> printables = gModel.getPrintables();
			gView.printPrintables(printables, forward, gModel.getScore(), help);

			gameEnd = gModel.run(getAlignmentFromUser(), forward, shot);
			shot = false;
			if (gameEnd) {
				endGame();
			}
		}

		/**
		 * ends the current Game
		 */
		private void endGame() {
			hModel.addScore(gModel.getPlayername(), gModel.getScore());
			gView.gameover();
			this.cancel();
		}

		/**
		 * returns the alignment from the userinput
		 * 
		 * @return the alignment from the userinput
		 */
		private int getAlignmentFromUser() {
			int deltaAlignment = 0;
			int turnspeed = 2;
			if (fast) {
				turnspeed = 4;
			}
			if (right) {
				deltaAlignment = turnspeed;
			}
			if (left) {
				deltaAlignment = -turnspeed;
			}
			return deltaAlignment;
		}
	}

	/**
	 * changes the active guis
	 * 
	 * @param nextGUI
	 *            the gui which is active afterwards
	 * @param currentGUI
	 *            the gui which is inactive afterwards
	 */
	protected void changeWindow(GUI nextGUI, GUI currentGUI) {
		nextGUI.setLocation(currentGUI.getLocationOnScreen());
		nextGUI.setVisible(true);
		currentGUI.setVisible(false);
	}

	/**
	 * Handles the menu view and exchange the window
	 * 
	 * @author danielschmidt
	 * 
	 */
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == mView.startBtn) {
				constructGame();
				changeWindow(gView, mView);
				startGame();
			} else if (source == mView.loadBtn) {
				reloadGame();
				changeWindow(gView, mView);
			} else if (source == mView.highscoreBtn) {
				constructHighscore();
				changeWindow(hView, mView);
			} else if (source == mView.settingsBtn) {
				sView.setValues(oReader.getPlayerName(), oReader.getStartLevel(), oReader.getSpeed());
				changeWindow(sView, mView);
			} else if (source == mView.exitBtn) {
				System.exit(0);
			}
		}
	}

	/**
	 * Handles the settings view and exchange the window
	 * 
	 * @author danielschmidt
	 * 
	 */
	class SettingsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == sView.backBtn) {
				changeWindow(mView, sView);
			} else if (source == sView.saveBtn) {
				ArrayList<String> options = new ArrayList<String>();
				options.add(sView.getPlayerName());
				options.add(String.valueOf(sView.getStartLevel()));
				options.add(String.valueOf(sView.getSpeed()));
				try {
					oReader.save(options);
				}
				catch (Exception e1) {
					sView.printError("Settings konnten nicht geladen werden!", e1.getMessage());
				}
			}
		}
	}

	/**
	 * Handles the highscore view and exchange the window
	 * 
	 * @author danielschmidt
	 * 
	 */
	class HighscoreListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == hView.backBtn) {
				changeWindow(mView, hView);
			} else if (source == hView.resetBtn) {
				hModel.resetScore();
				changeWindow(mView, hView);
				constructHighscore();
				changeWindow(hView, mView);
			}

		}
	}

	/**
	 * Handles the input during the game
	 * 
	 * @author danielschmidt
	 * 
	 */
	class GameListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				changeWindow(mView, gView);
				if (gameEnd || loadingError) {
					Game game = new Game(oReader.getPlayerName(), RESOLUTION, oReader.getSpeed(),
					        oReader.getStartLevel());
					gReader.saveGame(game);
					loadingError = false;
				} else {
					gReader.saveGame(gModel);
				}
				try {
					timer.cancel();
				}
				catch (Exception ex) {

				}
			}
			if (e.getKeyCode() == KeyEvent.VK_F1) {
				help = true;
			}
			if (!gameEnd) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						forward = true;
					break;
					case KeyEvent.VK_A:
						left = true;
					break;
					case KeyEvent.VK_D:
						right = true;
					break;
					case KeyEvent.VK_SHIFT:
						fast = true;
					default:
					break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1) {
				help = false;
			}
			if (!gameEnd) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						forward = false;
					break;
					case KeyEvent.VK_A:
						left = false;
					break;
					case KeyEvent.VK_D:
						right = false;
					break;
					case KeyEvent.VK_SHIFT:
						fast = false;
					break;
					case KeyEvent.VK_SPACE:
						shot = true;
					default:
					break;
				}
			}
		}
	}
}
