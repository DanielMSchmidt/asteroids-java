package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public abstract class GUI extends JFrame {
	BufferedImage picture;
	BufferedImage originalPicture;

    GUI(String title) {
        super(title);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL bildURL = getClass().getResource("/view/stars.jpg");
    	
		try {
			picture = ImageIO.read(bildURL);
		} catch (IOException e) {
			//FIXME Add errror handling
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    class GraphicPanel extends JPanel {

        GraphicPanel() {
            setBackground(Color.black);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(picture, 0, 0, this);

        }

    }
}
