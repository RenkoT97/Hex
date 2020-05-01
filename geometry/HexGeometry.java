package geometry;

public class HexGeometry {
	private static final double SQRT3 = Math.sqrt(3);
	private static final double[][] ALPHAS = verticeArray();
	public int boardSize;
	private double 
		panelHeight, panelWidth, marginX, marginY,
		hexagonEdge, triangleHeight;
	private Hexagon[][] hexagonMatrix;
	
	private static double[][] verticeArray () {
		double[][] verticeArray = new double[6][2];
		for (int i = 0; i < 6; i++) {
			double phi = Math.PI / 6 + i * Math.PI / 3;
			verticeArray[i][0] = Math.cos(phi);
			verticeArray[i][1] = Math.sin(phi);
		}
		return verticeArray;
	}
	
	public HexGeometry(
		int boardSize, double panelWidth, double panelHeight, 
		double marginX, double marginY
	) {
		this.boardSize = boardSize;
		this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		updateDimensions(
			panelWidth, panelHeight, marginX, marginY
		);
	}

	public void updateDimensions(
		double width, double height, 
		double marginX, double marginY
	) {
		this.panelWidth = width;
		this.panelHeight = height;
		this.marginX = marginX;
		this.marginY = marginY;
		calculateEdgeLenght();
		calculateTriangleHeight();
        setHexagonMatrix();
	}

	private void calculateEdgeLenght() {
		double boardHeight = this.panelHeight - 2 * this.marginY;
		double boardWidth = this.panelWidth - 2 * this.marginX;
		this.hexagonEdge = 2 * Math.min(
			boardWidth / HexGeometry.SQRT3, 
			boardHeight
		) / (3.0 * this.boardSize - 1);
	}

	private void calculateTriangleHeight() {
		this.triangleHeight = HexGeometry.SQRT3 * this.hexagonEdge / 2;
	}

	private int[][] centerToVertices(double x, double y) {
		int[][] array = new int[2][6];
		for (int i = 0; i < 6; i++) {
			array[0][i] = (int) (x + this.hexagonEdge * ALPHAS[i][0]);
			array[1][i] = (int) (y - this.hexagonEdge * ALPHAS[i][1]);
		}
		return array;
	}
	
	private void setHexagonMatrix() {
		double width = this.marginX + this.triangleHeight;
		double height = this.marginY + this.hexagonEdge;
		double dwidth = this.triangleHeight;
		double dheight = 3 * this.hexagonEdge / 2;
		for (int i = 0; i < this.boardSize; i++) {
			double width1 = width;
			for (int j = 0; j < this.boardSize; j++) {
				int[][] hexvertices = centerToVertices(width1, height);
				this.hexagonMatrix[i][j] = new Hexagon(
					hexvertices[0], hexvertices[1]
				);
				width1 += 2 * this.triangleHeight;
			}
			width += dwidth;
			height += dheight;
		}
	}

	private double[] translateCoordinates(double x, double y) {
		double tx = x - this.marginX - HexGeometry.SQRT3 * (
			(y - this.marginY) / 3 - this.hexagonEdge / 2
		);
		double ty = 2 * y / HexGeometry.SQRT3 - this.marginY;
		return new double[] {tx, ty};
	}

	public Hexagon getHexagonByPosition(double x, double y) {
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
					if (hexagon.contains(x, y)) return hexagon;
				}
		return null;
	}

	public Hexagon getHexagonByIndex(int i, int j) {
		return this.hexagonMatrix[i][j];
	}

}