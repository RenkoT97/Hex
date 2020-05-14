package geometry;

import java.awt.Color;
import java.awt.Polygon;

import enums.FieldType;

@SuppressWarnings("serial")
public class Hexagon extends Polygon {
	public Color color, borderColor;
	public FieldType type;

	public Hexagon(int[] vx, int[] vy) {
		super(vx, vy, 6);
		this.type = FieldType.EMPTY;
	}

	public void resetPoints(int[] vx, int[] vy) {
		this.reset();
		this.npoints = 6;
		this.xpoints = vx;
		this.ypoints = vy;
	}
}