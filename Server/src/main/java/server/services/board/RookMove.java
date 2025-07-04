package server.services.board;

import server.services.strategy.common.PieceInterface;

public class RookMove extends MoveDecorator {

    public RookMove(Move move) {
        super(move);
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {

        //TODO implement castling logic for rook
        return super.makeMove(currentPiece, targetSquare, boardService);
    }
}
