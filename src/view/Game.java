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
	BufferedImage background;
	int shipAlignment;


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
	
	public void printPrintables(ArrayList<Printable> list, boolean moving, int score, boolean help) {
		picture = copy(originalPicture);
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
					switch ((int)item.getSize()){
						case 30:
							g.drawImage(asteroid30Img, position.x, position.y, this);
						case 20:
							g.drawImage(asteroid20Img, position.x, position.y, this);
						default:
							g.drawImage(asteroid10Img, position.x, position.y, this);
					}
				} else {
					g.drawImage(rotate(shotImg, item.getAlignment()), position.x-8, position.y-8, this);
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
