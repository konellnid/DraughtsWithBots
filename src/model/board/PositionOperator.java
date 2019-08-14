package model.board;

import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;

public class PositionOperator {
    private int boardSideLength;
    private int[] whitePromotionZone;
    private int[] blackPromotionZone;
    private boolean pieceWasPromotedDuringLastMove;

    private static final String NO_SUCH_PIECE_MESSAGE = "There is no such piece!";
    private static final String ILLEGAL_MOVE_MESSAGE = "It is not possible to make such a move!";

    public PositionOperator(int boardSideLength) {
        this.boardSideLength = boardSideLength;
        setUpPromotionZones();
    }

    private void setUpPromotionZones() {
        switch (boardSideLength) {
            case 8:
                whitePromotionZone = new int[]{36, 37, 38, 39};
                blackPromotionZone = new int[]{5, 6, 7, 8};
                break;
            case 10:
                whitePromotionZone = new int[]{55, 56, 57, 58, 59};
                blackPromotionZone = new int[]{6, 7, 8, 9, 10};
                break;
            case 12:
                whitePromotionZone = new int[]{78, 79, 80, 81, 82, 83};
                blackPromotionZone = new int[]{7, 8, 9, 10, 11, 12};
                break;
        }
    }

    public void performMoveOnPosition(Position position, Move move, boolean isWhiteMove) {

    }

    private void checkerReachedPromotionZone(Position position, int checkerTileNumber) {

    }
}
