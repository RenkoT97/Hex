import java.lang.Math;
import java.util.Arrays;
import java.awt.Color;

class GeometryHex {
	private static double marginRatio = 1 / 20;
	public int boardSize;
	public double panelHeight, panelWidth;
    private double[][][] hexagonCenters;
    private Hexagon[][] hexagonMatrix;
	private double margin, hexagonEdge, triangleHeight;
	
	GeometryHex(int boardSize, double panelHeight, double panelWidth, double margin) {
		this.boardSize = boardSize;
		this.panelHeight = panelHeight;
		this.panelWidth = panelWidth;
		this.margin = margin;
        this.hexagonCenters = new double[boardSize][boardSize][2];
        this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		calculateEdgeLenght();
		calculateTriangleHeight();
        calculateCenters();
        setHexagonMatrix();
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
			2 * (boardWidth) / (Math.sqrt(3) * foo),
			2 * (boardHeight) / foo
		);
	}

	private void calculateTriangleHeight() {
		this.triangleHeight = Math.sqrt(3) * this.hexagonEdge / 2;
	}
	
	private void calculateCenters() {
		double height = this.margin + this.hexagonEdge;
		double width = this.margin + this.triangleHeight;
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
				this.hexagonCenters[i][j][0] = width + j * 2 * this.triangleHeight;
				this.hexagonCenters[i][j][1] = height;
			}
			width += this.triangleHeight;
			height += 3 * this.hexagonEdge / 2;
		}
	}

	public void updateDimensions(double width, double height) {
		this.panelWidth = width;
		this.panelHeight = height;
		calculateMargin();
		calculateEdgeLenght();
		calculateTriangleHeight();
        calculateCenters();
        setHexagonMatrix();
	}
	
	public void write() {
		System.out.println(Arrays.deepToString(this.hexagonCenters));
	}
	
	public double[][] vertices(double x, double y) {
		double[][] array = new double[6][2];
		double alpha = Math.PI / 6;
		for (int i = 0; i < 6; i++) {
			array[i][0] = x + this.hexagonEdge * Math.cos(alpha);
			array[i][1] = y - this.hexagonEdge * Math.sin(alpha);
			alpha += Math.PI / 3;
		}
		return array;
	}

	public void setHexagonMatrix() {
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
                place = this.hexagonCenters[i][j];
                hexagon = new Hexagon(white);
                hexagon.setVertices(vertices(place[i], place[j]));
                this.hexagonMatrix[i][j] = hexagon;
			}
		}
	}

}

class Hexagon {
	private Color color;
	private double[][] vertices;

	Hexagon(Color color) {
		this.color = color;
		this.vertices = new double[6][2];
    }
    
    private void setVertices(double[][] vertices) {
        this.vertices = vertices;
    }
}