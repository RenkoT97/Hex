package geometry;

import java.util.HashSet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import enums.FieldType;
import leader.Leader;
import geometry.HexGeometry;
import geometry.Hexagon;
import logika.HexPlayer;

@SuppressWarnings("serial")
public class HexPanel extends JPanel implements
    MouseListener, MouseMotionListener 
{   
    public static Color COLOR_EMPTY_EDGE = Color.WHITE;
    public static Color COLOR_EMPTY_FILL = Color.BLACK;
    public static Color COLOR_HOVER = new Color(15, 15, 15);
    public static Color COLOR_PLAYER0 = new Color(255, 128, 0);
    public static Color COLOR_PLAYER1 = new Color(80, 168, 227);
    public static Color COLOR_WINNING = new Color(255, 0, 0);

    private int n, width, height;
    private Hexagon hovering;
    private HexGeometry geometry;

    public HexPanel (int n, int width, int height) {
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
        this.width = width; this.height = height;
        this.geometry.updateDimensions(width, height);
        this.repaint();
    }

    public Dimension getPrefferedDimension () {
        return new Dimension(this.width, this.height);
    }

    public void hexagonPlayed (int i, int j, HexPlayer p) {
        Hexagon hex = this.geometry.getHexagonByIndex(i, j);
        hex.color = p.color;
        switch (p.index) {
            case PLAYER0: hex.type = FieldType.TYPE0;
            case PLAYER1: hex.type = FieldType.TYPE1;
        }
        this.repaint();
    }

    public void markWinningPath (HashSet<int[]> path) {
        for (int[] ij : path) {
            Hexagon hx = this.geometry.getHexagonByIndex(ij[0], ij[1]);
            hx.borderColor = COLOR_WINNING;
        }
        this.repaint();
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
                    COLOR_EMPTY_EDGE : hx.borderColor
                );
                g.drawPolygon(hx);
            }
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        int[] ij = geometry.getIndexByPosition(x, y);
        if (ij == null) return;
        Leader.playHuman (ij[0], ij[1]);
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