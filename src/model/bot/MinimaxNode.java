package model.bot;

import model.board.Position;
import model.bot.position_rater.PositionRater;

import java.util.LinkedList;
import java.util.List;

public class MinimaxNode {
    private Position position;
    private List<MinimaxNode> childrenNodes;
    private int nodeDepthLeft;
    private boolean isWhiteMove;
    private int foundRating;
    private boolean isEndNode;

    public MinimaxNode(Position position, int nodeDepthLeft, boolean isWhiteMove) {
        this.position = position;
        this.nodeDepthLeft = nodeDepthLeft;
        this.isWhiteMove = isWhiteMove;

        childrenNodes = new LinkedList<>();
        isEndNode = false;
    }

    public void setAsWinningNode() {
        if (isWhiteMove) foundRating = PositionRater.BLACK_WON;
        else foundRating = PositionRater.WHITE_WON;
    }

    public void addChild(Position position) {
        childrenNodes.add(new MinimaxNode(position, nodeDepthLeft - 1, !isWhiteMove));
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
        return childrenNodes;
    }

    public boolean isEndNode() {
        return isEndNode;
    }

    public void setNodeAsEndNode() {
        isEndNode = true;
    }

    public boolean hasChildren() {
        return !childrenNodes.isEmpty();
    }

    public void pickBestChildRating() {
        int bestFoundRating;
        if (isWhiteMove) bestFoundRating = PositionRater.BLACK_WON;
        else bestFoundRating = PositionRater.WHITE_WON;

        for (MinimaxNode childRating : childrenNodes) {
            bestFoundRating = pickBetterRating(bestFoundRating, childRating.getFoundRating());
        }
    }

    private int pickBetterRating(int bestFoundRating, int foundRating) {
        if (isWhiteMove) return Integer.max(bestFoundRating, foundRating);
        else return Integer.min(bestFoundRating, foundRating);
    }

    public int getFoundRating() {
        return foundRating;
    }

    public void setFoundRating(int foundRating) {
        this.foundRating = foundRating;
    }

    public List<Position> getBestChildren() {
        List<Position> bestChildren;



        return null;
    }
}
