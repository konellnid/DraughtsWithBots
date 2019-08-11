package model.input_handler;

import controller.BoardController;
import model.board.Move;
import model.board.Position;
import model.bot.GameBot;
import model.game.Game;

import java.util.LinkedList;
import java.util.List;

public class BotInputHandler implements InputHandler {
    private final int DELAY_TIME = 700;
    private Game game;
    private GameBot gameBot;
    private BoardController boardController;

    public BotInputHandler(BoardController boardController, Game game, GameBot gameBot) {
        this.boardController = boardController;
        this.game = game;
        this.gameBot = gameBot;
    }

    @Override
    public void newMoveIsExpected(List<Move> possibleMoves, Position position) {
        new Thread(() -> {
            Move chosenMove = gameBot.choseAMoveFrom(possibleMoves, position);
            choiceWasMade(chosenMove);
        }).start();
    }

    private void choiceWasMade(Move move) {

        if (move.isBeatingSequence()) {
            List<Move> stepByStepMoves = splitMoveIntoSmallerMoves(move);

            for (Move smallMove : stepByStepMoves) {
                waitDelay();
                boardController.makeBeatingMove(smallMove);
            }
        } else {
            waitDelay();
            boardController.makeStandardMove(move);
        }
        game.makeMove(move);
    }

    private void waitDelay() {
        try {
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Move> splitMoveIntoSmallerMoves(Move beatingMove) {
        List<Integer> beatingMoveSequence = beatingMove.getMoveSequence();
        List<Move> stepByStepMoves = new LinkedList<>();

        for (int endOfJumpPlaceInMoveSequence = 2; endOfJumpPlaceInMoveSequence < beatingMove.size(); endOfJumpPlaceInMoveSequence += 2) {
            Move smallMove = new Move();

            Integer startingTile = beatingMoveSequence.get(endOfJumpPlaceInMoveSequence - 2);
            Integer enemyTile = beatingMoveSequence.get(endOfJumpPlaceInMoveSequence - 1);
            Integer destinationTile = beatingMoveSequence.get(endOfJumpPlaceInMoveSequence);

            smallMove.addNewTileToMoveSequence(startingTile);
            smallMove.addNewTileToMoveSequence(enemyTile);
            smallMove.addNewTileToMoveSequence(destinationTile);

            stepByStepMoves.add(smallMove);
        }

        return stepByStepMoves;
    }
}

