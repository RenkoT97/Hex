package inteligenca;

import java.util.ArrayList;

import enums.PlayerIndex;
import inteligenca.Tools;
import logic.*;

public class Alphabeta {

    public HexLogicDfs logic;
    private Tools tools;
    public Alphabeta(HexLogicDfs logic) {
        this.logic = logic;
        this.tools = new Tools(logic);
    }
    
    public double alphabeta(int depth, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int k) {
        if (logic.fieldEmpty(hexRow, hexColumn) == false) {
            return Double.NEGATIVE_INFINITY;
        }
        logic.makeMove(player, hexRow, hexColumn);
        double alfa = Double.POSITIVE_INFINITY;
        double beta = Double.NEGATIVE_INFINITY;
        if (depth < 1) {
            logic.reverseMove();
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                return Double.NEGATIVE_INFINITY;
            }
            if (player == maximizingPlayer) {
                double value = Double.NEGATIVE_INFINITY;
                for (int[] neighbour : moves) {
                    value = Math.max(value, neighbour[0]);
                }
                return value;
            }
            else {
                double value = Double.POSITIVE_INFINITY;
                for (int[] neighbour : moves) {
                    value = Math.min(value, neighbour[0]);
                }
                return value;
            }
        }
        if (player == maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                return Double.NEGATIVE_INFINITY;
            }
            for (int[] neighbour : moves) {
                value = Math.max(value, alphabeta(depth-1, neighbour[1], neighbour[2], logic.currentPlayer, maximizingPlayer, k));
                alfa = Math.max(alfa, value);
                if (alfa >= beta) {
                    continue;
                }
            }
            logic.reverseMove();
            return value;
        }
        else {
            double value = Double.POSITIVE_INFINITY;
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                return -50;
            }
            for (int[] neighbour : moves) {
                value = Math.min(value, alphabeta(depth-1, neighbour[1], neighbour[2], logic.currentPlayer, maximizingPlayer, k));
                System.out.println(value);
                beta = Math.min(beta, value);
                if (alfa >= beta) {
                    continue;
                }
            }
            logic.reverseMove();
            return value;
        }


    }
}