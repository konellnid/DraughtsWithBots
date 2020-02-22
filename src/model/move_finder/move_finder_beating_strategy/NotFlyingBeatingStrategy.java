package model.move_finder.move_finder_beating_strategy;

import model.board.Move;
import model.move_finder.DirectionsValueBySize;

public abstract class NotFlyingBeatingStrategy extends MoveFinderBeatingStrategy {
    public NotFlyingBeatingStrategy(DirectionsValueBySize directions) {
        super(directions);
    }

    @Override
    void checkForBeatingMoveInDirection(Move move, int direction) {
        int pieceTileNumber = move.getLastPositionOfThePiece();
        int expectedEnemyTile = pieceTileNumber + direction;
        int expectedFreeTile = expectedEnemyTile + direction;

        if (currentBasicBitSets.getEnemyPieces().get(expectedEnemyTile)) {
            if (currentBasicBitSets.getFreeTileNumbers().get(expectedFreeTile)) {
                currentBasicBitSets.getEnemyPieces().clear(expectedEnemyTile);

                Move foundMove = move.getCopy();
                foundMove.addNewTileNumberToMoveSequence(expectedEnemyTile);
                foundMove.addNewTileNumberToMoveSequence(expectedFreeTile);

                updateFoundMoves(foundMove);

                findBeatingMovesFromMoveSequence(foundMove);

                currentBasicBitSets.getEnemyPieces().set(expectedEnemyTile);
            }
        }
    }
}
