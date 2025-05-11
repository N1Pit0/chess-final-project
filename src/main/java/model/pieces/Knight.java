package model.pieces;

import model.pieces.common.Piece;
import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.KnightStrategy;

import java.util.List;

public class Knight extends Piece {

    public Knight(PieceColor color, SquareInterface initSq, String img_file) {
        super(color, initSq, img_file);
    }

    @Override
    public List<SquareInterface> getLegalMoves(BoardService boardService) {

        return new KnightStrategy(this).getLegalMoves(boardService);
    }

}
