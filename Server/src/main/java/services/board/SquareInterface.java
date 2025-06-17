package services.board;

import dtos.SquareState;
import dtos.enums.PieceColor;
import services.strategy.common.PieceInterface;

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
