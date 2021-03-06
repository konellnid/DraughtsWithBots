package model.bot.position_rater;

import model.board.Position;
import model.board.PositionGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

// every test compares the scoring from PositionRater with expected score - that's why it's moved to @AfterEach method (may be changed in the future)
class PositionRaterTest {
    private static final int BOARD_SIDE_LENGTH_EIGHT = 8;
    private static final int BOARD_SIDE_LENGTH_TEN = 10;
    private static final int BOARD_SIDE_LENGTH_TWELVE = 12;
    private static final boolean NOT_ACTiVE = false;

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

    @AfterEach
    void getActualRatingAndCompareWithExpected() {
        actualPositionRating = positionRater.getRatingOfPosition(position);

        assertEquals(expectedPositionRating, actualPositionRating);
    }

    private void prepareEmptyBitSets(int boardSideLengthEight) {
        position = positionGenerator.generateEmptyPositionForBoardSide(boardSideLengthEight);
        whitePieces = position.getWhitePieces();
        blackPieces = position.getBlackPieces();
        kings = position.getKings();
    }

    private void createPositionRaterForBoardSideLength(int boardSideLength) {
        positionRater = new PositionRater(positionRaterSettings, boardSideLength);
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
    void shouldSeeThatMainDiagonalIsNotControlled() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_EIGHT);
        positionRaterSettings.setBonusForBeingCloserToPromotionLineActive(NOT_ACTiVE);

        whitePieces.set(12);
        whitePieces.set(28);
        blackPieces.set(24);
        kings.set(12);

        int whiteScore = (20 + 10); // 20 for king, 10 for checker,
        int blackScore = 10; // 10 for checker

        expectedPositionRating = whiteScore - blackScore;
    }

    @Test
    void shouldGivePointsForMaintainingMainDiagonal() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_EIGHT);
        positionRaterSettings.setBonusForBeingCloserToPromotionLineActive(NOT_ACTiVE);

        whitePieces.set(5);
        blackPieces.set(12);
        blackPieces.set(24);
        kings.set(12);

        int whiteScore = 10; // 10 for checker
        int blackScore = (10 + 20) + 5; // 10 for checker, 20 for kings, 5 for controlling main diagonal

        expectedPositionRating = whiteScore - blackScore;
    }

    @Test
    void shouldReturnZeroForStartingPositionOfBoardSizeEight() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);
        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_EIGHT);

        expectedPositionRating = 0;
    }

    @Test
    void shouldReturnProperRatingForDefaultSettings() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(15);
        whitePieces.set(33);
        blackPieces.set(5);
        blackPieces.set(16);
        kings.set(5);
        kings.set(16);

        int whiteRating = (10 * 2) + (2 + 6);   // 10 points for each checker, additional for rows being closer to promotion row
        int blackRating = (20 * 2) + (5);       // 20 points for each king, 5 points for controlling main diagonal

        expectedPositionRating = whiteRating - blackRating;
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
    @Test
    void shouldRatePositionAsWhiteVictory() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TEN);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_TEN);

        whitePieces.set(12);
        whitePieces.set(44);
        whitePieces.set(52);
        kings.set(52);

        expectedPositionRating = PositionRater.WHITE_WON;
    }

    @Test
    void shouldWorkProperlyWithSettingsOtherThanDefault() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TEN);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_TEN);
        positionRaterSettings.setPointsPerChecker(7);
        positionRaterSettings.setPointsPerKing(28);

        whitePieces.set(41);
        whitePieces.set(31);
        blackPieces.set(13);
        blackPieces.set(55);
        kings.set(55);

        int whiteScore = (7 * 2) + (4 + 6); // 7 for each checker, 4 and 6 for being close to promotion row
        int blackScore = (7 + 28) + 8 + 5; // 7 for checker, 28 for king, 8 for being closer to promotion row, 5 for controlling main row

        expectedPositionRating = whiteScore - blackScore;
    }

    @Test
    void shouldReturnZeroForStartingPositionOfBoardSizeTen() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TEN);
        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_TEN);

        expectedPositionRating = 0;
    }

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
    @Test
    void shouldSeeControllingMainDiagonalWithMultipleKings() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_TWELVE);
        positionRaterSettings.setBonusForBeingCloserToPromotionLineActive(NOT_ACTiVE);

        whitePieces.set(51);
        blackPieces.set(78);
        blackPieces.set(60);
        blackPieces.set(36);
        kings.set(78);
        kings.set(60);
        kings.set(36);

        int whiteScore = 10; // 10 for checker
        int blackScore = (20 * 3) + 5; // 20 for each king, 5 for controlling main diagonal

        expectedPositionRating = whiteScore - blackScore;
    }

    @Test
    void shouldRatePositionAsBlackVictory() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);
        prepareEmptyBitSets(BOARD_SIDE_LENGTH_TWELVE);

        blackPieces.set(83);
        blackPieces.set(43);
        blackPieces.set(18);
        kings.set(18);

        expectedPositionRating = PositionRater.BLACK_WON;
    }

    @Test
    void shouldReturnZeroForStartingPositionOfBoardSizeTwelve() {
        createPositionRaterForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);
        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_TWELVE);

        expectedPositionRating = 0;
    }

}