package model.input_handler;

import model.board.Move;
import model.board.Position;

import java.util.List;

public interface InputHandler {
    void newMoveIsExpected(List<Move> possibleMoves, Position position);
}
