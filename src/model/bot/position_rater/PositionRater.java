package model.bot.position_rater;

import model.board.Position;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

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

        if (positionRaterSettings.isBonusForBeingCloserToPromotionLineActive()) {
            whiteScore += getWhiteScoreForBeingCloserToThePromotionRow(whiteCheckers);
            blackScore += getBlackScoreForBeingCloserToThePromotionRow(blackCheckers);
        }

        return whiteScore - blackScore;
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

    private int getWhiteScoreForBeingCloserToThePromotionRow(BitSet whiteCheckers) {
        int score = 0;

        List<Integer> listOfTowsWithCheckers = getListOfRowsFromBitSet(whiteCheckers);

        for (Integer checkerRow : listOfTowsWithCheckers) {
            score += checkerRow;
        }

        return score;
    }

    private int getBlackScoreForBeingCloserToThePromotionRow(BitSet blackCheckers) {
        int score = 0;

        List<Integer> listOfRowsWithCheckers = getListOfRowsFromBitSet(blackCheckers);

        for (Integer checkerRow : listOfRowsWithCheckers) {
            score += (boardSideLength - checkerRow - 1);
        }

        return score;
    }

    private List<Integer> getListOfRowsFromBitSet(BitSet checkers) {
        List<Integer> listOfRowsWithCheckers = new ArrayList<>(checkers.cardinality());

        for (int checkerTileNumber = whiteCheckers.length(); (checkerTileNumber = whiteCheckers.previousSetBit(checkerTileNumber-1)) >= 0; ) {
            checkerTileNumber -= boardSideLength / 2;
            int doubleRowIndicator = boardSideLength;

            while(checkerTileNumber > doubleRowIndicator) {
                checkerTileNumber--;
                doubleRowIndicator += boardSideLength;
            }

            checkerTileNumber--;

            int currentCheckerRow = checkerTileNumber / 4; // bottom row is marked as 0

            listOfRowsWithCheckers.add(currentCheckerRow);
        }

        return listOfRowsWithCheckers;
    }
}
