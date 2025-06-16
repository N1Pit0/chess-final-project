package services.board;

import dtos.enums.PieceColor;
import model.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardServiceTest {
    @Mock
    private BoardInterface boardInterface;

    @InjectMocks
    private BoardServiceImpl boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeBoardSquares() {
        // Arrange
        SquareInterface[][] expectedBoardSquares = new SquareInterface[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                PieceColor squareColor = ((x + y) % 2 == 0) ? PieceColor.WHITE : PieceColor.BLACK;
                expectedBoardSquares[y][x] = new Square(squareColor, x, y);
            }
        }

        // Act
        boardService.initializeBoardSquares();

        // Assert
        verify(boardInterface, times(2)).initializeBoardSquares();
    }

    @Test
    void testInitializePieces() {
        // Act
        boardService.initializePieces();

        // Assert
        verify(boardInterface, times(2)).initializePieces();
    }

    @Test
    void testGetWhiteKing() {
        // Arrange
        PieceInterface expectedWhiteKing = mock(PieceInterface.class);
        when(boardInterface.getWhiteKing()).thenReturn(Optional.of(expectedWhiteKing));

        // Act
        Optional<PieceInterface> actualWhiteKing = boardService.getWhiteKing();

        // Assert
        assertTrue(actualWhiteKing.isPresent());
        assertEquals(expectedWhiteKing, actualWhiteKing.get());
    }

    @Test
    void testSetWhiteKing() {
        // Arrange
        PieceInterface whiteKing = mock(PieceInterface.class);

        // Act
        boardService.setWhiteKing(whiteKing);

        // Assert
        verify(boardInterface, times(1)).setWhiteKing(whiteKing);
    }

    @Test
    void testGetBlackKing() {
        // Arrange
        PieceInterface expectedBlackKing = mock(PieceInterface.class);
        when(boardInterface.getBlackKing()).thenReturn(Optional.of(expectedBlackKing));

        // Act
        Optional<PieceInterface> actualBlackKing = boardService.getBlackKing();

        // Assert
        assertTrue(actualBlackKing.isPresent());
        assertEquals(expectedBlackKing, actualBlackKing.get());
    }

    @Test
    void testSetBlackKing() {
        // Arrange
        PieceInterface blackKing = mock(PieceInterface.class);

        // Act
        boardService.setBlackKing(blackKing);

        // Assert
        verify(boardInterface, times(1)).setBlackKing(blackKing);
    }

    @Test
    void testGetWhitePieces() {
        // Arrange
        List<PieceInterface> expectedWhitePieces = new ArrayList<>();
        when(boardInterface.getWhitePieces()).thenReturn(expectedWhitePieces);

        // Act
        List<PieceInterface> actualWhitePieces = boardService.getWhitePieces();

        // Assert
        assertEquals(expectedWhitePieces, actualWhitePieces);
    }

    @Test
    void testSetWhitePieces() {
        // Arrange
        List<PieceInterface> whitePieces = new ArrayList<>();

        // Act
        boardService.setWhitePieces(whitePieces);

        // Assert
        verify(boardInterface, times(1)).setWhitePieces(whitePieces);
    }

    @Test
    void testGetBlackPieces() {
        // Arrange
        List<PieceInterface> expectedBlackPieces = new ArrayList<>();
        when(boardInterface.getBlackPieces()).thenReturn(expectedBlackPieces);

        // Act
        List<PieceInterface> actualBlackPieces = boardService.getBlackPieces();

        // Assert
        assertEquals(expectedBlackPieces, actualBlackPieces);
    }

    @Test
    void testSetBlackPieces() {
        // Arrange
        List<PieceInterface> blackPieces = new ArrayList<>();

        // Act
        boardService.setBlackPieces(blackPieces);

        // Assert
        verify(boardInterface, times(1)).setBlackPieces(blackPieces);
    }

    @Test
    void testGetBoardSquareArray() {
        // Arrange
        SquareInterface[][] expectedBoardSquares = new SquareInterface[8][8];
        when(boardInterface.getBoardSquareArray()).thenReturn(expectedBoardSquares);

        // Act
        SquareInterface[][] actualBoardSquares = boardService.getBoardSquareArray();

        // Assert
        assertSame(expectedBoardSquares, actualBoardSquares);
    }

    @Test
    void testWhiteTurnInitiallyTrue() {
        // Assert
        assertTrue(boardService.isWhiteTurn());
    }
}