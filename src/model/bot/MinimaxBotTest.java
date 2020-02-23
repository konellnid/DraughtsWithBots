package model.bot;

import model.board.Move;
import model.board.Position;
import model.board.PositionGenerator;
import model.board.PositionOperator;
import model.bot.position_rater.PositionRater;
import model.bot.position_rater.PositionRaterSettings;
import model.move_finder.MoveFinderSettings;
import model.move_finder.PossibleMovesFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxBotTest {
    private static final int BOARD_SIDE_LENGTH_EIGHT = 8;
    private static final int BOARD_SIDE_LENGTH_TEN = 10;
    private static final int BOARD_SIDE_LENGTH_TWELVE = 12;
    private static final boolean EXCEEDING_IS_ALLOWED = true;
    private static final boolean EXCEEDING_IS_NOT_ALLOWED = false;
    private static final boolean IS_WHITE_MOVE = true;
    private static final boolean IS_BLACK_MOVE = true;
    private static final boolean FLYING_KING_IS_ENABLED = true;
    private static final boolean FLYING_KING_IS_DISABLED = false;
    private static final boolean CHECKERS_CAN_BEAT_BACKWARD = true;
    private static final boolean CHECKERS_CAN_NOT_BEAT_BACKWARD = false;

    private MinimaxBot minimaxBot;
    private MinimaxBotSettings minimaxBotSettings;
    private PositionOperator positionOperator;
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

        whitePieces = new BitSet();
        blackPieces = new BitSet();
        kings = new BitSet();
        position = new Position(whitePieces, blackPieces, kings);
    }

    void prepareObjectsForBoardSideLength(int boardSideLength, boolean isFlyingKingEnabled, boolean isCheckerBeatingBackwardsEnabled) {
        positionOperator = new PositionOperator(boardSideLength);
        positionRater = new PositionRater(positionRaterSettings, boardSideLength);
        MoveFinderSettings moveFinderSettings = new MoveFinderSettings(isFlyingKingEnabled, isCheckerBeatingBackwardsEnabled);
        possibleMovesFinder = new PossibleMovesFinder(moveFinderSettings, boardSideLength);
    }

    // TESTS FOR BOARD SIDE LENGTH = 9
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
    void shouldTakeControlOverMainDiagonal() {
        prepareObjectsForBoardSideLength(BOARD_SIDE_LENGTH_EIGHT, FLYING_KING_IS_ENABLED, CHECKERS_CAN_BEAT_BACKWARD);
        minimaxBotSettings = new MinimaxBotSettings(1, EXCEEDING_IS_ALLOWED, 0);
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