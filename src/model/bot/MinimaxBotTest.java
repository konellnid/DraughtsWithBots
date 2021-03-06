package model.bot;

import model.board.Move;
import model.board.Position;
import model.board.PositionGenerator;
import model.bot.position_rater.PositionRater;
import model.bot.position_rater.PositionRaterSettings;
import model.move_finder.MoveFinderSettings;
import model.move_finder.PossibleMovesFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxBotTest {
    private static final int BOARD_SIDE_LENGTH_EIGHT = 8;
    private static final int BOARD_SIDE_LENGTH_TEN = 10;
    private static final int BOARD_SIDE_LENGTH_TWELVE = 12;
    private static final boolean EXCEEDING_IS_ALLOWED = true;
    private static final boolean EXCEEDING_IS_NOT_ALLOWED = false;
    private static final boolean IS_WHITE_MOVE = true;
    private static final boolean IS_BLACK_MOVE = false;
    private static final boolean FLYING_KING_IS_ENABLED = true;
    private static final boolean FLYING_KING_IS_DISABLED = false;
    private static final boolean CHECKERS_CAN_BEAT_BACKWARD = true;
    private static final boolean CHECKERS_CAN_NOT_BEAT_BACKWARD = false;

    private MinimaxBot minimaxBot;
    private MinimaxBotSettings minimaxBotSettings;
    private PositionRaterSettings positionRaterSettings;
    private PositionRater positionRater;
    private PossibleMovesFinder possibleMovesFinder;

    private Position position;
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    @BeforeEach
    void setUp() {
        positionRaterSettings = new PositionRaterSettings();
    }

    private void generatePosition(int boardSideLength) {
        PositionGenerator positionGenerator = new PositionGenerator();
        position = positionGenerator.generateEmptyPositionForBoardSide(boardSideLength);

        whitePieces = position.getWhitePieces();
        blackPieces = position.getBlackPieces();
        kings = position.getKings();
    }

    void prepareBasicObjects(int boardSideLength, boolean isFlyingKingEnabled, boolean isCheckerBeatingBackwardsEnabled) {
        positionRater = new PositionRater(positionRaterSettings, boardSideLength);
        MoveFinderSettings moveFinderSettings = new MoveFinderSettings(isFlyingKingEnabled, isCheckerBeatingBackwardsEnabled);
        possibleMovesFinder = new PossibleMovesFinder(moveFinderSettings, boardSideLength);
    }

    // TESTS FOR BOARD SIDE LENGTH = 8
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
    void shouldMoveToBlockEnemyPath() {
        generatePosition(BOARD_SIDE_LENGTH_EIGHT);
        prepareBasicObjects(BOARD_SIDE_LENGTH_EIGHT, FLYING_KING_IS_ENABLED, CHECKERS_CAN_BEAT_BACKWARD);
        minimaxBotSettings = new MinimaxBotSettings(3, EXCEEDING_IS_NOT_ALLOWED);
        minimaxBot = new MinimaxBot(minimaxBotSettings, positionRater, BOARD_SIDE_LENGTH_EIGHT, possibleMovesFinder);

        whitePieces.set(20);

        blackPieces.set(33);

        List<Move> possibleMoves = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_MOVE);

        // this move makes white certain about winning
        Move expectedMove = new Move(20, 24);

        Move actualMove = minimaxBot.choseAMoveFrom(possibleMoves, position);

        assertEquals(expectedMove, actualMove);
    }

    @Test
    void shouldSeeExceededBeating() {
        generatePosition(BOARD_SIDE_LENGTH_EIGHT);
        prepareBasicObjects(BOARD_SIDE_LENGTH_EIGHT, FLYING_KING_IS_ENABLED, CHECKERS_CAN_BEAT_BACKWARD);
        minimaxBotSettings = new MinimaxBotSettings(1, EXCEEDING_IS_ALLOWED);
        minimaxBot = new MinimaxBot(minimaxBotSettings, positionRater, BOARD_SIDE_LENGTH_EIGHT, possibleMovesFinder);

        whitePieces.set(20);
        kings.set(20);

        blackPieces.set(24);
        blackPieces.set(25);
        blackPieces.set(36);
        kings.set(36);


        List<Move> possibleMoves = possibleMovesFinder.getAvailableMovesFrom(position, IS_WHITE_MOVE);

        // white should give up the main diagonal to avoid getting beaten by black king at 36
        List<Move> expectedMoves = new LinkedList<>();
        expectedMoves.add(new Move(20, 25, 30));
        expectedMoves.add(new Move(20, 25, 35));

        Move actualMove = minimaxBot.choseAMoveFrom(possibleMoves, position);

        assertTrue(expectedMoves.contains(actualMove));
    }

    @Test
    void shouldTakeControlOverMainDiagonal() {
        generatePosition(BOARD_SIDE_LENGTH_EIGHT);
        prepareBasicObjects(BOARD_SIDE_LENGTH_EIGHT, FLYING_KING_IS_ENABLED, CHECKERS_CAN_BEAT_BACKWARD);
        minimaxBotSettings = new MinimaxBotSettings(1, EXCEEDING_IS_ALLOWED);
        minimaxBot = new MinimaxBot(minimaxBotSettings, positionRater, BOARD_SIDE_LENGTH_EIGHT, possibleMovesFinder);

        whitePieces.set(14);
        blackPieces.set(30);
        kings.set(30);

        List<Move> possibleMoves = possibleMovesFinder.getAvailableMovesFrom(position, IS_BLACK_MOVE);

        Move expectedMove = new Move(30, 20); // controlling main diagonal would give +5 for black score

        Move actualMove = minimaxBot.choseAMoveFrom(possibleMoves, position);

        assertEquals(expectedMove, actualMove);
    }


}