package inteligenca;

import java.util.ArrayList;
import java.util.Random;

import splosno.Koordinati;
import splosno.KdoIgra;

import enums.PlayerIndex;

import logika.HexLogicDfs;
import logika.Igra;

public class Inteligenca extends KdoIgra {
    private Tools tools;
    public static Random rangen = new Random();

    public Inteligenca ()  {
        super("Nejc & Tjaša");
        this.tools = null;
    }

    private int[] _izberiPotezo(HexLogicDfs logic, int k1, int k2, int depth) {
        Alphabeta alphabeta = new Alphabeta(logic);
        PlayerIndex p = logic.currentPlayer;
        ArrayList<int[]> arrayl = tools.getBestMoves(logic.currentPlayer, k1);
        int d = arrayl.size();
        double max = Double.NEGATIVE_INFINITY;
        int ind = 0;
        for (int i = 0; i < d-1; i++) {
            double value = alphabeta.alphabeta(depth, arrayl.get(i)[1], arrayl.get(i)[2], p, p, k2);
            if (value > max) {
                max = value;
                ind = i;
            }
        }
        int[] move = new int[] {arrayl.get(ind)[1], arrayl.get(ind)[2]};
        if (move[1] == (int) move[1]) return move;
        ArrayList<int[]> array = logic.getEmptyFields();
        return array.get(rangen.nextInt(array.size() - 1));
    }

    public Koordinati izberiPotezo (Igra igra) {
        HexLogicDfs logic = igra.getLogic();
        if (this.tools == null) this.tools = new Tools(logic);
        int[] ij = _izberiPotezo(logic, 3 * logic.n, 3, 4);
        return new Koordinati(ij[0], ij[1]);
    }
}