package server.model.pieces;

import server.model.pieces.common.Piece;
import server.services.board.Move;
import server.services.board.SquareInterface;
import shared.enums.PieceColor;
import shared.enums.PieceType;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(PieceColor pieceColor, SquareInterface initSq, Move move) {
        super(pieceColor, initSq, move);
        setPieceType(PieceType.BISHOP);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return getPieceStrategy().getLegalMoves(squareArrayBoard);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "B";
    }

}
