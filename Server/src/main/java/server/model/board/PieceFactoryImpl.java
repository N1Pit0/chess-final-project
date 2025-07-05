package server.model.board;

import server.model.pieces.*;
import server.services.board.*;
import server.services.strategy.*;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;
import shared.enums.PieceColor;

public class PieceFactoryImpl implements PieceFactoryInterface {
    public PieceInterface getPawn(PieceColor color, SquareInterface square) {

        var pawnMove = new PawnMove(new BaseMove());
        pawnMove.setPieceFactory(this);

        PieceInterface piece = new Pawn(color, square, pawnMove);

        PieceStrategy pieceStrategy = new PawnStrategy(piece);

        piece.setPieceStrategy(pieceStrategy);
        return piece;
    }

    public PieceInterface getBishop(PieceColor color, SquareInterface square) {
        PieceInterface bishop = new Bishop(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new BishopStrategy(bishop);

        bishop.setPieceStrategy(pieceStrategy);
        return bishop;
    }

    public PieceInterface getKnight(PieceColor color, SquareInterface square) {
        PieceInterface knight = new Knight(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new KnightStrategy(knight);

        knight.setPieceStrategy(pieceStrategy);
        return knight;
    }

    public PieceInterface getRook(PieceColor color, SquareInterface square) {
        PieceInterface rook = new Rook(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new RookStrategy(rook);

        rook.setPieceStrategy(pieceStrategy);
        return rook;
    }

    public PieceInterface getQueen(PieceColor color, SquareInterface square) {
        PieceInterface queen = new Queen(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new QueenStrategy(queen);

        queen.setPieceStrategy(pieceStrategy);
        return queen;
    }

    public PieceInterface getKing(PieceColor color, SquareInterface square) {
        PieceInterface king = new King(color, square, new KingMove(new BaseMove()));

        PieceStrategy pieceStrategy = new KingStrategy(king);

        king.setPieceStrategy(pieceStrategy);
        return king;
    }
}
