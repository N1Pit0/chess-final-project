package model.pieces;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import model.pieces.common.Piece;
import services.board.SquareInterface;

import services.strategy.KnightStrategy;

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
