//package services;
//
//import dtos.BoardState;
//import services.board.BoardService;
//import services.board.SquareInterface;
//import services.strategy.common.PieceInterface;
//
//import java.util.List;
//import java.util.Optional;
//
//public class BoardStateService implements BoardService {
//
//    private BoardState boardState;
//    public BoardStateService(BoardState boardState) {
//        this.boardState = boardState;
//    }
//
//    @Override
//    public void initializePieces() {
//
//    }
//
//    @Override
//    public void initializeBoardSquares() {
//
//    }
//
//    @Override
//    public Optional<PieceInterface> getWhiteKing() {
//        return Optional.empty();
//    }
//
//    @Override
//    public void setWhiteKing(PieceInterface piece) {
//
//    }
//
//    @Override
//    public Optional<PieceInterface> getBlackKing() {
//        return boardState.getBlackKing();
//    }
//
//    @Override
//    public void setBlackKing(PieceInterface piece) {
//
//    }
//
//    @Override
//    public List<PieceInterface> getWhitePieces() {
//        return List.of();
//    }
//
//    @Override
//    public void setWhitePieces(List<PieceInterface> pieces) {
//
//    }
//
//    @Override
//    public List<PieceInterface> getBlackPieces() {
//        return List.of();
//    }
//
//    @Override
//    public void setBlackPieces(List<PieceInterface> pieces) {
//
//    }
//
//    @Override
//    public SquareInterface[][] getBoardSquareArray() {
//        return new SquareInterface[0][];
//    }
//
//    @Override
//    public PieceInterface getCurrPiece() {
//        return null;
//    }
//
//    @Override
//    public void setCurrPiece(PieceInterface piece) {
//
//    }
//
//    @Override
//    public boolean isWhiteTurn() {
//        return false;
//    }
//
//    @Override
//    public void setWhiteTurn(boolean whiteTurn) {
//
//    }
//
//    @Override
//    public int getCurrX() {
//        return 0;
//    }
//
//    @Override
//    public void setCurrX(int x) {
//
//    }
//
//    @Override
//    public int getCurrY() {
//        return 0;
//    }
//
//    @Override
//    public void setCurrY(int y) {
//
//    }
//}
