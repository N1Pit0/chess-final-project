package server.model.pieces;

import shared.enums.PieceColor;
import shared.enums.PieceType;
import lombok.Getter;
import server.model.pieces.common.Piece;
import server.services.board.BoardService;
import server.services.board.SquareInterface;

import server.services.strategy.PawnStrategy;

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
    public String toPGNFormatSymbol() {
        return "";
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return new PawnStrategy(this).getLegalMoves(squareArrayBoard);
    }

    public void dummy() {
    }

}
