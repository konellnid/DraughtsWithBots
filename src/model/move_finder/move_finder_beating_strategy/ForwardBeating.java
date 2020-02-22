package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.DirectionsValueBySize;

public class ForwardBeating extends NotFlyingBeatingStrategy {
    public ForwardBeating(DirectionsValueBySize directions) {
        super(directions);
    }

    @Override
    void findBeatingMovesFromMoveSequence(Move move) {
        checkForBeatingMoveInDirection(move, frontLeftDirectionValue);
        checkForBeatingMoveInDirection(move, frontRightDirectionValue);
    }
}
