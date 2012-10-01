package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Displays the settings
 * 
 * @author danielschmidt
 * 
 */
public class Settings extends GUI {

	public JButton backBtn;
	public JButton saveBtn;
	JTextField playerName;
	JTextField startlevel;
	JTextField speed;

	/**
	 * Constructor for the settingsview
	 * 
	 * @param resolution
	 *            resolution the view should have
	 */
	public Settings(Dimension resolution) {
		super("Universe - Settings", resolution);
		paintArea = new GraphicPanel();
		GridBagLayout gbl = new GridBagLayout();
		paintArea.setLayout(gbl);
		UIManager.put("Label.font", new Font("sansserif", Font.BOLD, 18));
		UIManager.put("Label.foreground", Color.WHITE);

		backBtn = createButton("Back to main menu");
		saveBtn = createButton("Save");

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = GridBagConstraints.EAST;
		constraints.anchor = GridBagConstraints.EAST;

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(gbl);

		playerName = new JTextField(10);
		startlevel = new JTextField(10);
		speed = new JTextField(10);

		JLabel playerNamelbl = new JLabel("Playername: ");
		JLabel startlevellbl = new JLabel("Startlevel: ");
		JLabel speedlbl = new JLabel("Speed: ");

		addComponent(panel, gbl, constraints, playerNamelbl);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		addComponent(panel, gbl, constraints, playerName);
		constraints.gridwidth = GridBagConstraints.EAST;
		constraints.anchor = GridBagConstraints.EAST;
		addComponent(panel, gbl, constraints, startlevellbl);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		addComponent(panel, gbl, constraints, startlevel);
		constraints.gridwidth = GridBagConstraints.EAST;
		constraints.anchor = GridBagConstraints.EAST;
		addComponent(panel, gbl, constraints, speedlbl);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		addComponent(panel, gbl, constraints, speed);
		constraints.insets = new Insets(0, 0, 50, 0);
		constraints.gridwidth = GridBagConstraints.REMAINDER;

		addComponent(paintArea, gbl, constraints, panel);

		addButton(paintArea, gbl, saveBtn);
		addButton(paintArea, gbl, backBtn);

		add(paintArea);
	}

	/**
	 * set the values in the textfields
	 * 
	 * @param name
	 *            the players name
	 * @param level
	 *            the level from which the game should start
	 * @param speedvalue
	 *            the speed
	 */
	public void setValues(String name, int level, int speedvalue) {
		playerName.setText(name);
		startlevel.setText(String.valueOf(level));
		speed.setText(String.valueOf(speedvalue));
	}

	/**
	 * getter for playername
	 * 
	 * @return the playername, which was given in
	 */
	public String getPlayerName() {
		return playerName.getText();
	}

	/**
	 * getter for startlevel
	 * 
	 * @return the startlevel, which was given in
	 */
	public int getStartLevel() {
		return Integer.valueOf(startlevel.getText());
	}

	/**
	 * getter for Speed
	 * 
	 * @return the speed, which was given in
	 */
	public int getSpeed() {
		return Integer.valueOf(speed.getText());
	}

	/**
	 * sets the listener to the buttons
	 * 
	 * @param listener
	 *            listener which should be added
	 */
	public void setListener(ActionListener listener) {
		this.saveBtn.addActionListener(listener);
		this.backBtn.addActionListener(listener);
	}
}
