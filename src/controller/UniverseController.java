package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import model.*;
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

	boolean gameEnd;
	boolean forward;
	boolean left;
	boolean right;
	boolean shot;
	boolean fast;
	boolean help;

	protected UniverseController() {
		super();
		this.gModel = new model.Game("Test", new Dimension(800, 600));
		this.hModel = new model.Highscore();
		this.gView = new view.Game(gModel.getResolution());
		this.mView = new view.Menu(gModel.getResolution());
		this.sView = new view.Settings(gModel.getResolution());
		this.hView = new view.Highscore(hModel.getScores(), gModel.getResolution());

		this.gView.setKeyListener(new GameListener());
		this.mView.setListener(new MenuListener());
		this.sView.setListener(new SettingsListener());
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
				gView.gameover();
				gModel.resetGame();
				this.cancel();
			}

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
				changeWindow(gView, mView);
				startGame();
			} else if (source == mView.loadBtn) {
				changeWindow(gView, mView);
				reloadGame();
			} else if (source == mView.highscoreBtn) {
				hView.refreshScores(hModel.getScores());
				changeWindow(hView, mView);
			} else if (source == mView.settingsBtn) {
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
				// TODO Implement method! Use implemented getter!
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
				hView.refreshScores(hModel.getScores());
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
