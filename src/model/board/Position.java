package model.board;

import model.move_finder.PossibleMovesFinder;

import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;

public class Position {

    /* Board of sideLength = 8: total BitSet size = 45
        |  39  38  37  36|
        |35  34  33  32  |
        |  30  29  28  27|
        |26  25  24  23  |
        |  21  20  19  18|
        |17  16  15  14  |
        |  12  11  10  09|
        |08  07  06  05  |
     */

    private static final String NO_SUCH_PIECE_MESSAGE = "There is no such piece!";
    private static final String ILLEGAL_MOVE_MESSAGE = "It is not possible to make such a move!";

    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

    public BitSet getWhitePieces() {
        return whitePieces;
    }

    public BitSet getBlackPieces() {
        return blackPieces;
    }

    public BitSet getKings() {
        return kings;
    }

    public Position() {
        whitePieces = new BitSet(45);
        blackPieces = new BitSet(45);
        kings = new BitSet(45);
    }

    public Position(BitSet whitePieces, BitSet blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        kings = new BitSet(whitePieces.size());
    }

    public Position(Position position) {
        whitePieces = (BitSet) position.getWhitePieces().clone();
        blackPieces = (BitSet) position.getBlackPieces().clone();
        kings = (BitSet) position.getKings().clone();
    }

    void generatePosition(BitSet whitePieces, BitSet blackPieces, BitSet kings) {
        this.whitePieces = (BitSet) whitePieces.clone();
        this.blackPieces = (BitSet) blackPieces.clone();
        this.kings = (BitSet) kings.clone();
    }

    public void generateStartingPosition() {
        whitePieces.set(5, 13);
        whitePieces.set(14, 18);

        blackPieces.set(27, 31);
        blackPieces.set(32, 40);
    }

    public void performMoveOnPosition(Move move, boolean isWhiteMove) {
        int startingTile = move.getStartingPositionOfThePiece();
        int endTile = move.getLastPositionOfThePiece();
        BitSet currentBitSet = getCurrentBitSet(isWhiteMove);

        checkArgument(currentBitSet.get(startingTile), NO_SUCH_PIECE_MESSAGE);
        checkArgument(new PossibleMovesFinder().isMovePossible(this, move, isWhiteMove), ILLEGAL_MOVE_MESSAGE);

        if(isKing(startingTile)) {
            performKingMove(startingTile, endTile, currentBitSet);
        } else {
            performPieceMove(startingTile, endTile, currentBitSet);
            if(shouldPieceBePromoted(endTile, isWhiteMove)) {
                promote(endTile);
            }
        }
        if(move.isBeatingSequence()) {
            removeBeatenPiecesFromBoard(move);
        }
    }

    private BitSet getCurrentBitSet(boolean isWhiteMove) {
        if(isWhiteMove) {
            return whitePieces;
        }
        return blackPieces;
    }

    private boolean isKing(int piecePosition) {
        return kings.get(piecePosition);
    }

    private void performKingMove(int startingTile, int endTile, BitSet currentBitSet) {
        kings.set(endTile);
        performPieceMove(startingTile, endTile, currentBitSet);
    }

    private void performPieceMove(int startingTile, int endTile, BitSet currentBitSet) {
        currentBitSet.set(endTile);
        removePieceFromBoard(startingTile);
    }

    private void removePieceFromBoard(int bitIndex) {
        whitePieces.clear(bitIndex);
        blackPieces.clear(bitIndex);
        kings.clear(bitIndex);
    }

    private void removeBeatenPiecesFromBoard(Move move) {
        boolean isEnemyPiece = false;
        for(int piecePosition: move.getMoveSequence()) {
            if(isEnemyPiece) {
                removePieceFromBoard(piecePosition);
            }
            isEnemyPiece = !isEnemyPiece;
        }
    }

    public boolean shouldPieceBePromoted(int piecePosition, boolean isWhite) {
        checkArgument(getCurrentBitSet(isWhite).get(piecePosition), NO_SUCH_PIECE_MESSAGE);
        //TODO this should be changed as now it only checks whether proper color piece reached promoting row, no matter if the piece is already promoted (works fine for the view, but may be changed)
        if (isWhite) {
            return isInPromotionZoneForWhite(piecePosition);
        } else {
            return isInPromotionZoneForBlack(piecePosition);
        }
    }

    private void promote(int pieceToPromote) {
        kings.set(pieceToPromote);
    }

    private boolean isInPromotionZoneForWhite(int piecePosition) {
        return piecePosition > 35;
    }

    private boolean isInPromotionZoneForBlack(int piecePosition) {
        return piecePosition < 9;
    }

    public int getBoardSideLength() {
        switch (whitePieces.size()) {
            case 45:
                return 8;
            case 66:
                return 10;
            case 91:
                return 12;
            default:
                return -1;
        }
    }
}
