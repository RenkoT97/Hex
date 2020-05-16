import java.awt.Color;
import java.util.Arrays;
import geometry.HexGeometry;

public class T {
	public static void main(String[] args) {
        HexGeometry g = new HexGeometry(11, 600.0, 600.0);
        //g.write();
        //Hexagon hexagon = new Hexagon(Color.WHITE, g.vertices(0.0, 0.0));
        //System.out.println(g.isInConvexHull(50.0, 50.0, hexagon));
        System.out.println(g.boardSize);
    }
}