package model.move_finder;

import java.util.BitSet;

public class BitwiseOperator {
    private BitSet tilePositions;

    public BitwiseOperator() {
        generateTilePositions();
    }

    public BitSet merge(BitSet whitePieces, BitSet blackPieces) {
        BitSet allPieces = (BitSet) whitePieces.clone();
        allPieces.or(blackPieces);
        return allPieces;
    }

    public BitSet getOwnCheckers(BitSet ownPieces, BitSet kings) {
        BitSet ownCheckers = (BitSet) kings.clone();
        ownCheckers.flip(0, ownCheckers.size());
        ownCheckers.and(ownPieces);
        return ownCheckers;
    }

    public BitSet getOwnKings(BitSet ownPieces, BitSet kings) {
        BitSet ownKings = (BitSet) kings.clone();
        ownKings.and(ownPieces);
        return ownKings;
    }

    private void generateTilePositions() {
        tilePositions = new BitSet(45);
        tilePositions.set(5, 13);
        tilePositions.set(14, 22);
        tilePositions.set(23, 31);
        tilePositions.set(32, 40);
    }

    public BitSet getTilePositions() {
        return tilePositions;
    }

    public BitSet getShiftedCopy(BitSet bitSetToShift, int shift) {
        BitSet shiftedCopy = new BitSet(bitSetToShift.size());

        for (int i = bitSetToShift.nextSetBit(0); i >= 0; i = bitSetToShift.nextSetBit(i + 1)) {
            int positionInShiftedCopy = i + shift;
            if (positionInShiftedCopy >= 0 && positionInShiftedCopy < shiftedCopy.size()) {
                shiftedCopy.set(positionInShiftedCopy);
            }
        }

        shiftedCopy.and(tilePositions);

        return shiftedCopy;
    }
}
