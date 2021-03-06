package model.input_handler;

import controller.BoardController;
import model.board.Move;
import model.board.Position;
import model.game.Game;

import java.util.*;

public class PlayerInputHandler implements InputHandler {
    private List<Move> possibleMoves;
    private Set<Integer> possibleStartingPositions;
    private Integer currentlySelectedTileNumber;
    private Set<Integer> possibleDestinationTileNumbersFromSelectedTile;
    private BoardController boardController;
    private Integer currentPlaceInMoveList;
    private boolean isBeatingMove;
    private int moveSequenceTotalLength;
    private Game game;

    public PlayerInputHandler(BoardController boardController, Game game) {
        this.boardController = boardController;
        this.game = game;
    }

    @Override
    public void newMoveIsExpected(List<Move> possibleMoves, Position position) {
        this.possibleMoves = possibleMoves;
        findPossibleStartingPositions();
        boardController.addNewPressedTilesObserver(this);
        isBeatingMove = isBeatingMove();
        currentPlaceInMoveList = 0;
        moveSequenceTotalLength = possibleMoves.get(0).size();
        possibleDestinationTileNumbersFromSelectedTile = new HashSet<>();
    }

    private boolean isBeatingMove() {
        Move exampleMove = possibleMoves.get(0);
        return exampleMove.isBeatingSequence();
    }

    private void findPossibleStartingPositions() {
        possibleStartingPositions = new HashSet<>();

        for (Move move : possibleMoves) {
            possibleStartingPositions.add(move.getStartingPositionOfThePiece());
        }
    }

    public void tileGotPressed(int tileNumber) {
        if (possibleStartingPositions.contains(tileNumber)) {
            newStartingPosition(tileNumber);
        } else if (possibleDestinationTileNumbersFromSelectedTile.contains(tileNumber)) {
            possibleStartingPositions.clear();
            boardController.highlight(tileNumber);

            if (isBeatingMove) {
                removeUnreachableBeatingMoves(tileNumber);
                currentPlaceInMoveList += 2;
                sendSmallMoveToBoardController();

                if (currentPlaceInMoveList == moveSequenceTotalLength - 1) {
                    wholeMoveSequenceWasMade();
                } else {
                    currentlySelectedTileNumber = tileNumber;
                    findTilesReachableFromCurrentlySelectedTile();
                }
            } else {
                replacePossibleMovesWithCurrentlySelectedMove(tileNumber);
                Move chosenMove = possibleMoves.get(0);
                boardController.makeStandardMove(chosenMove);
                wholeMoveSequenceWasMade();
            }
        }
    }

    private void newStartingPosition(int tileNumber) {
        boardController.highlight(tileNumber);
        currentlySelectedTileNumber = tileNumber;
        findTilesReachableFromCurrentlySelectedTile();
    }

    private void findTilesReachableFromCurrentlySelectedTile() {
        possibleDestinationTileNumbersFromSelectedTile = new HashSet<>();

        for (Move possibleMove : possibleMoves) {
            if (possibleMove.get(currentPlaceInMoveList).equals(currentlySelectedTileNumber)) {
                if (isBeatingMove) possibleDestinationTileNumbersFromSelectedTile.add(possibleMove.get(currentPlaceInMoveList + 2));
                else possibleDestinationTileNumbersFromSelectedTile.add(possibleMove.get(currentPlaceInMoveList + 1));
            }
        }
    }

    private void wholeMoveSequenceWasMade() {
        boardController.removePressedTilesObserver();
        Move chosenMove = possibleMoves.get(0);
        game.makeMove(chosenMove);
    }

    private void sendSmallMoveToBoardController() {
        Move smallMove = new Move();
        Move exampleMove = possibleMoves.get(0);
        smallMove.addNewTileNumberToMoveSequence(exampleMove.get(currentPlaceInMoveList - 2));
        smallMove.addNewTileNumberToMoveSequence(exampleMove.get(currentPlaceInMoveList - 1));
        smallMove.addNewTileNumberToMoveSequence(exampleMove.get(currentPlaceInMoveList));
        boardController.makeBeatingMove(smallMove);
    }

    private void replacePossibleMovesWithCurrentlySelectedMove(int tileNumber) {
        Move chosenMove = new Move();
        chosenMove.addNewTileNumberToMoveSequence(currentlySelectedTileNumber);
        chosenMove.addNewTileNumberToMoveSequence(tileNumber);
        possibleMoves.clear();
        possibleMoves.add(chosenMove);
    }

    private void removeUnreachableBeatingMoves(int tileNumber) {
        possibleMoves.removeIf(checkedMove -> !(checkedMove.get(currentPlaceInMoveList).equals(currentlySelectedTileNumber) &&
                checkedMove.get(currentPlaceInMoveList + 2).equals(tileNumber)));
    }



}
