package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class PositionOperatorTest {
    private static final boolean IS_WHITE_TURN = true;
    private static final boolean IS_BLACK_TURN = false;

    private PositionOperator positionOperator;
    private Position testPosition;
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    @BeforeEach
    void setUp() {
        positionOperator = new PositionOperator(8);
        PositionGenerator positionGenerator = new PositionGenerator();
        testPosition = positionGenerator.generateEmptyPositionForBoardSide(8);
        whitePieces = new BitSet(45);
        blackPieces = new BitSet(45);
        kings = new BitSet(45);
    }

    @Test
    void shouldMakeASimpleMoveOnAPosition() {
        whitePieces.set(10);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        positionOperator.performMoveOnPosition(testPosition, new Move(10, 15), IS_WHITE_TURN);

        assertTrue(testPosition.getWhitePieces().get(15));
        assertFalse(testPosition.getWhitePieces().get(10));
    }

    @Test
    void shouldProperlyMakeSingleBeatingMove() {
        whitePieces.set(20);
        blackPieces.set(25);
        Move beatingMove = new Move(20, 25, 30);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        positionOperator.performMoveOnPosition(testPosition, beatingMove, IS_WHITE_TURN);
        assertTrue(testPosition.getWhitePieces().get(30));
        assertFalse(testPosition.getBlackPieces().get(25));
    }

    @Test
    void shouldProperlyMakeBeatingMove() {
        whitePieces.set(16);
        blackPieces.set(21);
        blackPieces.set(30);
        blackPieces.set(29);
        blackPieces.set(19);
        Move beatingMove = new Move(16, 21, 26, 30, 34, 29, 24, 19, 14);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        positionOperator.performMoveOnPosition(testPosition, beatingMove, IS_WHITE_TURN);
        assertTrue(testPosition.getWhitePieces().get(14));
        assertFalse(testPosition.getBlackPieces().get(21));
        assertFalse(testPosition.getBlackPieces().get(30));
        assertFalse(testPosition.getBlackPieces().get(29));
        assertFalse(testPosition.getBlackPieces().get(19));
    }

    @Test
    void shouldProperlyMakeKingMove() {
        whitePieces.set(17);
        kings.set(17);
        Move kingMove = new Move(17, 33);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        positionOperator.performMoveOnPosition(testPosition, kingMove, IS_WHITE_TURN);
        assertTrue(testPosition.getWhitePieces().get(33));
        assertTrue(testPosition.getKings().get(33));
        assertFalse(testPosition.getBlackPieces().get(17));
        assertFalse(testPosition.getKings().get(17));
    }

    @Test
    void shouldPieceBePromotedAfterTheMove() {
        whitePieces.set(35);
        Move promotionMove = new Move(35, 39);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        positionOperator.performMoveOnPosition(testPosition, promotionMove, IS_WHITE_TURN);
        assertTrue(testPosition.getWhitePieces().get(39));
        assertTrue(testPosition.getKings().get(39));
    }

    @Test
    void shouldThrowExceptionOnMovingNonExistingPiece() {
        whitePieces.set(30);
        Move invalidMove = new Move(21, 26);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        assertThrows(IllegalArgumentException.class, () -> positionOperator.performMoveOnPosition(testPosition, invalidMove, IS_WHITE_TURN));
    }

    @Test
    void shouldThrowExceptionForIllegalMove() {
        whitePieces.set(30);
        Move invalidMove = new Move(30, 20);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        assertThrows(IllegalArgumentException.class, () -> positionOperator.performMoveOnPosition(testPosition, invalidMove, IS_WHITE_TURN));
    }

    @Test
    void shouldNotPieceBePromotedAfterTheMove() {
        blackPieces.set(20);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        Move notPromotingMove = new Move(20, 24);
        positionOperator.performMoveOnPosition(testPosition, notPromotingMove, IS_WHITE_TURN);
        assertFalse(testPosition.isKing(20));
    }

    @Test
    void shouldKingRemainPromotedAfterTheMove() {
        whitePieces.set(39);
        kings.set(39);
        testPosition.generatePosition(whitePieces, blackPieces, kings);
        Move kingMove = new Move(39, 24);
        positionOperator.performMoveOnPosition(testPosition, kingMove, IS_WHITE_TURN);

        assertFalse(testPosition.isKing(39));
        assertTrue(testPosition.isKing(24));
    }

}
