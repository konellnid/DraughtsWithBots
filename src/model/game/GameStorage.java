package model.game;

import model.board.Move;
import model.board.Position;
import model.board.PositionGenerator;
import model.board.PositionOperator;

import java.util.BitSet;

public class GameStorage {
    private static final int MAX_MOVES_TILL_DRAW = 30;

    private int movesTillDraw;
    private Position position;
    private PositionOperator positionOperator;
    private boolean isWhiteTurn;

    GameStorage(int boardSideLength) {
        movesTillDraw = MAX_MOVES_TILL_DRAW;
        PositionGenerator positionGenerator = new PositionGenerator();
        position = positionGenerator.generateStartingPositionForBoardOfSideLength(boardSideLength);
        positionOperator = new PositionOperator(boardSideLength);
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
        positionOperator.performMoveOnPosition(position, move, isWhiteTurn);
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

    boolean pieceGotPromoted() {
        return positionOperator.pieceWasPromotedDuringLastMove();
    }
}
