package services.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RookStrategyTest {
    @Mock
    private PieceInterface rook;

    @Mock
    private SquareInterface currentSquare;

    private SquareInterface[][] squareArrayMock;

    @InjectMocks
    private RookStrategy rookStrategy;

    @BeforeEach
    void setup() {
        try (var autocloseable = MockitoAnnotations.openMocks(this)) {
            rookStrategy = new RookStrategy(rook);
            setupMockedSquareArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test: Rook moves from the center of the board
    @Test
    void shouldReturnLegalMovesFromCenterOfBoard() {
        // Arrange
        when(rook.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);

        // Act
        List<SquareInterface> legalMoves = rookStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(14, legalMoves.size(), "The rook should have 14 legal moves from the center of the board.");
    }

    // Test: Rook moves from the edge of the board
    @Test
    void shouldReturnLegalMovesFromEdgeOfBoard() {
        // Arrange
        when(rook.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(0);
        when(currentSquare.getYNum()).thenReturn(4);

        // Act
        List<SquareInterface> legalMoves = rookStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(14, legalMoves.size(), "The rook should have 14 legal moves from the edge of the board.");
    }

    // Test: Rook moves from the corner of the board
    @Test
    void shouldReturnLegalMovesFromCornerOfBoard() {
        // Arrange
        when(rook.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(0);
        when(currentSquare.getYNum()).thenReturn(0);

        // Act
        List<SquareInterface> legalMoves = rookStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(14, legalMoves.size(), "The rook should have 14 legal moves from the corner of the board.");
    }

    // Test: Excluding friendly occupied squares
    @Test
    void shouldExcludeFriendlyOccupiedSquares() {
        // Arrange
        when(rook.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);
        when(rook.getPieceColor()).thenReturn(PieceColor.WHITE);

        SquareInterface friendlyOccupiedSquare = squareArrayMock[4][5];
        PieceInterface friendlyPiece = mock(PieceInterface.class);
        when(friendlyOccupiedSquare.isOccupied()).thenReturn(true);
        when(friendlyOccupiedSquare.getOccupyingPiece()).thenReturn(friendlyPiece);
        when(friendlyPiece.getPieceColor()).thenReturn(PieceColor.WHITE);

        // Act
        List<SquareInterface> legalMoves = rookStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.stream().noneMatch(move -> move.equals(friendlyOccupiedSquare)),
                "The rook should not move to squares occupied by friendly pieces."
        );
    }

    // Test: Including opponent-occupied squares
    @Test
    void shouldIncludeOpponentOccupiedSquares() {
        // Arrange
        when(rook.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);
        when(rook.getPieceColor()).thenReturn(PieceColor.WHITE);

        SquareInterface opponentOccupiedSquare = squareArrayMock[4][5];
        PieceInterface opponentPiece = mock(PieceInterface.class);
        when(opponentOccupiedSquare.isOccupied()).thenReturn(true);
        when(opponentOccupiedSquare.getOccupyingPiece()).thenReturn(opponentPiece);
        when(opponentPiece.getPieceColor()).thenReturn(PieceColor.BLACK);

        // Act
        List<SquareInterface> legalMoves = rookStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.contains(opponentOccupiedSquare),
                "The rook should be able to capture opponent pieces."
        );
    }

    private void setupMockedSquareArray() {
        squareArrayMock = new SquareInterface[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                SquareInterface square = mock(SquareInterface.class);
                when(square.isOccupied()).thenReturn(false);
                squareArrayMock[y][x] = square;
            }
        }
    }
}