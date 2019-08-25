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
    private int upperRight;
    private int upperLeft;
    private int lowerRight;
    private int lowerLeft;

    private MoveFinderSettings moveFinderSettings;
    private int currentBeatingLength;
    private BitwiseOperator bitwiseOperator;
    private List<Move> availableMoves;
    private boolean isWhiteMove;
    private BasicBitSets basicBitSets;


    public PossibleMovesFinder(MoveFinderSettings moveFinderSettings, int boardSideLength) {
        this.moveFinderSettings = moveFinderSettings;
        bitwiseOperator = new BitwiseOperator(boardSideLength);
        declareProperDirections(boardSideLength);
    }

    private void declareProperDirections(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
                upperRight = 4;
                upperLeft = 5;
                break;
            case 10:
                upperRight = 5;
                upperLeft = 6;
                break;
            case 12:
                upperRight = 6;
                upperLeft = 7;
                break;
        }

        lowerRight = (-1) * upperLeft;
        lowerLeft = (-1) * upperRight;
    }

    public List<Move> getAvailableMovesFrom(Position position, boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
        prepareBasicBitSets(position, isWhiteMove);
        availableMoves = new LinkedList<>();
        currentBeatingLength = 0;
        prepareBasicBitSets(position, isWhiteMove);

        if (!basicBitSets.getOwnKings().isEmpty()) checkForPromotedBeating();
        if (!basicBitSets.getOwnCheckers().isEmpty()) checkForStandardBeating();

        if (noBeatingWasFound()) {
            if (!basicBitSets.getOwnKings().isEmpty()) checkForPromotedMoves();
            if (!basicBitSets.getOwnCheckers().isEmpty()) checkForStandardMove();
        }

        return availableMoves;
    }

    private void checkForStandardBeating() {
        for (int checkerPositionIndex = basicBitSets.getOwnCheckers().nextSetBit(0); checkerPositionIndex >= 0; checkerPositionIndex = basicBitSets.getOwnCheckers().nextSetBit(checkerPositionIndex + 1)) {
            basicBitSets.getFreeTileNumbers().set(checkerPositionIndex);
            Move move = new Move();
            move.addNewTileNumberToMoveSequence(checkerPositionIndex);
            findAStandardBeatingFromCurrentMoveSequence(move);
            basicBitSets.getFreeTileNumbers().clear(checkerPositionIndex);
        }
    }

    private void findAStandardBeatingFromCurrentMoveSequence(Move move) {
        findAStandardBeatingInDirection(move, upperLeft);
        findAStandardBeatingInDirection(move, upperRight);
        findAStandardBeatingInDirection(move, lowerRight);
        findAStandardBeatingInDirection(move, lowerLeft);
    }

    private void findAStandardBeatingInDirection(Move move, int direction) {
        int expectedEnemyTileNumber = move.getLastPositionOfThePiece() + direction;
        int expectedFreeTileNumber = expectedEnemyTileNumber + direction;

        if (basicBitSets.getEnemyPieces().get(expectedEnemyTileNumber))
            if (basicBitSets.getFreeTileNumbers().get(expectedFreeTileNumber)) {
                basicBitSets.getEnemyPieces().clear(expectedEnemyTileNumber);

                Move foundMove = move.getCopy();
                foundMove.addNewTileNumberToMoveSequence(expectedEnemyTileNumber);
                foundMove.addNewTileNumberToMoveSequence(expectedFreeTileNumber);

                updateAvailableMoves(foundMove);

                findAStandardBeatingFromCurrentMoveSequence(foundMove);

                basicBitSets.getEnemyPieces().set(expectedEnemyTileNumber);
            }
    }

    private void checkForPromotedBeating() {
        for (int kingPosition = basicBitSets.getOwnKings().nextSetBit(0); kingPosition >= 0; kingPosition = basicBitSets.getOwnKings().nextSetBit(kingPosition + 1)) {
            Move move = new Move();
            move.addNewTileNumberToMoveSequence(kingPosition);
            basicBitSets.getFreeTileNumbers().set(kingPosition);
            findAPromotedBeatingFromCurrentMoveSequence(move, 0);
            basicBitSets.getFreeTileNumbers().clear(kingPosition);
        }
    }

    private void findAPromotedBeatingFromCurrentMoveSequence(Move move, int comingDirection) {
        if (comingDirection != lowerLeft) checkForPromotedBeatingInDirection(move, upperRight);
        if (comingDirection != lowerRight) checkForPromotedBeatingInDirection(move, upperLeft);
        if (comingDirection != upperRight) checkForPromotedBeatingInDirection(move, lowerLeft);
        if (comingDirection != upperLeft) checkForPromotedBeatingInDirection(move, lowerRight);
    }

    private void checkForPromotedBeatingInDirection(Move move, int direction) {
        int endTileNumber = move.getLastPositionOfThePiece();
        int enemyTileNumber = findClosestEnemyInDirectionFromTile(direction, endTileNumber);
        if (properEnemyWasFound(enemyTileNumber)) {
            int expectedFreeTileNumber = enemyTileNumber + direction;
            basicBitSets.getEnemyPieces().clear(enemyTileNumber); //this will be fixed after finding all (if any) further beatings

            while (basicBitSets.getFreeTileNumbers().get(expectedFreeTileNumber)) {
                Move updatedMove = move.getCopy();
                updatedMove.addNewTileNumberToMoveSequence(enemyTileNumber);


                updatedMove.addNewTileNumberToMoveSequence(expectedFreeTileNumber);
                updateAvailableMoves(updatedMove);

                findAPromotedBeatingFromCurrentMoveSequence(updatedMove, direction);

                expectedFreeTileNumber += direction;
            }

            basicBitSets.getEnemyPieces().set(enemyTileNumber);
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

        while (basicBitSets.getFreeTileNumbers().get(checkedPosition)) {
            checkedPosition += direction;
        }

        if (basicBitSets.getEnemyPieces().get(checkedPosition)) return checkedPosition;
        else return -1;
    }


    private void checkForPromotedMoves() {
        checkForFlyingMoveInDirection(upperRight);
        checkForFlyingMoveInDirection(upperLeft);
        checkForFlyingMoveInDirection(lowerRight);
        checkForFlyingMoveInDirection(lowerLeft);
    }

    private void checkForFlyingMoveInDirection(int direction) {
        int totalDirection = direction;
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(basicBitSets.getOwnKings(), direction);
        shiftedCopy.and(basicBitSets.getFreeTileNumbers());

        while (!shiftedCopy.isEmpty()) {
            for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
                availableMoves.add(new Move(i - totalDirection, i));
            }

            totalDirection += direction;
            shiftedCopy = bitwiseOperator.getShiftedCopy(shiftedCopy, direction);
            shiftedCopy.and(basicBitSets.getFreeTileNumbers());
        }

    }

    private void checkForStandardMove() {
        if (isWhiteMove) {
            checkFrStandardMoveInDirection(upperLeft);
            checkFrStandardMoveInDirection(upperRight);
        } else {
            checkFrStandardMoveInDirection(lowerLeft);
            checkFrStandardMoveInDirection(lowerRight);
        }

    }

    private void checkFrStandardMoveInDirection(int direction) {
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(basicBitSets.getOwnCheckers(), direction);
        shiftedCopy.and(basicBitSets.getFreeTileNumbers());

        for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
            availableMoves.add(new Move(i - direction, i));
        }

    }

    private boolean noBeatingWasFound() {
        return availableMoves.size() == 0;
    }


    private void prepareBasicBitSets(Position position, boolean isWhiteMove) {
        basicBitSets = new BasicBitSets(position, isWhiteMove, bitwiseOperator);
    }
}
