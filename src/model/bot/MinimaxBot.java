package model.bot;

import model.board.Move;
import model.board.Position;
import model.board.PositionOperator;
import model.move_finder.PossibleMovesFinder;

import java.util.List;
import java.util.Random;

public class MinimaxBot extends GameBot {
    private MinimaxBotSettings minimaxBotSettings;
    private PossibleMovesFinder possibleMovesFinder;
    private PositionOperator positionOperator;
    private Random random;
    private boolean isWhitePlayer;

    public MinimaxBot(int boardSideLength, MinimaxBotSettings minimaxBotSettings, boolean isWhitePlayer) {
        this.minimaxBotSettings = minimaxBotSettings;
        this.isWhitePlayer = isWhitePlayer;
        possibleMovesFinder = new PossibleMovesFinder(boardSideLength);
        positionOperator = new PositionOperator(boardSideLength);
        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {

        return null;
    }


}
