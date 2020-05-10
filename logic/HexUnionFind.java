package logic;

public class HexUnionFind {
    private int n;
    private int[][][] structure;
    
    public HexUnionFind (int n) {
        this.n = n;
        this.structure = new int[n+2][n+2][2];
        for (int i = 1; i < n + 1; i++)
            for (int j = 1; j < n + 1; j++) {
                this.structure[i][j][0] = i;
                this.structure[i][j][1] = j;
            }
        for (int j = 1; j < n+1; j++) {
            this.structure[0][j] = new int[] {0, 1};
            this.structure[n+1][j] = new int[] {n+1, 1};
            this.structure[j][0] = new int[] {1, 0};
            this.structure[j][n+1] = new int[] {1, n+1};
        }
    }

    private int[] getSource (int i, int j) {
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

    public void joinComponents (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i+1, j+1);
        int[] sourcekl = this.getSource(k+1, l+1);
        // we could consider the number of children but it isn't cached
        if (sourceij[0] != sourcekl[0] || sourceij[1] != sourcekl[1]) {
            sourceij[0] = sourcekl[0];
            sourceij[1] = sourcekl[1];
        }
    }

    public boolean inSameComponent (int i, int j, int k, int l) {
        int[] sourceij = this.getSource(i+1, j+1);
        int[] sourcekl = this.getSource(k+1, l+1);
        return (sourceij[0] == sourcekl[0] && sourceij[1] == sourcekl[1]);
    }

    public void repr() {
        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < n + 2; j++) {
                int[] field = this.structure[i][j];
                System.out.print(field[0] + " " + field[1] + "  ");
            }
            System.out.println("");
        }
    }
}