package inteligenca;

import java.util.ArrayList;

import splosno.Koordinati;
import splosno.KdoIgra;

import enums.PlayerIndex;

import logika.HexLogicDfs;
import logika.Igra;
import inteligenca.Tools;

public class Inteligenca extends KdoIgra {
    private Tools tools;

    public Inteligenca ()  {
        super("Nejc & Tjaša");
        this.tools = null;
    }

    public Koordinati izberiPotezo (Igra igra) {
        HexLogicDfs logic = igra.getLogic();
        PlayerIndex player = logic.currentPlayer;
        if (this.tools == null)
            this.tools = new Tools(logic);
        ArrayList<int[]> moves = tools.getBestMoves(player, 1);
        int[] move = moves.get(0);
        return new Koordinati(move[1], move[2]);
    }
}