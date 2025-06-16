package model.pieces;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import model.pieces.common.Piece;
import services.board.SquareInterface;

import services.strategy.QueenStrategy;

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
