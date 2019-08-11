package model.game;

import model.board.Move;
import model.board.Position;

import java.util.BitSet;

public class GameStorage {
    private static final int MAX_MOVES_TILL_DRAW = 30;

    private int movesTillDraw;
    private Position position;
    private boolean isWhiteTurn;

    GameStorage() {
        movesTillDraw = MAX_MOVES_TILL_DRAW;
        position = new Position();
        position.generateStartingPosition();
        isWhiteTurn = true;
    }

    int getMovesTillDraw() {
        return movesTillDraw;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }

    boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    void updateGameStorageWithMove(Move move) {
        updateMovesTillDraw(move);
        position.performMoveOnPosition(move, isWhiteTurn);
        //isWhiteTurn = !isWhiteTurn;
    }

    private void updateMovesTillDraw(Move move) {
        if (move.isBeatingSequence()) movesTillDraw = MAX_MOVES_TILL_DRAW;
        else {
            int startingPosition = move.getStartingPositionOfThePiece();
            BitSet kingBitSet = position.getKings();

            if (kingBitSet.get(startingPosition)) movesTillDraw--;
            else movesTillDraw = MAX_MOVES_TILL_DRAW;
        }
    }

    boolean pieceGetsPromotedDuringMove(Move move) {
        return position.shouldPieceBePromoted(move.getLastPositionOfThePiece(), isWhiteTurn);
    }
}
