package server.services.strategy;

import server.model.pieces.common.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.services.board.BoardService;
import server.services.board.SquareInterface;
import shared.enums.PieceColor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class KnightStrategyTest {
    @Mock
    private BoardService boardService;

    @Mock
    private SquareInterface currentSquare;

    @Mock
    private Piece knight;

    private SquareInterface[][] squareArrayMock;

    @InjectMocks
    private KnightStrategy knightStrategy;

    @BeforeEach
    void setup() {
        try (var autocloseable = MockitoAnnotations.openMocks(this)) {
            knightStrategy = new KnightStrategy(knight);
            setupMockedSquareArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //Test: Knight Moves from Center
    @Test
    void shouldReturnLegalMovesFromCenterOfBoard() {
        // Arrange
        when(knight.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);


        // Act
        List<SquareInterface> legalMoves = knightStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(8, legalMoves.size(), "The knight should have 8 legal moves from the center of the boardService.");
        for (SquareInterface move : legalMoves) {
            verify(move, atLeastOnce()).isOccupied(); // Ensures occupation logic is checked.
        }
    }

    //Test: Knight Moves from Edge
    @Test
    void shouldReturnLegalMovesFromEdgeOfBoard() {
        // Arrange
        when(knight.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(0);
        when(currentSquare.getYNum()).thenReturn(1);
        when(knight.getPieceColor()).thenReturn(PieceColor.WHITE);

        // Act
        List<SquareInterface> legalMoves = knightStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertEquals(3, legalMoves.size(), "The knight should have 3 legal moves from the edge of the boardService.");
    }

    //Test: Occupied Squares - friendly
    @Test
    void shouldExcludeFriendlyOccupiedSquares() {
        // Arrange
        when(knight.getCurrentSquare()).thenReturn(currentSquare);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);

        // Simulate friendly piece occupying one of the target squares
        SquareInterface targetSquare = squareArrayMock[5][6];
        Piece friendlyPiece = mock(Piece.class);
        when(knight.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(targetSquare.isOccupied()).thenReturn(true);
        when(targetSquare.getOccupyingPiece()).thenReturn(friendlyPiece);
        when(friendlyPiece.getPieceColor()).thenReturn(PieceColor.WHITE);

        // Act
        List<SquareInterface> legalMoves = knightStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.stream().noneMatch(move -> move.equals(targetSquare)),
                "The knight should not move to squares occupied by friendly pieces."
        );
    }

    @Test
    void shouldIncludeOpponentOccupiedSquares() {
        // Arrange
        when(knight.getCurrentSquare()).thenReturn(currentSquare);
        when(knight.getPieceColor()).thenReturn(PieceColor.WHITE);
        when(currentSquare.getXNum()).thenReturn(4);
        when(currentSquare.getYNum()).thenReturn(4);

        // Simulate opponent piece occupying one of the target squares
        SquareInterface targetSquare = squareArrayMock[5][6];
        Piece targetPiece = mock(Piece.class);

        when(targetSquare.isOccupied()).thenReturn(true);
        when(targetSquare.getOccupyingPiece()).thenReturn(targetPiece);
        when(targetPiece.getPieceColor()).thenReturn(PieceColor.BLACK); // Simulating opponent color.

        // Act
        List<SquareInterface> legalMoves = knightStrategy.getLegalMoves(squareArrayMock);

        // Assert
        assertTrue(
                legalMoves.contains(targetSquare),
                "The knight should be able to capture opponent pieces."
        );
    }

    private void setupMockedSquareArray() {
        squareArrayMock = new SquareInterface[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                SquareInterface square = mock(SquareInterface.class);
                when(square.isOccupied()).thenReturn(false); // Default: all squares unoccupied.
                squareArrayMock[y][x] = square;
            }
        }
        when(boardService.getBoardSquareArray()).thenReturn(squareArrayMock);
    }

}
