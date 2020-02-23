package model.bot;

import model.board.Position;

import java.util.List;

public class MinimaxNode {
    private Position position;
    private List<MinimaxNode> reachablePositions;
    private int nodeDepthLeft;
    private boolean isWhiteMove;

    public MinimaxNode(Position position, int nodeDepthLeft, boolean isWhiteMove) {
        this.position = position;
        this.nodeDepthLeft = nodeDepthLeft;
        this.isWhiteMove = isWhiteMove;
    }

    public boolean isWhiteMove() {
        return isWhiteMove;
    }

    public int getNodeDepthLeft() {
        return nodeDepthLeft;
    }
}
