package controller;


import dtos.enums.PieceColor;

public interface GameWindowInterface {
    void checkmateOccurred(PieceColor pieceColor);

    void stalemateOccurred();
}
