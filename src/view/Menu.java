package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Menu extends GUI{
    public JButton startBtn;
    public JButton loadBtn;
    public JButton highscoreBtn;
    public JButton settingsBtn;
    public JButton exitBtn;
    
    public Menu(Dimension resolution) {
        super("Universe - Menu", resolution);

        paintArea = new GraphicPanel();

        GridBagLayout gbl = new GridBagLayout();
        paintArea.setLayout(gbl);

        //Title
        JLabel title = new JLabel("UNIVERSE");
        title.setFont(new Font("sansserif", Font.BOLD, 42));
        title.setForeground(Color.green);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(0, 5, 100, 5);
        addComponent(paintArea, gbl, constraints, title);

        //Buttons
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

        add(paintArea);
    }
    
    static void addButton(Container cont, GridBagLayout gbl, JButton btn) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(0, 5, 0, 5);
        addComponent(cont, gbl, constraints, btn);
    }
    
    public void setListener(ActionListener l) {
        this.startBtn.addActionListener(l);
        this.loadBtn.addActionListener(l);
        this.highscoreBtn.addActionListener(l);
        this.settingsBtn.addActionListener(l);
        this.exitBtn.addActionListener(l);
    }
}
