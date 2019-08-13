package view;

import controller.BoardController;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

/*  Board of sideLength = 8: total BitSet size = 45
      |  39  38  37  36|
      |35  34  33  32  |
      |  30  29  28  27|
      |26  25  24  23  |
      |  21  20  19  18|
      |17  16  15  14  |
      |  12  11  10  09|
      |08  07  06  05  |

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
public class BoardView {
    private static final int TILE_SIDE_LENGTH = 75;

    private GridPane boardGridPane;
    private BoardController boardController;
    private HashMap<Integer, TileWithPiece> tilesByNumberMap;

    public BoardView(StackPane boardStackPane, BoardController boardController) {
        this.boardController = boardController;

        boardGridPane = new GridPane();
        boardStackPane.getChildren().addAll(boardGridPane);

        generateHashMapWithAllPossibleTiles();
        generateBoardWithProperTilesWithPieces(8); // default board to show at start
    }

    private void generateHashMapWithAllPossibleTiles() {
        tilesByNumberMap = new HashMap<>();

        for (int tileNumber = 5; tileNumber < 84; tileNumber++) {
            Rectangle squareTile = new Rectangle(TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
            squareTile.setFill(Color.GREEN);
            TileWithPiece tileWithPiece = new TileWithPiece(squareTile, tileNumber);

            addProperActionToTileWithChecker(tileWithPiece, tileNumber);

            tilesByNumberMap.put(tileNumber, tileWithPiece);
        }
    }

    public void generateBoardWithProperTilesWithPieces(int boardSideLength) {
        resetAllTiles();
        boardGridPane.getChildren().removeAll();
        boardGridPane.setAlignment(Pos.CENTER);
        Stack<Integer> stackWithIndexes = getProperStackOfTileNumbers(boardSideLength);

        for (int row = 0; row < boardSideLength; row++) {
            for (int column = 0; column < boardSideLength; column++) {
                if ((row + column) % 2 == 0) {
                    Rectangle standardSquareTile = new Rectangle(TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
                    standardSquareTile.setFill(Color.BEIGE);
                    boardGridPane.add(standardSquareTile, column, row);
                } else {
                    Integer tileNumber = stackWithIndexes.pop();

                    TileWithPiece tileWithPiece = tilesByNumberMap.get(tileNumber);

                    boardGridPane.add(tileWithPiece, column, row);
                }
            }
        }
    }

    private Stack<Integer> getProperStackOfTileNumbers(int boardSideLength) {
        Stack<Integer> tileNumbers = new Stack<>();
        int halfOfBoardSideLength = boardSideLength / 2;

        int currentTileNumber = halfOfBoardSideLength + 1; // starting from bottom row, from right to left

        for (int currentIterationNumber = 0; currentIterationNumber < halfOfBoardSideLength; currentIterationNumber++) {
            for (int amountOfTileNumbersAddedInCurrentIteration = 0; amountOfTileNumbersAddedInCurrentIteration < boardSideLength; amountOfTileNumbersAddedInCurrentIteration++) {
                tileNumbers.push(currentTileNumber);
                currentTileNumber++;
            }

            currentTileNumber++;
        }

        return tileNumbers;
    }

    private void addProperActionToTileWithChecker(TileWithPiece tileWithPiece, Integer tileNumber) {
        tileWithPiece.setOnMouseClicked(event -> {
            boardController.tileClicked(tileNumber);
            System.out.println(tileNumber);
        });
    }

    public void showChecker(int tileNumber, Color color) {
        TileWithPiece tileWithPiece = tilesByNumberMap.get(tileNumber);

        tileWithPiece.showChecker(color, false);

    }

    public void promote(int tileNumber) {
        TileWithPiece tileWithPiece = tilesByNumberMap.get(tileNumber);

        tileWithPiece.promote();
    }

    public void moveChecker(int startingPosition, int endPosition) {
        TileWithPiece startingTile = tilesByNumberMap.get(startingPosition);
        TileWithPiece endTile = tilesByNumberMap.get(endPosition);

        endTile.copyPropertiesFrom(startingTile);
        startingTile.removePiece();

    }

    public void removeHighlight(Integer clickedTile) {
        TileWithPiece tileToRemoveHighlight = tilesByNumberMap.get(clickedTile);
        tileToRemoveHighlight.removeHighlight();
    }

    public void resetAllTiles() {
        for (TileWithPiece tileWithPiece : tilesByNumberMap.values()) {
            tileWithPiece.removeHighlight();
            tileWithPiece.removePiece();
        }
    }

    public void removeChecker(Integer enemyPosition) {
        TileWithPiece tileWithPiece = tilesByNumberMap.get(enemyPosition);
        tileWithPiece.removePiece();
    }

    public void highLight(Integer clickedTile) {
        TileWithPiece tileWithPiece = tilesByNumberMap.get(clickedTile);
        tileWithPiece.highlight();
    }

    public void showTextOnTiles() {
        tilesByNumberMap.values().forEach(TileWithPiece::showText);
    }

    public void hideTextOnTiles() {
        tilesByNumberMap.values().forEach(TileWithPiece::hideText);
    }
}
