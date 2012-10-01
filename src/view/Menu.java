package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Displays the menu
 * 
 * @author danielschmidt
 * 
 */
@SuppressWarnings("serial")
public class Menu extends GUI {
	public JButton startBtn;
	public JButton loadBtn;
	public JButton highscoreBtn;
	public JButton settingsBtn;
	public JButton exitBtn;

	/**
	 * Constructor for Menu
	 * 
	 * @param resolution
	 *            the resolution of the menu
	 */
	public Menu(Dimension resolution) {
		super("Universe - Menu", resolution);

		paintArea = new GraphicPanel();

		GridBagLayout gbl = new GridBagLayout();
		paintArea.setLayout(gbl);

		// Title
		JLabel title = new JLabel("UNIVERSE");
		title.setFont(new Font("sansserif", Font.BOLD, 42));
		title.setForeground(Color.green);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(0, 5, 100, 5);
		addComponent(paintArea, gbl, constraints, title);

		addButtons(gbl);

		add(paintArea);
	}

	/**
	 * adds Buttons to the menu
	 * 
	 * @param gbl
	 *            the gridBagLayout which is used
	 */
	private void addButtons(GridBagLayout gbl) {
		startBtn = createButton("Start");
		loadBtn = createButton("Load Game");
		highscoreBtn = createButton("Highscore");
		settingsBtn = createButton("Settings");
		exitBtn = createButton("Exit");
		addButton(paintArea, gbl, startBtn);
		addButton(paintArea, gbl, loadBtn);
		addButton(paintArea, gbl, highscoreBtn);
		addButton(paintArea, gbl, settingsBtn);
		addButton(paintArea, gbl, exitBtn);
	}

	/**
	 * adds button to the menu
	 * 
	 * @param cont
	 *            container which is used
	 * @param gbl
	 *            the layout which is used
	 * @param btn
	 *            the button which should be added
	 */
	static void addButton(Container cont, GridBagLayout gbl, JButton btn) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(0, 5, 0, 5);
		addComponent(cont, gbl, constraints, btn);
	}

	/**
	 * Sets the Listeners for the menu
	 * 
	 * @param listener
	 *            the listener which should be set
	 */
	public void setListener(ActionListener listener) {
		this.startBtn.addActionListener(listener);
		this.loadBtn.addActionListener(listener);
		this.highscoreBtn.addActionListener(listener);
		this.settingsBtn.addActionListener(listener);
		this.exitBtn.addActionListener(listener);
	}
}
