package server.services.board;

import server.services.strategy.common.PieceInterface;
import shared.enums.PieceColor;

public interface PieceFactoryInterface {

    PieceInterface getPawn(PieceColor color, SquareInterface square);

    PieceInterface getBishop(PieceColor color, SquareInterface square);

    PieceInterface getKnight(PieceColor color, SquareInterface square);

    PieceInterface getRook(PieceColor color, SquareInterface square);

    PieceInterface getQueen(PieceColor color, SquareInterface square);

    PieceInterface getKing(PieceColor color, SquareInterface square);
}
