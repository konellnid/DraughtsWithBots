package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    private Position testPosition;

    @BeforeEach
    void setUp() {
        testPosition = new Position();
    }

    @Test
    void performMoveOnPositionSingleMoveTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(10);
        testPosition.generatePosition(whitePieces, new BitSet(45), new BitSet(45));
        testPosition.performMoveOnPosition(new Move(10, 15), true);
        assertTrue(testPosition.getWhitePieces().get(15));
        assertFalse(testPosition.getWhitePieces().get(10));
    }

    @Test
    void performMoveOnPositionSingleBeatingMoveTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(20);
        BitSet blackPieces = new BitSet(45);
        blackPieces.set(25);
        Move beatingMove = new Move(20, 25, 30);
        testPosition.generatePosition(whitePieces, blackPieces, new BitSet(45));
        testPosition.performMoveOnPosition(beatingMove, true);
        assertTrue(testPosition.getWhitePieces().get(30));
        assertFalse(testPosition.getBlackPieces().get(25));
    }

    @Test
    void performMoveOnPositionMultipleBeatingsMoveTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(16);
        BitSet blackPieces = new BitSet(45);
        blackPieces.set(21);
        blackPieces.set(30);
        blackPieces.set(29);
        blackPieces.set(19);
        Move beatingMove = new Move(16, 21, 26, 30, 34, 29, 24, 19, 14);
        testPosition.generatePosition(whitePieces, blackPieces, new BitSet(45));
        testPosition.performMoveOnPosition(beatingMove, true);
        assertTrue(testPosition.getWhitePieces().get(14));
        assertFalse(testPosition.getBlackPieces().get(30));
    }

    @Test
    void performMoveOnPositionKingMoveTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(17);
        BitSet kings = new BitSet(45);
        kings.set(17);
        Move kingMove = new Move(17, 33);
        testPosition.generatePosition(whitePieces, new BitSet(45), kings);
        testPosition.performMoveOnPosition(kingMove, true);
        assertTrue(testPosition.getWhitePieces().get(33));
        assertTrue(testPosition.getKings().get(33));
        assertFalse(testPosition.getBlackPieces().get(17));
        assertFalse(testPosition.getKings().get(17));
    }

    @Test
    void performMoveOnPositionPromotionTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(35);
        Move promotionMove = new Move(35, 39);
        testPosition.generatePosition(whitePieces, new BitSet(45), new BitSet(45));
        testPosition.performMoveOnPosition(promotionMove, true);
        assertTrue(testPosition.getWhitePieces().get(39));
        assertTrue(testPosition.getKings().get(39));
    }

    @Test
    void performMoveOnPositionNotExistingPieceTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(30);
        Move invalidMove = new Move(21, 26);
        testPosition.generatePosition(whitePieces, new BitSet(45), new BitSet(45));
        assertThrows(IllegalArgumentException.class, () -> testPosition.performMoveOnPosition(invalidMove, true));
    }

    @Test
    void performMoveOnPositionIllegalMoveTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(30);
        Move invalidMove = new Move(30, 20);
        testPosition.generatePosition(whitePieces, new BitSet(45), new BitSet(45));
        assertThrows(IllegalArgumentException.class, () -> testPosition.performMoveOnPosition(invalidMove, true));
    }

    @Test
    void shouldPieceBePromotedPieceInPromotionZoneTest() {
        BitSet whitePieces = new BitSet(45);
        whitePieces.set(39);
        testPosition.generatePosition(whitePieces, new BitSet(45), new BitSet(45));
        assertTrue(testPosition.shouldPieceBePromoted(39, true));
    }

    @Test
    void shouldPieceBePromotedPieceOutsideOfPromotionZoneTest() {
        BitSet blackPieces = new BitSet(45);
        blackPieces.set(20);
        testPosition.generatePosition(new BitSet(45), blackPieces, new BitSet(45));
        assertFalse(testPosition.shouldPieceBePromoted(20, false));
    }

    @Test
    void shouldPieceBePromotedNotExistingPieceTest() {
        testPosition.generatePosition(new BitSet(45), new BitSet(45), new BitSet(45));
        assertThrows(IllegalArgumentException.class, () -> testPosition.shouldPieceBePromoted(20, true));
    }

}
