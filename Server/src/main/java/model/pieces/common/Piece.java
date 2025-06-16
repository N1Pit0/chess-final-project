package model.pieces.common;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import lombok.Getter;
import lombok.Setter;
import services.board.BoardService;
import services.board.Move;
import services.board.SquareInterface;
import services.strategy.common.PieceInterface;

import java.util.List;


@Getter
@Setter
public abstract class Piece implements PieceInterface {
    private final PieceColor pieceColor;
    private SquareInterface currentSquare;
    private PieceType pieceType;
    private boolean wasMoved;

    public Piece(PieceColor pieceColor, SquareInterface initSq) {
        this.pieceColor = pieceColor;
        this.currentSquare = initSq;
    }

    public boolean move(SquareInterface targetSquare, BoardService boardService) {

        return Move.makeMove(this, targetSquare, boardService);
    }

    // No implementation, to be implemented by each subclass
    public abstract List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);
}