package inteligence;

import java.util.ArrayList;

import enums.PlayerIndex;
import logic.*;

public class Alphabeta {

    public HexLogicDfs logic;
    private Rankings tools;
    public Alphabeta(HexLogicDfs logic) {
        this.logic = logic;
        this.tools = new Rankings(logic);
    }
    
    public double alphabeta(int depth, double addpt, int hexRow, int hexColumn, PlayerIndex player, PlayerIndex maximizingPlayer, int k) {
        addpt += 0;
        if (logic.fieldEmpty(hexRow, hexColumn) == false) {
            return -1.0 * logic.n * logic.n;
        }
        logic.makeMove(player, hexRow, hexColumn);
        if (logic.hasWon(player)) {
            logic.reverseMove();
            return 1.0 * logic.n * logic.n * addpt;
        }
        double alfa = -1.0 * logic.n * logic.n;
        double beta = 1.0 * logic.n * logic.n;
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
                return value * addpt;
            }
            else {
                double value = 1.0 * logic.n * logic.n;
                for (int[] neighbour : moves) {
                    value = Math.min(value, neighbour[0]);
                }
                return value * addpt;
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
                value = Math.max(value, alphabeta(depth-1, addpt, neighbour[1], neighbour[2], logic.currentPlayer, maximizingPlayer, k));
                alfa = Math.max(alfa, value);
                if (alfa >= beta) {
                    continue;
                }
            }
            logic.reverseMove();
            return value * addpt;
        }
        else {
            double value = 1.0 * logic.n * logic.n;
            ArrayList<int[]> moves = tools.getBestMoves(logic.currentPlayer, k);
            if (moves.isEmpty()) {
                logic.reverseMove();
                return -1.0 * logic.n * logic.n;
            }
            for (int[] neighbour : moves) {
                value = Math.min(value, alphabeta(depth-1, addpt, neighbour[1], neighbour[2], logic.currentPlayer, maximizingPlayer, k));
                beta = Math.min(beta, value);
                if (alfa >= beta) {
                    continue;
                }
            }
            logic.reverseMove();
            return value * addpt;
        }


    }
}