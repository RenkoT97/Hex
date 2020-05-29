import java.util.Random;
import java.util.ArrayList;

import splosno.Koordinati;

import logika.HexPlayer;
import logika.HexLogicDfs;
import logika.Igra;

import inteligenca.Inteligenca;

class test {
    private static Random rangen = new Random();
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {

        Igra igra = new Igra ();
        HexLogicDfs logic = igra.getLogic();
        Inteligenca intel = new Inteligenca();

        while (true) {
            HexPlayer player = igra.getCurrentPlayer();
            Koordinati ij = null;
            switch (player.index) {
                case PLAYER0: 
                    ArrayList<int[]> ran = logic.getEmptyFields();
                    int[] move = ran.get(rangen.nextInt(ran.size()));
                    ij = new Koordinati (move[0], move[1]);
                    break;
                case PLAYER1: 
                    ij = intel.izberiPotezo(igra);
                    break;
            }
            igra.odigraj(ij);
            if (igra.hasWon()) break;
        }
        System.out.println(
            "Winner is " + igra.getLastPlayer().index.name()
        );

        }
    }
}