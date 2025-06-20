package server.services.board;

import server.services.strategy.common.PieceInterface;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    void initializePieces();

    void initializeBoardSquares();

    Optional<PieceInterface> getWhiteKing();

    void setWhiteKing(PieceInterface piece);

    Optional<PieceInterface> getBlackKing();

    void setBlackKing(PieceInterface piece);

    List<PieceInterface> getWhitePieces();

    void setWhitePieces(List<PieceInterface> pieces);

    List<PieceInterface> getBlackPieces();

    void setBlackPieces(List<PieceInterface> pieces);

    SquareInterface[][] getBoardSquareArray();

    PieceInterface getCurrPiece();

    void setCurrPiece(PieceInterface piece);

    boolean isWhiteTurn();

    void setWhiteTurn(boolean whiteTurn);

    int getCurrX();

    void setCurrX(int x);

    int getCurrY();

    void setCurrY(int y);

    BoardInterface getBoard();


}
