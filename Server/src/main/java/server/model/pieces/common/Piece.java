package server.model.pieces.common;

import shared.dtos.PieceState;
import shared.dtos.SquareState;
import shared.enums.PieceColor;
import shared.enums.PieceType;
import lombok.Getter;
import lombok.Setter;
import server.services.board.BoardService;
import server.services.board.Move;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;

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

    @Override
    public PieceState toPieceState(){
        SquareState squareState = currentSquare.toSquareState();
        //        squareState.setOccupyingPiece(pieceState);
        return new PieceState(pieceColor,squareState,pieceType,wasMoved);
    }

    // No implementation, to be implemented by each subclass
    public abstract List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);
}