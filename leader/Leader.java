package leader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Collections;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import enums.PlayerType;
import enums.PlayerIndex;
import enums.GameStatus;
import logic.HexPlayer;
import logic.HexGame;
import logic.HexLogicDfs;
import logic.HexLogicUnionFind;
import geometry.HexFrame;

import inteligenca.Tools;
import inteligenca.Alphabeta;

public class Leader {
    public static int N;
    public static HexGame hexgame;
    public static Tools tools;
    public static HexFrame hexframe;
    public static GameStatus status = GameStatus.VOID;
    public static Random rangen = new Random();

    public static void newGame (
        int n, HexPlayer p1, HexPlayer p2, HexFrame f
    ) {
        status = GameStatus.ACTIVE;
        hexgame = new HexGame(n, p1, p2);
        hexframe = f;
        tools = new Tools(hexgame.getLogic());
        HexPlayer current = hexgame.getCurrentPlayer();
        if (current.type.equals(PlayerType.MACHINE))
            playMachine();
    }

    public static void clearGame () {
        hexgame = null;
        status = GameStatus.VOID;
    }

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

    public static HashSet<int[]> winningPath () {
        return hexgame.getWinningPath();
    }

    public static void playBase (int i, int j) {
        if (!status.equals(GameStatus.ACTIVE)) return;
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
                int[] poteza = getMachineMove(3 * hexgame.getLogic().n, 3, 4);
                try {TimeUnit.SECONDS.sleep(1);} 
                catch (Exception e) {};
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

    public static ArrayList<int[]> getRandomMoves(int k) {
        ArrayList<int[]> arrayl = new ArrayList<int[]>();
        ArrayList<int[]> ran = hexgame.getLogic().getEmptyFields();
        k = Math.min(ran.size(), k);    
        for (int i = 0; i < k; i++) {
            int r = rangen.nextInt(ran.size());
            System.out.println(r);
            int[] move = ran.get(r);
            System.out.println(move);
            arrayl.add(move);
            ran.remove(r);
        }
        System.out.println(arrayl.get(0));
        return arrayl;
    }
    
    public static int[] getMachineMove (int k1, int k2, int depth) {
        ArrayList<int[]> arrayl = hexgame.getLogic().getEmptyFields();
        k1 = Math.min(arrayl.size() - 1, k1);
        Alphabeta alphabeta = new Alphabeta(hexgame.getLogic());
        PlayerIndex p = hexgame.getLogic().currentPlayer;
        Collections.shuffle(arrayl);
        for (int i = k1; i < arrayl.size(); i++)
            arrayl.remove(i);
        //ArrayList<int[]> arrayl = getRandomMoves(k1);
        //ArrayList<int[]> arrayl = tools.getBestMoves(hexgame.getLogic().currentPlayer, k1);
        int d = arrayl.size();
        double max = Double.NEGATIVE_INFINITY;
        int ind = 0;
        for (int i = 0; i < d; i++) {
            System.out.println(arrayl.get(i)[0]);
            System.out.println(arrayl.get(i)[0]);
            double value = alphabeta.alphabeta(depth, arrayl.get(i)[0], arrayl.get(i)[1], p, p, k2);
            if (value > max) {
                max = value;
                ind = i;
            }
        }
        int[] move = new int[] {arrayl.get(ind)[1], arrayl.get(ind)[2]};
        return move;
    }
}