package logic;

import java.util.HashMap;
import java.util.HashSet;

import enums.LeaderCode;
import enums.PlayerIndex;
import logic.HexLogic;
import logic.HexPlayer;

public class HexGame {
    private int n;
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

    public HexPlayer winner () {
        if (this.hexlogic.winner == null) return null;
        return this.playermap.get(this.hexlogic.winner);
    }

    public boolean playTurn (int i, int j) {
        return this.playTurn (this.currentPlayer(), i, j);
    }
    public HexPlayer currentPlayer () {
        return this.playermap.get(
            this.hexlogic.currentPlayer
        );
    }

    public boolean hasWon (HexPlayer p) {
        return this.hexlogic.hasWon(p.index);
    }
    public boolean hasWon () {
        PlayerIndex current = this.currentPlayer().index;
        PlayerIndex last = PlayerIndex.PLAYER1;
        switch (current) {
            case PLAYER0: last = PlayerIndex.PLAYER1;
            case PLAYER1: last = PlayerIndex.PLAYER0;
        }
        return this.hexlogic.hasWon(last);
    }

    public HexPlayer getWinner () {
        PlayerIndex winner = this.hexlogic.winner;
        if (winner == null) return null;
        return this.playermap.get(winner);
    }

    public HashSet<int[]> getWinningPath () {
        return this.hexlogic.winningPath();
    }

    public void repr() {
        this.hexlogic.repr();
    }
}