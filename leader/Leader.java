package leader;

import java.util.HashSet;

import enums.PlayerType;
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

    public static void clearGame () {
        hexgame = null;
        status = GameStatus.VOID;
    }

    public static boolean humanTurn () {
        if (!status.equals(GameStatus.ACTIVE)) return false;
        HexPlayer currentPlayer = hexgame.getCurrentPlayer();
        if (currentPlayer == null) return false;
        return currentPlayer.type.equals(PlayerType.HUMAN);
    }

    public static HexPlayer currentPlayer () {
        if (!status.equals(GameStatus.ACTIVE)) return null;
        return hexgame.getCurrentPlayer ();
    }

    public static HexPlayer winningPlayer () {
        if (!status.equals(GameStatus.ACTIVE)) return null;
        return hexgame.getWinner();
    }

    public static HashSet<int[]> winningPath () {
        return hexgame.getWinningPath();
    }

    public static void playHuman (int i, int j) {
        if (!status.equals(GameStatus.ACTIVE) || !humanTurn())
            return;
        boolean isvalid = hexgame.playTurn(i, j);
        if (!isvalid) return;
        hexframe.hexPanel.hexagonPlayed(
            i, j, hexgame.getLastPlayer()
        );
        boolean haswon = hexgame.hasWon();
        if (haswon) {
            HashSet<int[]> path = winningPath();
            hexframe.hexPanel.markWinningPath(path);
            status = GameStatus.FINISHED;
        }
    }

    public static void playMachine () {
        System.out.println("Not Implemeneted");
    }
}