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
	
	GeometryHex(int boardSize, double panelHeight, double panelWidth) {
		this.boardSize = boardSize;
		this.panelHeight = panelHeight;
		this.panelWidth = panelWidth;
        this.hexagonCenters = new double[boardSize][boardSize][2];
		this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		calculateMargin();
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
        resetHexagonMatrix();
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
				double[] place = this.hexagonCenters[i][j];
				Color white = new Color(255,255,255);
                this.hexagonMatrix[i][j] = new Hexagon(
					white, vertices(place[i], place[j])
				);
			}
		}
	}

	public void resetHexagonMatrix() {
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
				this.hexagonMatrix[i][j].resetVertices(vertices(
				this.hexagonCenters[i][j][0], this.hexagonCenters[i][j][1]
				));
			}
		}
	}

	public void updateHexagonMatrix(int x, int j, Color color) {
		this.hexagonMatrix[i][j].changeColor(color);
	}

/*
	private double projection(double x, double y) {
		return x - Math.sqrt(3) * y / 3;
	}

	public int[] getHexagon(double x, double y) {
		double projectionOfX = projection(x, y);
		foo0 = Math.sqrt(3 * (Math.pow(this.hexagonEdge, 2) - Math.pow(this.triangleHeight, 2)));
		if ((projectionOfX < projection(this.margin, this.margin + 3 * this.hexagonEdge / 2))
		|| (projectionOfX > (projection(this.margin + 2 * this.hexagonEdge * this.boardSize,
		this.margin + foo0)))) {
			return null;
		}
		if (y < 3 * this.hexagonEdge / 2) {
			int[] lines = {1};
		}
		else if (y > this.margin + this.hexagonEdge * (3.0 * this.boardSize / 2 - 1)) {
			int[] lines = {this.boardSize};
		}
		else {
			for (int i = 1; i < this.boardSize - 1; i++) {
				foo1 = this.margin + this.hexagonEdge * (1 + 3.0 * i / 2) - y;
				if ((3 * this.hexagonEdge / 2 >= foo1) && (foo1 >= 0)) {
					int[] lines = {i, i + 1};
					break outerloop;
				}
			}
		}
		if (projectionOfX <= projection(this.margin, this.margin + foo0)) {
			int[] columns = {1};
		}
		else if (projectionOfX >= projection(this.margin + 2 * this.hexagonEdge * this.boardSize, this.margin + foo0)) {
			int[] columns = {this.boardSize};
		}
		else {
			for (int i = 1; i < this.boardSize - 1; i++) {
				if ((projectionOfX >= projection(this.margin + 2 * this.hexagonEdge * i, this.margin + foo0)) &&
				(projectionOfX <= projection(this.margin + this.hexagonEdge * (2 * i + 3), this.margin))) {
				   int[] columns = {i, i + 1};
				   break outerloop;
			   }
			}
		}
		for (i = 0; i < lines.length; i++) {
			for (j = 0; j < columns.length; i++) {
				if (isInHexagon(x, y, this.hexagonMatrix[i][j].vertices)) {
					int[] place = {lines[i], columns[j]};
					return place;
				}
			}
		}
		return null;
	}
 */
	public int[] getHexagon(double x, double y) {
		//vem da je neki narobe s tipi pa enimi spremenljivkami sam ne morm dobr razmišljat zdejle
		double foo = y - this.margin - this.hexagonEdge / 4;
		double line = Math.ceil((Math.hypot(Math.sqrt(3) * foo / 3, foo)) / (2 * this.hexagonEdge));
		double column = (x - (Math.sqrt(3) * (y / 3 + this.margin / 3 + this.hexagonEdge / 2))) /
		(2 * this.hexagonEdge);
		if ((line < 1) || (line > this.boardSize)) {
			return null;
		}
		//mogoče bi bilo pri največjem odmiku zaradi napake treba preveriti 3, glej sliko
		else if ((Math.floor(column) > 0) || (Math.floor(column) <= this.boardSize)) {
			//ja vem bom popravla ko se spomnim kako
			if ((Math.ceil(column) > 0) || (Math.ceil(column) <= this.boardSize)) {
				int[] columns = {(Math.floor(column)), (Math.ceil(column))};
			}
			else {
				int[] columns = {Math.floor(column)};
			}
		}
		else if ((Math.ceil(column) > 0) || (Math.ceil(column) <= this.boardSize))	{
			int[] columns = {Math.ceil(column)};
		}
		else return null;
		for (int j = 0; j < columns.length; j++) {
			if (isInConvexHull(x, y, this.hexagonMatrix[line - 1][columns[j] - 1])) {
				int[] place = {line, columns[j]};
				return place;
			}
		}
		return null;
	}
	 

	public boolean isInConvexHull(double x, double y, Hexagon hexagon) {
		//lahko nekaj drugega narediva z razredi		
		Point point = Point(Math.round(x), Math.round(y));
		int[] xs = new int[6];
		int[] ys = new int[6];
		for (int i = 0; i < 6; i++) {
			xs[i] = round(hexagon.vertices[i][0]);
			ys[i] = round(hexagon.vertices[i][1]);
		}
		Polygon polygon = Polygon(xs,ys,6);
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