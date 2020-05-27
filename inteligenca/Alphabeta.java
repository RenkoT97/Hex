package inteligenca;

import enums.PlayerIndex;
import logic.*;

public class Alphabeta {
    HexGame igra = new HexGame(3);
    HexLogicDfs logic = igra.getLogic();
    Tools tools = new Tools(logic);
    
    public double alphabeta(int depth, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int n, int k) {
        logic.makeMove(player, hexRow, hexColumn);
        double alfa = Double.POSITIVE_INFINITY;
        double beta = Double.NEGATIVE_INFINITY;
        if (depth < 1) {
            logic.reverseMove();
            if (player == maximizingPlayer) {
                double value = Double.NEGATIVE_INFINITY;
                for (int[] neighbour : tools.getBestMoves(player, k)) {
                    value = Math.max(value, neighbour[0]);
                }
                return value;
            }
            else {
                double value = Double.POSITIVE_INFINITY;
                for (int[] neighbour : tools.getBestMoves(player, k)) {
                    value = Math.min(value, neighbour[0]);
                }
                return value;
            }
        }
        if (player == maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            for (int[] neighbour : tools.getBestMoves(player, k)) {
                value = Math.max(value, alphabeta(depth-1, neighbour[0], neighbour[1], logic.currentPlayer, maximizingPlayer, n, k));
                alfa = Math.max(alfa, value);
                if (alfa >= beta) {
                    logic.reverseMove();
                    continue;
                }
            }
            logic.reverseMove();
            return value;
        }
        else {
            double value = Double.POSITIVE_INFINITY;
            for (int[] neighbour : tools.getBestMoves(player, k)) {
                value = Math.min(value, alphabeta(depth-1, neighbour[0], neighbour[1], logic.currentPlayer, maximizingPlayer, n, k));
                beta = Math.min(beta, value);
                if (alfa >= beta) {
                    logic.reverseMove();
                    continue;
                }
            }
            logic.reverseMove();
            return value;
        }


    }
}