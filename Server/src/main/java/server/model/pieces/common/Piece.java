package server.model.pieces.common;

import lombok.Getter;
import lombok.Setter;
import server.services.board.BoardService;
import server.services.board.Move;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;
import shared.dtos.PieceState;
import shared.dtos.SquareState;
import shared.enums.PieceColor;
import shared.enums.PieceType;

import java.util.List;


@Getter
@Setter
public abstract class Piece implements PieceInterface {
    private final PieceColor pieceColor;
    private SquareInterface currentSquare;
    private PieceType pieceType;
    private boolean wasMoved;
    private PieceStrategy pieceStrategy;
    private Move move;


    public Piece(PieceColor pieceColor, SquareInterface initSq, Move move) {
        this.pieceColor = pieceColor;
        this.currentSquare = initSq;
        this.wasMoved = false;
        this.move = move;
    }

    public boolean movePiece(SquareInterface targetSquare, BoardService boardService) {
        return move.makeMove(this, targetSquare, boardService);
    }

    @Override
    public PieceState toPieceState() {
        SquareState squareState = currentSquare.toSquareState();
        //        squareState.setOccupyingPiece(pieceState);
        return new PieceState(pieceColor, squareState, pieceType, wasMoved);
    }

    // No implementation, to be implemented by each subclass
    public abstract List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);
}