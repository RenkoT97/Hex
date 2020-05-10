package leader;

import enums.PlayerType;
import enums.PlayerIndex;
import enums.LeaderCode;
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

    public static LeaderCode playHuman (int i, int j) {
        if (hexgame == null || !humanTurn()) 
            return LeaderCode.MOVE_INVALID;
        boolean isvalid = hexgame.playTurn(i, j);
        if (!isvalid) 
            return LeaderCode.MOVE_INVALID;
        System.out.println("REPR");
        hexgame.repr();
        boolean haswon = hexgame.hasWon();
        if (haswon) 
            return LeaderCode.PLAYER_WON;
        return LeaderCode.MOVE_VALID;
    }

    public static void playMachine () {

    }
}