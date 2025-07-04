package server.model.pieces;

import server.services.board.Move;
import server.services.strategy.common.PieceStrategy;
import shared.enums.PieceColor;
import shared.enums.PieceType;
import lombok.Getter;
import server.model.pieces.common.Piece;
import server.services.board.BoardService;
import server.services.board.SquareInterface;

import server.services.strategy.PawnStrategy;

import java.util.List;

@Getter
public class Pawn extends Piece {

    public Pawn(PieceColor pieceColor, SquareInterface initSq, Move move) {
        super(pieceColor, initSq, move);
        setPieceType(PieceType.PAWN);
    }

    @Override
    public boolean movePiece(SquareInterface targetSquare, BoardService boardService) {
        boolean b = super.movePiece(targetSquare, boardService);
        setWasMoved(true);
        return b;
    }

    @Override
    public String toPGNFormatSymbol() {
        return "";
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return getPieceStrategy().getLegalMoves(squareArrayBoard);
    }

}
