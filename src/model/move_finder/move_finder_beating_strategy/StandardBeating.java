package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.DirectionsValueBySize;

public class StandardBeating extends NotFlyingBeatingStrategy {
    public StandardBeating(DirectionsValueBySize directions) {
        super(directions);
    }

    @Override
    void findBeatingMovesFromMoveSequence(Move move) {
        checkForBeatingMoveInDirection(move, directions.upperRight);
        checkForBeatingMoveInDirection(move, directions.upperLeft);
        checkForBeatingMoveInDirection(move, directions.lowerRight);
        checkForBeatingMoveInDirection(move, directions.lowerLeft);
    }
}
