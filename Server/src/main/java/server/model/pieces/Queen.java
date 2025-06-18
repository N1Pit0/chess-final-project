package server.model.pieces;

import enums.PieceColor;
import enums.PieceType;
import server.model.pieces.common.Piece;
import server.services.board.SquareInterface;

import server.services.strategy.QueenStrategy;

import java.util.List;

public class Queen extends Piece {

    public Queen(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.QUEEN);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new QueenStrategy(this).getLegalMoves(squareArrayBoard);
    }

}
