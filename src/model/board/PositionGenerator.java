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
        int currentTileNumber = (boardSideLength / 2) + 1;

        for (int firstTwoRowsCounter = 0; firstTwoRowsCounter < boardSideLength; firstTwoRowsCounter++) {
            whitePieces.set(currentTileNumber);
            currentTileNumber++;
        }

        currentTileNumber++; // skips the tile number between the second and third row

        for (int thirdRowCounter = 0; thirdRowCounter < boardSideLength / 2; thirdRowCounter++) {
            whitePieces.set(currentTileNumber);
            currentTileNumber++;
        }
    }

    private void addBlackCheckers(int boardSideLength) {
        int auxiliaryNumber = (boardSideLength / 2) - 1;
        int currentTileNumber = boardSideLength * auxiliaryNumber + auxiliaryNumber;

        for (int firstRowCounter = 0; firstRowCounter < boardSideLength / 2; firstRowCounter++) {
            blackPieces.set(currentTileNumber);
            currentTileNumber++;
        }

        currentTileNumber++; // skips the tile number between the first and second row (looking from bottom of the board)

        for (int lastTwoRowsCounter = 0; lastTwoRowsCounter < boardSideLength; lastTwoRowsCounter++) {
            blackPieces.set(currentTileNumber);
            currentTileNumber++;
        }
    }
}
