package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Settings extends GUI{
	
	public JButton backBtn;
	public JButton saveBtn;
    JTextField playerName;
    JTextField startlevel;
    JTextField speed;
    
	public Settings(Dimension resolution){
		 super("Universe - Settings", resolution);
	        GraphicPanel paintArea = new GraphicPanel();
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
	        constraints.insets = new Insets(0,0,50,0);
	        constraints.gridwidth = GridBagConstraints.REMAINDER;
	        
	        addComponent(paintArea, gbl, constraints, panel);
	        
	        addButton(paintArea, gbl, saveBtn);
	        addButton(paintArea, gbl, backBtn);

	        add(paintArea);
	}
	
    public void setValues(String name, int level, int speedvalue) {
        playerName.setText(name);
        startlevel.setText(String.valueOf(level));
        speed.setText(String.valueOf(speedvalue));
    }

    public String getPlayerName() {
        return playerName.getText();
    }

    public int getStartLevel() {
        return Integer.valueOf(startlevel.getText());
    }

    public int getSpeed() {
        return Integer.valueOf(speed.getText());
    }
    
    public void setListener(ActionListener l) {
        this.saveBtn.addActionListener(l);
        this.backBtn.addActionListener(l);
    }
}
