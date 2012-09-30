package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import model.*;
import utilities.OptionsReader;
import view.*;

public class UniverseController {
	/**
	 * @param gView
	 * @param gModel
	 */

	Timer timer;

	protected view.Game gView;
	protected view.Menu mView;
	protected view.Settings sView;
	protected view.Highscore hView;

	protected model.Game gModel;
	protected model.Highscore hModel;

	protected utilities.OptionsReader oReader;

	boolean gameEnd;
	boolean forward;
	boolean left;
	boolean right;
	boolean shot;
	boolean fast;
	boolean help;

	protected UniverseController() {
		super();

		oReader = new OptionsReader();
		constructGame();
		constructMenu();
		constructHighscore();
		constructSettings();
	}

	private void constructGame() {
		String playername = oReader.getPlayerName();
		int speed = oReader.getSpeed();
		int level = oReader.getStartLevel();
		this.gModel = new model.Game(playername, new Dimension(800, 600), speed, level);
		this.gView = new view.Game(gModel.getResolution());
		this.gView.setKeyListener(new GameListener());
	}

	private void constructMenu() {
		this.mView = new view.Menu(gModel.getResolution());
		this.mView.setListener(new MenuListener());
	}

	private void constructSettings() {
		this.sView = new view.Settings(gModel.getResolution());
		this.sView.setListener(new SettingsListener());
	}

	private void constructHighscore() {
		this.hModel = new model.Highscore();
		this.hView = new view.Highscore(hModel.getScores(), gModel.getResolution());
		this.hView.setListener(new HighscoreListener());
	}

	public void showMenu() {
		mView.setVisible(true);
	}

	private void startGame() {
		this.gameEnd = false;

		forward = false;
		left = false;
		right = false;
		shot = false;
		fast = false;
		help = false;

		timer = new Timer();

		timer.schedule(new Task(), 0, 10);
	}

	private void reloadGame() {
		// TODO laden realisieren.

	}

	class Task extends TimerTask {
		public void run() {
			ArrayList<Printable> printables = gModel.getPrintables();
			gView.printPrintables(printables, forward, gModel.getScore(), help);

			gameEnd = gModel.run(getAlignmentFromUser(), forward, shot);
			shot = false;
			if (gameEnd) {
				endGame();
			}

		}

		private void endGame() {
			hModel.addScore(gModel.getPlayername(), gModel.getScore());
			gView.gameover();
			gModel.resetGame();
			this.cancel();
		}

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

	protected void changeWindow(GUI c1, GUI c2) {
		c1.setLocation(c2.getLocationOnScreen());
		c1.setVisible(true);
		c2.setVisible(false);
	}

	// Menu
	class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == mView.startBtn) {
				constructGame();
				changeWindow(gView, mView);
				startGame();
			} else if (source == mView.loadBtn) {
				changeWindow(gView, mView);
				reloadGame();
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

	// Settings
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
					// FIXME Print error Output somehow!
				}
			}
		}
	}

	// Highscore
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

	// Game Keys
	class GameListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				changeWindow(mView, gView);
				gModel.resetGame();
				timer.cancel();
			}
			if (e.getKeyCode() == KeyEvent.VK_F1) {
				help = true;
			}
			if (!gameEnd) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					forward = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					left = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					right = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					fast = true;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F1) {
				help = false;
			}
			if (!gameEnd) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					forward = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					shot = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					left = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					right = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					fast = false;
				}
			}
		}
	}
}
