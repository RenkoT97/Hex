import java.util.Scanner;

class HexUnionFind {
    private int n;
    private int[][][] structure;
    
    public HexUnionFind (int n) {
        this.n = n;
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
            this.structure[n+1][j][1] = n - 1;
        }
    }

    public int[] getSource (int i, int j) {
        int ti = i; int tj = j;
        int[] source = this.structure[ti][tj];
        while (!(source[0] == ti && source[1] == tj)) {
            ti = source[0]; tj = source[1];
            source = this.structure[ti][tj];
        }
        while (!(source[0] == i && source[1] == j)) {
            int k = this.structure[i][j][0];
            int l = this.structure[i][j][1];
            this.structure[i][j] = source.clone();
            i = k; j = l;
        }
        return source;
    }

    public void rewireEdgeNode(int i, int j) {
        if (i == 0)  {
            int[] source = this.getSource(i+1, j);
            source[0] = 0; source[1] = 0;
        } else if (i == n-1) {
            int[] source = this.getSource(i+1, j);
            source[0] = n+1; source[1] = n-1;
        }
    }

    public void joinComponents (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i+1, j);
        int[] sourcekl = this.getSource(k+1, l);
        // we could consider the number of children but it isn't cached
        if (sourceij[0] != sourcekl[0] || sourceij[1] != sourcekl[1]) {
            sourceij[0] = sourcekl[0];
            sourceij[1] = sourcekl[1];
        }
    }

    private boolean inSameComponent (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i, j);
        int[] sourcekl = this.getSource(k, l);
        return (sourceij[0] == sourcekl[0] && sourceij[1] == sourcekl[1]);
    }

    public boolean hasBeenConnected () {
        return this.inSameComponent(0, 0, n + 1, n - 1);
    }

    public void repr () {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                int[] elt = this.structure[i][j];
                System.out.print(elt[0] + " " + elt[1] + "  ");
            }
            System.out.println("");
        }
    }
}

class HexLogic {
    public enum fieldType {EMPTY, TYPE1, TYPE2, INVALID}
    public int boardLength, boardSize;
    public fieldType[][] board;
    private HexUnionFind uf1, uf2;
    static private int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}
    };

    public HexLogic (int n) {
        this.boardLength = n;
        this.boardSize = n * n;
        this.board = new fieldType[n][n];
        this.uf1 = new HexUnionFind(n);
        this.uf2 = new HexUnionFind(n);
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                this.board[i][j] = fieldType.EMPTY;
    }

    public fieldType fieldAt (int i, int j) {
        if (i >= 0 && j >= 0 && i < this.boardLength && j < this.boardLength) 
            return this.board[i][j];
        else return fieldType.INVALID;
    }

    // this should be called after fieldAt(i, j) returns EMPTY
    public void setField (fieldType t, int i, int j) {
        this.board[i][j] = t;
        if (t == fieldType.TYPE1) {
            if (i == 0 || i == this.boardLength - 1)
                this.uf1.rewireEdgeNode(i, j);
            for (int[] ij : HexLogic.fieldRelations) {
                int di = i + ij[0]; int dj = j + ij[1];
                if (this.fieldAt(di, dj) == fieldType.TYPE1)
                    this.uf1.joinComponents(i, j, di, dj);
            }
        } if (t == fieldType.TYPE2) {
            if (j == 0 || j == this.boardLength - 1)
                this.uf2.rewireEdgeNode(j, i);
            for (int[] ij : HexLogic.fieldRelations) {
                int di = i + ij[0]; int dj = j + ij[1];
                if (this.fieldAt(di, dj) == fieldType.TYPE2)
                    this.uf2.joinComponents(j, i, dj, di);
            }
        }
    }

    public boolean hasWon (fieldType t) {
        if (t == fieldType.TYPE1) return this.uf1.hasBeenConnected();
        if (t == fieldType.TYPE2) return this.uf2.hasBeenConnected();
        else return false;
    }

    public void repr (fieldType t) {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) 
                System.out.print(this.board[i][j].name());
            System.out.println();
        }
        System.out.println();
        if (t == fieldType.TYPE1) uf1.repr();
        else uf2.repr();
    }

}

public class Test {
    public static void main (String[] args) { 
        HexLogic l = new HexLogic(8);
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        while (true) {   
            System.out.println("Enter username");
            String[] userName = myObj.nextLine().split(" ");
            HexLogic.fieldType a = (userName[0].equals("1")) ? HexLogic.fieldType.TYPE1 : HexLogic.fieldType.TYPE2;
            l.setField(a, Integer.parseInt(userName[1]), Integer.parseInt(userName[2]));
            l.repr(a);
            if (l.hasWon(a)) 
                System.out.println(a.name());
            
        }
        
    }
}