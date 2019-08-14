package model.board;

import java.util.BitSet;

public class Position {
    private BitSet whitePieces;
    private BitSet blackPieces;
    private BitSet kings;

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

    public BitSet getWhitePieces() {
        return whitePieces;
    }

    public BitSet getBlackPieces() {
        return blackPieces;
    }

    public BitSet getKings() {
        return kings;
    }

    void removePieceFromBoard(int bitIndex) {
        whitePieces.clear(bitIndex);
        blackPieces.clear(bitIndex);
        kings.clear(bitIndex);
    }

    boolean isKing(int piecePosition) {
        return kings.get(piecePosition);
    }

    public void setAsKing(int endTileNumber) {
        kings.set(endTileNumber);
    }


}

