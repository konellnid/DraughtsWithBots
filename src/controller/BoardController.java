package controller;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.board.Move;
import model.board.Position;
import model.input_handler.PlayerInputHandler;
import view.BoardView;


import java.util.BitSet;

public class BoardController {
    private BoardView boardView;
    private int currentHighlightedTileNumber;
    private PlayerInputHandler pressedTilesObserver;

    BoardController(StackPane boardStackPane) {
        boardView = new BoardView(boardStackPane, this);
        currentHighlightedTileNumber = 0;
    }

    public void tileClicked(Integer tileNumber) {
        if (pressedTilesObserver != null) {
            pressedTilesObserver.tileGotPressed(tileNumber);
        }
    }

    public void highlight(Integer clickedTileNumber) {
        if (currentHighlightedTileNumber != 0) boardView.removeHighlight(currentHighlightedTileNumber);
        currentHighlightedTileNumber = clickedTileNumber;
        boardView.highLight(clickedTileNumber);
    }


    public void showPositionOnBoard(Position position) {
        boardView.generateBoardWithProperTilesWithPieces(position.getBoardSideLength());

        showOnBoardInColour(position.getWhitePieces(), Color.WHITE);
        showOnBoardInColour(position.getBlackPieces(), Color.RED);
    }

    private void showOnBoardInColour(BitSet pieces, Color color) {
        for (int pieceTileNumber = pieces.length(); (pieceTileNumber = pieces.previousSetBit(pieceTileNumber - 1)) >= 0; ) {
            boardView.showChecker(pieceTileNumber, color);
        }
    }

    public void makeBeatingMove(Move smallBeatingMove) {
        boardView.moveChecker(smallBeatingMove.getStartingPositionOfThePiece(), smallBeatingMove.getLastPositionOfThePiece());
        Integer enemyPosition = smallBeatingMove.getMoveSequence().get(1);
        boardView.removeChecker(enemyPosition);
        highlight(smallBeatingMove.getLastPositionOfThePiece());

    }

    public void makeStandardMove(Move move) {
        boardView.moveChecker(move.getStartingPositionOfThePiece(), move.getLastPositionOfThePiece());
        highlight(move.getLastPositionOfThePiece());
    }

    public void promote(int pieceToPromote) {
        boardView.promote(pieceToPromote);
    }

    public void addNewPressedTilesObserver(PlayerInputHandler playerInputHandler) {
        pressedTilesObserver = playerInputHandler;
    }

    public void removePressedTilesObserver() {
        pressedTilesObserver = null;
    }

    public void setShowingTextOnTiles(boolean shouldBeShown) {
        if (shouldBeShown) boardView.showTextOnTiles();
        else boardView.hideTextOnTiles();

    }
}
