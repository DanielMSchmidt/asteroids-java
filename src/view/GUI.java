package view;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Delivers basic functionallity to the other GUIs which
 * inherite from this one
 * 
 * @author danielschmidt
 * 
 */
public abstract class GUI extends JFrame {
	BufferedImage picture;
	BufferedImage originalPicture;

	GraphicPanel paintArea;
	Dimension resolution;

	GUI(String title, Dimension resolution) {
		super(title);
		setSize(resolution.width, resolution.height);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		picture = loadImage("/view/stars.jpg");
		originalPicture = copy(picture);
	}

	/**
	 * copys an image
	 * 
	 * @param image
	 *            the image which should be copied
	 * @return a copy of the input image
	 */
	static BufferedImage copy(BufferedImage image) {
		ColorModel colorModel = image.getColorModel();
		return new BufferedImage(colorModel, image.copyData(null), colorModel.isAlphaPremultiplied(), null);
	}

	/**
	 * loads an image from a source
	 * 
	 * @param source
	 *            the source of the image
	 * @return the loaded image
	 */
	public BufferedImage loadImage(String source) {
		BufferedImage img = null;
		URL bildURL = getClass().getResource(source);
		try {
			img = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	/**
	 * Graphic Panel with black background
	 * 
	 * @author danielschmidt
	 * 
	 */
	class GraphicPanel extends JPanel {
		GraphicPanel() {
			setBackground(Color.black);
		}

		public void paintComponent(Graphics g) {
			g.drawImage(picture, 0, 0, this);
		}
	}

	/**
	 * creates a button, captioned with the inputtext
	 * 
	 * @param buttonText
	 *            text, which should be on the button
	 * @return the generated button
	 */
	public static JButton createButton(String buttonText) {
		JButton btn = new JButton(buttonText);
		btn.setBorderPainted(false);
		btn.setFont(new Font("sansserif", Font.BOLD, 18));
		btn.setForeground(Color.white);
		btn.setBackground(Color.black);
		btn.setOpaque(false);
		return btn;
	}

	/**
	 * Adds a component to a container
	 * 
	 * @param cont
	 *            container to which the component should be added
	 * @param gbl
	 *            the gridbaglayout
	 * @param gbc
	 *            the gridbagconstrains
	 * @param c
	 *            the component, which should be added
	 */
	static void addComponent(Container cont, GridBagLayout gbl, GridBagConstraints gbc, Component c) {
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	/**
	 * Prints an error out to the view
	 * 
	 * @param errorString
	 *            errormessage by you
	 * @param errorMessage
	 *            errormessage by the system
	 */
	public void printError(String errorString, String errorMessage) {
		picture = copy(originalPicture);
		if (picture != null) {

			Graphics2D g = picture.createGraphics();
			g.setColor(Color.red);
			g.setFont(new Font("sansserif", Font.BOLD, 20));
			g.drawString(errorString, 20, resolution.height / 2);
			g.setColor(Color.white);
			g.setFont(new Font("sansserif", Font.BOLD, 12));
			g.drawString(errorMessage, 20, resolution.height / 2 + 30);
		}
		paintArea.repaint();
	}

	/**
	 * adds a button to a container
	 * 
	 * @param cont
	 *            container to which the button should be added
	 * @param gbl
	 *            the gridBagLayout
	 * @param btn
	 *            the button which should be added
	 */
	static void addButton(Container cont, GridBagLayout gbl, JButton btn) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		addComponent(cont, gbl, constraints, btn);
	}
}
