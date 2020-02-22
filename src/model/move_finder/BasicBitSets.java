package model.move_finder;

import model.board.Position;

import java.util.BitSet;

public class BasicBitSets {
    private BitSet freeTileNumbers;
    private BitSet ownCheckers;
    private BitSet ownKings;
    private BitSet enemyPieces;

    public BasicBitSets(Position position, boolean isWhiteMove, BitwiseOperator bitwiseOperator) {
        BitSet ownPieces;
        if (isWhiteMove) {
            ownPieces = position.getWhitePieces();
            enemyPieces = (BitSet) position.getBlackPieces().clone();
        } else {
            ownPieces = position.getBlackPieces();
            enemyPieces = (BitSet) position.getWhitePieces().clone();
        }

        freeTileNumbers = bitwiseOperator.merge(position.getWhitePieces(), position.getBlackPieces());
        freeTileNumbers.flip(0, freeTileNumbers.size());
        freeTileNumbers.and(bitwiseOperator.getExistingTileNumbers());
        ownCheckers = bitwiseOperator.getOwnCheckers(ownPieces, position.getKings());
        ownKings = bitwiseOperator.getOwnKings(ownPieces, position.getKings());
    }

    public BitSet getFreeTileNumbers() {
        return freeTileNumbers;
    }

    public BitSet getOwnCheckers() {
        return ownCheckers;
    }

    public BitSet getOwnKings() {
        return ownKings;
    }

    public BitSet getEnemyPieces() {
        return enemyPieces;
    }
}
