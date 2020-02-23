package model.bot;

import model.board.Position;
import model.bot.position_rater.PositionRater;

import java.util.LinkedList;
import java.util.List;

public class MinimaxNode {
    private Position position;
    private List<MinimaxNode> reachablePositions;
    private int nodeDepthLeft;
    private boolean isWhiteMove;
    private int foundRating;
    private boolean isEndNode;

    public MinimaxNode(Position position, int nodeDepthLeft, boolean isWhiteMove) {
        this.position = position;
        this.nodeDepthLeft = nodeDepthLeft;
        this.isWhiteMove = isWhiteMove;

        reachablePositions = new LinkedList<>();
        isEndNode = false;
    }

    public void setAsWinningNode() {
        if (isWhiteMove) foundRating = PositionRater.BLACK_WON;
        else foundRating = PositionRater.WHITE_WON;
    }

    public void addChild(Position position) {
        reachablePositions.add(new MinimaxNode(position, nodeDepthLeft - 1, !isWhiteMove));
    }

    public boolean isWhiteMove() {
        return isWhiteMove;
    }

    public int getNodeDepthLeft() {
        return nodeDepthLeft;
    }

    public Position getPosition() {
        return position;
    }

    public List<MinimaxNode> getChildren() {
        return reachablePositions;
    }

    public boolean isEndNode() {
        return isEndNode;
    }

    public void setNodeAsEndNode() {
        isEndNode = true;
    }
}
