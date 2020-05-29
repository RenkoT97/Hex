package logika;

import java.util.Stack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import enums.FieldType;
import enums.PlayerIndex;

@SuppressWarnings("serial")
public class HexLogicDfs {
    public int boardLength, n;
    public PlayerIndex currentPlayer, winner;
    private FieldType[][] board;
    private Stack<int[]> placementStack;
    public static int[][] fieldRelations = {
        {1, -1}, {1, 0}, {0, 1}, 
        {0, -1}, {-1, 1}, {-1, 0}
    };
    private static HashMap<PlayerIndex, FieldType> indexToField = new HashMap<PlayerIndex, FieldType>() {{
        put(PlayerIndex.PLAYER0, FieldType.TYPE0);
        put(PlayerIndex.PLAYER1, FieldType.TYPE1);
    }};

    public HexLogicDfs (int n) {
        this.boardLength = this.n = n;
        this.currentPlayer = PlayerIndex.PLAYER0;
        this.placementStack = new Stack<int[]> ();
        this.board = new FieldType[n][n];
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                this.board[i][j] = FieldType.EMPTY;
    }

    public FieldType fieldAt (int i, int j) {
        if (i >= 0 && i < this.boardLength && 
            j >= 0  && j < this.boardLength
        ) return this.board[i][j];
        else return FieldType.INVALID;
    }

    private void shiftCurrentPlayer () {
        switch (this.currentPlayer) {
            case PLAYER0: 
                this.currentPlayer = PlayerIndex.PLAYER1;
                break;
            case PLAYER1: 
                this.currentPlayer = PlayerIndex.PLAYER0;
                break;
        }
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
        this.board[i][j] = indexToField.get(player);
        placementStack.push(new int[] {i, j});
        this.shiftCurrentPlayer();
    }

    public void reverseMove () {
        int[] lastMove = this.placementStack.pop();
        this.board[lastMove[0]][lastMove[1]] = FieldType.EMPTY;
        this.shiftCurrentPlayer();
    }

    public boolean hasWon (PlayerIndex player) {
        FieldType fieldt = indexToField.get(player);
        HashSet<int[]> path = pathDfs(
            fieldt, this.placementStack.lastElement()
        );
        boolean source = false;
        boolean sink = false;
        int index = (fieldt.equals(FieldType.TYPE0)) ? 0 : 1;
        for (int[] ij : path) {
            if (ij[index] == 0) source = true;
            if (ij[index] == n - 1) sink = true;
            if (source && sink) break;
        } 
        if (source && sink) {
            this.winner = player;
            return true;
        } else return false;
    }

    public HashSet<int[]> winningPath () {
        FieldType fieldt = null;
        switch (this.winner) {
            case PLAYER0: 
                fieldt = FieldType.TYPE0;
                break;
            case PLAYER1: 
                fieldt = FieldType.TYPE1;
                break;
        }
        return pathDfs(
            fieldt, this.placementStack.lastElement()
        );
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
                for (int[] dij : HexLogicDfs.fieldRelations) {
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

    public ArrayList<int[]> getEmptyFields() {
        ArrayList<int[]> arr = new ArrayList<int[]>();
        for (int i = 0; i < this.boardLength; i++)
            for (int j = 0; j < this.boardLength; j++)
                if (this.fieldEmpty(i, j))
                    arr.add(new int[] {i, j});
        return arr;
    }

    public void repr () {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) 
                System.out.print(this.board[i][j].name());
            System.out.println();
        }
    }
}
