package model.bot;

import javafx.geometry.Pos;
import model.board.Move;
import model.board.Position;
import model.board.PositionOperator;
import model.bot.position_rater.PositionRater;
import model.move_finder.PossibleMovesFinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinimaxBot extends GameBot {
    private MinimaxBotSettings minimaxBotSettings;
    private PositionRater positionRater;
    private PossibleMovesFinder possibleMovesFinder;
    private PositionOperator positionOperator;
    private Random random;

    public MinimaxBot(MinimaxBotSettings minimaxBotSettings, PositionRater positionRater, int boardSideLength, PossibleMovesFinder possibleMovesFinder) {
        this.minimaxBotSettings = minimaxBotSettings;
        this.positionRater = positionRater;
        this.possibleMovesFinder = possibleMovesFinder;

        positionOperator = new PositionOperator(boardSideLength);

        random = new Random();
    }

    @Override
    public Move choseAMoveFrom(List<Move> possibleMoves, Position currentPosition) {
        if (possibleMoves.size() == 1) return possibleMoves.get(0);

        boolean isWhiteMove = checkIfIsWhiteMove(possibleMoves, currentPosition);

        MinimaxNode startingNode = new MinimaxNode(currentPosition, minimaxBotSettings.getSearchingDepth(), isWhiteMove);

        operateFromNode(startingNode);

        List<Position> bestFoundPositions = startingNode.getBestChildren();
        List<Move> bestMoves = findMovesReachingToPositions(startingNode, possibleMoves, bestFoundPositions);

        int chosenMoveIndex = random.nextInt(bestMoves.size());

        System.out.println("Found rating: " + startingNode.getFoundRating());

        return bestMoves.get(chosenMoveIndex);
    }

    private List<Move> findMovesReachingToPositions(MinimaxNode node, List<Move> possibleMovesFromNode, List<Position> bestFoundPositions) {
        List<Move> bestMoves = new LinkedList<>();

        for (Move possibleMove : possibleMovesFromNode) {
            Position startingPositionCopy = new Position(node.getPosition());
            positionOperator.performMoveOnPosition(startingPositionCopy, possibleMove, node.isWhiteMove());
            if (bestFoundPositions.contains(startingPositionCopy)) bestMoves.add(possibleMove);
        }

        return bestMoves;
    }

    private boolean checkIfIsWhiteMove(List<Move> possibleMoves, Position position) {
        Move exampleMove = possibleMoves.get(0);
        int startingTileNumber = exampleMove.getStartingPositionOfThePiece();

        return position.getWhitePieces().get(startingTileNumber);
    }

    private void operateFromNode(MinimaxNode node) {
        addPossibleChildrenFromNode(node);

        if (!node.isEndNode() && node.hasChildren()) {
            operateOnNodeChildren(node);
        }

        if (node.hasChildren()) {
            node.pickBestChildRating();
        } else if (!node.isEndNode()) {
            int positionRating = positionRater.getRatingOfPosition(node.getPosition());
            node.setFoundRating(positionRating);
        }
    }

    private void operateOnNodeChildren(MinimaxNode node) {
        for (MinimaxNode childNode : node.getChildren()) {
            operateFromNode(childNode);
        }
    }

    private void addPossibleChildrenFromNode(MinimaxNode node) {
        if (node.getNodeDepthLeft() > 0) {
            List<Move> possibleMovesFromNode = possibleMovesFinder.getAvailableMovesFrom(node.getPosition(), node.isWhiteMove());
            if (possibleMovesFromNode.size() != 0) {
                addReachablePositionsAsChildNodes(node, possibleMovesFromNode);
            } else {
                node.setAsWinningNode();
            }
        } else if (minimaxBotSettings.isAllowedExceedingSearchingDepthIfBeatingIsFound()) {
            List<Move> possibleMovesFromNode = possibleMovesFinder.getAvailableMovesFrom(node.getPosition(), node.isWhiteMove());
            if (possibleMovesFromNode.isEmpty()) {
                node.setAsWinningNode();
            } else if (listContainsBeatingMoves(possibleMovesFromNode)) {
                addReachablePositionsAsChildNodes(node, possibleMovesFromNode);
            }
        }
    }

    private boolean listContainsBeatingMoves(List<Move> possibleMovesFromNode) {
        if (!possibleMovesFromNode.isEmpty()) {
            Move exampleMove = possibleMovesFromNode.get(0);
            return exampleMove.isBeatingSequence();
        } else {
            return false;
        }
    }

    private void addReachablePositionsAsChildNodes(MinimaxNode node, List<Move> possibleMovesFromNode) {
        for (Move move : possibleMovesFromNode) {
            Position positionCopy = new Position(node.getPosition());
            positionOperator.performMoveOnPosition(positionCopy, move, node.isWhiteMove());
            node.addChild(positionCopy);
        }
    }
}
