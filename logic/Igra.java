package logic;

import logic.HexPlayer;
import logic.HexGame;
import splosno.Koordinati;

public class Igra extends HexGame {
    private static HexPlayer nullPlayer = new HexPlayer(
        null, null, null
    );

    public Igra (int n) {
        super(n, nullPlayer, nullPlayer);
    }

    public Igra () {
        super(11, nullPlayer, nullPlayer);
    }

    public boolean odigraj (Koordinati t) {
        return playTurn(t.getX(), t.getY());
    }

}