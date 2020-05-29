package geometry;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import geometry.HexPanel;
import enums.PlayerIndex;
import enums.PlayerType;
import logika.HexPlayer;
import logika.HexGame;
import leader.Leader;

@SuppressWarnings("serial")
public class HexFrame extends JFrame implements 
    ActionListener, ComponentListener 
{
    private int height, width;
    private JMenuBar options;
    private JMenu gameOptions;
    private JMenuItem gameSize, playerTypes, gameStart;
    private JLabel status;

    public HexPanel hexPanel;
    public HexGame game;
    private int n;
    private HexPlayer player0, player1;

    public HexFrame (int width, int height) {
        this.height = height; 
        this.width = width;

        this.setTitle("Hex");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

        this.options = new JMenuBar();
        this.setJMenuBar(this.options);
        this.gameOptions = new JMenu("Game Settings");
        this.options.add(gameOptions);

        this.gameSize = new JMenuItem("Game Size");
        this.gameOptions.add(gameSize);
        this.gameSize.addActionListener(this);

        this.playerTypes = new JMenuItem("Player Types");
        this.gameOptions.add(playerTypes);
        this.playerTypes.addActionListener(this);

        this.gameStart = new JMenuItem("Start Game");
        this.gameOptions.add(gameStart);
        this.gameStart.addActionListener(this);

        this.hexPanel = new HexPanel(n, width, height);
        this.add(hexPanel);

        GridBagConstraints frameLayout = new GridBagConstraints();
		frameLayout.gridx = 0;
		frameLayout.gridy = 0;
		frameLayout.fill = GridBagConstraints.BOTH;
		frameLayout.weightx = 1.0;
		frameLayout.weighty = 1.0;
		this.getContentPane().add(hexPanel, frameLayout);
		
		this.status = new JLabel();
		this.status.setFont(new Font(
            this.status.getFont().getName(),
			this.status.getFont().getStyle(),
            20
        ));
		GridBagConstraints statusLayout = new GridBagConstraints();
		statusLayout.gridx = 0;
		statusLayout.gridy = 1;
		statusLayout.anchor = GridBagConstraints.CENTER;
		this.getContentPane().add(
            this.status, statusLayout
        );
        
        this.updateDimensions();
        this.updateStatus();
        this.addComponentListener(this);
        this.setSize(new Dimension(width, height));
        this.setVisible(true);
        this.setFocusable(true);
    }

    public void updateAll () {
        this.updateStatus();
    }

    private void updateStatus () {
        String build = "";
        HexPlayer player;
        switch (Leader.status) {
            case ACTIVE: 
                player = Leader.currentPlayer();
                build = String.format(
                    "Player Turn: %s",
                    player.index.name()
                );
                break;
            case FINISHED:
                player = Leader.winningPlayer();
                build = String.format(
                    "Game Winner: %s",
                    player.index.name()
                );
                break;
            case VOID: 
                build = String.format(
                    "Game Type: %s vs %s",
                    (this.player0 == null) ? "NONE" : this.player0.type.name(), 
                    (this.player1 == null) ? "NONE" : this.player1.type.name()
                );
        }
        this.status.setText(build);
    }

    private void resetHexSize (int n) {
        this.n = n;
        this.hexPanel.resetHexSize(n);
        this.hexPanel.setFocusable(true);
        this.hexPanel.setVisible(true);
        this.hexPanel.updateUI();
    }

    private int inputGameSize () {
        return Integer.parseInt(
            JOptionPane.showInputDialog(
                this, "Select Game Size"
            )
        );
    }

    private String inputPlayerTypes () {
        String[] possibilities = {
            "Human on Human", 
            "Human on Machine", 
            "Machine on Human",
            "Machine on Machine"
        };
        return (String) JOptionPane.showInputDialog(
            this,
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
            Leader.clearGame();
        } else if (name.equals("Player Types")) {
            String types = inputPlayerTypes();
            PlayerType t1, t2;
            if (types.equals("Human on Human")) {
                t1 = PlayerType.HUMAN;
                t2 = PlayerType.HUMAN;
            } else if (types.equals("Human on Machine")) {
                t1 = PlayerType.HUMAN;
                t2 = PlayerType.MACHINE;
            } else if (types.equals("Machine on Human")) {
                t1 = PlayerType.MACHINE;
                t2 = PlayerType.HUMAN;
            } else if (types.equals("Machine on Machine")) {
                t1 = PlayerType.MACHINE;
                t2 = PlayerType.MACHINE;
            } else return;
            this.player0 = new HexPlayer(
                PlayerIndex.PLAYER0, t1, 
                HexPanel.COLOR_PLAYER0
            );
            this.player1 = new HexPlayer(
                PlayerIndex.PLAYER1, t2,
                HexPanel.COLOR_PLAYER1
            );
            Leader.clearGame();
        } else if (name.equals("Start Game")) {
            if (this.n > 0 && 
                this.player0 != null &&
                this.player1 != null &&
                this.game == null
            ) Leader.newGame(n, player0, player1, this);
        }
        updateStatus();
    }

    private void updateDimensions() {
        Dimension frameD = this.getSize();
        this.width = frameD.width; 
        this.height = frameD.height;
        Dimension panelD = this.hexPanel.getSize();
        this.hexPanel.updateDimensions(
            panelD.width, panelD.height
        );
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.updateDimensions();
    }

    @Override
    public void componentHidden(ComponentEvent e) {return;}
    @Override
    public void componentMoved(ComponentEvent e) {return;}
    @Override
    public void componentShown(ComponentEvent e) {return;}
}