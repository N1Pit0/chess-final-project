package services.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.board.SquareInterface;
import services.strategy.common.PieceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static services.enums.PieceColor.BLACK;
import static services.enums.PieceColor.WHITE;

public class BishopStrategyTest {
    @Mock
    private PieceInterface bishop;

    @Mock
    private SquareInterface currentSquare;

    private SquareInterface[][] squareArrayMock;

    @InjectMocks
    private BishopStrategy bishopStrategy;

    @BeforeEach
    void setup() {
        try (var autocloseable = MockitoAnnotations.openMocks(this)) {
            bishopStrategy = new BishopStrategy(bishop);
            setupMockedSquareArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test: Bishop moves from the center of the board
    @Test
    void shouldReturnLegalMovesFromCenterOfBoard() {
        // Arrange
        when(bishop.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);

        // Act
        List<SquareInterface> legalMoves = bishopStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(13, legalMoves.size(), "The bishop should have 13 legal moves from the center of the board.");
        for (SquareInterface move : legalMoves) {
            verify(move, atLeastOnce()).isOccupied();
        }
    }

    // Test: Bishop moves from the edge of the board
    @Test
    void shouldReturnLegalMovesFromEdgeOfBoard() {
        // Arrange
        when(bishop.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(0);
        when(currentSquare.getYNum()).thenReturn(4);

        // Act
        List<SquareInterface> legalMoves = bishopStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(7, legalMoves.size(), "The bishop should have 7 legal moves from the edge of the board.");
    }

    // Test: Excluding friendly occupied squares
    @Test
    void shouldExcludeFriendlyOccupiedSquares() {
        // Arrange
        when(bishop.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);
        when(bishop.getPieceColor()).thenReturn(WHITE);

        SquareInterface friendlyOccupiedSquare = squareArrayMock[6][6];
        PieceInterface friendlyPiece = mock(PieceInterface.class);
        when(friendlyOccupiedSquare.isOccupied()).thenReturn(true);
        when(friendlyOccupiedSquare.getOccupyingPiece()).thenReturn(friendlyPiece);
        when(friendlyPiece.getPieceColor()).thenReturn(WHITE);

        // Act
        List<SquareInterface> legalMoves = bishopStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.stream().noneMatch(move -> move.equals(friendlyOccupiedSquare)),
                "The bishop should not move to squares occupied by friendly pieces."
        );
    }

    // Test: Including opponent-occupied squares
    @Test
    void shouldIncludeOpponentOccupiedSquares() {
        // Arrange
        when(bishop.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);
        when(bishop.getPieceColor()).thenReturn(WHITE);

        SquareInterface opponentOccupiedSquare = squareArrayMock[6][6];
        PieceInterface opponentPiece = mock(PieceInterface.class);
        when(opponentOccupiedSquare.isOccupied()).thenReturn(true);
        when(opponentOccupiedSquare.getOccupyingPiece()).thenReturn(opponentPiece);
        when(opponentPiece.getPieceColor()).thenReturn(BLACK);

        // Act
        List<SquareInterface> legalMoves = bishopStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.contains(opponentOccupiedSquare),
                "The bishop should be able to capture opponent pieces."
        );
    }

    // Test: Bishop's path is blocked by other pieces
    @Test
    void shouldStopAtBlockingPieces() {
        // Arrange
        when(bishop.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);

        SquareInterface blockingSquare = squareArrayMock[6][6];
        PieceInterface blockingPiece = mock(PieceInterface.class);
        when(blockingSquare.isOccupied()).thenReturn(true);
        when(blockingSquare.getOccupyingPiece()).thenReturn(blockingPiece);

        // Act
        List<SquareInterface> legalMoves = bishopStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.stream().noneMatch(move -> move.equals(squareArrayMock[7][7])),
                "The bishop should stop at blocking pieces and not move further."
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