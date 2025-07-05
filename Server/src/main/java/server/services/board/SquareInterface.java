package server.services.board;

import server.services.strategy.common.PieceInterface;
import shared.dtos.SquareState;
import shared.enums.PieceColor;

import java.io.Serializable;

public interface SquareInterface extends Serializable {
    PieceColor getSquareColor();

    PieceInterface getOccupyingPiece();

    void setOccupyingPiece(PieceInterface occupyingPiece);

    boolean isOccupied();

    int getXNum();

    void setXNum(int xNum);

    int getYNum();

    void setYNum(int yNum);

    void put(PieceInterface piece);

    SquareState toSquareState();

    String toAlgebraic();


}
