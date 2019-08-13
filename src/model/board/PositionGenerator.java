package model.board;

import java.util.BitSet;

public class PositionGenerator {
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_EIGHT = 45;
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_TEN = 66;
    private final static int BIT_SET_SIZE_OF_SIDE_LENGTH_TWELVE = 91;

    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    public Position generateBoardOfSideLength(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
            case 10:
            case 12:

            default:
                return null;
        }
    }

    private void initializeBitSetsWithSize(int bitSetSize) {
        whitePieces = new BitSet(bitSetSize);
        blackPieces = new BitSet(bitSetSize);
        kings = new BitSet(bitSetSize);
    }

    private void addWhiteCheckers(int boardSideLength) {
        int currentTileNumber = (boardSideLength / 2) + 1;

        for (int firstTwoRowsCounter = 0; firstTwoRowsCounter < boardSideLength; firstTwoRowsCounter++) {
            whitePieces.set(currentTileNumber);
        }

        currentTileNumber++; // skips the tile number between the second and third row

        for (int thirdRowCounter = 0; thirdRowCounter < boardSideLength / 2; thirdRowCounter++) {

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
