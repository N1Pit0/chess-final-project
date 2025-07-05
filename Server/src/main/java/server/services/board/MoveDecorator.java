package server.services.board;

import server.services.strategy.common.PieceInterface;

public class MoveDecorator implements Move {

    private final Move move;

    public MoveDecorator(Move move) {
        this.move = move;
    }

    @Override
    public boolean makeMove(PieceInterface currentPiece, SquareInterface targetSquare, BoardService boardService) {
        return move.makeMove(currentPiece, targetSquare, boardService);
    }
}
