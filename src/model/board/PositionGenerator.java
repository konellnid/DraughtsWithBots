package model.board;

import java.util.BitSet;

public class PositionGenerator {
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_EIGHT = 45;
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_TEN = 66;
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_TWELVE = 91;

    private BitSet whitePieces;
    private BitSet blackPieces;

    public Position generateStartingPositionForBoardOfSideLength(int boardSideLength) {
        if (!isProperBoardSideLength(boardSideLength)) return null;

        initializeProperBitSetsForBoardSide(boardSideLength);

        addWhiteCheckers(boardSideLength);
        addBlackCheckers(boardSideLength);

        return new Position(whitePieces, blackPieces);
    }

    public Position generateEmptyPositionForBoardSide(int boardSideLength) {
        if (!isProperBoardSideLength(boardSideLength)) return null;
        initializeProperBitSetsForBoardSide(boardSideLength);
        return new Position(whitePieces, blackPieces);
    }

    private boolean isProperBoardSideLength(int boardSideLength) {
        if (boardSideLength == 8) return true;
        if (boardSideLength == 10) return true;
        if (boardSideLength == 12) return true;

        return false;
    }

    private void initializeProperBitSetsForBoardSide(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
                initializeBitSetsWithSize(BIT_SET_SIZE_OF_SIDE_LENGTH_EIGHT);
                break;
            case 10:
                initializeBitSetsWithSize(BIT_SET_SIZE_OF_SIDE_LENGTH_TEN);
                break;
            case 12:
                initializeBitSetsWithSize(BIT_SET_SIZE_OF_SIDE_LENGTH_TWELVE);
                break;
            default:
                whitePieces = new BitSet();
                blackPieces = new BitSet();
        }
    }

    private void initializeBitSetsWithSize(int bitSetSize) {
        whitePieces = new BitSet(bitSetSize);
        blackPieces = new BitSet(bitSetSize);
        // clarification: kings BitSet is created in the Position class constructor
    }

    private void addWhiteCheckers(int boardSideLength) {
        int smallestTileNumberInRow = (boardSideLength / 2) + 1;

        addCheckersToBitSet(whitePieces, boardSideLength, smallestTileNumberInRow, 0);
    }

    private void addBlackCheckers(int boardSideLength) {
        int smallestTileNumberInRow = getProperSmallestBlackTileNumber(boardSideLength);
        int doubleRowIndicator = (boardSideLength == 10) ? 0 : 1;

        addCheckersToBitSet(blackPieces, boardSideLength, smallestTileNumberInRow, doubleRowIndicator);
    }

    private int getProperSmallestBlackTileNumber(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
                return 27;
            case 10:
                return 39;
            case 12:
                return 52;
        }

        return -1;
    }

    private void addCheckersToBitSet(BitSet checkersBitSet, int boardSideLength, int smallestTileNumberInRow, int doubleRowIndicator) {
        int numberOfUnfilledRowsWithCheckers = boardSideLength / 2 - 1;
        int numberOfPiecesInARow = boardSideLength / 2;

        while (numberOfUnfilledRowsWithCheckers > 0) {
            checkersBitSet.set(smallestTileNumberInRow, smallestTileNumberInRow + numberOfPiecesInARow);
            smallestTileNumberInRow += numberOfPiecesInARow;

            doubleRowIndicator++;
            if (doubleRowIndicator == 2) {
                smallestTileNumberInRow++;
                doubleRowIndicator = 0;
            }

            numberOfUnfilledRowsWithCheckers--;
        }
    }
}
