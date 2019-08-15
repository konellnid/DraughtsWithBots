package model.bot;

import model.board.Move;
import model.board.Position;
import model.board.PositionOperator;
import model.bot.position_rater.PositionRater;
import model.move_finder.PossibleMovesFinder;

import java.util.List;
import java.util.Random;

public class MinimaxBot extends GameBot {
    private MinimaxBotSettings minimaxBotSettings;
    private PositionRater positionRater;
    private PossibleMovesFinder possibleMovesFinder;
    private PositionOperator positionOperator;
    private Random random;

    public MinimaxBot(MinimaxBotSettings minimaxBotSettings, PositionRater positionRater, int boardSideLength) {
        this.minimaxBotSettings = minimaxBotSettings;
        this.positionRater = positionRater;

        possibleMovesFinder = new PossibleMovesFinder(boardSideLength);
        positionOperator = new PositionOperator(boardSideLength);

        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {

        return null;
    }


}
