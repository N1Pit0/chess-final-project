package server.model.pieces;

import server.model.pieces.common.Piece;
import server.services.board.Move;
import server.services.board.SquareInterface;
import shared.enums.PieceColor;
import shared.enums.PieceType;

import java.util.List;

public class Rook extends Piece {

    public Rook(PieceColor pieceColor, SquareInterface initSq, Move move) {
        super(pieceColor, initSq, move);
        ;
        setPieceType(PieceType.ROOK);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return getPieceStrategy().getLegalMoves(squareArrayBoard);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "R";
    }

}

