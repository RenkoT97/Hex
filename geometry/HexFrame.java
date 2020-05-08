package geometry;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import geometry.HexPanel;
import enums.PlayerIndex;
import enums.PlayerType;
import logic.HexPlayer;
import logic.HexGame;

public class HexFrame implements ActionListener, ComponentListener {
    private int height, width;
    private HexPanel hexPanel;
    private JFrame mainframe;
    private JMenuBar options;
    private JMenu gameOptions;
    private JMenuItem gameSize, playerTypes, gameStart;

    public HexGame game;
    private int n;
    private HexPlayer player1, player2;

    public HexFrame (int width, int height) {
        this.height = height; 
        this.width = width;
        this.mainframe = new JFrame();

        this.options = new JMenuBar();
        this.gameOptions = new JMenu("Game Settings");

        this.gameSize = new JMenuItem("gameSize");
        this.gameOptions.add(gameSize);
        this.gameSize.addActionListener(this);

        this.playerTypes = new JMenuItem("playertypes");
        this.gameOptions.add(playerTypes);
        this.playerTypes.addActionListener(this);

        this.gameStart = new JMenuItem("startgame");
        this.gameOptions.add(gameStart);
        this.gameStart.addActionListener(this);

        this.options.add(gameOptions);
        this.mainframe.add(this.options, BorderLayout.NORTH);
        this.mainframe.addComponentListener(this);
        this.mainframe.setSize(new Dimension(width, height));
        this.mainframe.setVisible(true);
        this.mainframe.setFocusable(true);
    }

    private void newHex (int n) {
        this.n = n;
        if (this.hexPanel == null) {
            this.hexPanel = new HexPanel(n, width, height);
            this.mainframe.add(hexPanel);
        } else this.hexPanel.changeSize(n);
        this.hexPanel.setFocusable(true);
        this.hexPanel.setVisible(true);
        this.hexPanel.updateUI();
    }

    private int grabInputNum (String title) {
        return Integer.parseInt(
            JOptionPane.showInputDialog(
                this.mainframe, title
            )
        );
    }

    public void actionPerformed (ActionEvent e) {
        String name = e.getActionCommand();
        if (name.equals("gameSize")) {
            int n = grabInputNum("Select Game Size");
            this.newHex (n);
        } else if (name.equals("playerTypes")) {
            
        } else if (name.equals("gamestart")) {
            if (this.n > 0 && 
                this.player1 != null &&
                this.player2 != null
            ) this.game = new HexGame (n, player1, player2);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension d = this.mainframe.getSize();
        this.width = d.width; this.height = d.height;
        if (this.hexPanel != null)
            this.hexPanel.updateDimensions(width, height);
    }

    @Override
    public void componentHidden(ComponentEvent e) {return;}
    @Override
    public void componentMoved(ComponentEvent e) {return;}
    @Override
    public void componentShown(ComponentEvent e) {return;}
}