package model.pieces;

import enums.PieceColor;
import enums.PieceType;
import model.pieces.common.Piece;
import services.board.SquareInterface;

import services.strategy.KingStrategy;

import java.util.List;

public class King extends Piece {

    public King(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.KING);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return new KingStrategy(this).getLegalMoves(squareArrayBoard);
    }

}
