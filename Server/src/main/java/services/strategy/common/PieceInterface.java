package services.strategy.common;

import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;

import java.awt.*;
import java.util.List;

public interface PieceInterface {
    List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);

    PieceColor getPieceColor();

    SquareInterface getCurrentSquare();

    void setCurrentSquare(SquareInterface currentSquare);

    boolean move(SquareInterface targetSquare, BoardService boardService);

    boolean isWasMoved();

    Image getImage();

}
