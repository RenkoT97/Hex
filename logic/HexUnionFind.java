package logic;

public class HexUnionFind {
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
        return source.clone();
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