package model.bot;

import model.board.Move;
import model.board.Position;

import java.util.List;
import java.util.Random;

public class RandomBot extends GameBot {
    private Random random;

    public RandomBot() {
        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {

        int randomPlaceInList = random.nextInt(possibleMoves.size());

        Move chosenMove = possibleMoves.get(randomPlaceInList);

        return chosenMove;
    }
}
