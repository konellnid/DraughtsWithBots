package model.move_finder;

import java.util.BitSet;

public class BasicBitSets {
    private BitSet freeTileNumbers;
    private BitSet ownCheckers;
    private BitSet ownKings;
    private BitSet enemyPieces;

    public BasicBitSets() {
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
