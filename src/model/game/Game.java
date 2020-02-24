package model.game;

import controller.BoardController;
import controller.MainViewController;
import model.board.Move;
import model.board.Position;
import model.bot.MinimaxBot;
import model.bot.MinimaxBotSettings;
import model.bot.RandomBot;
import model.bot.RandomPieceBot;
import model.bot.position_rater.PositionRater;
import model.bot.position_rater.PositionRaterSettings;
import model.input_handler.BotInputHandler;
import model.input_handler.InputHandler;
import model.input_handler.PlayerInputHandler;
import model.move_finder.MoveFinderSettings;
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
    private List<Move> currentlyPossibleMoves;

    private GameStorage gameStorage;


    public Game(BoardController boardController, MainViewController mainViewController) {
        this.boardController = boardController;
        this.mainViewController = mainViewController;

    }

    public void newGame(PlayerType whitePlayer, PlayerType blackPlayer, int boardSideLength, MoveFinderSettings moveFinderSettings) {
        gameStorage = new GameStorage(boardSideLength);

        possibleMovesFinder = new PossibleMovesFinder(moveFinderSettings, boardSideLength);

        if (whitePlayer == PlayerType.MINIMAX_BOT) {
            MinimaxBotSettings whiteMinimaxBotSettings = mainViewController.getWhiteMinimaxBotSettings();
            PositionRaterSettings whitePositionRaterSettings = mainViewController.getWhitePositionRaterSettings();

            whiteInputHandler = generateMinMaxInputHandler(whiteMinimaxBotSettings, whitePositionRaterSettings, boardSideLength);
        } else {
            whiteInputHandler = generateStandardInputHandler(whitePlayer, boardSideLength);
        }

        if (blackPlayer == PlayerType.MINIMAX_BOT) {
            MinimaxBotSettings blackMinimaxBotSettings = mainViewController.getBlackMinimaxBotSettings();
            PositionRaterSettings blackPositionRaterSettings = mainViewController.getBlackPositionRaterSettings();

            blackInputHandler = generateMinMaxInputHandler(blackMinimaxBotSettings, blackPositionRaterSettings, boardSideLength);
        } else {
            blackInputHandler = generateStandardInputHandler(blackPlayer, boardSideLength);
        }

        boardController.showPositionOnBoard(new Position(gameStorage.getPosition()), boardSideLength);

        nextTurn();
    }

    private BotInputHandler generateMinMaxInputHandler(MinimaxBotSettings minimaxBotSettings, PositionRaterSettings positionRaterSettings, int boardSideLength) {
        PositionRater positionRater = new PositionRater(positionRaterSettings, boardSideLength);
        MinimaxBot minimaxBot = new MinimaxBot(minimaxBotSettings, positionRater, boardSideLength, possibleMovesFinder);
        BotInputHandler inputHandler = new BotInputHandler(boardController, this, minimaxBot);
        return inputHandler;
    }

    private void nextTurn() {
        currentlyPossibleMoves = possibleMovesFinder.getAvailableMovesFrom(gameStorage.getPosition(), gameStorage.isWhiteTurn());
        System.out.println("next turn possible moves size: " + currentlyPossibleMoves.size());
        if (currentlyPossibleMoves.size() == 0) {
            boolean isWhiteMove = gameStorage.isWhiteTurn();
            if (isWhiteMove) endGame(BLACK_WON);
            else endGame(WHITE_WON);
        } else if (tooManyMovesWereMadeWithoutBeatingOrCheckerMove()) {
            endGame(DRAW);
        } else {
            noticeProperInputHandler(currentlyPossibleMoves);
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

    private InputHandler generateStandardInputHandler(PlayerType playerType, int boardSideLength) {
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
                break;
        }

        return inputHandler;
    }


    public void makeMove(Move move) {
        if (!currentlyPossibleMoves.contains(move)) throw new IllegalArgumentException();
        gameStorage.updateGameStorageWithMove(move);
        mainViewController.updateMovesTillDraw(gameStorage.getMovesTillDraw());
        System.out.println(gameStorage.getPosition().getWhitePieces().get(move.getLastPositionOfThePiece()));

        if (gameStorage.pieceGotPromoted()) {
            boardController.promote(move.getLastPositionOfThePiece());
        }

        gameStorage.setWhiteTurn(!gameStorage.isWhiteTurn());
        nextTurn();
    }
}
