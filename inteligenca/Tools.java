package inteligenca;

import java.util.Deque;
import java.util.LinkedList;

import logic.HexGame;
import logic.HexLogicDfs;

public class Tools {
    private int n;
    private HexGame game;
    private HexLogicDfs logic;
    private static int[][] fieldR = HexLogicDfs.fieldRelations;

    public Tools (HexGame game) {
        this.game = game;
        this.logic = game.getLogic();
        this.n = game.n;
    }

    private int[][] markedList () {
        int[][] marked = new int[game.n][game.n];
        for (int k = 0; k < game.n; k++) 
            for (int l = 0; l < game.n; l++)
                marked[k][l] = -1;
        return marked;
    }

    private int[][][] pointerList () {
        int[][][] pointer = new int[game.n][game.n][2];
        for (int k = 0; k < game.n; k++)
            for (int l = 0; l < game.n; l++)
                pointer[k][l] = new int[] {k, l};
        return pointer;
    }

    public int bfsB (int s, int t, int lim) {
        int[][] marked = markedList();
        int[][][] pointer = pointerList();
        Deque<int[]> dq = new LinkedList<int[]> ();
        dq.addFirst(new int[] {s, t});
        marked[s][t] = 0;
        int[] source = new int[] {-1, -1};
        int[] sink = new int[] {-1, -1};
        while (!dq.isEmpty()) {
            int[] field = dq.removeLast();
            int i = field[0];
            int j = field[1];
            if (i == 0 && source[0] != -1) source = new int[] {i, j};
            if (i == n - 1 && sink[0] != -1) sink = new int[] {i, j};
            if (source[0] + sink[0] >= 0) break;
            for (int[] dij : fieldR) {
                int ni = i + dij[0]; 
                int nj = j + dij[1];
                switch (logic.fieldAt(ni, nj)) {
                    case EMPTY: 
                        if (marked[ni][nj] == -1) {
                            marked[ni][nj] = 0;
                            pointer[ni][nj] = new int[] {i, j};
                            dq.addFirst(new int[] {ni, nj});
                        }
                        break;
                    case TYPE0:
                        if (marked[ni][nj] == -1) {
                            marked[ni][nj] = 1;
                            pointer[ni][nj] = new int[] {i, j};
                            dq.addLast(new int[] {ni, nj});
                        }
                        break;
                    default: break;
                }
            }    
        }
        if (source[0] == -1 || sink[0] == -1) return -1;
        int pathlen = 0;
        int[] ij = source.clone();
        while (ij[0] != s && ij[1] != t) {
            int m = marked[ij[0]][ij[1]];
            if (m == 0) pathlen++;
            marked[ij[0]][ij[1]] = -2;
            ij = pointer[ij[0]][ij[1]].clone();
        }
        ij = sink.clone();
        while (ij[0] != s && ij[1] != t) {
            int m = marked[ij[0]][ij[1]];
            boolean wasCounted = (marked[ij[0]][ij[1]] == -2);
            if (!wasCounted && m == 0) pathlen++;
            ij = pointer[ij[0]][ij[1]].clone();
        }
        return pathlen;
    } 
} 