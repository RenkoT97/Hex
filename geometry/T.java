import java.util.Arrays;

import GeometryHex;

public class T {
	public static void main(String[] args) {
        GeometryHex g = new GeometryHex(2, 600.0, 600.0, 2.0);
        g.write();
        System.out.println(Arrays.deepToString(g.vertices(0.0, 0.0)));
	}
}