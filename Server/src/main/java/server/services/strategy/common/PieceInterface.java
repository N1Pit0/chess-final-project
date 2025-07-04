package server.services.strategy.common;

import server.services.board.BoardService;
import server.services.board.SquareInterface;
import shared.dtos.PieceState;
import shared.enums.PieceColor;
import shared.enums.PieceType;

import java.io.Serializable;
import java.util.List;

public interface PieceInterface extends Serializable {
    List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);

    PieceColor getPieceColor();

    SquareInterface getCurrentSquare();

    void setCurrentSquare(SquareInterface currentSquare);

    boolean movePiece(SquareInterface targetSquare, BoardService boardService);

    boolean isWasMoved();

    void setWasMoved(boolean wasMoved);

    PieceType getPieceType();

    PieceState toPieceState();

    String toPGNFormatSymbol();

    void setPieceStrategy(PieceStrategy pieceStrategy);

}
