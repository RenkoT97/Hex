import java.awt.Color;
import java.util.Arrays;

public class T {
	public static void main(String[] args) {
        GeometryHex g = new GeometryHex(11, 600.0, 600.0);
        g.write();
        System.out.println(Arrays.toString(g.getHexagon(20.0, 20.0)));
        //Hexagon hexagon = new Hexagon(Color.WHITE, g.vertices(0.0, 0.0));
        //System.out.println(g.isInConvexHull(50.0, 50.0, hexagon));
        System.out.println(Arrays.deepToString(g.vertices(0.0, 0.0)));
    }
}