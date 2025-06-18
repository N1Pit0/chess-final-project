package controller.old;


import enums.PieceColor;

public interface GameWindowInterface {
    void checkmateOccurred(PieceColor pieceColor);

    void stalemateOccurred();
}
