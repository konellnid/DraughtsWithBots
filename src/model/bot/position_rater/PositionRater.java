package model.bot.position_rater;

import model.board.Position;

import java.util.BitSet;

public class PositionRater {
    public static final int WHITE_WON = Integer.MAX_VALUE;
    public static final int BLACK_WON = Integer.MIN_VALUE;

    private PositionRaterSettings positionRaterSettings;
    private int boardSideLength;
    private BitSet whiteCheckers;
    private BitSet whiteKings;
    private BitSet blackCheckers;
    private BitSet blackKings;
    private int whiteScore;
    private int blackScore;


    public PositionRater(PositionRaterSettings positionRaterSettings, int boardSideLength) {
        this.positionRaterSettings = positionRaterSettings;
        this.boardSideLength = boardSideLength;
    }

    public int getRatingOfPosition(Position position) {
        prepareWhiteBitSets(position.getWhitePieces(), position.getKings());
        prepareBlackBitSets(position.getBlackPieces(), position.getKings());

        whiteScore = getBasicPieceScore(whiteCheckers, whiteKings);
        blackScore = getBasicPieceScore(blackCheckers, blackKings);

        return 0;
    }

    private void prepareWhiteBitSets(BitSet whitePieces, BitSet kings) {
        whiteCheckers = (BitSet) whitePieces.clone();
        whiteCheckers.andNot(kings);

        whiteKings = (BitSet) whitePieces.clone();
        whiteKings.and(kings);
    }

    private void prepareBlackBitSets(BitSet blackPieces, BitSet kings) {
        blackCheckers = (BitSet) blackPieces.clone();
        blackCheckers.andNot(kings);

        blackKings = (BitSet) blackPieces.clone();
        blackKings.and(kings);
    }

    private int getBasicPieceScore(BitSet checkers, BitSet kings) {
        int pieceScore = 0;

        pieceScore += positionRaterSettings.getPointsPerChecker() * checkers.cardinality();
        pieceScore += positionRaterSettings.getPointsPerKing() * kings.cardinality();

        return pieceScore;
    }
}
