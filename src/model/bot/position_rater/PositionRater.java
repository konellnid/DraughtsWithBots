package model.bot.position_rater;

import model.board.Position;

public class PositionRater {
    public static final int WHITE_WON = Integer.MAX_VALUE;
    public static final int BLACK_WON = Integer.MIN_VALUE;

    private PositionRaterSettings positionRaterSettings;
    private int boardSideLength;

    public PositionRater(PositionRaterSettings positionRaterSettings, int boardSideLength) {
        this.positionRaterSettings = positionRaterSettings;
        this.boardSideLength = boardSideLength;
    }

    public int getRatingOfPosition(Position position) {
        return 0;
    }
}
