package leader;

import logic.HexGame;

public class Leader {
    public static int N;
    
    public static boolean humanTurn;
    public static HexGame hexgame;

    public static void newGame () {
        hexgame = new HexGame(N);
    }

    public static void playHuman (int i, int j) {
        if (hexgame == null || !humanTurn) return;
    }
}