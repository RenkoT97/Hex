package logic;

import java.util.HashMap;

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

    public void playTurn (HexPlayer p, int i, int j) {
        this.hexlogic.makeMove(p.index, i, j);
    }

    public HexPlayer currentPlayer () {
        return this.playermap.get(
            this.hexlogic.currentPlayer
        );
    }
}