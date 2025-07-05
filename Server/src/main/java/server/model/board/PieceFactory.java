package server.model.board;

import server.model.pieces.*;
import server.services.board.BaseMove;
import server.services.board.KingMove;
import server.services.board.PawnMove;
import server.services.board.SquareInterface;
import server.services.strategy.*;
import server.services.strategy.common.PieceStrategy;
import shared.enums.PieceColor;

public class PieceFactory {
    public Pawn getPawn(PieceColor color, SquareInterface square) {
        Pawn piece = new Pawn(color, square, new PawnMove(new BaseMove()));

        PieceStrategy pieceStrategy = new PawnStrategy(piece);

        piece.setPieceStrategy(pieceStrategy);
        return piece;
    }

    public Bishop getBishop(PieceColor color, SquareInterface square) {
        Bishop bishop = new Bishop(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new BishopStrategy(bishop);

        bishop.setPieceStrategy(pieceStrategy);
        return bishop;
    }

    public Knight getKnight(PieceColor color, SquareInterface square) {
        Knight knight = new Knight(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new KnightStrategy(knight);

        knight.setPieceStrategy(pieceStrategy);
        return knight;
    }

    public Rook getRook(PieceColor color, SquareInterface square) {
        Rook rook = new Rook(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new RookStrategy(rook);

        rook.setPieceStrategy(pieceStrategy);
        return rook;
    }

    public Queen getQueen(PieceColor color, SquareInterface square) {
        Queen queen = new Queen(color, square, new BaseMove());

        PieceStrategy pieceStrategy = new QueenStrategy(queen);

        queen.setPieceStrategy(pieceStrategy);
        return queen;
    }

    public King getKing(PieceColor color, SquareInterface square) {
        King king = new King(color, square, new KingMove(new BaseMove()));

        PieceStrategy pieceStrategy = new KingStrategy(king);

        king.setPieceStrategy(pieceStrategy);
        return king;
    }
}
