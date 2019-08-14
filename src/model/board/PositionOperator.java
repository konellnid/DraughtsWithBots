package model.board;

import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;

public class PositionOperator {
    private int boardSideLength;
    private int[] whitePromotionZone;
    private int[] blackPromotionZone;
    private boolean pieceWasPromotedDuringLastMove;

    private static final String NO_SUCH_PIECE_MESSAGE = "There is no such piece!";

    public PositionOperator(int boardSideLength) {
        this.boardSideLength = boardSideLength;
        setUpPromotionZones();
    }

    private void setUpPromotionZones() {
        switch (boardSideLength) {
            case 8:
                whitePromotionZone = new int[]{36, 37, 38, 39};
                blackPromotionZone = new int[]{5, 6, 7, 8};
                break;
            case 10:
                whitePromotionZone = new int[]{55, 56, 57, 58, 59};
                blackPromotionZone = new int[]{6, 7, 8, 9, 10};
                break;
            case 12:
                whitePromotionZone = new int[]{78, 79, 80, 81, 82, 83};
                blackPromotionZone = new int[]{7, 8, 9, 10, 11, 12};
                break;
        }
    }

    public void performMoveOnPosition(Position position, Move move, boolean isWhiteMove) {
        pieceWasPromotedDuringLastMove = false;

        int startingTileNumber = move.getStartingPositionOfThePiece();
        int endTileNumber = move.getLastPositionOfThePiece();
        BitSet currentBitSet = getProperBitSetFromPosition(position, isWhiteMove);

        checkArgument(currentBitSet.get(startingTileNumber), NO_SUCH_PIECE_MESSAGE);

        if (move.isBeatingSequence()) {
            removeBeatenPiecesFromPosition(position, move);
        }

        if (position.isKing(startingTileNumber)) {
            position.removePieceFromBoard(startingTileNumber);
            currentBitSet.set(endTileNumber);
            position.setAsKing(endTileNumber);
        } else {
            position.removePieceFromBoard(startingTileNumber);
            currentBitSet.set(endTileNumber);

            if (shouldPieceBePromoted(endTileNumber, isWhiteMove)) {
                position.setAsKing(endTileNumber);
                pieceWasPromotedDuringLastMove = true;
            }
        }
    }

    private boolean shouldPieceBePromoted(int endTileNumber, boolean isWhiteMove) {
        if (isWhiteMove) return isTileNumberPartOfPromotionZoneTileNumbers(whitePromotionZone, endTileNumber);
        else return isTileNumberPartOfPromotionZoneTileNumbers(blackPromotionZone, endTileNumber);
    }

    private boolean isTileNumberPartOfPromotionZoneTileNumbers(int[] promotionZoneTileNumbers, int endTileNumber) {
        for (int promotionTileNumber : promotionZoneTileNumbers) {
            if (promotionTileNumber == endTileNumber) return true;
        }

        return false;
    }

    private void removeBeatenPiecesFromPosition(Position position, Move move) {
        for (int beatenPieceTileNumberInMovePlace = 1; beatenPieceTileNumberInMovePlace < move.size(); beatenPieceTileNumberInMovePlace += 2) {
            int beatenPieceTileNumber = move.get(beatenPieceTileNumberInMovePlace);
            position.removePieceFromBoard(beatenPieceTileNumber);
        }
    }

    private BitSet getProperBitSetFromPosition(Position position, boolean isWhiteMove) {
        if (isWhiteMove) return position.getWhitePieces();
        else return position.getBlackPieces();
    }
}
