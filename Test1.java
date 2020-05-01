import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import logic.HexLogic;
import geometry.HexGeometry;
import geometry.Hexagon;

@SuppressWarnings("serial")
class Game extends JPanel implements
    MouseListener, MouseMotionListener 
{   
    private int n, width, height;
    private Color player0, player1, hover;
    private Hexagon hovering;
    private HexLogic logic;
    private HexGeometry geometry;

    public Game (int n, int width, int height) {
        this.n = n;
        this.player0 = Color.BLUE; 
        this.player1 = Color.GREEN;
        this.hover = Color.GRAY;
        this.logic = new HexLogic(n);
        this.geometry = new HexGeometry(
            n, width, height
        );
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void updateDimensions (
        int width, int height, int marginX, int marginY
    ) {
        this.geometry.updateDimensions(
            width, height
        );
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
                g.setColor(Color.BLACK);
                g.drawPolygon(hx);
                g.setColor(hx.color);
                g.fillPolygon(hx);
            }
    }

    @Override
    public void mousePressed (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon h = geometry.getHexagonByPosition(x, y);
        h.color = Color.RED;
        this.repaint();
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        int x = e.getX(); int y = e.getY();
        Hexagon h = geometry.getHexagonByPosition(x, y);
        h.color = Color.RED;
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
            this.hovering.color = Color.RED;
            this.repaint();
        } else if (this.hovering != null) {
            this.hovering.color = Color.BLACK;
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

class HexApp implements ActionListener, ComponentListener {
    private int height, width;
    private JFrame mainframe;
    private JMenuBar options;
    private Game hex;

    public HexApp (int n, int width, int height) {
        this.height = height; 
        this.width = width;
        this.mainframe = new JFrame();
        this.options = new JMenuBar();
        this.hex = new Game(n, width, height);

        JMenu gameOptions = new JMenu();
        JMenuItem gameSize = new JMenuItem("gameSize");
        gameOptions.add(gameSize);
        gameSize.addActionListener(this);
        this.options.add(gameOptions);

        this.mainframe.add(hex);
        this.mainframe.add(this.options, BorderLayout.NORTH);

        this.hex.setFocusable(true);
        this.mainframe.addComponentListener(this);
        this.mainframe.setSize(new Dimension(width, height));
        this.mainframe.setVisible(true);
        this.mainframe.setFocusable(true);
    }

    public void actionPerformed (ActionEvent e) {
        String name = e.getActionCommand();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension d = this.mainframe.getSize();
        int x = d.width; int y = d.height;
        this.hex.updateDimensions(x, y, 20, 20);
    }

    @Override
    public void componentHidden(ComponentEvent e) {return;}
    @Override
    public void componentMoved(ComponentEvent e) {return;}
    @Override
    public void componentShown(ComponentEvent e) {return;}
}

public class Test1 {

    public static void main(String[] args) {
        HexApp hex = new HexApp(11, 500, 500);
    }
}