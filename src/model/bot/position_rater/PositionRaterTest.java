package model.bot.position_rater;

import model.board.Position;
import model.board.PositionGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class PositionRaterTest {
    private static final int BOARD_SIDE_LENGTH_EIGHT = 8;
    private static final int BOARD_SIDE_LENGTH_TEN = 10;
    private static final int BOARD_SIDE_LENGTH_TWELVE = 12;

    private PositionGenerator positionGenerator;
    private PositionRaterSettings positionRaterSettings;
    private PositionRater positionRater;
    private Position position;
    private int expectedPositionRating;
    private int actualPositionRating;
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    @BeforeEach
    void setUp() {
        positionGenerator = new PositionGenerator();
        positionRaterSettings = new PositionRaterSettings();
    }

    private void prepareEmptyBitSets(int boardSideLengthEight) {
        position = positionGenerator.generateEmptyPositionForBoardSide(boardSideLengthEight);
        whitePieces = position.getWhitePieces();
        blackPieces = position.getBlackPieces();
        kings = position.getKings();
    }

    // BOARD SIDE LENGTH = 8 TESTS
    /*
      |  39  38  37  36|
      |35  34  33  32  |
      |  30  29  28  27|
      |26  25  24  23  |
      |  21  20  19  18|
      |17  16  15  14  |
      |  12  11  10  09|
      |08  07  06  05  |
     */
    @Test
    void shouldReturnProperRatingForDefaultSettings() {
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_EIGHT);
        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_EIGHT);
        positionRater = new PositionRater(positionRaterSettings, BOARD_SIDE_LENGTH_EIGHT);

        expectedPositionRating =
        actualPositionRating = positionRater.getRatingOfPosition(position);

        assertEquals(expectedPositionRating, actualPositionRating);
    }


    // BOARD SIDE LENGTH = 10 TESTS
    /*
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
     */

    // BOARD SIDE LENGTH = 12 TESTS
    /*
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