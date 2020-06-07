package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.SwingWorker;

import enums.PlayerType;
import enums.PlayerIndex;
import enums.GameStatus;
import logic.HexPlayer;
import logic.HexGame;
import geometry.HexFrame;

import inteligence.Rankings;
import inteligence.Alphabeta;
import inteligence.Inteligence;

public class Server {
    public static int N;
    public static HexGame hexgame;
    public static Inteligence intel;
    public static Rankings tools;
    public static HexFrame hexframe;
    public static GameStatus status = GameStatus.VOID;
    public static Random rangen = new Random();

    public static void newGame (
        int n, HexPlayer p1, HexPlayer p2, HexFrame f
    ) {
        status = GameStatus.ACTIVE; // mark the game is active
        hexgame = new HexGame(n, p1, p2);
        hexframe = f;
        tools = new Rankings(hexgame.getLogic());
        intel = new Inteligence(hexgame.getLogic());
        HexPlayer current = hexgame.getCurrentPlayer();
        if (current.type.equals(PlayerType.MACHINE))
            playMachine(); // if machine is the first to start
    }

    public static void clearGame () {
        hexgame = null;
        status = GameStatus.VOID; // mark the game inactive
    }

    // check if its human's turn
    public static boolean humanTurn () {
        if (!status.equals(GameStatus.ACTIVE)) return false;
        HexPlayer currentPlayer = hexgame.getCurrentPlayer();
        if (currentPlayer == null) return false;
        return currentPlayer.type.equals(PlayerType.HUMAN);
    }

    public static HexPlayer currentPlayer () {
        if (!status.equals(GameStatus.ACTIVE)) return null;
        return hexgame.getCurrentPlayer ();
    }

    public static HexPlayer winningPlayer () {
        if (!status.equals(GameStatus.FINISHED)) return null;
        return hexgame.getWinner();
    }

    // get the path of the winning player
    public static HashSet<int[]> winningPath () {
        return hexgame.getWinningPath();
    }

    // someone played a field i, j
    private static void playBase (int i, int j) {
        if (!status.equals(GameStatus.ACTIVE)) return;
        // returns true if move was valid
        boolean isvalid = hexgame.playTurn(i, j); 
        if (!isvalid) return;
        HexPlayer recent = hexgame.getLastPlayer();
        HexPlayer current = hexgame.getCurrentPlayer();
        hexframe.hexPanel.hexagonPlayed(i, j, recent);
        boolean haswon = hexgame.hasWon(recent);
        hexframe.updateAll();
        if (haswon) {
            HashSet<int[]> path = winningPath();
            hexframe.hexPanel.markWinningPath(path);
            status = GameStatus.FINISHED;
            hexframe.updateAll();
        } else if (current.type.equals(PlayerType.MACHINE))
            playMachine();
    }

    public static void playHuman (int i, int j) {
        if (!humanTurn()) return;
        playBase(i, j);
    }

    public static void playMachine () {
		SwingWorker<int[], Void> worker = new SwingWorker<int[], Void> () {
			@Override
			protected int[] doInBackground() {
                //int[] poteza = getMachineMove();
                int[] poteza = getMachineMove(6 * hexgame.getLogic().n, 3, 4);
				return poteza;
			}
			@Override
			protected void done () {
				int[] poteza = null;
                try {poteza = get();} catch (Exception e) {};
                playBase(poteza[0], poteza[1]);
			}
		};
		worker.execute();
    }
    public static int[] getMachineMove (int k1, int k2, int depth) {
        Alphabeta alphabeta = new Alphabeta(hexgame.getLogic());
        PlayerIndex p = hexgame.getLogic().currentPlayer;
        ArrayList<int[]> arrayl = tools.getBestMoves(hexgame.getLogic().currentPlayer, k1);
        int d = arrayl.size();
        if (d==0) {
            ArrayList<int[]> array = hexgame.getLogic().getEmptyFields();
            return array.get(rangen.nextInt(array.size() - 1));
        }
        double max = -50.0;
        int ind = 0;
        for (int i = 0; i < d-1; i++) {
            double value = alphabeta.alphabeta(depth, 1.0, arrayl.get(i)[1], arrayl.get(i)[2], p, p, k2);
            if (value > max) {
                max = value;
                ind = i;
            }
        }
        if (arrayl.isEmpty() == false) {
            int[] move = new int[] {arrayl.get(ind)[1], arrayl.get(ind)[2]};
            if (move[1] == (int) move[1]) return move;
        }
        ArrayList<int[]> array = hexgame.getLogic().getEmptyFields();
        return array.get(rangen.nextInt(array.size() - 1));
    }
}