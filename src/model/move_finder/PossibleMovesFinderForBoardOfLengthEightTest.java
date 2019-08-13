package model.move_finder;

import model.board.Move;
import model.board.Position;
import model.board.PositionGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMovesFinderForBoardOfLengthEightTest {
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
        possibleMovesFinder = new PossibleMovesFinder(boardSideLength);
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
    void shouldProperlyFindBothBeatingSequences() {
        prepareBitSetsAndPositionFinderForBoardSideLength(BOARD_SIDE_LENGTH_TEN);

        blackPieces.set(25);
        blackPieces.set(42);
        blackPieces.set(46);

        whitePieces.set(34);
        whitePieces.set(13);

        expectedMoveList.add(new Move(34, 46, 52, 42, 37, 25, 19));
        expectedMoveList.add(new Move(13, 25, 37, 42, 52, 46, 40));

        actualMoveList = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_TURN);

        sortBothLists();

        assertEquals(expectedMoveList, actualMoveList);
    }



}