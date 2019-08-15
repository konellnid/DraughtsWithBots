package model.bot;

import model.board.Position;
import model.board.PositionGenerator;
import model.board.PositionOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxBotTest {
    private final static boolean EXCEEDING_IS_ALLOWED = true;
    private final static boolean EXCEEDING_IS_NOT_ALLOWED = false;

    private MinimaxBot minimaxBot;
    private MinimaxBotSettings minimaxBotSettings;
    private PositionOperator positionOperator;
    private PositionGenerator positionGenerator;
    private Position position;
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    @BeforeEach
    void setUp() {
    }

    void prepareObjectsForBoardSideLength(int boardSideLength) {
        positionOperator = new PositionOperator(boardSideLength);
        position = positionGenerator.generateEmptyPositionForBoardSide(boardSideLength);
        whitePieces = position.getWhitePieces();
        blackPieces = position.getBlackPieces();
        kings = position.getKings();

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
    void shouldTakeControlOver() {

    }
}