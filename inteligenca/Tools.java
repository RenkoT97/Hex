package inteligenca;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayList;

import enums.FieldType;
import enums.PlayerIndex;

import logic.HexPlayer;
import logic.HexLogicDfs;


public class Tools {
    private int n;
    private HexLogicDfs logic;
    private static int[][] fieldR = HexLogicDfs.fieldRelations;

    public Tools (HexLogicDfs logic) {
        this.logic = logic;
        this.n = logic.n;
    }

    private PlayerIndex otherPlayerIndex (PlayerIndex p) {
        switch (p) {
            case PLAYER0: return PlayerIndex.PLAYER1;
            case PLAYER1: return PlayerIndex.PLAYER0;
        }
        return null;
    }

    private FieldType playerIndexToFieldType(PlayerIndex i) {
        switch (i) {
            case PLAYER0: return FieldType.TYPE0;
            case PLAYER1: return FieldType.TYPE1;
        }
        return null;
    }

    public int[] getBestMove (FieldType type) {
        int minpath = n*n*n*n;
        int[] minpos = new int[] {0,0};
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (logic.fieldEmpty(i, j)) {
                    int rank = pathDistance(i, j, type);
                    if (rank != -1 && rank < minpath) {
                        minpath = rank;
                        minpos = new int[] {i, j};
                    }
                }
            }
        return minpos;
    }

    private int[] minListRank (ArrayList<int[]> list) {
        int mn = n * n * n *n;
        int mnindex = 0;
        int i = 0;
        for (int[] elt : list) {
            if (elt[0] < mn) {
                mn = elt[0];
                mnindex = i;
            }
            i++;
        }
        return new int[] {mnindex, mn};
    }

    public ArrayList<int[]> getBestMoves (HexPlayer p, int k) {
        ArrayList<int[]> taken = new ArrayList<int[]> ();

        PlayerIndex other = otherPlayerIndex(p.index);
        FieldType other_type = playerIndexToFieldType(other);
        FieldType this_type = playerIndexToFieldType(p.index);
        
        int[] other_repr = getBestMove(other_type);
        // pathDistance ze zracuna path v procesu
        int other_default_rank = pathDistance(
            other_repr[0], other_repr[1], other_type
        );
        boolean[][] path = shortestPath(
            other_repr[0], other_repr[1], other_type
        );

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (logic.fieldEmpty(i, j)) {
                    int rank;
                    int this_rank = pathDistance(i, j, this_type);
                    if (this_rank == 0) rank = n*n; // winning pos
                    else if (path[i][j]) {
                        logic.makeMove(p.index, i, j);
                        int[] other_repr_t = getBestMove(other_type);
                        int other_rank = pathDistance(
                            other_repr_t[0], other_repr_t[1], other_type
                        );
                        logic.reverseMove();
                        rank = (other_rank == -1) ? n * n : 5*other_rank - this_rank;
                    } else rank = 5*other_default_rank - this_rank;

                    System.out.println(taken.size());
                    if (taken.size() < k) {
                        taken.add(new int[] {rank, i, j});
                    }
                    else {
                        int[] temp = minListRank(taken);
                        if (rank > temp[1]) {
                            taken.remove(temp[0]);
                            taken.add(new int[] {rank, i, j});
                        }

                    }
                }
            }
        }
        return taken;
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

    public boolean[][] shortestPath (int s, int t, FieldType type) {
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
        if (source[0] == -1 || sink[0] == -1) return null;
        boolean[][] onpath = new boolean[n][n];
        int[] ij = source.clone();
        while (ij[0] != s || ij[1] != t) {
            int i = ij[0]; int j = ij[1];
            if (marked[i][j] == 0) onpath[i][j] = true;
            marked[i][j] = -2;
            ij = pointer[i][j].clone();
        }
        ij = sink.clone();
        while (ij[0] != s || ij[1] != t) {
            int i = ij[0]; int j = ij[1];
            boolean wasCounted = (marked[i][j] == -2);
            if (!wasCounted && marked[i][j] == 0) onpath[i][j] = true;
            ij = pointer[i][j].clone();
        }
        return onpath;
    } 

    public int pathDistanceProof (int s, int t, FieldType type) {
        boolean[][] mat = shortestPath (s, t, type);
        if (mat == null) return -1;
        int dist = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mat[i][j]) dist++;
        return dist;
    }

    // this is an approximation (could be off in rare cases)
    public int pathDistance (int s, int t, FieldType type) {
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