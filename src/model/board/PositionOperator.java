package model.board;

public class PositionOperator {
    private int boardSideLength;
    private int[] whitePromotionZone;
    private int[] blackPromotionZone;
    private boolean pieceWasPromotedDuringLastMove;

    public PositionOperator(int boardSideLength) {
        this.boardSideLength = boardSideLength;

        setUpPromotionZones();
    }

    private void setUpPromotionZones() {

    }

    public void performMoveOnPosition(Position position, Move move) {

    }



    private void checkerReachedPromotionZone(Position position, int checkerTileNumber) {

    }
}
