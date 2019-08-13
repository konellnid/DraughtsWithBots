package model.move_finder;

import java.util.BitSet;

public class BitwiseOperator {
    private BitSet existingTileNumbers;

    public BitwiseOperator(int boardSideLength) {
        generateTilePositions(boardSideLength);
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

    private void generateTilePositions(int boardSideLength) {
        initializeProperSizeBitSet(boardSideLength);
        findExistingTileNumbersForBoardSize(boardSideLength);
    }

    private void findExistingTileNumbersForBoardSize(int boardSideLength) {
        int smallestNumberInTwoRows = (boardSideLength / 2) + 1;

        for (int numberOfRowsCounter = 0; numberOfRowsCounter < boardSideLength; numberOfRowsCounter += 2) {
            existingTileNumbers.set(smallestNumberInTwoRows, smallestNumberInTwoRows + boardSideLength);
            smallestNumberInTwoRows = smallestNumberInTwoRows + boardSideLength + 1;
        }
    }

    private void initializeProperSizeBitSet(int boardSideLength) {
        switch (boardSideLength) {
            case 8:
                existingTileNumbers = new BitSet(45);
                break;
            case 10:
                existingTileNumbers = new BitSet(66);
                break;
            case 12:
                existingTileNumbers = new BitSet(91);
                break;
        }


    }

    public BitSet getExistingTileNumbers() {
        return existingTileNumbers;
    }

    public BitSet getShiftedCopy(BitSet bitSetToShift, int shift) {
        BitSet shiftedCopy = new BitSet(bitSetToShift.size());

        for (int i = bitSetToShift.nextSetBit(0); i >= 0; i = bitSetToShift.nextSetBit(i + 1)) {
            int positionInShiftedCopy = i + shift;
            if (positionInShiftedCopy >= 0 && positionInShiftedCopy < shiftedCopy.size()) {
                shiftedCopy.set(positionInShiftedCopy);
            }
        }

        shiftedCopy.and(existingTileNumbers);

        return shiftedCopy;
    }
}
