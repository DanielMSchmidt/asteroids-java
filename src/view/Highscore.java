package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Highscore extends GUI {

	public JButton backBtn;
	public JButton resetBtn;
	String[] title = { "Score", "Player" };
	JTable scoreTable;

	public Highscore(String[][] scores, Dimension resolution) {
		super("Universe - Highscore", resolution);
		GraphicPanel paintArea = new GraphicPanel();

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

	public void refreshScores(String[][] scores) {
		scoreTable = new JTable(scores, title);
		this.update(getGraphics());
		repaint();
	}

	public void setListener(ActionListener l) {
		this.resetBtn.addActionListener(l);
		this.backBtn.addActionListener(l);
	}
}
