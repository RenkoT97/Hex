package logic;
import logic.HexUnionFind;
import logic.FieldType;

public class HexLogic {
    public int boardLength, boardSize, n;
    private FieldType[][] board;
    private HexUnionFind uf1, uf2;
    private static int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}
    };

    public HexLogic (int n) {
        this.boardLength = this.n = n;
        this.boardSize = n * n;
        this.board = new FieldType[n][n];
        this.uf1 = new HexUnionFind(n);
        this.uf2 = new HexUnionFind(n);
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                this.board[i][j] = FieldType.EMPTY;
    }

    public FieldType fieldAt (int i, int j) {
        if (i >= 0 && j >= 0 && i < this.boardLength && j < this.boardLength) 
            return this.board[i][j];
        else return FieldType.INVALID;
    }

    // this should be called after fieldAt(i, j) returns EMPTY
    public void setField (FieldType t, int i, int j) {
        this.board[i][j] = t;
        if (t == FieldType.TYPE1) {
            // this has to be done to connect the edge to the 
            // played chip in order to easily compute winners
            if (i == 0) this.uf1.joinComponents(i, j, -1, 0);
            if (i == n - 1) this.uf1.joinComponents(i, j, n, n-1);
            for (int[] dij : HexLogic.fieldRelations) {
                int di = i + dij[0]; int dj = j + dij[1];
                if (this.fieldAt(di, dj) == FieldType.TYPE1)
                    this.uf1.joinComponents(i, j, di, dj);
            }
        } else if (t == FieldType.TYPE2) {
            // this has to be done to connect the edge to the 
            // played chip in order to easily compute winners
            if (j == 0) this.uf1.joinComponents(j, i, -1, 0);
            if (j == n - 1) this.uf1.joinComponents(j, i, n, n-1);
            for (int[] dij : HexLogic.fieldRelations) {
                int di = i + dij[0]; int dj = j + dij[1];
                if (this.fieldAt(di, dj) == FieldType.TYPE2)
                    this.uf2.joinComponents(j, i, dj, di);
            }
        }
    }

    public boolean hasWon (FieldType t) {
        if (t == FieldType.TYPE1) return this.uf1.hasBeenConnected();
        if (t == FieldType.TYPE2) return this.uf2.hasBeenConnected();
        else return false;
    }

    public void repr (FieldType t) {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) 
                System.out.print(this.board[i][j].name());
            System.out.println();
        }
    }
}
