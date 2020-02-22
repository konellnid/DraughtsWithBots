package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.DirectionsValueBySize;

public class FlyingBeating extends MoveFinderBeatingStrategy {
    public FlyingBeating(DirectionsValueBySize directions) {
        super(directions);
    }

    @Override
    void findBeatingMovesFromMoveSequence(Move move) {
        checkForBeatingMoveInDirection(move, directions.upperRight);
        checkForBeatingMoveInDirection(move, directions.upperLeft);
        checkForBeatingMoveInDirection(move, directions.lowerRight);
        checkForBeatingMoveInDirection(move, directions.lowerLeft);
    }

    @Override
    void checkForBeatingMoveInDirection(Move move, int direction) {
        int kingTileNumber = move.getLastPositionOfThePiece();
        int nextNotFreeTile = skipFreeTiles(kingTileNumber, direction);

        if (currentBasicBitSets.getEnemyPieces().get(nextNotFreeTile)) {
            currentBasicBitSets.getEnemyPieces().clear(nextNotFreeTile);

            int expectedFreeTile = nextNotFreeTile + direction;

            while (currentBasicBitSets.getFreeTileNumbers().get(expectedFreeTile)) {
                Move foundMove = move.getCopy();
                foundMove.addNewTileNumberToMoveSequence(nextNotFreeTile);
                foundMove.addNewTileNumberToMoveSequence(expectedFreeTile);

                updateFoundMoves(foundMove);

                findBeatingMovesFromMoveSequenceFromDirection(foundMove, direction);

                expectedFreeTile += direction;
            }
            currentBasicBitSets.getEnemyPieces().set(nextNotFreeTile);
        }
    }

    private void findBeatingMovesFromMoveSequenceFromDirection(Move move, int comingDirection) {
        if (comingDirection != directions.upperRight) checkForBeatingMoveInDirection(move, directions.upperRight);
        if (comingDirection != directions.upperLeft) checkForBeatingMoveInDirection(move, directions.upperLeft);
        if (comingDirection != directions.lowerRight) checkForBeatingMoveInDirection(move, directions.lowerRight);
        if (comingDirection != directions.lowerLeft) checkForBeatingMoveInDirection(move, directions.lowerLeft);
    }

    private int skipFreeTiles(int kingTileNumber, int direction) {
        int expectedNotFreeTileNumber = kingTileNumber + direction;

        while (currentBasicBitSets.getFreeTileNumbers().get(expectedNotFreeTileNumber)) {
            expectedNotFreeTileNumber += direction;
        }
        return expectedNotFreeTileNumber;
    }
}
