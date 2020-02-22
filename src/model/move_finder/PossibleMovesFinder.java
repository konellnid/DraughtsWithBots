package model.move_finder;

import model.board.Move;
import model.board.Position;
import model.move_finder.move_finder_beating_strategy.FlyingBeating;
import model.move_finder.move_finder_beating_strategy.ForwardBeating;
import model.move_finder.move_finder_beating_strategy.MoveFinderBeatingStrategy;
import model.move_finder.move_finder_beating_strategy.StandardBeating;

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
    private MoveFinderSettings moveFinderSettings;
    private int currentLongestMoveLength;
    private BitwiseOperator bitwiseOperator;
    private List<Move> availableMoves;
    private boolean isWhiteMove;
    private BasicBitSets basicBitSets;
    private DirectionsValueBySize directions;
    private MoveFinderBeatingStrategy checkerBeatingStrategy;
    private MoveFinderBeatingStrategy kingBeatingStrategy;


    public PossibleMovesFinder(MoveFinderSettings moveFinderSettings, int boardSideLength) {
        this.moveFinderSettings = moveFinderSettings;
        bitwiseOperator = new BitwiseOperator(boardSideLength);

        declareProperDirections(boardSideLength);
        chooseCheckerBeatingStrategies();
        chooseKingBeatingStrategies();
    }

    private void declareProperDirections(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
                directions = DirectionsValueBySize.BOARD_OF_SIZE_EIGHT;
                break;
            case 10:
                directions = DirectionsValueBySize.BOARD_OF_SIZE_TEN;
                break;
            case 12:
                directions = DirectionsValueBySize.BOARD_OF_SIZE_TWELVE;
                break;
        }
    }

    private void chooseCheckerBeatingStrategies() {
        if (moveFinderSettings.isFlyingKingEnabled()) {
            kingBeatingStrategy = new FlyingBeating(directions);
        } else {
            kingBeatingStrategy = new StandardBeating(directions);
        }
    }

    private void chooseKingBeatingStrategies() {
        if (moveFinderSettings.isCheckerBeatingBackwardsEnabled()) {
            checkerBeatingStrategy = new StandardBeating(directions);
        } else {
            checkerBeatingStrategy = new ForwardBeating(directions);
        }
    }

    public List<Move> getAvailableMovesFrom(Position position, boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
        prepareBasicBitSets(position, isWhiteMove);
        availableMoves = new LinkedList<>();
        currentLongestMoveLength = 0;

        checkerBeatingStrategy.setForwardDirection(isWhiteMove);

        checkForMoves();

        return availableMoves;
    }

    private void checkForMoves() {
        checkForCheckersBeating();
        checkForKingsBeating();

        if (noBeatingWasFound()) {
            checkForCheckersMove();
            checkForKingsMove();
        }
    }

    private void checkForKingsMove() {
        if (moveFinderSettings.isFlyingKingEnabled()) {
            checkForKingsFlyingMove();
        } else {
            checkForStandardMoveInDirection(basicBitSets.getOwnKings(), directions.upperLeft);
            checkForStandardMoveInDirection(basicBitSets.getOwnKings(), directions.upperRight);
            checkForStandardMoveInDirection(basicBitSets.getOwnKings(), directions.lowerLeft);
            checkForStandardMoveInDirection(basicBitSets.getOwnKings(), directions.lowerRight);
        }
    }

    private void checkForCheckersMove() {
        if (isWhiteMove) {
            checkForStandardMoveInDirection(basicBitSets.getOwnCheckers(), directions.upperLeft);
            checkForStandardMoveInDirection(basicBitSets.getOwnCheckers(), directions.upperRight);
        } else {
            checkForStandardMoveInDirection(basicBitSets.getOwnCheckers(), directions.lowerLeft);
            checkForStandardMoveInDirection(basicBitSets.getOwnCheckers(), directions.lowerRight);
        }
    }

    private void checkForStandardMoveInDirection(BitSet bitSetToShift, int direction) {
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(bitSetToShift, direction);
        shiftedCopy.and(basicBitSets.getFreeTileNumbers());

        for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
            availableMoves.add(new Move(i - direction, i));
        }
    }

    private void checkForKingsFlyingMove() {
        checkForFlyingMoveInDirection(directions.lowerLeft);
        checkForFlyingMoveInDirection(directions.lowerRight);
        checkForFlyingMoveInDirection(directions.upperLeft);
        checkForFlyingMoveInDirection(directions.upperRight);
    }

    private void checkForFlyingMoveInDirection(int direction) {
        BitSet shiftedCopy = bitwiseOperator.getShiftedCopy(basicBitSets.getOwnKings(), direction);
        shiftedCopy.and(basicBitSets.getFreeTileNumbers());
        int totalDirection = direction;

        while (!shiftedCopy.isEmpty()) {
            for (int i = shiftedCopy.nextSetBit(0); i >= 0; i = shiftedCopy.nextSetBit(i + 1)) {
                availableMoves.add(new Move(i - totalDirection, i));
            }

            totalDirection += direction;

            shiftedCopy = bitwiseOperator.getShiftedCopy(shiftedCopy, direction);
            shiftedCopy.and(basicBitSets.getFreeTileNumbers());
        }
    }

    private void checkForCheckersBeating() {
        for (int checkerTileNumber = basicBitSets.getOwnCheckers().nextSetBit(0); checkerTileNumber >= 0; checkerTileNumber = basicBitSets.getOwnCheckers().nextSetBit(checkerTileNumber + 1)) {
            List<Move> foundMoves = checkerBeatingStrategy.checkForBeatingMoves(checkerTileNumber, basicBitSets);
            updateAvailableMoves(foundMoves);
        }
    }

    private void checkForKingsBeating() {
        for (int kingTileNumber = basicBitSets.getOwnKings().nextSetBit(0); kingTileNumber >= 0; kingTileNumber = basicBitSets.getOwnKings().nextSetBit(kingTileNumber + 1)) {
            List<Move> foundMoves = kingBeatingStrategy.checkForBeatingMoves(kingTileNumber, basicBitSets);
            updateAvailableMoves(foundMoves);
        }
    }


    private void updateAvailableMoves(List<Move> foundMoves) {
        if (!foundMoves.isEmpty()) {
            int foundMoveSize = foundMoves.get(0).size();

            if (foundMoveSize > currentLongestMoveLength) {
                availableMoves.clear();
                availableMoves = foundMoves;
                currentLongestMoveLength = foundMoveSize;
            } else if (foundMoveSize == currentLongestMoveLength) {
                availableMoves.addAll(foundMoves);
            }
        }
    }

    private boolean noBeatingWasFound() {
        return availableMoves.size() == 0;
    }


    private void prepareBasicBitSets(Position position, boolean isWhiteMove) {
        basicBitSets = new BasicBitSets(position, isWhiteMove, bitwiseOperator);
    }
}
