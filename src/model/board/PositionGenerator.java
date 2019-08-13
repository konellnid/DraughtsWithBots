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

    public Position generateEmptyPositionForBoardSide(int boardSideLength) {
        initializeProperBitSetsForBoardSide(boardSideLength);
        return new Position(whitePieces, blackPieces);
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
        int weirdNumber = (boardSideLength / 2) - 1;
        int currentTileNumber = boardSideLength * weirdNumber + weirdNumber;

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

    /*  Board of sideLength = 8: total BitSet size = 45
      |  39  38  37  36|
      |35  34  33  32  |
      |  30  29  28  27|
      |26  25  24  23  |
      |  21  20  19  18|
      |17  16  15  14  |
      |  12  11  10  09|
      |08  07  06  05  |

    Board of sideLength = 10: total BitSet size = 66
      |  59  58  57  56  55|
      |54  53  52  51  50  |
      |  48  47  46  45  44|
      |43  42  41  40  39  |
      |  37  36  35  34  33|
      |32  31  30  29  28  |
      |  26  25  24  23  22|
      |21  20  19  18  17  |
      |  15  14  13  12  11|
      |10  09  08  07  06  |

Board of sideLength = 12: total BitSet size = 91
      |  83  82  81  80  79  78|
      |77  76  75  74  73  72  |
      |  70  69  68  67  66  65|
      |64  63  62  61  60  59  |
      |  57  56  55  54  53  52|
      |51  50  49  48  47  46  |
      |  44  43  42  41  40  39|
      |38  37  36  35  34  33  |
      |  31  30  29  28  27  26|
      |25  24  23  22  21  20  |
      |  18  17  16  15  14  13|
      |12  11  10  09  08  07  |

*/
}
