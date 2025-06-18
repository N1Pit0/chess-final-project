package model.pieces;

import enums.PieceColor;
import enums.PieceType;
import lombok.Getter;
import model.pieces.common.Piece;
import services.board.BoardService;
import services.board.SquareInterface;

import services.strategy.PawnStrategy;

import java.util.List;

@Getter
public class Pawn extends Piece {

    public Pawn(PieceColor color, SquareInterface initSq) {
        super(color, initSq);
        setPieceType(PieceType.PAWN);
    }

    @Override
    public boolean move(SquareInterface targetSquare, BoardService boardService) {
        boolean b = super.move(targetSquare, boardService);
        setWasMoved(true);
        return b;
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return new PawnStrategy(this).getLegalMoves(squareArrayBoard);
    }

    public void dummy() {
    }

}
