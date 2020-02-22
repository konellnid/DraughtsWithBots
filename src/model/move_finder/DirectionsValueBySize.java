package model.move_finder;

public enum DirectionsValueBySize {
    BOARD_OF_SIZE_EIGHT(4, 5, -5, -4),
    BOARD_OF_SIZE_TEN(5, 6, -6, -5),
    BOARD_OF_SIZE_TWELVE(6, 7, -7, -6);

    DirectionsValueBySize(int upperRight, int upperLeft, int lowerRight, int lowerLeft) {
        this.upperRight = upperRight;
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.lowerLeft = lowerLeft;
    }

    public int upperRight;
    public int upperLeft;
    public int lowerRight;
    public int lowerLeft;
}
