package geometry;

import java.util.HashSet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import enums.FieldType;
import enums.PlayerIndex;
import enums.LeaderCode;
import leader.Leader;
import geometry.HexGeometry;
import geometry.Hexagon;

@SuppressWarnings("serial")
public class HexPanel extends JPanel implements
    MouseListener, MouseMotionListener 
{   
    private static Color COLOR_EMPTY_EDGE = Color.WHITE;
    private static Color COLOR_EMPTY_FILL = Color.BLACK;
    private static Color COLOR_HOVER = new Color(15, 15, 15);
    private static Color COLOR_PLAYER0 = Color.BLUE;
    private static Color COLOR_PLAYER1 = Color.GREEN;

    private int n, width, height;
    private Hexagon hovering;
    private HexGeometry geometry;
    private PlayerIndex currentPlayer;

    public HexPanel (int n, int width, int height) {
        this.currentPlayer = PlayerIndex.PLAYER0;
        this.width = width;
        this.height = height;
        this.resetHexSize(n);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void resetHexSize (int n) {
        this.n = n;
        this.geometry = new HexGeometry(n, this.width, this.height);
        this.repaint();
    }

    public void updateDimensions (int width, int height) {
        this.height = height; this.width = width;
        this.geometry.updateDimensions(width, height);
        this.repaint();
    }

    public Dimension getPrefferedDimension () {
        return new Dimension(this.width, this.height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < this.n; i++)
            for (int j = 0; j < this.n; j++) {
                Hexagon hx = geometry.getHexagonByIndex(i, j);
                g.setColor((hx.color == null) ? COLOR_EMPTY_FILL : hx.color);
                g.fillPolygon(hx);
                g.setColor(
                    (hx.borderColor == null) ? 
                    COLOR_EMPTY_FILL : hx.borderColor
                );
                g.drawPolygon(hx);
            }
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        int[] ij = geometry.getIndexByPosition(x, y);
        if (ij == null) return;
        LeaderCode status = Leader.playHuman (ij[0], ij[1]);
        switch (status) {
            case MOVE_VALID:
                Hexagon hex = this.geometry.getHexagonByIndex(
                    ij[0], ij[1]
                );
                switch (this.currentPlayer) {
                    case PLAYER0: 
                        hex.color = COLOR_PLAYER0;
                        hex.type = FieldType.TYPE0;
                        this.currentPlayer = PlayerIndex.PLAYER1;
                        break;
                    case PLAYER1:
                        hex.color = COLOR_PLAYER1;
                        hex.type = FieldType.TYPE1;
                        this.currentPlayer = PlayerIndex.PLAYER0;
                        break;
                }
                this.repaint();
                break;
            case PLAYER_WON:
                HashSet<int[]> path = Leader.winningPath();
                if (path == null) return;
                for (int[] cord : path) {
                    Hexagon hx = this.geometry.getHexagonByIndex(
                        cord[0], cord[1]
                    );
                    if (hx != null)
                        hx.borderColor = Color.YELLOW;
                }
                break;
            case MOVE_INVALID:
                System.out.println("Move Invalid");
                break;
        }  
    }

    @Override
    public void mousePressed (MouseEvent e) {
        //this.mouseClicked (e);
    }

    @Override
    public void mouseMoved (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon hexag = geometry.getHexagonByPosition(x, y);
        if (hexag != null) {
            if (
                this.hovering != null && 
                this.hovering.type.equals(FieldType.EMPTY)
            ) this.hovering.color = COLOR_EMPTY_FILL;
            if (hexag.type.equals(FieldType.EMPTY)) {
                this.hovering = hexag;
                this.hovering.color = COLOR_HOVER;
            }
            this.repaint();
        } else if (
            this.hovering != null &&
            this.hovering.type.equals(FieldType.EMPTY)
        ) {
            this.hovering.color = COLOR_EMPTY_FILL;
            this.hovering = null;
            this.repaint();
        }
    }

    @Override 
    public void mouseEntered (MouseEvent e) {return;}
    @Override 
    public void mouseExited (MouseEvent e) {return;}
    @Override
    public void mouseDragged (MouseEvent e) {return;}
    @Override
    public void mouseReleased (MouseEvent e) {return;}
}