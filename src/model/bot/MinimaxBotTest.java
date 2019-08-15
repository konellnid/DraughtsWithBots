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

    @Test
    void choseAMoveFrom() {

    }
}