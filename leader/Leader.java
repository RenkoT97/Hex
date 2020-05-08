package leader;

import enums.PlayerType;
import enums.PlayerIndex;
import logic.HexPlayer;
import logic.HexGame;

public class Leader {
    public static int N;
    
    public static boolean humanTurn;
    public static HexGame hexgame;

    public static boolean humanTurn () {
        if (hexgame == null) return false;
        return hexgame.currentPlayer().type.equals(
            PlayerType.HUMAN
        );
    }

    public static void newGame (
        int n, HexPlayer p1, HexPlayer p2
    ) {
        hexgame = new HexGame(n, p1, p2);
    }

    public static boolean playHuman (int i, int j) {
        if (hexgame == null || !humanTurn()) 
            return false;
        return hexgame.playTurn(i, j);
    }

    public static void playMachine () {

    }
}