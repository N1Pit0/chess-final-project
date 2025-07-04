package server.model.pieces;

import server.services.board.Move;
import server.services.strategy.common.PieceStrategy;
import shared.enums.PieceColor;
import shared.enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;

import server.services.strategy.KnightStrategy;

import java.util.List;

public class Knight extends Piece {

    public Knight(PieceColor pieceColor, SquareInterface initSq, Move move) {
        super(pieceColor, initSq, move);
        setPieceType(PieceType.KNIGHT);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return getPieceStrategy().getLegalMoves(squareArrayBoard);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "N";
    }

}
