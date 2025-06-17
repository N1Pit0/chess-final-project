package services.strategy.common;

import dtos.PieceState;
import dtos.enums.PieceColor;

import dtos.enums.PieceType;
import services.board.BoardService;
import services.board.SquareInterface;

import java.io.Serializable;
import java.util.List;

public interface PieceInterface extends Serializable {
    List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);

    PieceColor getPieceColor();

    SquareInterface getCurrentSquare();

    void setCurrentSquare(SquareInterface currentSquare);

    boolean move(SquareInterface targetSquare, BoardService boardService);

    boolean isWasMoved();

    PieceType getPieceType();

    PieceState toPieceState();

}
