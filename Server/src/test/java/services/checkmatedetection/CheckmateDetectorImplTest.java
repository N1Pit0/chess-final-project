package services.checkmatedetection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.board.BoardService;
import services.board.SquareInterface;
import services.checkmatedetection.CheckmateDetectorImpl;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CheckmateDetectorImplTest {
    @Mock
    private BoardService boardService;

    @Mock
    private PieceInterface whiteKing;

    @Mock
    private PieceInterface blackKing;

    @Mock
    private PieceInterface whiteRook;

    @Mock
    private PieceInterface blackBishop;

    @Mock
    private SquareInterface whiteKingSquare;

    @Mock
    private SquareInterface blackKingSquare;

    @Mock
    private SquareInterface rookSquare;

    @Mock
    private SquareInterface bishopSquare;

    @InjectMocks
    private CheckmateDetectorImpl checkmateDetector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Stub the getPieceColor() method for each mock PieceInterface
        when(whiteKing.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(blackKing.getPieceColor()).thenReturn(PieceColor.BLACK);
        when(whiteRook.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(blackBishop.getPieceColor()).thenReturn(PieceColor.BLACK);

        // Stub the getCurrentSquare() method for each mock PieceInterface
        when(whiteKing.getCurrentSquare()).thenReturn(whiteKingSquare);
        when(blackKing.getCurrentSquare()).thenReturn(blackKingSquare);
        when(whiteRook.getCurrentSquare()).thenReturn(rookSquare);
        when(blackBishop.getCurrentSquare()).thenReturn(bishopSquare);

        // Stub the getBoardSquareArray() method of the mock BoardService
        when(boardService.getBoardSquareArray()).thenReturn(new SquareInterface[8][8]);
    }

    @Test
    void testIsInCheck_WhiteKingInCheck() {
        // Arrange
        when(boardService.getWhiteKing()).thenReturn(Optional.of(whiteKing));
        when(whiteKingSquare.getOccupyingPiece()).thenReturn(whiteKing);
        when(boardService.getBlackPieces()).thenReturn(List.of(blackBishop));
        when(blackBishop.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(whiteKingSquare));

        // Act
        boolean result = checkmateDetector.isInCheck(boardService, PieceColor.WHITE);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsInCheck_BlackKingNotInCheck() {
        // Arrange
        when(boardService.getBlackKing()).thenReturn(Optional.of(blackKing));
        when(boardService.getWhitePieces()).thenReturn(List.of(whiteRook));
        when(whiteRook.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(rookSquare));

        // Act
        boolean result = checkmateDetector.isInCheck(boardService, PieceColor.BLACK);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsInCheckmate_WhiteKingInCheckmate() {
        // Arrange
        when(boardService.getWhiteKing()).thenReturn(Optional.of(whiteKing));
        when(boardService.getBlackPieces()).thenReturn(List.of(blackBishop));
        when(blackBishop.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(whiteKingSquare));
        when(boardService.getWhitePieces()).thenReturn(List.of(whiteKing));
        when(whiteKing.getLegalMoves(any(SquareInterface[][].class))).thenReturn(new ArrayList<>());

        // Act
        boolean result = checkmateDetector.isInCheckmate(boardService, PieceColor.WHITE);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsInCheckmate_BlackKingNotInCheckmate() {
        // Arrange
        when(boardService.getBlackKing()).thenReturn(Optional.of(blackKing));
        when(blackKing.getCurrentSquare()).thenReturn(blackKingSquare);
        when(boardService.getWhitePieces()).thenReturn(List.of(whiteRook));
        when(whiteRook.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(rookSquare));
        when(boardService.getBlackPieces()).thenReturn(List.of(blackKing));
        when(blackKing.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(bishopSquare));

        // Act
        boolean result = checkmateDetector.isInCheckmate(boardService, PieceColor.BLACK);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsInStalemate_WhiteKingInStalemate() {
        // Arrange
        when(boardService.getWhiteKing()).thenReturn(Optional.of(whiteKing));
        when(boardService.getBlackPieces()).thenReturn(List.of(blackBishop));
        when(blackBishop.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(bishopSquare));
        when(boardService.getWhitePieces()).thenReturn(List.of(whiteKing));
        when(whiteKing.getLegalMoves(any(SquareInterface[][].class))).thenReturn(new ArrayList<>());

        // Act
        boolean result = checkmateDetector.isInStalemate(boardService, PieceColor.WHITE);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsInStalemate_BlackKingNotInStalemate() {
        // Arrange
        when(boardService.getBlackKing()).thenReturn(Optional.of(blackKing));
        when(blackKing.getCurrentSquare()).thenReturn(blackKingSquare);
        when(boardService.getWhitePieces()).thenReturn(List.of(whiteRook));
        when(whiteRook.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(rookSquare));
        when(boardService.getBlackPieces()).thenReturn(List.of(blackKing));
        when(blackKing.getLegalMoves(any(SquareInterface[][].class))).thenReturn(List.of(bishopSquare));

        // Act
        boolean result = checkmateDetector.isInStalemate(boardService, PieceColor.BLACK);

        // Assert
        assertFalse(result);
    }

    // Additional test cases for hasLegalMoveWithoutCheck() and checkHelper() methods

    // ...
}