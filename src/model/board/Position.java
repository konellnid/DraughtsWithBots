package model.board;

import model.move_finder.PossibleMovesFinder;

import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;

public class Position {
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

