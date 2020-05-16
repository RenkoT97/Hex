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
		int boardSize, double panelWidth, double panelHeight
	) {
		this.boardSize = boardSize;
		this.hexagonMatrix = new Hexagon[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				this.hexagonMatrix[i][j] = new Hexagon (
					new int[] {0, 0, 0, 0, 0, 0}, 
					new int[] {0, 0, 0, 0, 0, 0}
				);
		updateDimensions(panelWidth, panelHeight);
	}

	public void updateDimensions(
		double width, double height
	) {
		this.panelWidth = width;
		this.panelHeight = height - 34.0;
		calculateEdgeLenght();
		calculateTriangleHeight();
		calculateMargins();
		setHexagonPoints();
		System.out.println("ZAČETEK");
		System.out.println("marginX:");
		System.out.println(this.marginX);
		System.out.println("marginY:");
		System.out.println(this.marginY);
		System.out.println("prvi 6kotnik:");
		//System.out.println(this.hexagonMatrix[0][0]);
		System.out.println("visina:");
		System.out.println(this.panelHeight);
		System.out.println("sirina:");
		System.out.println(this.panelWidth);
		System.out.println("zadnji 6kotnik:");
		//System.out.println(this.hexagonMatrix[this.boardSize-1][this.boardSize-1]);
		System.out.println("STRANICA:");
		System.out.println(this.hexagonEdge);
		System.out.println("Sirina plosce:");
		System.out.println(this.triangleHeight * (3*this.boardSize - 1));
		System.out.println("Visina plosce:");
		System.out.println(this.hexagonEdge * (3*this.boardSize + 1) / 2);
	}

	private void calculateEdgeLenght() {
		double boardHeight = this.panelHeight;
		double boardWidth = this.panelWidth;
		this.hexagonEdge = 2 * Math.min(
			boardWidth
		/ (HexGeometry.SQRT3 * (3.0 * this.boardSize - 1)), 
			boardHeight
		 / (3.0 * this.boardSize + 1));
	}

	private void calculateTriangleHeight() {
		this.triangleHeight = HexGeometry.SQRT3 * this.hexagonEdge / 2;
	}

	private void calculateMargins() {
		this.marginX = (
			this.panelWidth - this.triangleHeight * (
				3 * this.boardSize - 1
			)
		) / 2;
		this.marginY = (
			this.panelHeight - this.hexagonEdge * (
				3 * this.boardSize + 1
			) / 2
		) / 2;
	}

	private int[][] centerToVertices(double x, double y) {
		int[][] array = new int[2][6];
		for (int i = 0; i < 6; i++) {
			array[0][i] = (int) (x + this.hexagonEdge * ALPHAS[i][0]);
			array[1][i] = (int) (y - this.hexagonEdge * ALPHAS[i][1]);
		}
		return array;
	}
	
	private void setHexagonPoints() {
		double width = this.marginX + this.triangleHeight;
		double height = this.marginY + this.hexagonEdge;
		double dwidth = this.triangleHeight;
		double dheight = 3 * this.hexagonEdge / 2;
		for (int i = 0; i < this.boardSize; i++) {
			double width1 = width;
			for (int j = 0; j < this.boardSize; j++) {
				int[][] hexvertices = centerToVertices(width1, height);
				this.hexagonMatrix[i][j].resetPoints(
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

	public int[] getIndexByPosition (double x, double y) {
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
		for (int i = firstRow; i <= lastRow + 1; i++)
			for (int j = firstColumn; j <= lastColumn + 1; j++)
				if ((i >= 0) && (i < this.boardSize) &&
					(j >= 0) && (j < this.boardSize)
				) {
					Hexagon hexagon = this.hexagonMatrix[i][j];
					if (hexagon.contains(x, y)) 
						return new int[] {i, j};
				}
		return null;
	}

	public Hexagon getHexagonByPosition(double x, double y) {
		int[] ij = this.getIndexByPosition(x, y);
		if (ij != null) return this.hexagonMatrix[ij[0]][ij[1]];
		return null;
	}

	public Hexagon getHexagonByIndex(int i, int j) {
		return this.hexagonMatrix[i][j];
	}

}