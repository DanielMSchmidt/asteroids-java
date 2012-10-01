package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Displays the highscore
 * 
 * @author danielschmidt
 * 
 */
public class Highscore extends GUI {

	public JButton backBtn;
	public JButton resetBtn;
	String[] title = { "Score", "Player" };
	JTable scoreTable;

	/**
	 * Constructor for the Highscoreview
	 * 
	 * @param scores
	 *            the scores which should be displayed
	 * @param resolution
	 *            the resolution of the view
	 */
	public Highscore(String[][] scores, Dimension resolution) {
		super("Universe - Highscore", resolution);
		paintArea = new GraphicPanel();

		GridBagLayout gbl = new GridBagLayout();
		paintArea.setLayout(gbl);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(0, 5, 20, 5);

		scoreTable = new JTable(scores, title);
		scoreTable.setShowGrid(false);
		backBtn = createButton("Back to main menu");
		resetBtn = createButton("Reset Highscore");

		addComponent(paintArea, gbl, constraints, new JScrollPane(scoreTable));
		addButton(paintArea, gbl, resetBtn);
		addButton(paintArea, gbl, backBtn);

		add(paintArea);
	}

	/**
	 * refreshes the scores in the view
	 * 
	 * @param scores
	 *            the scores which should be updated
	 */
	public void refreshScores(String[][] scores) {
		scoreTable = new JTable(scores, title);
		this.update(getGraphics());
		repaint();
	}

	/**
	 * sets the listener
	 * 
	 * @param listener
	 *            listener which should be set
	 */
	public void setListener(ActionListener listener) {
		this.resetBtn.addActionListener(listener);
		this.backBtn.addActionListener(listener);
	}
}
