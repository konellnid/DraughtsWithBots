package model.move_finder;

import model.board.Move;
import model.board.Position;

import java.util.*;

/*  Board of sideLength = 8: total BitSet size = 45
        |  39  38  37  36|
        |35  34  33  32  |
        |  30  29  28  27|
        |26  25  24  23  |
        |  21  20  19  18|
        |17  16  15  14  |
        |  12  11  10  09|
        |08  07  06  05  |
*/
public class PossibleMovesFinder {
    private static final int UPPER_RIGHT = 4;
    private static final int UPPER_LEFT = 5;
    private static final int LOWER_RIGHT = -5;
    private static final int LOWER_LEFT = -4;

    private int currentBeatingLength;
    private BitSet freeTileNumbers;
    private BitSet ownCheckers;
    private BitSet ownKings;
    private BitSet enemyPieces;
    private BitwiseOperator bitwiseOperator;
    private List<Move> availableMoves;
    private boolean isWhiteMove;


    public PossibleMovesFinder() {
        bitwiseOperator = new BitwiseOperator();
    }
    
    public List<Move> getAvailableMovesFrom(Position position, boolean isWhiteMove) { //TODO make available for different board sizes

        this.isWhiteMove = isWhiteMove;
        availableMoves = new LinkedList<>();
        currentBeatingLength = 0;
        prepareBasicBitSets(position, isWhiteMove);

        if (!ownKings.isEmpty()) checkForPromotedBeating();
        if (!ownCheckers.isEmpty()) checkForStandardBeating();

        if (noBeatingWasFound()) {
            if (!ownKings.isEmpty()) checkForPromotedMoves();
            if (!ownCheckers.isEmpty()) checkForStandardMove();
        }

        return availableMoves;
    }

    private void checkForStandardBeating() {
        for (int checkerPositionIndex = ownCheckers.nextSetBit(0); checkerPositionIndex >= 0; checkerPositionIndex = ownCheckers.nextSetBit(checkerPositionIndex + 1)) {
            freeTileNumbers.set(checkerPositionIndex);
            Move move = new Move();
            move.addNewTileNumberToMoveSequence(checkerPositionIndex);
            findAStandardBeatingFromCurrentMoveSequence(move);
            freeTileNumbers.clear(checkerPositionIndex);
        }
    }

    private void findAStandardBeatingFromCurrentMoveSequence(Move move) {
        findAStandardBeatingInDirection(move, UPPER_LEFT);
        findAStandardBeatingInDirection(move, UPPER_RIGHT);
        findAStandardBeatingInDirection(move, LOWER_RIGHT);
        findAStandardBeatingInDirection(move, LOWER_LEFT);
    }

    private void findAStandardBeatingInDirection(Move move, int direction) {
        int expectedEnemyTileNumber = move.getLastPositionOfThePiece() + direction;
        int expectedFreeTileNumber = expectedEnemyTileNumber + direction;

        if (enemyPieces.get(expectedEnemyTileNumber)) if (freeTileNumbers.get(expectedFreeTileNumber)) {
            enemyPieces.clear(expectedEnemyTileNumber);

            Move foundMove = move.getCopy();
            foundMove.addNewTileNumberToMoveSequence(expectedEnemyTileNumber);
            foundMove.addNewTileNumberToMoveSequence(expectedFreeTileNumber);

            updateAvailableMoves(foundMove);

            findAStandardBeatingFromCurrentMoveSequence(foundMove);

            enemyPieces.set(expectedEnemyTileNumber);
        }
    }

    private void checkForPromotedBeating() {
        for (int kingPosition = ownKings.nextSetBit(0); kingPosition >= 0; kingPosition = ownKings.nextSetBit(kingPosition + 1)) {
            Move move = new Move();
            move.addNewTileNumberToMoveSequence(kingPosition);
            freeTileNumbers.set(kingPosition);
            findAPromotedBeatingFromCurrentMoveSequence(move, 0);
            freeTileNumbers.clear(kingPosition);
        }
    }

    private void findAPromotedBeatingFromCurrentMoveSequence(Move move, int comingDirection) {
        if (comingDirection != LOWER_LEFT) checkForPromotedBeatingInDirection(move, UPPER_RIGHT);
        if (comingDirection != LOWER_RIGHT) checkForPromotedBeatingInDirection(move, UPPER_LEFT);
        if (comingDirection != UPPER_RIGHT) checkForPromotedBeatingInDirection(move, LOWER_LEFT);
        if (comingDirection != UPPER_LEFT) checkForPromotedBeatingInDirection(move, LOWER_RIGHT);
    }

    private void checkForPromotedBeatingInDirection(Move move, int direction) {
        int endTileNumber = move.getLastPositionOfThePiece();
        int enemyTileNumber = findClosestEnemyInDirectionFromTile(direction, endTileNumber);
        if (properEnemyWasFound(enemyTileNumber)) {
            int expectedFreeTileNumber = enemyTileNumber + direction;
            enemyPieces.clear(enemyTileNumber); //this will be fixed after finding all (if any) further beatings

            while (freeTileNumbers.get(expectedFreeTileNumber)) {
                Move updatedMove = move.getCopy();
                updatedMove.addNewTileNumberToMoveSequence(enemyTileNumber);


                updatedMove.addNewTileNumberToMoveSequence(expectedFreeTileNumber);
                updateAvailableMoves(updatedMove);

                findAPromotedBeatingFromCurrentMoveSequence(updatedMove, direction);

                expectedFreeTileNumber += direction;
            }

            enemyPieces.set(enemyTileNumber);
        }
    }

    private void updateAvailableMoves(Move foundMove) {
        if (foundMove.size() > currentBeatingLength) {
            availableMoves.clear();
            availableMoves.add(foundMove);
            currentBeatingLength = foundMove.size();
        } else if (foundMove.size() == currentBeatingLength) {
            availableMoves.add(foundMove);
        }
    }

    private boolean properEnemyWasFound(int enemyPosition) {
        return enemyPosition > -1;
    }

    private int findClosestEnemyInDirectionFromTile(int direction, int currentKingPosition) {
        int checkedPosition = currentKingPosition + direction;

        while (freeTileNumbers.get(checkedPosition)) {
            checkedPosition += direction;
        }

        if (enemyPieces.get(checkedPosition)) return checkedPosition;
        else return -1;
    }


    private void checkForPromotedMoves() {
        checkForFlyingMoveInDirection(UPPER_RIGHT);
        checkForFlyingMoveInDirection(UPPER_LEFT);
        checkForFlyingMoveInDirection(LOWER_RIGHT);
        checkForFlyingMoveInDirection(LOWER_LEFT);
    }

    private void checkForFlyingMoveInDirection(int direction) {
        int totalDirection = direction;
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(ownKings, direction);
        shiftedCopy.and(freeTileNumbers);

        while (!shiftedCopy.isEmpty()) {
            for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
                availableMoves.add(new Move(i - totalDirection, i));
            }

            totalDirection += direction;
            shiftedCopy = bitwiseOperator.getShiftedCopy(shiftedCopy, direction);
            shiftedCopy.and(freeTileNumbers);
        }

    }

    private void checkForStandardMove() {
        if (isWhiteMove) {
            checkFrStandardMoveInDirection(UPPER_LEFT);
            checkFrStandardMoveInDirection(UPPER_RIGHT);
        } else {
            checkFrStandardMoveInDirection(LOWER_LEFT);
            checkFrStandardMoveInDirection(LOWER_RIGHT);
        }

    }

    private void checkFrStandardMoveInDirection(int direction) {
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(ownCheckers, direction);
        shiftedCopy.and(freeTileNumbers);

        for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
            availableMoves.add(new Move(i - direction, i));
        }

    }

    private boolean noBeatingWasFound() {
        return availableMoves.size() == 0;
    }


    private void prepareBasicBitSets(Position position, boolean isWhiteMove) {
        BitSet ownPieces;
        if (isWhiteMove) {
            ownPieces = position.getWhitePieces();
            enemyPieces = (BitSet) position.getBlackPieces().clone();
        } else {
            ownPieces = position.getBlackPieces();
            enemyPieces = (BitSet) position.getWhitePieces().clone();
        }

        freeTileNumbers = bitwiseOperator.merge(position.getWhitePieces(), position.getBlackPieces());
        freeTileNumbers.flip(0, freeTileNumbers.size());
        freeTileNumbers.and(bitwiseOperator.getExistingTileNumbers());
        ownCheckers = bitwiseOperator.getOwnCheckers(ownPieces, position.getKings());
        ownKings = bitwiseOperator.getOwnKings(ownPieces, position.getKings());
    }

    public boolean isMovePossible(Position position, Move move, boolean isWhiteMove) {
        return getAvailableMovesFrom(position, isWhiteMove).contains(move);
        //TODO: it's probably possible to use here some of the private methods instead of whole public method
    }
}
