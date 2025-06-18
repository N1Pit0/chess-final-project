package dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@ToString
@Getter
@Setter
public class BoardState implements Serializable {
    private SquareState[][] boardSquareArray;
    private List<PieceState> blackPieces;
    private List<PieceState> whitePieces;

    private PieceState whiteKing;
    private PieceState blackKing;

    private boolean whiteTurn;
    private PieceState currPiece;
    private int currX;
    private int currY;

    public BoardState(SquareState[][] boardSquareArray,List<PieceState> blackPieces, List<PieceState> whitePieces,PieceState whiteKing,PieceState blackKing, boolean whiteTurn) {
        this.boardSquareArray = boardSquareArray;
        this.blackPieces = blackPieces;
        this.whitePieces = whitePieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.whiteTurn = whiteTurn;
    }

    public BoardState(){

    }

//    ew BoardState(white, black, null,null,true);
//    }public static BoardState dummy() {
//        List<PieceState> white = new ArrayList<>();
//        white.add(new PieceState(PieceColor.WHITE, new SquareState(5,5,PieceColor.WHITE), PieceType.PAWN, false));
//        white.add(new PieceState(PieceColor.WHITE,new SquareState(0,4,PieceColor.BLACK),PieceType.KING, false));
//
//        List<PieceState> black = new ArrayList<>();
//        black.add(new PieceState(PieceColor.BLACK, new SquareState(2,5,PieceColor.WHITE), PieceType.PAWN, false));
//        black.add(new PieceState(PieceColor.BLACK,new SquareState(6,4,PieceColor.BLACK),PieceType.KING, false));
//
//        return n
}
