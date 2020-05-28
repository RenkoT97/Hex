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
  /*  
    public double alphabeta(int depth, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int k) {
        logic.makeMove(player, hexRow, hexColumn);
        double alfa = Double.POSITIVE_INFINITY;
        double beta = Double.NEGATIVE_INFINITY;
        if (depth < 1) {
            ArrayList<int[]> moves = tools.getBestMoves(player, k);
            logic.reverseMove();
            if (moves.size() == 0) {
                return beta;
            }
            if (logic.currentPlayer == maximizingPlayer) {
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
            System.out.println("test");
            for (int[] neighbour : logic.getEmptyFields()) {
                value = Math.max(value, alphabeta(depth-1, neighbour[0], neighbour[1], logic.currentPlayer, maximizingPlayer, k));
                System.out.println("test2");
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
            for (int[] neighbour : logic.getEmptyFields()) {
                value = Math.min(value, alphabeta(depth-1, neighbour[0], neighbour[1], logic.currentPlayer, maximizingPlayer, k));
                beta = Math.min(beta, value);
                if (alfa >= beta) {
                    continue;
                }
            }
            logic.reverseMove();
            return value;
        }


    }
*/
    
    public double alphabeta(int depth, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int k) {
        logic.makeMove(player, hexRow, hexColumn);
        double alfa = Double.POSITIVE_INFINITY;
        double beta = Double.NEGATIVE_INFINITY;
        if (depth < 1) {
            logic.reverseMove();
            if (player == maximizingPlayer) {
                double value = Double.NEGATIVE_INFINITY;
                for (int[] neighbour : tools.getBestMoves(logic.currentPlayer, k)) {
                    value = Math.max(value, neighbour[0]);
                }
                return value;
            }
            else {
                double value = Double.POSITIVE_INFINITY;
                for (int[] neighbour : tools.getBestMoves(logic.currentPlayer, k)) {
                    value = Math.min(value, neighbour[0]);
                }
                return value;
            }
        }
        if (player == maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            for (int[] neighbour : tools.getBestMoves(logic.currentPlayer, k)) {
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
            for (int[] neighbour : tools.getBestMoves(logic.currentPlayer, k)) {
                value = Math.min(value, alphabeta(depth-1, neighbour[1], neighbour[2], logic.currentPlayer, maximizingPlayer, k));
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
}