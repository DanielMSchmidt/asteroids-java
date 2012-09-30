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

    GUI(String title, Dimension resolution) {
        super(title);
        setSize(resolution.width, resolution.height);
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
		originalPicture = deepCopy(picture);
    }
    
    static BufferedImage deepCopy(BufferedImage bi) {
    	 ColorModel cm = bi.getColorModel();
    	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	 WritableRaster raster = bi.copyData(null);
    	 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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
    
    static void addButton(Container cont, GridBagLayout gbl, JButton btn) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        addComponent(cont, gbl, constraints, btn);
    }
}
