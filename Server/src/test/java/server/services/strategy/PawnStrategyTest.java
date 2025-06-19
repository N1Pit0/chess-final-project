package server.services.strategy;

import shared.enums.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PawnStrategyTest {
    @Mock
    private PieceInterface pawn;

    @Mock
    private SquareInterface currentSquare;

    private SquareInterface[][] squareArrayMock;

    @InjectMocks
    private PawnStrategy pawnStrategy;

    @BeforeEach
    void setup() {
        try (var autocloseable = MockitoAnnotations.openMocks(this)) {
            pawnStrategy = new PawnStrategy(pawn);
            setupMockedSquareArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test: Regular forward move (one step)
    @Test
    void shouldReturnRegularForwardMove() {
        // Arrange
        when(pawn.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(6);
        when(pawn.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(pawn.isWasMoved()).thenReturn(true);

        // Act
        List<SquareInterface> legalMoves = pawnStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(1, legalMoves.size(), "The pawn should have 1 legal move (regular forward move).");
        assertTrue(legalMoves.contains(squareArrayMock[5][4]), "The pawn should be able to move one step forward.");
    }

    // Test: Initial two-step move (only when the pawn hasn't moved yet)
    @Test
    void shouldReturnInitialTwoStepMove() {
        // Arrange
        when(pawn.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(6);
        when(pawn.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(pawn.isWasMoved()).thenReturn(false);

        // Act
        List<SquareInterface> legalMoves = pawnStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(2, legalMoves.size(), "The pawn should have 2 legal moves (regular forward move and initial two-step move).");
        assertTrue(legalMoves.contains(squareArrayMock[5][4]), "The pawn should be able to move one step forward.");
        assertTrue(legalMoves.contains(squareArrayMock[4][4]), "The pawn should be able to move two steps forward initially.");
    }

    // Test: Diagonal attack moves (capturing opponent pieces)
    @Test
    void shouldReturnDiagonalAttackMoves() {
        // Arrange
        when(pawn.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(6);
        when(pawn.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(pawn.isWasMoved()).thenReturn(true);

        SquareInterface opponentSquareLeft = squareArrayMock[5][3];
        SquareInterface opponentSquareRight = squareArrayMock[5][5];
        PieceInterface opponentPieceLeft = mock(PieceInterface.class);
        PieceInterface opponentPieceRight = mock(PieceInterface.class);
        when(opponentSquareLeft.isOccupied()).thenReturn(true);
        when(opponentSquareLeft.getOccupyingPiece()).thenReturn(opponentPieceLeft);
        when(opponentPieceLeft.getPieceColor()).thenReturn(PieceColor.BLACK);
        when(opponentSquareRight.isOccupied()).thenReturn(true);
        when(opponentSquareRight.getOccupyingPiece()).thenReturn(opponentPieceRight);
        when(opponentPieceRight.getPieceColor()).thenReturn(PieceColor.BLACK);

        // Act
        List<SquareInterface> legalMoves = pawnStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(3, legalMoves.size(), "The pawn should have 3 legal moves (regular forward move and two diagonal attack moves).");
        assertTrue(legalMoves.contains(squareArrayMock[5][4]), "The pawn should be able to move one step forward.");
        assertTrue(legalMoves.contains(opponentSquareLeft), "The pawn should be able to capture the opponent piece on the left diagonal.");
        assertTrue(legalMoves.contains(opponentSquareRight), "The pawn should be able to capture the opponent piece on the right diagonal.");
    }

    // Test: Blocked forward move (when a piece is directly in front of the pawn)
    @Test
    void shouldNotReturnBlockedForwardMove() {
        // Arrange
        when(pawn.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(6);
        when(pawn.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(pawn.isWasMoved()).thenReturn(true);

        SquareInterface blockedSquare = squareArrayMock[5][4];
        PieceInterface blockingPiece = mock(PieceInterface.class);
        when(blockedSquare.isOccupied()).thenReturn(true);
        when(blockedSquare.getOccupyingPiece()).thenReturn(blockingPiece);

        // Act
        List<SquareInterface> legalMoves = pawnStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(legalMoves.isEmpty(), "The pawn should have no legal moves when blocked by a piece directly in front.");
    }

    // Test: Blocked diagonal attack moves (when friendly pieces occupy the diagonal squares)
    @Test
        void shouldNotReturnBlockedDiagonalAttackMoves() {
        // Arrange
        when(pawn.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(6);
        when(pawn.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(pawn.isWasMoved()).thenReturn(true);

        SquareInterface friendlySquareLeft = squareArrayMock[5][3];
        SquareInterface friendlySquareRight = squareArrayMock[5][5];
        PieceInterface friendlyPieceLeft = mock(PieceInterface.class);
        PieceInterface friendlyPieceRight = mock(PieceInterface.class);
        when(friendlySquareLeft.isOccupied()).thenReturn(true);
        when(friendlyPieceLeft.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(friendlySquareLeft.getOccupyingPiece()).thenReturn(friendlyPieceLeft);
        when(friendlySquareRight.isOccupied()).thenReturn(true);
        when(friendlyPieceRight.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(friendlySquareRight.getOccupyingPiece()).thenReturn(friendlyPieceRight);

        // Act
        List<SquareInterface> legalMoves = pawnStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(1, legalMoves.size(), "The pawn should have only 1 legal move (regular forward move).");
        assertTrue(legalMoves.contains(squareArrayMock[5][4]), "The pawn should be able to move one step forward.");
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