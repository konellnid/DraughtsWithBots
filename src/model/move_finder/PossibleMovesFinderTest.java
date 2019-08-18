package model.move_finder;

import model.board.Move;
import model.board.Position;
import model.board.PositionGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMovesFinderTest {
    private static final int BOARD_SIDE_LENGTH_EIGHT = 8;
    private static final int BOARD_SIDE_LENGTH_TEN = 10;
    private static final int BOARD_SIDE_LENGTH_TWELVE = 12;
    private static final boolean IS_WHITE_TURN = true;
    private static final boolean IS_BLACK_TURN = false;

    private PossibleMovesFinder possibleMovesFinder;
    private List<Move> expectedMoveList;
    private Position position;
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;
    private List<Move> actualMoveList;
    private PositionGenerator positionGenerator;

    @BeforeEach
    void init() {
        expectedMoveList = new LinkedList<>();
        actualMoveList = new LinkedList<>();
        positionGenerator = new PositionGenerator();
    }

    void prepareBitSetsAndPositionFinderForBoardSideLength(int boardSideLength) {
        // before each test, the position is empty (all bits in BitSets are set to 0)
        MoveFinderSettings moveFinderSettings = new MoveFinderSettings(true, true);
        possibleMovesFinder = new PossibleMovesFinder(moveFinderSettings, boardSideLength);
        position = positionGenerator.generateEmptyPositionForBoardSide(boardSideLength);
        whitePieces = position.getWhitePieces();
        blackPieces = position.getBlackPieces();
        kings = position.getKings();
    }

    private void sortBothLists() {
        Comparator<Move> moveComparator = (o1, o2) -> {
            List<Integer> o1MoveSequence = o1.getMoveSequence();
            List<Integer> o2MoveSequence = o2.getMoveSequence();

            for (int i = 0; i < o1MoveSequence.size() && i < o2MoveSequence.size(); i++) {
                if (o1MoveSequence.get(i) < o2MoveSequence.get(i)) return -1;
                if (o1MoveSequence.get(i) > o2MoveSequence.get(i)) return 1;

            }
            return 0;
        };

        expectedMoveList.sort(moveComparator);
        actualMoveList.sort(moveComparator);
    }


    // TESTS FOR BOARD SIDE LENGTH = 8
    /* Board of sideLength = 8: total BitSet size = 45
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
    void shouldNotAllowCheckerBeatingBackwards() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        MoveFinderSettings customMoveFinderSettings = new MoveFinderSettings(true, false);
        possibleMovesFinder = new PossibleMovesFinder(customMoveFinderSettings, BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(20);
        whitePieces.set(19);
        blackPieces.set(24);

        expectedMoveList.add(new Move(24, 29));
        expectedMoveList.add(new Move(24, 28));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldStopCheckerAtPromotionLineWithNoBackwardBeatingsEnabled() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        MoveFinderSettings customMoveFinderSettings = new MoveFinderSettings(true, false);
        possibleMovesFinder = new PossibleMovesFinder(customMoveFinderSettings, BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(25);
        whitePieces.set(34);
        whitePieces.set(33);

        blackPieces.set(20);

        expectedMoveList.add(new Move(20, 25, 30, 34, 38));
        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }


    @Test
    void shouldReturnAllPossibleMovesFromStartingPosition() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        expectedMoveList.add(new Move(17, 21));
        expectedMoveList.add(new Move(16, 21));
        expectedMoveList.add(new Move(16, 20));
        expectedMoveList.add(new Move(15, 20));
        expectedMoveList.add(new Move(15, 19));
        expectedMoveList.add(new Move(14, 19));
        expectedMoveList.add(new Move(14, 18));

        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_EIGHT);
        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertAll("Move lists from starting position are not the same",
                () -> assertEquals(expectedMoveList.size(), actualMoveList.size(), "Move lists are not the same size"),
                () -> assertEquals(expectedMoveList, actualMoveList, "Move lists contain different Moves")
        );

    }


    @Test
    void shouldReturnProperNonBeatingMovesForKing() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        blackPieces.set(30);
        kings.set(30);

        whitePieces.set(26);
        whitePieces.set(15);
        whitePieces.set(10);


        expectedMoveList.add(new Move(30, 35));
        expectedMoveList.add(new Move(30, 34));
        expectedMoveList.add(new Move(30, 38));
        expectedMoveList.add(new Move(30, 25));
        expectedMoveList.add(new Move(30, 20));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }


    @Test
    void shouldPickTheLongerBeatingSequenceForKing() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(16);

        blackPieces.set(19);
        blackPieces.set(20); // 19 and 20 are beaten in a shorter sequence, that SHOULD NOT be chosen
        blackPieces.set(21);
        blackPieces.set(34);
        blackPieces.set(23);

        kings.set(16);
        kings.set(19);
        kings.set(21);

        expectedMoveList.add(new Move(16, 21, 26, 34, 38, 23, 18));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldGiveTwoPossibleMoveSequencesOneStartingWithKingAndOneWithChecker() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        blackPieces.set(11);
        blackPieces.set(16);
        kings.set(16);

        whitePieces.set(15);
        whitePieces.set(23);
        whitePieces.set(32);
        whitePieces.set(21);
        whitePieces.set(30);

        expectedMoveList.add(new Move(11, 15, 19, 23, 27, 32, 37));
        expectedMoveList.add(new Move(16, 21, 26, 30, 38, 23, 18));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }


    @Test
    void shouldGiveEmptyListOfMoveSequencesDueToBlockedPieces() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(27);
        blackPieces.set(32);
        blackPieces.set(37);

        whitePieces.set(35);
        blackPieces.set(39);

        whitePieces.set(8);
        kings.set(8);
        blackPieces.set(12);
        blackPieces.set(16);

        whitePieces.set(10);
        blackPieces.set(14);
        blackPieces.set(18);
        blackPieces.set(15);
        blackPieces.set(20);

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        assertEquals(0, actualMoveList.size());
    }


    @Test
    void shouldProperlyAllowCheckerToBeatBothBackwardsAndForward() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        blackPieces.set(16);

        whitePieces.set(11);
        whitePieces.set(10);
        whitePieces.set(19);
        whitePieces.set(29);
        whitePieces.set(30);

        expectedMoveList.add(new Move(16, 11, 6, 10, 14, 19, 24, 29, 34, 30, 26));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }


    @Test
    void shouldNotAllowKingToGoInCompletelyOppositeDirectionDuringBeating() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(8);
        kings.set(8);

        blackPieces.set(12);
        blackPieces.set(30);
        blackPieces.set(15);

        expectedMoveList.add(new Move(8, 12, 20, 30, 35));
        expectedMoveList.add(new Move(8, 12, 20, 15, 10));
        expectedMoveList.add(new Move(8, 12, 20, 15, 5));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldNotGoIntoAnInfinitePromotedBeatingLoop() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        whitePieces.set(7);
        kings.set(7);

        blackPieces.set(19);
        blackPieces.set(28);
        blackPieces.set(30);
        blackPieces.set(16);

        expectedMoveList.add(new Move(7, 19, 23, 28, 38, 30, 26, 16, 11));
        expectedMoveList.add(new Move(7, 19, 23, 28, 38, 30, 26, 16, 6));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldNotGoIntoAnInfiniteStandardBeatingLoop() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT);

        blackPieces.set(20);

        whitePieces.set(24);
        whitePieces.set(33);
        whitePieces.set(34);
        whitePieces.set(25);

        expectedMoveList.add(new Move(20, 24, 28, 33, 38, 34, 30, 25, 20));
        expectedMoveList.add(new Move(20, 25, 30, 34, 38, 33, 28, 24, 20));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    // ________________________________________________________________________
    // TESTS FOR BOARD SIDE LENGTH = 10
    /*
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
     */

    @Test
    void shouldFindProperMovesForKing() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        whitePieces.set(35);
        whitePieces.set(40);
        whitePieces.set(8);

        blackPieces.set(20);

        kings.set(20);

        expectedMoveList.add(new Move(20, 25));
        expectedMoveList.add(new Move(20, 30));
        expectedMoveList.add(new Move(20, 14));
        expectedMoveList.add(new Move(20, 15));
        expectedMoveList.add(new Move(20, 10));
        expectedMoveList.add(new Move(20, 26));
        expectedMoveList.add(new Move(20, 32));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldNotAllowInfiniteBeating() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        whitePieces.set(30);
        whitePieces.set(41);
        whitePieces.set(42);
        whitePieces.set(31);

        blackPieces.set(25);

        expectedMoveList.add(new Move(25, 30, 35, 41, 47, 42, 37, 31, 25));
        expectedMoveList.add(new Move(25, 31, 37, 42, 47, 41, 35, 30, 25));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

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
    void shouldNotAllowFlyingMovesForKing() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        MoveFinderSettings customMoveFinderSettings = new MoveFinderSettings(false, true);
        possibleMovesFinder = new PossibleMovesFinder(customMoveFinderSettings, BOARD_SIDE_LENGTH_TEN);

        whitePieces.set(19);
        kings.set(19);
        blackPieces.set(22);

        expectedMoveList.add(new Move(19, 25));
        expectedMoveList.add(new Move(19, 24));
        expectedMoveList.add(new Move(19, 13));
        expectedMoveList.add(new Move(19, 14));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldFindAllPossibleMovesForStartingPosition() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_TEN);

        expectedMoveList.add(new Move(22, 28));
        expectedMoveList.add(new Move(23, 28));
        expectedMoveList.add(new Move(23, 29));
        expectedMoveList.add(new Move(24, 29));
        expectedMoveList.add(new Move(24, 30));
        expectedMoveList.add(new Move(25, 30));
        expectedMoveList.add(new Move(25, 31));
        expectedMoveList.add(new Move(26, 31));
        expectedMoveList.add(new Move(26, 32));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldProperlyFindBothBeatingSequencesForKings() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        blackPieces.set(25);
        blackPieces.set(42);
        blackPieces.set(46);

        whitePieces.set(34);
        whitePieces.set(13);

        kings.set(34);
        kings.set(13);

        expectedMoveList.add(new Move(34, 46, 52, 42, 37, 25, 19));
        expectedMoveList.add(new Move(13, 25, 37, 42, 52, 46, 40));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    // ________________________________________________________________________
    // TESTS FOR BOARD SIDE LENGTH = 12
    /*
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

    @Test
    void shouldFindAllBeatingsForCheckers() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);

        blackPieces.set(29);
        blackPieces.set(41);
        blackPieces.set(43);
        blackPieces.set(44);

        whitePieces.set(36);
        whitePieces.set(23);

        expectedMoveList.add(new Move(36, 43, 50, 44, 38));
        expectedMoveList.add(new Move(23, 29, 35, 41, 47));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldFindAllBeatingsForKing() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);

        whitePieces.set(41);
        whitePieces.set(72);
        whitePieces.set(66);
        whitePieces.set(37);

        blackPieces.set(29);

        kings.set(29);

        expectedMoveList.add(new Move(29, 41, 59, 66, 73, 37, 31));
        expectedMoveList.add(new Move(29, 41, 59, 66, 73, 37, 25));
        expectedMoveList.add(new Move(29, 41, 65, 72, 79, 37, 31));
        expectedMoveList.add(new Move(29, 41, 65, 72, 79, 37, 25));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }

    @Test
    void shouldFindAllPossibleMovesFromStartingPosition() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TWELVE);

        position = positionGenerator.generateStartingPositionForBoardOfSideLength(BOARD_SIDE_LENGTH_TWELVE);

        expectedMoveList.add(new Move(33, 39));
        expectedMoveList.add(new Move(33, 40));
        expectedMoveList.add(new Move(34, 40));
        expectedMoveList.add(new Move(34, 41));
        expectedMoveList.add(new Move(35, 41));
        expectedMoveList.add(new Move(35, 42));
        expectedMoveList.add(new Move(36, 42));
        expectedMoveList.add(new Move(36, 43));
        expectedMoveList.add(new Move(37, 43));
        expectedMoveList.add(new Move(37, 44));
        expectedMoveList.add(new Move(38, 44));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }


}