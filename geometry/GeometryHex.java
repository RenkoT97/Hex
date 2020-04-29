import java.lang.Math;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Point;

class GeometryHex {
	private static double marginRatio = 1 / 20;
	private static double sqr = Math.sqrt(3);
	public int boardSize;
	public double panelHeight, panelWidth;
    private Hexagon[][] hexagonMatrix;
	private double margin, hexagonEdge, triangleHeight;
	
	GeometryHex(int boardSize, double panelHeight, double panelWidth) {
		this.boardSize = boardSize;
		this.panelHeight = panelHeight;
		this.panelWidth = panelWidth;
		this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		this.calculateMargin();
		this.calculateEdgeLenght();
		this.calculateTriangleHeight();
        this.calculateCenters();
		this.setHexagonMatrix();
	}

	//koordinatni sistem se zacne levo zgoraj, prva koordinata je sirina, druga pa visina

	private void calculateMargin() {
		this.margin = GeometryHex.marginRatio * this.panelWidth;
	}

	private void calculateEdgeLenght() {
		double boardHeight = this.panelHeight - 2 * this.margin;
		double boardWidth = this.panelWidth - 2 * this.margin;
		double foo = 3.0 * this.boardSize - 1;
		this.hexagonEdge = Math.min(
			2 * (boardWidth) / (GeometryHex.sqr * foo),
			2 * (boardHeight) / foo
		);
	}

	private void calculateTriangleHeight() {
		this.triangleHeight = GeometryHex.sqr * this.hexagonEdge / 2;
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
				width1 += 2 * this.triangleHeight;
				hexagonCenters[i][j][1] = height;
			}
			width += dwidth;
			height += dheight;
		}
		return hexagonCenters;
	}

	public void updateDimensions(double width, double height) {
		this.panelWidth = width;
		this.panelHeight = height;
		calculateMargin();
		calculateEdgeLenght();
		calculateTriangleHeight();
		calculateCenters();
        resetHexagonMatrix();
	}
	
	public void write() {
		System.out.println(Arrays.deepToString(this.calculateCenters()));
	}
	
	public double[][] vertices(double x, double y) {
		double[][] array = new double[6][2];
		double[] array1 = new double[6];
		for (int i = 0; i < 6; i++)
			array1[i] = Math.PI / 6 + i * Math.PI / 3;
		for (int i = 0; i < 6; i++) {
			array[i][0] = x + this.hexagonEdge * Math.cos(array1[i]);
			array[i][1] = y - this.hexagonEdge * Math.sin(array1[i]);
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

	public void updateHexagonMatrix(int i, int j, Color color) {
		this.hexagonMatrix[i][j].changeColor(color);
	}

	public int[] getHexagon(double x, double y) {
		double width = x - this.margin - GeometryHex.sqr * ((y - this.margin) / 3 - this.hexagonEdge / 2);
		double height = 2 * y / GeometryHex.sqr - this.margin;
		int firstRow = (int)Math.floor((height - this.hexagonEdge / 2) / (2 * this.hexagonEdge));
		int lastRow = (int)Math.ceil(height / (2 * this.hexagonEdge));
		int[] rows = new int[lastRow - firstRow + 1];
		int firstColumn = (int)Math.floor((width - this.hexagonEdge / 2) / (2 * this.hexagonEdge));
		int lastColumn = (int)Math.ceil(width / (2 * this.hexagonEdge));
		int[] columns = new int[lastColumn - firstColumn + 1];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow;
			firstRow++;
		}
		for (int i = 0; i < columns.length; i++) {
			columns[i] = firstColumn;
			firstColumn += 1;
		}
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < columns.length; j++) {
				if ((rows[i] >= 0) && (rows[i] < this.boardSize)) {
					if ((columns[j] >= 0) && (columns[j] <= this.boardSize)) {
						Hexagon hexagon = this.hexagonMatrix[rows[i]][columns[j]];
						if (isInConvexHull(x, y, hexagon)) {
							int[] place = {rows[i], columns[j]};
							return place;
						}
					}
				}
			}
		}
		return null;
	}
	 

	public boolean isInConvexHull(double x, double y, Hexagon hexagon) {		
		Point point = new Point((int)Math.round(x), (int)Math.round(y));
		int[] xs = new int[6];
		int[] ys = new int[6];
		for (int i = 0; i < 6; i++) {
			xs[i] = (int)Math.round(hexagon.vertices[i][0]);
			ys[i] = (int)Math.round(hexagon.vertices[i][1]);
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