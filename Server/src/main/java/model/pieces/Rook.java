package model.pieces;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import model.pieces.common.Piece;
import services.board.SquareInterface;

import services.strategy.RookStrategy;

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

