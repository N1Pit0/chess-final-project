package model.pieces;

import model.pieces.common.Piece;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.RookStrategy;

import java.util.List;

public class Rook extends Piece {

    public Rook(PieceColor color, SquareInterface initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new RookStrategy(this).getLegalMoves(squareArrayBoard);
    }

}

