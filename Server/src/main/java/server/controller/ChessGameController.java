package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shared.enums.GameStatusType;
import shared.enums.PieceColor;
import server.services.board.BoardService;
import server.services.board.SquareInterface;
import server.services.checkmatedetection.CheckmateDetector;
import server.services.strategy.common.PieceInterface;

import java.util.Arrays;
import java.util.List;
@Service
public class ChessGameController {


    private BoardService boardService;
    private CheckmateDetector checkmateDetector;

    @Autowired
    public ChessGameController(BoardService boardService, CheckmateDetector checkmateDetector) {
        this.boardService = boardService;
        this.checkmateDetector = checkmateDetector;
    }

    public boolean setPlayablePiece(String squareString) {
        SquareInterface square = Arrays.stream(boardService.getBoardSquareArray()).flatMap(Arrays::stream).filter(x -> x.toAlgebraic().equals(squareString)).findFirst().orElse(null);
        if (square == null || !square.isOccupied()) {
            return false;
        }
        PieceInterface currentPiece = square.getOccupyingPiece();
        if (currentPiece.getPieceColor().equals(PieceColor.BLACK) && boardService.isWhiteTurn()) {
            return false;
        }
        if (currentPiece.getPieceColor().equals(PieceColor.WHITE) && !boardService.isWhiteTurn()) {
            return false;
        }
        boardService.setCurrPiece(currentPiece);
        return true;
    }

    public GameStateEnum makeMove(String squareString) {

        SquareInterface targetSquare = Arrays.stream(boardService.getBoardSquareArray())
                .flatMap(Arrays::stream)
                .filter(x -> x.toAlgebraic().equals(squareString))
                .findFirst()
                .orElse(null);

        if (targetSquare == null) {
            return GameStateEnum.ERROR;
        }

        PieceInterface currentPiece = boardService.getCurrPiece();
        if (boardService.getCurrPiece() == null) return GameStateEnum.ERROR;

        PieceColor currentPieceColor = boardService.getCurrPiece().getPieceColor();
        if (currentPieceColor.equals(PieceColor.BLACK) && boardService.isWhiteTurn()) return GameStateEnum.ERROR;

        if (currentPieceColor.equals(PieceColor.WHITE) && !boardService.isWhiteTurn()) return GameStateEnum.ERROR;

        List<SquareInterface> legalMoves = currentPiece.getLegalMoves(boardService.getBoardSquareArray());

        if (!legalMoves.contains(targetSquare)) return GameStateEnum.ERROR;

        // Store the original square of the current piece
        SquareInterface originalSquare = currentPiece.getCurrentSquare();

        // Store the captured piece (if any)
        PieceInterface capturedPiece = targetSquare.getOccupyingPiece();

        return makeMoveAndCheckSpecialRules(originalSquare, currentPiece, currentPieceColor, capturedPiece, targetSquare);
    }


    private GameStateEnum makeMoveAndCheckSpecialRules(SquareInterface originalSquare, PieceInterface originalPiece, PieceColor originalPieceColor, PieceInterface targetPiece, SquareInterface targetSquare) {

//        // Make the move
        originalPiece.move(targetSquare, boardService);

        // Check if the current player's king is in check after the move
        if (checkmateDetector.isInCheck(boardService, originalPieceColor)) {
            // Undo the move
            originalPiece.move(originalSquare, boardService);

            // Restore the captured piece (if any)
            if (targetPiece != null) {
                targetSquare.setOccupyingPiece(targetPiece);
                if (targetPiece.getPieceColor() == PieceColor.WHITE) {
                    boardService.getWhitePieces().add(targetPiece);
                } else {
                    boardService.getBlackPieces().add(targetPiece);
                }
            }
            return GameStateEnum.CHECK;
//            System.out.println("Invalid move. Your king is in check!");
        } else {

            PieceColor opponentColor = originalPieceColor.equals(PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            // Check if the opponent is in checkmate
            if (checkmateDetector.isInCheckmate(boardService, opponentColor)) {
                return GameStateEnum.CHECKMATE;
            }
            // Check if the opponent is in stalemate
            else if (checkmateDetector.isInStalemate(boardService, opponentColor)) {
                return GameStateEnum.STALEMATE;
            }
            // Change the turn to the other player
            boardService.setWhiteTurn(!boardService.isWhiteTurn());

            return GameStateEnum.ONGOING;
        }
    }

    public enum GameStateEnum {
        CHECKMATE, CHECK, STALEMATE, ONGOING, ERROR;

        public static GameStatusType toGameStatusType(GameStateEnum gameStateEnum) throws Exception {
            return switch (gameStateEnum) {
                case CHECKMATE -> GameStatusType.CHECKMATE;
                case CHECK -> GameStatusType.CHECK;
                case STALEMATE -> GameStatusType.STALEMATE;
                case ONGOING -> GameStatusType.ONGOING;
                default -> throw new Exception("did not mapped");
            };
        };
    }


}
