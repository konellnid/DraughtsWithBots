package model.bot;

import model.board.Move;
import model.board.Position;

import java.util.*;

public class RandomPieceBot extends GameBot {
    private Random random;

    public RandomPieceBot() {
        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {
        List<Integer> possibleStartingTileNumbers = getStartingTileNumbersFromPossibleMoves(possibleMoves);
        int chosenStartingTile = getRandomElementFromList(possibleStartingTileNumbers);

        possibleMoves.removeIf(move -> move.getStartingPositionOfThePiece() != chosenStartingTile);

        Move chosenMove = getRandomElementFromList(possibleMoves);

        return chosenMove;
    }

    private List<Integer> getStartingTileNumbersFromPossibleMoves(List<Move> possibleMoves) {
        Set<Integer> treeSet = new TreeSet<>();

        for (Move possibleMove : possibleMoves) {
            treeSet.add(possibleMove.getStartingPositionOfThePiece());
        }

        return new LinkedList<>(treeSet);
    }

    private <E> E getRandomElementFromList(List<E> possibleStartingTileNumbers) {
        int listSize = possibleStartingTileNumbers.size();
        int randomPlaceInList = random.nextInt(listSize);

        return possibleStartingTileNumbers.get(randomPlaceInList);
    }
}
