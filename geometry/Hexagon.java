package geometry;

import java.awt.Color;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class Hexagon extends Polygon {
	public Color color;

	public Hexagon(int[] vx, int[] vy) {
		super(vx, vy, vx.length);
    }
}