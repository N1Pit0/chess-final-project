package dtos.enums;

public enum ImagePath {
    RESOURCES_WBISHOP_PNG("/chess-images/wbishop.png"),

    RESOURCES_BBISHOP_PNG("/chess-images/bbishop.png"),

    RESOURCES_WKNIGHT_PNG("/chess-images/wknight.png"),

    RESOURCES_BKNIGHT_PNG("/chess-images/bknight.png"),

    RESOURCES_WROOK_PNG("/chess-images/wrook.png"),

    RESOURCES_BROOK_PNG("/chess-images/brook.png"),

    RESOURCES_WKING_PNG("/chess-images/wking.png"),

    RESOURCES_BKING_PNG("/chess-images/bking.png"),

    RESOURCES_BQUEEN_PNG("/chess-images/bqueen.png"),

    RESOURCES_WQUEEN_PNG("/chess-images/wqueen.png"),

    RESOURCES_WPAWN_PNG("/chess-images/wpawn.png"),

    RESOURCES_BPAWN_PNG("/chess-images/bpawn.png");

    public final String label;

    private ImagePath(String label) {
        this.label = label;
    }
}
