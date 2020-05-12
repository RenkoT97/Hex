package logic;

import java.awt.Color;

import enums.PlayerIndex;
import enums.PlayerType;

public class HexPlayer {
    public PlayerIndex index; 
    public PlayerType type;
    public Color color;

    public HexPlayer (
        PlayerIndex index, 
        PlayerType type, 
        Color color
    ) {
        this.index = index;
        this.type = type;
        this.color = color;
    }
}