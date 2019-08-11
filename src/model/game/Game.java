package model.game;

import controller.BoardController;
import controller.MainViewController;
import model.board.Move;
import model.board.Position;
import model.bot.RandomBot;
import model.bot.RandomPieceBot;
import model.input_handler.BotInputHandler;
import model.input_handler.InputHandler;
import model.input_handler.PlayerInputHandler;
import model.move_finder.PossibleMovesFinder;

import java.util.List;

public class Game {
    private static final String WHITE_WON = "WINNER: WHITE";
    private static final String BLACK_WON = "WINNER: BLACK";
    private static final String DRAW = "DRAW";


    private PossibleMovesFinder possibleMovesFinder;
    private MainViewController mainViewController;
    private BoardController boardController;
    private InputHandler whiteInputHandler;
    private InputHandler blackInputHandler;

    private GameStorage gameStorage;


    public Game(BoardController boardController, MainViewController mainViewController) {
        this.boardController = boardController;
        this.mainViewController = mainViewController;

    }

    public void newGame(PlayerType whitePlayer, PlayerType blackPlayer) {
        gameStorage = new GameStorage();

        possibleMovesFinder = new PossibleMovesFinder();

        whiteInputHandler = generateInputHandler(whitePlayer);
        blackInputHandler = generateInputHandler(blackPlayer);

        boardController.showPositionOnBoard(new Position(gameStorage.getPosition()));

        nextTurn();
    }

    private void nextTurn() {
        List<Move> possibleMoves = possibleMovesFinder.getAvailableMovesFrom(gameStorage.getPosition(), gameStorage.isWhiteTurn());
        System.out.println("next turn possible moves size: " + possibleMoves.size());
        if (possibleMoves.size() == 0) {
            boolean isWhiteMove = gameStorage.isWhiteTurn();
            if (isWhiteMove) endGame(BLACK_WON);
            else endGame(WHITE_WON);
        } else if (tooManyMovesWereMadeWithoutBeatingOrCheckerMove()) {
            endGame(DRAW);
        } else {
            noticeProperInputHandler(possibleMoves);
        }
    }

    private void noticeProperInputHandler(List<Move> possibleMoves) {
        Position copyOfCurrentPosition = new Position(gameStorage.getPosition());
        if (gameStorage.isWhiteTurn()) {
            whiteInputHandler.newMoveIsExpected(possibleMoves, copyOfCurrentPosition);
        } else {
            blackInputHandler.newMoveIsExpected(possibleMoves, copyOfCurrentPosition);
        }
    }

    private boolean tooManyMovesWereMadeWithoutBeatingOrCheckerMove() {
        int movesTillDraw = gameStorage.getMovesTillDraw();
        return movesTillDraw == 0;
    }


    private void endGame(String resultText) {
        mainViewController.setResult(resultText);
    }

    private InputHandler generateInputHandler(PlayerType playerType) {
        InputHandler inputHandler = null;

        switch (playerType) {
            case REAL_PLAYER:
                inputHandler = new PlayerInputHandler(boardController, this);
                break;
            case RANDOM_BOT:
                inputHandler = new BotInputHandler(boardController, this, new RandomBot());
                break;
            case RANDOM_PIECE_BOT:
                inputHandler = new BotInputHandler(boardController, this, new RandomPieceBot());
        }

        return inputHandler;
    }


    public void makeMove(Move move) {
        gameStorage.updateGameStorageWithMove(move);
        mainViewController.updateMovesTillDraw(gameStorage.getMovesTillDraw());
        System.out.println(gameStorage.getPosition().getWhitePieces().get(move.getLastPositionOfThePiece()));
        if (gameStorage.pieceGetsPromotedDuringMove(move)) {
            boardController.promote(move.getLastPositionOfThePiece());
        }
        gameStorage.setWhiteTurn(!gameStorage.isWhiteTurn());
        nextTurn();
    }
}
