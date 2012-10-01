package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.Printable;
import model.SpaceObject;

/**
 * The GUI for the Game
 * 
 * @author danielschmidt
 * 
 */
public class Game extends GUI {
	BufferedImage spaceshipImg;
	BufferedImage spaceshipMovesImg;
	BufferedImage shotImg;
	BufferedImage asteroid10Img;
	BufferedImage asteroid20Img;
	BufferedImage asteroid30Img;
	BufferedImage background;
	int shipAlignment;

	/**
	 * Constructor for the Gameview
	 * 
	 * @param resolution
	 *            the resolution of the Gameview
	 */
	public Game(Dimension resolution) {
		super("Universe - Game", resolution);
		this.resolution = resolution;
		paintArea = new GraphicPanel();
		paintArea.setLayout(null);
		add(paintArea);

		spaceshipImg = loadImage("/view/spaceship.png");
		spaceshipMovesImg = loadImage("/view/spaceshipFire.png");
		asteroid30Img = loadImage("/view/asteroid30.png");
		asteroid20Img = loadImage("/view/asteroid20.png");
		asteroid10Img = loadImage("/view/asteroid10.png");
		shotImg = loadImage("/view/shot.png");
	}

	/**
	 * Prints all printables into a copy of the originalPicture
	 * 
	 * @param printables
	 *            the ArrayList of printables, which should be printed
	 * @param moving
	 *            <code>true</code> if the player moves
	 * @param score
	 *            the current score which should be printed
	 * @param help
	 *            if <code>true</code> it displays the help
	 */
	public void printPrintables(ArrayList<Printable> printables, boolean moving, int score, boolean help) {
		picture = copy(originalPicture);
		for (Printable printable : printables) {
			Point position = new Point((int) printable.getPosition().getX() - (int) printable.getSize(),
			        (int) printable.getPosition().getY() - (int) printable.getSize());
			if (picture != null) {
				Graphics2D g = picture.createGraphics();
				if (printable.getType() == "player") {
					shipAlignment = printable.getAlignment();
					if (moving) {
						g.drawImage(rotate(spaceshipMovesImg, shipAlignment), position.x, position.y, this);
					} else {
						g.drawImage(rotate(spaceshipImg, shipAlignment), position.x, position.y, this);
					}
				} else if (printable.getType() == "asteroid") {
					switch ((int) printable.getSize()) {
						case 30:
							g.drawImage(asteroid30Img, position.x, position.y, this);
						case 20:
							g.drawImage(asteroid20Img, position.x, position.y, this);
						default:
							g.drawImage(asteroid10Img, position.x, position.y, this);
					}
				} else {
					g.drawImage(rotate(shotImg, printable.getAlignment()), position.x - 8, position.y - 8, this);
				}
				g.setFont(new Font("sansserif", Font.BOLD, 14));
				g.setColor(Color.green);
				g.drawString("Help - F1", 10, 20);
				g.drawString("Points: " + score, resolution.width - 100, 20);
			}
		}
		if (help) {
			displayHelp();
		}
		paintArea.repaint();
	}

	/**
	 * rotates an image
	 * 
	 * @param image
	 *            the image which should be rotated
	 * @param angle
	 *            the rotationangle
	 * @return a rotated image
	 */
	public static BufferedImage rotate(BufferedImage image, int angle) {
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage newImage = new BufferedImage(w, h, image.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.toRadians(angle), w / 2, h / 2); // w/2 und h/2
		                                                // wegen drehpunkt
		g2.drawImage(image, null, 0, 0);

		return newImage;
	}

	/**
	 * displays the helpoutput
	 */
	public void displayHelp() {
		if (picture != null) {
			Graphics2D g = picture.createGraphics();
			g.setFont(new Font("sansserif", Font.BOLD, 14));
			g.drawString("w - forward", 10, 40);
			g.drawString("a - turn left", 10, 60);
			g.drawString("d - turn right", 10, 80);
			g.drawString("Shift - turn faster", 10, 100);
			g.drawString("Space - shoot", 10, 120);
			g.drawString("ESC - exit", 10, 140);
		}
	}

	/**
	 * prints the output, which shows that the Game is over
	 */
	public void gameover() {
		if (picture != null) {
			Graphics2D g = picture.createGraphics();
			g.setColor(Color.red);
			g.setFont(new Font("sansserif", Font.BOLD, 34));
			g.drawString("GAME OVER!", resolution.width / 2 - 100, resolution.height / 2);
			g.setFont(new Font("sansserif", Font.BOLD, 20));
			g.drawString("Press ESC", resolution.width / 2 - 35, resolution.height / 2 + 40);
		}
		paintArea.repaint();
	}

	/**
	 * adds a keylistener to this view
	 * 
	 * @param listener
	 *            the keylistener which should be added
	 */
	public void setKeyListener(KeyListener listener) {
		this.addKeyListener(listener);
	}
}
