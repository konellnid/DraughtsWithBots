package model.board;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Move {
    private List<Integer> moveSequence;

    public Move(Integer... tileNumbers) {
        moveSequence = Arrays.asList(tileNumbers);
    }

    public Move() {
        moveSequence = new ArrayList<>();
    }

    public void addNewTileNumberToMoveSequence(int number) {
        moveSequence.add(number);
    }

    public boolean isBeatingSequence() {
        return moveSequence.size() > 2;
    }

    public List<Integer> getMoveSequence() {
        return moveSequence;
    }

    public Move getCopy() {
        Move copy = new Move();
        copy.moveSequence = new ArrayList<>(moveSequence);
        return copy;
    }

    public Integer get(int placeInMoveSequence) {
        if (placeInMoveSequence >= 0 && placeInMoveSequence < moveSequence.size())
            return moveSequence.get(placeInMoveSequence);
        else return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moveSequence.equals(move.moveSequence);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> iterator = moveSequence.iterator();

        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) stringBuilder.append(", ");
        }

        stringBuilder.append("; ");
        return stringBuilder.toString();
    }

    public int getLastPositionOfThePiece() {
        if (moveSequence.size() > 0) {
            int lastElementIndex = moveSequence.size() - 1;
            return moveSequence.get(lastElementIndex);
        } else {
            return -1;
        }
    }

    public int getStartingPositionOfThePiece() {
        if (moveSequence.size() > 0) {
            return moveSequence.get(0);
        } else {
            return -1;
        }
    }

    public int size() {
        return moveSequence.size();
    }

}
