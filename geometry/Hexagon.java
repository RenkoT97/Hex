package geometry;

import java.awt.Color;
import java.awt.Polygon;

import enums.FieldType;

@SuppressWarnings("serial")
public class Hexagon extends Polygon {
	public Color color, borderColor;
	public FieldType type;

	public Hexagon(int[] vx, int[] vy) {
		super(vx, vy, vx.length);
		this.type = FieldType.EMPTY;
	}
}