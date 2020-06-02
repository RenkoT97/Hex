package inteligenca;

import java.util.ArrayList;
import java.util.Random;

import splosno.KdoIgra;
import enums.PlayerIndex;
import logika.HexLogicDfs;

public class Inteligenca extends KdoIgra {
    private HexLogicDfs logic;
    private Tools tools;
    public static Random rangen = new Random();

    public Inteligenca (HexLogicDfs logic)  {
        super("Nejc & Tjaša");
        this.logic = logic;
        this.tools = new Tools(logic);
    }

    public int[] getMove(int k1, int k2, int depth) {
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
        if (move[1] == (int) move[1]) {
            System.out.println(move[0] + " " + move[1]);
            logic.repr();
            return move;
        }
        ArrayList<int[]> array = logic.getEmptyFields();
        return array.get(rangen.nextInt(array.size() - 1));
    }
}