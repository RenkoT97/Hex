package logika;

import logika.HexGame;
import splosno.Koordinati;

public class Igra extends HexGame {

    public Igra (int n) {
        super(n);
    }

    public Igra () {
        this(11);
    }

    public boolean odigraj (Koordinati t) {
        return playTurn(t.getX(), t.getY());
    }

}