package server.services.board;

import server.services.strategy.common.PieceInterface;

public class PawnMove extends MoveDecorator {

    public PawnMove(Move move) {
        super(move);
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {

        //TODO implement en passant for pawn
        return super.makeMove(currentPiece, targetSquare, boardService);
    }
}
