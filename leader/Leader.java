package leader;

import java.util.HashSet;
import enums.PlayerType;
import enums.PlayerIndex;
import enums.LeaderCode;
import enums.GameStatus;
import logic.HexPlayer;
import logic.HexGame;
import geometry.HexFrame;

public class Leader {
    public static int N;
    public static HexGame hexgame;
    public static HexFrame hexframe;
    public static GameStatus status = GameStatus.VOID;

    public static void newGame (
        int n, HexPlayer p1, HexPlayer p2, HexFrame f
    ) {
        status = GameStatus.ACTIVE;
        hexgame = new HexGame(n, p1, p2);
        hexframe = f;
    }

    public static boolean humanTurn () {
        if (hexgame == null) return false;
        HexPlayer currentPlayer = hexgame.currentPlayer();
        if (currentPlayer == null) return false;
        return currentPlayer.type.equals(PlayerType.HUMAN);
    }

    public static HexPlayer currentPlayer () {
        if (hexgame == null) return null;
        return hexgame.currentPlayer ();
    }

    public static HexPlayer winningPlayer () {
        if (hexgame == null) return null;
        return hexgame.winner();
    }

    public static HashSet<int[]> winningPath () {
        return hexgame.getWinningPath();
    }

    public static LeaderCode playHuman (int i, int j) {
        if (hexgame == null || !humanTurn()) 
            return LeaderCode.MOVE_INVALID;
        boolean isvalid = hexgame.playTurn(i, j);
        if (!isvalid) 
            return LeaderCode.MOVE_INVALID;
        hexgame.repr();
        boolean haswon = hexgame.hasWon();
        System.out.println("HASWON" + haswon);
        if (haswon) {
            status = GameStatus.FINISHED;
            hexframe.updateAll();
            return LeaderCode.PLAYER_WON;
        }
        hexframe.updateAll();
        return LeaderCode.MOVE_VALID;
    }

    public static void playMachine () {

    }
}