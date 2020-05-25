package inteligenca;

import enums.PlayerIndex;
import enums.FieldType;

import java.util.Deque;
import java.util.LinkedList;

import logic.HexPlayer;
import logic.HexGame;
import logic.HexLogicDfs;

public class Tools {
    private int n;
    private HexLogicDfs logic;
    private static int[][] fieldR = HexLogicDfs.fieldRelations;

    public Tools (HexLogicDfs logic) {
        this.logic = logic;
        this.n = logic.n;
    }

    public int[] getBestMove (HexPlayer p) {
        FieldType type = null;
        switch (p.index) {
            case PLAYER0: type = FieldType.TYPE0; break;
            case PLAYER1: type = FieldType.TYPE1; break;
        }
        int minpath = n*n;
        int[] minpos = new int[] {-1, -1};
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (logic.fieldEmpty(i, j)) {
                    int rank = pathDistanceSimple(i, j, type);
                    if (rank != -1 && rank < minpath) {
                        minpath = rank;
                        minpos = new int[] {i, j};
                    }
                }
            }
        return minpos;
    }

    private int[][] distanceList () {
        int[][] marked = new int[n][n];
        for (int k = 0; k < n; k++) 
            for (int l = 0; l < n; l++)
                marked[k][l] = -1;
        return marked;
    }

    private int[][] markedList () {
        int[][] marked = new int[n][n];
        for (int k = 0; k < n; k++) 
            for (int l = 0; l < n; l++)
                marked[k][l] = -1;
        return marked;
    }

    private int[][][] pointerList () {
        int[][][] pointer = new int[n][n][2];
        for (int k = 0; k < n; k++)
            for (int l = 0; l < n; l++)
                pointer[k][l] = new int[] {k, l};
        return pointer;
    }

    public int pathDistance (int s, int t, FieldType type) {
        int index = (type.equals(FieldType.TYPE0)) ? 0 : 1;
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
            if (field[index] == 0 && source[0] == -1) 
                source = new int[] {i, j};
            if (field[index] == n - 1 && sink[0] == -1) 
                sink = new int[] {i, j};
            if (source[0] != -1 && sink[0] != -1) break;
            for (int[] dij : fieldR) {
                int ni = i + dij[0]; 
                int nj = j + dij[1];
                FieldType ftype = logic.fieldAt(ni, nj);
                if (ftype.equals(FieldType.EMPTY)) {
                    if (marked[ni][nj] == -1) {
                        marked[ni][nj] = 0;
                        pointer[ni][nj] = new int[] {i, j};
                        dq.addFirst(new int[] {ni, nj});
                    }
                } else if (ftype.equals(type)) {
                    if (marked[ni][nj] == -1) {
                        marked[ni][nj] = 1;
                        pointer[ni][nj] = new int[] {i, j};
                        dq.addLast(new int[] {ni, nj});
                    }
                }
            }    
        }
        if (source[0] == -1 || sink[0] == -1) return -1;
        int pathlen = 0;
        int[] ij = source.clone();
        while (ij[0] != s || ij[1] != t) {
            int i = ij[0]; int j = ij[1];
            if (marked[i][j] == 0) pathlen++;
            marked[i][j] = -2;
            ij = pointer[i][j].clone();
        }
        ij = sink.clone();
        while (ij[0] != s || ij[1] != t) {
            int i = ij[0]; int j = ij[1];
            boolean wasCounted = (marked[i][j] == -2);
            if (!wasCounted && marked[i][j] == 0) pathlen++;
            ij = pointer[i][j].clone();
        }
        return pathlen;
    } 

    public int pathDistanceSimple (int s, int t, FieldType type) {
        int index = (type.equals(FieldType.TYPE0)) ? 0 : 1;
        int[][] distance = distanceList();
        Deque<int[]> dq = new LinkedList<int[]> ();
        dq.addFirst(new int[] {s, t});
        distance[s][t] = 0;
        int source = -1; 
        int sink = -1;
        while (!dq.isEmpty()) {
            int[] field = dq.removeLast();
            int i = field[0];
            int j = field[1];
            if (field[index] == 0 && source == -1) source = distance[i][j];
            if (field[index] == n - 1 && sink == -1) sink = distance[i][j];
            if (source != -1 && sink != -1) break;
            for (int[] dij : fieldR) {
                int ni = i + dij[0]; 
                int nj = j + dij[1];
                FieldType ftype = logic.fieldAt(ni, nj);
                if (ftype.equals(FieldType.EMPTY)) {
                    if (distance[ni][nj] == -1) {
                        distance[ni][nj] = distance[i][j] + 1;
                        dq.addFirst(new int[] {ni, nj});
                    }
                } else if (ftype.equals(type)) {
                    if (distance[ni][nj] == -1) {
                        distance[ni][nj] = distance[i][j];
                        dq.addLast(new int[] {ni, nj});
                    }
                }
            }    
        }
        if (source == -1 || sink == -1) return -1;
        return source + sink;
    } 
} 