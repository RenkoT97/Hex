package geometry;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import geometry.HexPanel;
import enums.PlayerIndex;
import enums.PlayerType;
import logic.HexPlayer;
import logic.HexGame;
import leader.Leader;

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

        this.gameSize = new JMenuItem("Game Size");
        this.gameOptions.add(gameSize);
        this.gameSize.addActionListener(this);

        this.playerTypes = new JMenuItem("Player Types");
        this.gameOptions.add(playerTypes);
        this.playerTypes.addActionListener(this);

        this.gameStart = new JMenuItem("Start Game");
        this.gameOptions.add(gameStart);
        this.gameStart.addActionListener(this);

        this.options.add(gameOptions);
        this.mainframe.add(this.options, BorderLayout.NORTH);
        this.mainframe.addComponentListener(this);
        this.mainframe.setSize(new Dimension(width, height));
        this.mainframe.setVisible(true);
        this.mainframe.setFocusable(true);
    }

    private void resetHexSize (int n) {
        this.n = n;
        if (this.hexPanel == null) {
            this.hexPanel = new HexPanel(n, width, height);
            this.mainframe.add(hexPanel);
        } else this.hexPanel.resetHexSize(n);
        this.hexPanel.setFocusable(true);
        this.hexPanel.setVisible(true);
        this.hexPanel.updateUI();
    }

    private int inputGameSize () {
        return Integer.parseInt(
            JOptionPane.showInputDialog(
                this.mainframe, 
                "Select Game Size"
            )
        );
    }

    private String inputPlayerTypes () {
        String[] possibilities = {
            "Human on Human", 
            "Human on Machine", 
            "Machine on Machine"
        };
        return (String) JOptionPane.showInputDialog(
            this.mainframe,
            "Choose Player Types",
            "Player Options",
            JOptionPane.PLAIN_MESSAGE,
            null,
            possibilities,
            "Human on Human"
        );
    }

    public void actionPerformed (ActionEvent e) {
        String name = e.getActionCommand();
        if (name.equals("Game Size")) {
            int n = inputGameSize();
            this.resetHexSize (n);
        } else if (name.equals("Player Types")) {
            String s = inputPlayerTypes();
            PlayerType t1, t2;
            if (s.equals("Human on Human")) {
                t1 = PlayerType.HUMAN;
                t2 = PlayerType.HUMAN;
            } else if (s.equals("Human on Machine")) {
                t1 = PlayerType.HUMAN;
                t2 = PlayerType.MACHINE;
            } else if (s.equals("Machine on Machine")) {
                t1 = PlayerType.MACHINE;
                t2 = PlayerType.MACHINE;
            } else return;
            this.player1 = new HexPlayer(PlayerIndex.PLAYER0, t1);
            this.player2 = new HexPlayer(PlayerIndex.PLAYER1, t2);
        } else if (name.equals("Start Game")) {
            if (this.n > 0 && 
                this.player1 != null &&
                this.player2 != null &&
                this.game == null
            ) Leader.newGame(n, player1, player2);
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