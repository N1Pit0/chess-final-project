package model.pieces;

import model.pieces.common.Piece;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.QueenStrategy;

import java.util.List;

public class Queen extends Piece {

    public Queen(PieceColor color, SquareInterface initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return new QueenStrategy(this).getLegalMoves(squareArrayBoard);
    }

}
