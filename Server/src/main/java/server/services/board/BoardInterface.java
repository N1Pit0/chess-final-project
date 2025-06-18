package server.services.board;

import dtos.BoardState;
import server.services.strategy.common.PieceInterface;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BoardInterface extends Serializable {

    SquareInterface[][] getBoardSquareArray();

    Optional<PieceInterface> getWhiteKing();

    void setWhiteKing(PieceInterface whiteKing);

    Optional<PieceInterface> getBlackKing();

    void setBlackKing(PieceInterface blackKing);

    List<PieceInterface> getWhitePieces();

    void setWhitePieces(List<PieceInterface> whitePieces);

    List<PieceInterface> getBlackPieces();

    void setBlackPieces(List<PieceInterface> blackPieces);

    void initializePieces();

    void initializeBoardSquares();

    BoardState toBoardState();
}
