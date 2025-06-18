package server.model.pieces;

import enums.PieceColor;
import enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;

import server.services.strategy.RookStrategy;

import java.util.List;

public class Rook extends Piece {

    public Rook(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.ROOK);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new RookStrategy(this).getLegalMoves(squareArrayBoard);
    }

}

