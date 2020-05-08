package logic;

import enums.PlayerIndex;
import enums.PlayerType;

public class HexPlayer {
    public PlayerIndex index; 
    public PlayerType type;

    public HexPlayer (PlayerIndex index, PlayerType type) {
        this.index = index;
        this.type = type;
    }
}