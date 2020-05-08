package geometry;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import geometry.HexGeometry;
import geometry.Hexagon;

@SuppressWarnings("serial")
public class HexPanel extends JPanel implements
    MouseListener, MouseMotionListener 
{   
    private static Color COLOR_EMPTY_EDGE = Color.WHITE;
    private static Color COLOR_EMPTY_FILL = Color.BLACK;
    private static Color COLOR_HOVER = Color.RED;
    private static Color COLOR_PLAYER1 = Color.BLUE;
    private static Color COLOR_PLAYER2 = Color.GREEN;

    private int n, width, height;
    private Hexagon hovering;
    private HexGeometry geometry;

    public HexPanel (int n, int width, int height) {
        this.n = n;
        this.width = width;
        this.height = height;
        this.geometry = new HexGeometry(n, width, height);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.repaint();
    }

    public void changeSize (int n) {
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
                g.setColor(COLOR_EMPTY_EDGE);
                g.drawPolygon(hx);
            }
    }

    @Override
    public void mousePressed (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon h = geometry.getHexagonByPosition(x, y);
        h.color = Color.RED; // determine action
        this.repaint();
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon h = geometry.getHexagonByPosition(x, y);
        h.color = Color.RED; // determine action
        this.repaint();
    }

    @Override
    public void mouseMoved (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon hexag = geometry.getHexagonByPosition(x, y);
        if (hexag != null) {
            if (this.hovering != null)
                this.hovering.color = Color.BLACK;
            this.hovering = hexag;
            this.hovering.color = COLOR_HOVER;
            this.repaint();
        } else if (this.hovering != null) {
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