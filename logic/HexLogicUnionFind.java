package logic;

import java.util.Stack;
import java.util.HashSet;

import enums.FieldType;
import enums.PlayerIndex;

public class HexLogicUnionFind {
    public int boardLength, n;
    public PlayerIndex currentPlayer, winner;
    private FieldType[][] board;
    private HexUnionFind uf;
    private int[] lastMove;
    public static int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, 
        {0, -1}, {-1, 1}, {-1, 0}
    };

    public HexLogicUnionFind (int n) {
        this.boardLength = this.n = n;
        this.currentPlayer = PlayerIndex.PLAYER0;
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
        for (int[] dij : HexLogicUnionFind.fieldRelations) {
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

    public boolean fieldEmpty (int i, int j) {
        return this.fieldAt(i, j).equals(FieldType.EMPTY);
    }

    public boolean moveValid (PlayerIndex player, int i, int j) {
        return (
            this.fieldAt(i, j).equals(FieldType.EMPTY) && 
            this.currentPlayer.equals(player)
        );
    }

    public void makeMove(PlayerIndex player, int i, int j) {
        switch (player) {
            case PLAYER0: 
                this.setFieldType0(i, j);
                this.currentPlayer = PlayerIndex.PLAYER1;
                break;
            case PLAYER1: 
                this.setFieldType1(i, j);
                this.currentPlayer = PlayerIndex.PLAYER0;
                break;
        }
        lastMove = new int[] {i, j};
    }

    public boolean hasWon (PlayerIndex player) {
        switch (player) {
            case PLAYER0: 
                if (this.uf.inSameComponent(-1,0,n,0)) {
                    this.winner = player;
                    return true;
                }
            case PLAYER1: 
                if (this.uf.inSameComponent(0,-1,0,n)) {
                    this.winner = player;
                    return true;
                }
        }
        return false;
    }
    public HashSet<int[]> winningPath () {
        FieldType type = null;
        switch (this.winner) {
            case PLAYER0: 
                type = FieldType.TYPE0;
                break;
            case PLAYER1: 
                type = FieldType.TYPE1;
                break;
        }
        return pathDfs(type, lastMove);
    }

    private HashSet<int[]> pathDfs (
        FieldType t, int[] source
    ) {
        boolean[][] marked = new boolean[n][n];
        for (int k = 0; k < n; k++) 
            for (int l = 0; l < n; l++) 
                marked[k][l] = false;
        HashSet<int[]> path = new HashSet<int[]> ();
        Stack<int[]> stack = new Stack<int[]> ();
        stack.add(source);
        while (!stack.empty()) {
            source = stack.pop();
            path.add(source);
            int i = source[0]; 
            int j = source[1];
            if (!marked[i][j]) {
                marked[i][j] = true;
                for (int[] dij : HexLogicUnionFind.fieldRelations) {
                    int ti = i + dij[0];
                    int tj = j + dij[1];
                    if (ti >= 0 && ti < n && tj >= 0 && tj < n) {
                        if (!marked[ti][tj] && this.board[ti][tj].equals(t))
                            stack.push(new int[] {ti, tj});
                    }
                }
            }
        }
        return path;
    }

    public void repr () {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) 
                System.out.print(this.board[i][j].name());
            System.out.println();
        }
    }
}
