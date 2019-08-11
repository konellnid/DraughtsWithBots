package model.bot;

import model.board.Move;
import model.board.Position;

import java.util.List;

public abstract class GameBot {
    public abstract Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition);
}
