package logic;

import java.util.HashMap;
import java.util.HashSet;

import enums.LeaderCode;
import enums.PlayerIndex;
import enums.FieldType;
import logic.HexLogic;
import logic.HexPlayer;

public class HexGame {
    public int n;
    private HexLogic hexlogic;
    public HashMap<PlayerIndex, HexPlayer> playermap;

    public HexGame(int n, HexPlayer p0, HexPlayer p1) {
        this.n = n;
        this.hexlogic = new HexLogic(n);
        this.playermap = new HashMap<PlayerIndex, HexPlayer>();
        this.playermap.put(PlayerIndex.PLAYER0, p0);
        this.playermap.put(PlayerIndex.PLAYER1, p1);
    }

    public boolean playTurn (HexPlayer p, int i, int j) {
        if (this.hexlogic.moveValid(p.index, i, j)) {
            this.hexlogic.makeMove(p.index, i, j);
            return true;
        }
        return false;
    }
    public boolean playTurn (int i, int j) {
        return this.playTurn (
            this.getCurrentPlayer(), i, j
        );
    }

    public boolean hasWon (HexPlayer p) {
        return this.hexlogic.hasWon(p.index);
    }
    public boolean hasWon () {
        return this.hexlogic.hasWon(
            this.getLastPlayer().index
        );
    }

    public HexPlayer getLastPlayer () {
        PlayerIndex last = null;
        HexPlayer current = this.getCurrentPlayer();
        switch (current.index) {
            case PLAYER0: last = PlayerIndex.PLAYER1; break;
            case PLAYER1: last = PlayerIndex.PLAYER0; break;
        }
        return this.playermap.get(last);
    }

    public HexPlayer getCurrentPlayer () {
        return this.playermap.get(
            this.hexlogic.currentPlayer
        );
    }

    public HexPlayer getWinner () {
        PlayerIndex winner = this.hexlogic.winner;
        if (winner == null) return null;
        return this.playermap.get(winner);
    }

    public HashSet<int[]> getWinningPath () {
        return this.hexlogic.winningPath();
    }

    public HashSet<int[]> getPossibleMoves () {
        HashSet<int[]> pos = new HashSet<int[]> ();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) 
                if (this.hexlogic.fieldEmpty(i, j)) 
                    pos.add(new int[] {i, j});
        return pos;
    }

    public void repr() {
        this.hexlogic.repr();
    }
}