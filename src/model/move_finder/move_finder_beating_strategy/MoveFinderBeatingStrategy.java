package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.BasicBitSets;
import model.move_finder.DirectionsValueBySize;

import java.util.LinkedList;
import java.util.List;

public abstract class MoveFinderBeatingStrategy {
    protected BasicBitSets currentBasicBitSets;
    private List<Move> foundMoves;
    private int longestBeatingFoundSize;
    DirectionsValueBySize directions;
    int frontLeftDirectionValue;
    int frontRightDirectionValue;

    public MoveFinderBeatingStrategy(DirectionsValueBySize directions) {
        this.directions = directions;
    }

    public List<Move> checkForBeatingMoves(int startingTileNumber, BasicBitSets basicBitSets) {
        foundMoves = new LinkedList<>();
        currentBasicBitSets = basicBitSets;
        longestBeatingFoundSize = 0;

        currentBasicBitSets.getFreeTileNumbers().set(startingTileNumber);

        Move startingMove = new Move(startingTileNumber);
        findBeatingMovesFromMoveSequence(startingMove);

        currentBasicBitSets.getFreeTileNumbers().clear(startingTileNumber);

        return foundMoves;
    }

    void updateFoundMoves(Move move) {
        if (move.size() > longestBeatingFoundSize) {
            foundMoves.clear();
            foundMoves.add(move);
            longestBeatingFoundSize = move.size();
        } else if (move.size() == longestBeatingFoundSize) {
            foundMoves.add(move);
        }
    }

    public void setForwardDirection(boolean isWhiteMove) {
        if (isWhiteMove) {
            frontLeftDirectionValue = directions.upperLeft;
            frontRightDirectionValue = directions.upperRight;
        } else {
            frontLeftDirectionValue = directions.lowerLeft;
            frontRightDirectionValue = directions.lowerRight;
        }
    }

    abstract void findBeatingMovesFromMoveSequence(Move move);

    abstract void checkForBeatingMoveInDirection(Move move, int direction);
}
