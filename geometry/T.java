import java.util.Arrays;

import GeometryHex;

public class T {
	public static void main(String[] args) {
        GeometryHex g = new GeometryHex(2, 600.0, 600.0);
        g.write();
        g.getHexagon(5.0, 5.0);
        Hexagon hexagon = Hexagon g.hexagonMatrix[1][1];
        isInConvexHull(5.0, 5.0, hexagon);
        //System.out.println(Arrays.deepToString(g.vertices(0.0, 0.0)));
    }
}