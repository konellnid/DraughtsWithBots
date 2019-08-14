package model.bot.position_rater;

import model.board.Position;

public class PositionRater {
    private static final int WHITE_WON = Integer.MAX_VALUE;
    private static final int BLACK_WON = Integer.MIN_VALUE;

    private PositionRaterSettings positionRaterSettings;
    private int boardSideLength;

    public PositionRater(int boardSideLength) {
        this.boardSideLength = boardSideLength;
    }

    public int getRatingOfPosition(Position position) {
        return 0;
    }
}
