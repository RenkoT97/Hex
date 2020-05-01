package logic;
import logic.HexUnionFind;
import logic.FieldType;

public class HexLogic {
    public int boardLength, boardSize, n;
    private FieldType[][] board;
    private HexUnionFind uf;
    private boolean currentPlayer;
    private static int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, 
        {0, -1}, {-1, 1}, {-1, 0}
    };

    public HexLogic (int n) {
        this.boardLength = this.n = n;
        this.boardSize = n * n;
        this.currentPlayer = false;
        this.uf = new HexUnionFind(n);
        this.board = new FieldType[n][n];
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                this.board[i][j] = FieldType.EMPTY;
    }

    private FieldType fieldAt (int i, int j) {
        if (i >= 0 && i < this.boardLength && 
            j >= 0  && j < this.boardLength
        ) return this.board[i][j];
        else return FieldType.INVALID;
    }

    private void joinComponentsAround(FieldType t, int i, int j) {
        for (int[] dij : HexLogic.fieldRelations) {
            int di = i + dij[0]; int dj = j + dij[1];
            if (this.fieldAt(di, dj).equals(t))
                this.uf.joinComponents(i, j, di, dj);
        }
    }

    private void setFieldType0 (int i, int j) {
        this.board[i][j] = FieldType.TYPE0;
        if (i == 0) this.uf.joinComponents(i, j, -1, 0);
        if (i == n - 1) this.uf.joinComponents(i, j, n, 0);
        this.joinComponentsAround(FieldType.TYPE0, i, j);
    }

    private void setFieldType1 (int i, int j) {
        this.board[i][j] = FieldType.TYPE1;
        if (j == 0) this.uf.joinComponents(i, j, 0, -1);
        if (j == n - 1) this.uf.joinComponents(i, j, 0, n);
        this.joinComponentsAround(FieldType.TYPE1, i, j);
    }

    public boolean makeMove(boolean player, int i, int j) {
        if (!(
            this.fieldAt(i, j).equals(FieldType.EMPTY) && 
            player == this.currentPlayer
        )) return false;
        else if (player == false) this.setFieldType0(i, j);
        else if (player == true) this.setFieldType1(i, j);
        this.currentPlayer = !this.currentPlayer;
        return true;
    }

    public boolean hasWon (boolean player) {
        if (player == false) 
            return this.uf.inSameComponent(-1,0,n,0);
        else 
            return this.uf.inSameComponent(0,-1,0,n);
    }

    public void repr (FieldType t) {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) 
                System.out.print(this.board[i][j].name());
            System.out.println();
        }
    }
}
