package inteligenca;

import java.util.ArrayList;

import enums.PlayerIndex;
import inteligenca.Tools;
import logika.*;

public class Alphabeta {

    public HexLogicDfs logic;
    private Tools tools;
    public Alphabeta(HexLogicDfs logic) {
        this.logic = logic;
        this.tools = new Tools(logic);
    }
    
    public double alphabeta(int depth, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int k) {
        if (logic.fieldEmpty(hexRow, hexColumn) == false) {
            return -1.0 * logic.n * logic.n;
        }
        logic.makeMove(player, hexRow, hexColumn);
        double alfa = 1.0 * logic.n *logic.n;
        double beta = -1.0 * logic.n *logic.n;
        if (depth < 1) {
            logic.reverseMove();
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                return -1.0 * logic.n * logic.n;
            }
            if (player == maximizingPlayer) {
                double value = -logic.n * logic.n;;
                for (int[] neighbour : moves) {
                    value = Math.max(value, neighbour[0]);
                }
                return value;
            }
            else {
                double value = 1.0 * logic.n * logic.n;
                for (int[] neighbour : moves) {
                    value = Math.min(value, neighbour[0]);
                }
                return value;
            }
        }
        if (player == maximizingPlayer) {
            double value = -1.0 * logic.n * logic.n;
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                logic.reverseMove();
                return -1.0 * logic.n * logic.n;
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
            double value = 1.0 * logic.n * logic.n;
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                logic.reverseMove();
                return -1.0 * logic.n * logic.n;
            }
            for (int[] neighbour : moves) {
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