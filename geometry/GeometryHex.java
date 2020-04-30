import java.lang.Math;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Point;

class HexGeometry {
	private static final double SQRT3 = Math.sqrt(3);
	private static double marginRatio = 1 / 20;
	public int boardSize;
	public double panelHeight, panelWidth;
    private Hexagon[][] hexagonMatrix;
	private double margin, hexagonEdge, triangleHeight;
	
	HexGeometry(int boardSize, double panelHeight, double panelWidth) {
		this.boardSize = boardSize;
		this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		updateDimensions(panelWidth, panelHeight);
	}

	public void updateDimensions(double width, double height) {
		this.panelWidth = width;
		this.panelHeight = height;
		calculateMargin();
		calculateEdgeLenght();
		calculateTriangleHeight();
        resetHexagonMatrix();
	}

	private void calculateMargin() {
		this.margin = HexGeometry.marginRatio * this.panelWidth;
	}

	private void calculateEdgeLenght() {
		double boardHeight = this.panelHeight - 2 * this.margin;
		double boardWidth = this.panelWidth - 2 * this.margin;
		double foo = 3.0 * this.boardSize - 1;
		this.hexagonEdge = Math.min(
			2 * (boardWidth) / (HexGeometry.SQRT3 * foo),
			2 * (boardHeight) / foo
		);
	}

	private void calculateTriangleHeight() {
		this.triangleHeight = HexGeometry.SQRT3 * this.hexagonEdge / 2;
	}
	
	private double[][][] calculateCenters() {
		double[][][] hexagonCenters = new double[boardSize][boardSize][2];
		double height = this.margin + this.hexagonEdge;
		double width = this.margin + this.triangleHeight;
		double dwidth = this.triangleHeight;
		double dheight = 3 * this.hexagonEdge / 2;
		for (int i = 0; i < this.boardSize; i++) {
			double width1 = width;
			for (int j = 0; j < this.boardSize; j++) {
				hexagonCenters[i][j][0] = width1;				
				hexagonCenters[i][j][1] = height;
				width1 += 2 * this.triangleHeight;
			}
			width += dwidth;
			height += dheight;
		}
		return hexagonCenters;
	}
	
	public void write() {
		System.out.println(Arrays.deepToString(this.calculateCenters()));
	}
	
	public double[][] vertices(double x, double y) {
		double[] verticeArray = new double[6];
		for (int i = 0; i < 6; i++) {
			verticeArray[i] = Math.PI / 6 + i * Math.PI / 3;
		}
		double[][] array = new double[6][2];
		for (int i = 0; i < 6; i++) {
			array[i][0] = x + this.hexagonEdge * Math.cos(verticeArray[i]);
			array[i][1] = y - this.hexagonEdge * Math.sin(verticeArray[i]);
		}
		return array;
	}

	public void setHexagonMatrix() {
		double[][][] hexagonCenters = this.calculateCenters();	
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
				double[] place = hexagonCenters[i][j];
                this.hexagonMatrix[i][j] = new Hexagon(
					Color.WHITE, vertices(place[0], place[1])
				);
			}
		}
	}

	public void resetHexagonMatrix() {
		double[][][] hexagonCenters = this.calculateCenters();
		for (int i = 0; i < this.boardSize; i++)
			for (int j = 0; j < this.boardSize; j++)
				this.hexagonMatrix[i][j].resetVertices(vertices(
					hexagonCenters[i][j][0],
					hexagonCenters[i][j][1]
				));
	}

	public void updateColor(int i, int j, Color color) {
		this.hexagonMatrix[i][j].changeColor(color);
	}

	public double[] translateCoordinates(double x, double y) {
		return new double[] {
			x - this.margin - HexGeometry.SQRT3 * (
				(y - this.margin) / 3 - this.hexagonEdge / 2
			), 2 * y / HexGeometry.SQRT3 - this.margin
		};
	}

	public Hexagon getHexagon(double x, double y) {
		double[] xy = translateCoordinates(x, y);
		double diag = 2 * this.hexagonEdge;
		int firstRow = (int) Math.floor(
			(xy[1] - this.hexagonEdge / 2) / diag
		);
		int lastRow = (int) Math.ceil(xy[1] / diag);
		int firstColumn = (int) Math.floor(
			(xy[0] - this.hexagonEdge / 2) / diag
		);
		int lastColumn = (int) Math.ceil(xy[0] / diag);
		for (int i = firstRow; i <= lastRow; i++)
			for (int j = firstColumn; j <= lastColumn; j++)
				if ((i >= 0) && (i < this.boardSize) &&
					(j >= 0) && (j < this.boardSize)
				) {
					Hexagon hexagon = this.hexagonMatrix[i][j];
					if (isInConvexHull(x, y, hexagon))
						return hexagon;
				}
		return null;
	}
	 

	public boolean isInConvexHull(double x, double y, Hexagon hexagon) {		
		Point point = new Point((int) Math.round(x), (int) Math.round(y));
		int[] xs = new int[6];
		int[] ys = new int[6];
		for (int i = 0; i < 6; i++) {
			xs[i] = (int) Math.round(hexagon.vertices[i][0]);
			ys[i] = (int) Math.round(hexagon.vertices[i][1]);
		}
		Polygon polygon = new Polygon(xs,ys,6);
		return polygon.contains(point);
	}

}

class Hexagon {
	public Color color;
	public double[][] vertices;

	Hexagon(Color color, double[][] vertices) {
		this.color = color;
		this.vertices = vertices;
    }
    
    public void resetVertices(double[][] vertices) {
        this.vertices = vertices;
	}
	
	public void changeColor(Color color) {
		this.color = color;
	}

	public double[][] getEdges() {
		double[][] array = new double[6][2];
		return array;
	}
}