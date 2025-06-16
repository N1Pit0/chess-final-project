package dtos;

import dtos.enums.PieceColor;

import java.io.Serializable;

public class GameInit implements Serializable {
    private final PieceColor playerColor;

    public GameInit(PieceColor playerColor) {
        this.playerColor = playerColor;
    }

    public PieceColor getPlayerColor() {
        return playerColor;
    }

    @Override
    public String toString() {
        return "You are playing as " + playerColor;
    }
}
