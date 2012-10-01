package view;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

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
    
    static BufferedImage copy(BufferedImage bi) {
    	 ColorModel cm = bi.getColorModel();
    	 return new BufferedImage(cm, bi.copyData(null), cm.isAlphaPremultiplied(), null);
    }
    
	public BufferedImage loadImage(String source){
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
    
    class GraphicPanel extends JPanel {
        GraphicPanel() {
            setBackground(Color.black);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(picture, 0, 0, this);
        }
    }
    
    public static JButton createButton(String btnText) {
        JButton btn = new JButton(btnText);
        btn.setBorderPainted(false);
        btn.setFont(new Font("sansserif", Font.BOLD, 18));
        btn.setForeground(Color.white);
        btn.setBackground(Color.black);
        btn.setOpaque(false);
        return btn;
    }

    static void addComponent(Container cont, GridBagLayout gbl, GridBagConstraints gbc, Component c) {
        gbl.setConstraints(c, gbc);
        cont.add(c);
    }
    
	public void printError(String errorString, String errorMessage) {
		picture = copy(originalPicture);
		if (picture != null) {
			
			Graphics2D g = picture.createGraphics();
			g.setColor(Color.red);
			g.setFont(new Font("sansserif", Font.BOLD, 20));
			g.drawString(errorString, 20, resolution.height/2);
			g.setColor(Color.white);
			g.setFont(new Font("sansserif", Font.BOLD, 12));
			g.drawString(errorMessage, 20, resolution.height/2 + 30);
		}
		paintArea.repaint();
	}
    
    static void addButton(Container cont, GridBagLayout gbl, JButton btn) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        addComponent(cont, gbl, constraints, btn);
    }
}
