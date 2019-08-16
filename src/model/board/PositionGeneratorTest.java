package model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class PositionGeneratorTest {
    private PositionGenerator positionGenerator;
    private Position generatedPosition;
    private Position expectedPosition;
    private BitSet expectedWhitePieces;
    private BitSet expectedBlackPieces;


    @BeforeEach
    void init() {
        positionGenerator = new PositionGenerator();
        // notice: kings BitSet is initialized in Position class, it's always empty and of the size of whitePieces bitSet
    }

    /*
    Board of sideLength = 8: total BitSet size = 45
                |  39  38  37  36|
                |35  34  33  32  |
                |  30  29  28  27|
                |26  25  24  23  |
                |  21  20  19  18|
                |17  16  15  14  |
                |  12  11  10  09|
                |08  07  06  05  |
    */
    @Test
    void shouldReturnProperPositionForEightBoardSideLength() {
        initializeBitSetsOfSize(45);
        expectedWhitePieces.set(5, 13);
        expectedWhitePieces.set(14, 18);
        expectedBlackPieces.set(27, 31);
        expectedBlackPieces.set(32, 40);

        expectedPosition = new Position(expectedWhitePieces, expectedBlackPieces);
        generatedPosition = positionGenerator.generateStartingPositionForBoardOfSideLength(8);

        assertEquals(expectedPosition.getWhitePieces(), generatedPosition.getWhitePieces());
        assertEquals(expectedPosition.getBlackPieces(), generatedPosition.getBlackPieces());
        assertEquals(expectedPosition.getKings(), generatedPosition.getKings());
    }

    /*
        Board of sideLength = 10: total BitSet size = 66
      |  59  58  57  56  55|
      |54  53  52  51  50  |
      |  48  47  46  45  44|
      |43  42  41  40  39  |
      |  37  36  35  34  33|
      |32  31  30  29  28  |
      |  26  25  24  23  22|
      |21  20  19  18  17  |
      |  15  14  13  12  11|
      |10  09  08  07  06  |
     */

    @Test
    void shouldReturnProperPositionForTenBoardSideLength() {
        initializeBitSetsOfSize(66);
        expectedWhitePieces.set(6, 16);
        expectedWhitePieces.set(17, 27);
        expectedBlackPieces.set(39, 49);
        expectedBlackPieces.set(50, 60);

        expectedPosition = new Position(expectedWhitePieces, expectedBlackPieces);
        generatedPosition = positionGenerator.generateStartingPositionForBoardOfSideLength(10);

        assertEquals(expectedPosition.getWhitePieces(), generatedPosition.getWhitePieces());
        assertEquals(expectedPosition.getBlackPieces(), generatedPosition.getBlackPieces());
        assertEquals(expectedPosition.getKings(), generatedPosition.getKings());
    }

    /*
    Board of sideLength = 12: total BitSet size = 91
      |  83  82  81  80  79  78|
      |77  76  75  74  73  72  |
      |  70  69  68  67  66  65|
      |64  63  62  61  60  59  |
      |  57  56  55  54  53  52|
      |51  50  49  48  47  46  |
      |  44  43  42  41  40  39|
      |38  37  36  35  34  33  |
      |  31  30  29  28  27  26|
      |25  24  23  22  21  20  |
      |  18  17  16  15  14  13|
      |12  11  10  09  08  07  |
     */

    @Test
    void shouldReturnProperPositionForTwelveBoardSideLength() {
        initializeBitSetsOfSize(91);
        expectedWhitePieces.set(7, 19);
        expectedWhitePieces.set(20, 32);
        expectedWhitePieces.set(33, 39);
        expectedBlackPieces.set(52, 58);
        expectedBlackPieces.set(59, 71);
        expectedBlackPieces.set(72, 84);

        expectedPosition = new Position(expectedWhitePieces, expectedBlackPieces);
        generatedPosition = positionGenerator.generateStartingPositionForBoardOfSideLength(12);

        assertEquals(expectedPosition.getWhitePieces(), generatedPosition.getWhitePieces());
        assertEquals(expectedPosition.getBlackPieces(), generatedPosition.getBlackPieces());
        assertEquals(expectedPosition.getKings(), generatedPosition.getKings());
    }

    @Test
    void shouldReturnNullForUnavailableBoardSideLength() {
        generatedPosition = positionGenerator.generateStartingPositionForBoardOfSideLength(5);

        assertNull(generatedPosition);
    }

    private void initializeBitSetsOfSize(int bitSetSize) {
        expectedWhitePieces = new BitSet(bitSetSize);
        expectedBlackPieces = new BitSet(bitSetSize);
    }
}
