package model.pieces;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import model.pieces.common.Piece;
import services.board.SquareInterface;
import services.strategy.BishopStrategy;

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
