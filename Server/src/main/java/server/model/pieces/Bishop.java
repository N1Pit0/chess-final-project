package server.model.pieces;

import enums.PieceColor;
import enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;
import server.services.strategy.BishopStrategy;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.BISHOP);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new BishopStrategy(this).getLegalMoves(squareArrayBoard);
    }

}
