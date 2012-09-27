package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import model.*;

public class UniverseController {
	/**
	 * @param gView
	 * @param gModel
	 */
	protected UniverseController() {
		super();
		this.gView = new view.Game();
		this.gModel = new model.Game("Test", new Dimension(800, 600));
		this.gView.setKeyListener(new GameListener());
	}

	protected view.Game gView;
	protected model.Game gModel;

	boolean gameEnd;

	boolean forward;
	boolean left;
	boolean right;
	boolean shot;

	public void test() {
		this.gameEnd = false;
		gView.setVisible(true);
		this.gModel.run(0, false, false);

		Timer timer = new Timer();

		timer.schedule(new Task(), 0, 10);
	}

	class Task extends TimerTask {
		public void run() {
			ArrayList<Printable> printables = gModel.getPrintables();
			gView.printPrintables(printables, forward);

			try {
				gModel.run(getAlignmentFromUser(), forward, shot);
			}
			catch (GameOverException end) {
				gameEnd = true;
				gView.printScore(end.getMessage());
			}

		}
		//FIXME Add doublebuffering and multiple Tasks for IO and rest
		//TODO Maybe also own task for overlapping detection (i don't see problems there beside deadlocks)

		private int getAlignmentFromUser() {
			int deltaAlignment = 0;
			if (right) {
				deltaAlignment += 5;
			}
			if (left) {
				deltaAlignment -= 5;
			}
			return deltaAlignment;
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
				System.exit(0);
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
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					shot = true;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (!gameEnd) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					forward = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					shot = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					left = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					right = false;
				}
			}
		}
	}
}
