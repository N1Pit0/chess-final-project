package server.model.pieces;

import enums.PieceColor;
import enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;

import server.services.strategy.KnightStrategy;

import java.util.List;

public class Knight extends Piece {

    public Knight(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.KNIGHT);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new KnightStrategy(this).getLegalMoves(squareArrayBoard);
    }

}
