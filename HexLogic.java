class UnionFind {
    private int[][][] structure;
    
    public UnionFind (int n) {
        this.structure = new int[n+2][n][2];
        for (int i = 1; i < n + 1; i++)
            for (int j = 0; j < n; j++) {
                this.structure[i][j][0] = i;
                this.structure[i][j][1] = j;
            }
        for (int j = 0; j < n; j++) {
            this.structure[0][j][0] = 0;
            this.structure[0][j][1] = 0;
            this.structure[n+1][j][0] = n + 1;
            this.structure[n+1][j][1] = n + 1;
        }
    }

    private int[] getSource (int i, int j) {
        int[] source = this.structure[i][j];
        while (!(i != source[0] && j != source[1])) {
            i = source[0]; j = source[1];
            source[0] = this.structure[i][j][0];
            source[1] = this.structure[i][j][1];
        }
        while (!(i == source[0] && j == source[1])) {
            int k = this.structure[i][j][0];
            int l = this.structure[i][j][1];
            this.structure[i][j][0] = source[0];
            this.structure[i][j][1] = source[1];
            i = k; j = l;
        }
        return source;
    }

    public void joinComponents (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i+1, j);
        int[] sourcekl = this.getSource(k+1, l);
        // here we could consider the number of children of each source
        if (sourceij[0] != sourcekl[0] || sourceij[1] != sourcekl[1])  {
            sourceij[0] = sourcekl[0];
            sourceij[1] = sourcekl[1];
        }
    }

    public boolean inSameComponent (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i+1, j);
        int[] sourcekl = this.getSource(k+1, l);
        return (sourceij[0] == sourcekl[0] && sourceij[1] == sourcekl[1]);
    }
}


public class HexLogic {
    public enum fieldType {EMPTY, TYPE1, TYPE2, INVALID}
    public int boardLength, boardSize;
    private fieldType[][] board;
    private UnionFind uf1, uf2;
    static private int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}
    };

    public HexLogic (int n) {
        this.boardLength = n;
        this.boardSize = n * n;
        this.board = new fieldType[n][n];
        this.uf1 = new UnionFind(n);
        this.uf2 = new UnionFind(n);
    }

    public fieldType fieldAt (int i, int j) {
        if (i >= 0 && j >= 0 && i < this.boardLength && j < this.boardLength) 
            return this.board[i][j];
        else return fieldType.INVALID;
    }

    // this should be called after fieldAt(i, j) returns EMPTY
    public void setField (int i, int j, fieldType t) {
        this.board[i][j] = t;
        if (t == fieldType.TYPE1) {
            for (int[] ij : HexLogic.fieldRelations) {
                int di = i + ij[0]; int dj = j + ij[1];
                if (this.fieldAt(di, dj) == fieldType.TYPE1)
                    this.uf1.joinComponents(i, j, di, dj);
            }
        } else if (t == fieldType.TYPE2) {
            for (int[] ij : HexLogic.fieldRelations) {
                int di = i + ij[0]; int dj = j + ij[1];
                if (this.fieldAt(i + di, j + dj) == fieldType.TYPE1)
                    this.uf1.joinComponents(j, i, dj, di);
            }
        }
    }

    public boolean hasWon (fieldType t) {
        if (t == fieldType.TYPE1) 
            return uf1.inSameComponent(0, 0, this.boardLength, this.boardLength);
        else if (t == fieldType.TYPE2) 
            return uf2.inSameComponent(0, 0, this.boardLength, this.boardLength);
        else return false;
    }
}