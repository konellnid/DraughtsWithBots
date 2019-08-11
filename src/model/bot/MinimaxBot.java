package model.bot;

import model.board.Move;
import model.board.Position;
import model.move_finder.PossibleMovesFinder;

import java.util.List;
import java.util.Random;

public class MinimaxBot extends GameBot {
    private Random random;
    private PossibleMovesFinder possibleMovesFinder;


    public MinimaxBot(PossibleMovesFinder possibleMovesFinder) {
        this.possibleMovesFinder = possibleMovesFinder;
        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {

        return null;
    }


}
