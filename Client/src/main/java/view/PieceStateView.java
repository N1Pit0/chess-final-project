package view;

import dtos.PieceState;
import enums.ImagePath;
import enums.PieceColor;
import enums.PieceType;
import services.utils.ImageReaderUtil;
import services.utils.ImageReaderUtilImpl;
import services.utils.exceptions.ImageNotFoundException;

import java.awt.*;

public class PieceStateView {

    public void draw(Graphics g, PieceState piece) {


        g.drawImage(getImage(piece.getPieceType(),piece.getPieceColor()), 0, 0, null);
    }
    private Image getImage(PieceType pieceType, PieceColor pieceColor) {
        ImageReaderUtil imageReader = new ImageReaderUtilImpl();

        String imgFile;
        switch (pieceType) {
            case PAWN -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BPAWN_PNG.label
                    : ImagePath.RESOURCES_WPAWN_PNG.label;
            case KNIGHT -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BKNIGHT_PNG.label
                    : ImagePath.RESOURCES_WKNIGHT_PNG.label;
            case BISHOP -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BBISHOP_PNG.label
                    : ImagePath.RESOURCES_WBISHOP_PNG.label;
            case ROOK -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BROOK_PNG.label
                    : ImagePath.RESOURCES_WROOK_PNG.label;
            case QUEEN -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BQUEEN_PNG.label
                    : ImagePath.RESOURCES_WQUEEN_PNG.label;
            case KING -> imgFile = (pieceColor == PieceColor.BLACK)
                    ? ImagePath.RESOURCES_BKING_PNG.label
                    : ImagePath.RESOURCES_WKING_PNG.label;
            default -> throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }

        return imageReader.readImage(imgFile)
                .orElseThrow(() -> new ImageNotFoundException("Image not found for: " + imgFile));
    }
}
