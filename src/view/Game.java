package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

public class Game extends GUI {
	BufferedImage spaceshipImg;
	BufferedImage spaceshipMovesImg;
	BufferedImage shotImg;
	BufferedImage asteroidImg;
	GraphicPanel paintArea;
	BufferedImage background;
	int shipAlignment;

	public Game() {
		super("Universe - Game");
		paintArea = new GraphicPanel();

		paintArea.setLayout(null);
		add(paintArea);

		// FIXME Player isn't centered i guess (hold space and turn)
		// TODO Nicer code maybe?
		URL bildURL = getClass().getResource("/view/spaceship.png");
		try {
			spaceshipImg = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		bildURL = getClass().getResource("/view/spaceshipFire.png");
		try {
			spaceshipMovesImg = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		bildURL = getClass().getResource("/view/asteroid.png");
		try {
			asteroidImg = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		bildURL = getClass().getResource("/view/shot.png");
		try {
			shotImg = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printPrintables(ArrayList<Printable> list, boolean moving) {
		URL bildURL = getClass().getResource("/view/stars.jpg");

		try {
			picture = ImageIO.read(bildURL);

		}
		catch (IOException e) {
			// FIXME Errorhandling
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Printable item : list) {
			Point position = new Point((int) item.getPosition().getX() - (int) item.getSize(), (int) item.getPosition().getY() - (int) item.getSize());
			if (picture != null) {
				Graphics2D g = picture.createGraphics();
				if (item.getType() == "player") {

					shipAlignment = item.getAlignment();
					if (moving) {
						g.drawImage(rotate(spaceshipMovesImg, -shipAlignment), position.x, position.y, this);
					} else {
						g.drawImage(rotate(spaceshipImg, -shipAlignment), position.x, position.y, this);
					}
				} else if (item.getType() == "asteroid") {
					g.drawImage(asteroidImg, position.x, position.y, this);
				} else {
					//30 has to be transformed
					Point2D additionalSize = SpaceObject.transformVektorViaAngle(new Point(0, -30), shipAlignment);
					g.drawImage(rotate(shotImg, -shipAlignment), position.x + (int) additionalSize.getX(), position.y + (int) additionalSize.getY(), this);
				}
			}
			paintArea.repaint();
		}
	}

	public static BufferedImage rotate(BufferedImage img, int alignment) {
		// FIXME Shot should rotate too
		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage newImage = new BufferedImage(w, h, img.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.toRadians(-alignment), w / 2, h / 2); // w/2 und h/2
		                                                     // wegen drehpunkt
		g2.drawImage(img, null, 0, 0);

		return newImage;
	}

	public void printScore(String score) {
		if (picture != null) {
			Graphics2D g = picture.createGraphics();
			g.drawString("Ergebnis: " + score, 300, 300);
		}
		paintArea.repaint();
	}

	public void setKeyListener(KeyListener l) {
		this.addKeyListener(l);
	}
}
