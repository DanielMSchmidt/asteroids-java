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

public class Game extends GUI {
	BufferedImage spaceshipImg;
	BufferedImage spaceshipMovesImg;
	BufferedImage shotImg;
	BufferedImage asteroid10Img;
	BufferedImage asteroid20Img;
	BufferedImage asteroid30Img;
	GraphicPanel paintArea;
	BufferedImage background;
	int shipAlignment;
	Dimension resolution;

	public Game(Dimension resolution) {
		super("Universe - Game", resolution);
		this.resolution = resolution;
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

		bildURL = getClass().getResource("/view/asteroid30.png");
		try {
			asteroid30Img = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		bildURL = getClass().getResource("/view/asteroid20.png");
		try {
			asteroid20Img = ImageIO.read(bildURL);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		bildURL = getClass().getResource("/view/asteroid10.png");
		try {
			asteroid10Img = ImageIO.read(bildURL);
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

	public void printPrintables(ArrayList<Printable> list, boolean moving, int score, boolean help) {
		picture = deepCopy(originalPicture);
		for (Printable item : list) {
			Point position = new Point((int) item.getPosition().getX() - (int) item.getSize(), (int) item.getPosition().getY() - (int) item.getSize());
			if (picture != null) {
				Graphics2D g = picture.createGraphics();
				if (item.getType() == "player") {
					shipAlignment = item.getAlignment();
					if (moving) {
						g.drawImage(rotate(spaceshipMovesImg, shipAlignment), position.x, position.y, this);
					} else {
						g.drawImage(rotate(spaceshipImg, shipAlignment), position.x, position.y, this);
					}
				} else if (item.getType() == "asteroid") {
					if(item.getSize() == 30){
						g.drawImage(asteroid30Img, position.x, position.y, this);
					}
					else if(item.getSize() == 20){
						g.drawImage(asteroid20Img, position.x, position.y, this);
					}
					else{
						g.drawImage(asteroid10Img, position.x, position.y, this);
					}
						
				} else {
					Point2D additionalSize = SpaceObject.transformVektorViaAngle(new Point(0, -10), shipAlignment);
					g.drawImage(rotate(shotImg, item.getAlignment()), position.x + (int) additionalSize.getX(), position.y + (int) additionalSize.getY(), this);
				}
				g.setFont(new Font("sansserif", Font.BOLD, 14));
				g.setColor(Color.green);
				g.drawString("Help - F1", 10, 20);
				g.drawString("Points: " + score, resolution.width-100, 20);
			}			
		}
		if (help){
			displayHelp();
		}
		paintArea.repaint();
	}

	public static BufferedImage rotate(BufferedImage img, int alignment) {
		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage newImage = new BufferedImage(w, h, img.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.toRadians(alignment), w / 2, h / 2); // w/2 und h/2
		                                                     // wegen drehpunkt
		g2.drawImage(img, null, 0, 0);

		return newImage;
	}
	
	
	public void displayHelp(){
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

	public void gameover() {
		if (picture != null) {
			Graphics2D g = picture.createGraphics();
			g.setColor(Color.red);
			g.setFont(new Font("sansserif", Font.BOLD, 34));
			g.drawString("GAME OVER!", resolution.width/2-100, resolution.height/2);
			g.setFont(new Font("sansserif", Font.BOLD, 20));
			g.drawString("Press ESC", resolution.width/2-35, resolution.height/2 + 40);
		}
		paintArea.repaint();
	}

	public void setKeyListener(KeyListener l) {
		this.addKeyListener(l);
	}
}
