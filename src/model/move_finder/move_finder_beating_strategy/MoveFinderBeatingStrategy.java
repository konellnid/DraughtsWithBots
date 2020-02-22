package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.BasicBitSets;

import java.util.LinkedList;
import java.util.List;

public abstract class MoveFinderBeatingStrategy {
    protected BasicBitSets currentBasicBitSets;
    private List<Move> foundMoves;
    private int longestBeatingFoundSize;

    public List<Move> checkForBeatingMoves(int startingTileNumber, BasicBitSets basicBitSets) {
        foundMoves = new LinkedList<>();
        currentBasicBitSets = basicBitSets;
        longestBeatingFoundSize = 0;

        Move startingMove = new Move(startingTileNumber);
        findBeatingMovesFromMoveSequence(startingMove);

        return foundMoves;
    }

    abstract void findBeatingMovesFromMoveSequence(Move move);
    abstract void checkForBeatingMoveInDirection();

    void updateFoundMoves(Move move) {
        if (move.size() > longestBeatingFoundSize) {
            foundMoves.clear();
            foundMoves.add(move);
            longestBeatingFoundSize = move.size();
        } else if (move.size() == longestBeatingFoundSize) {
            foundMoves.add(move);
        }
    }
}
