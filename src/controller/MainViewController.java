package controller;

import model.game.Game;
import model.game.PlayerType;
import model.move_finder.MoveFinderSettings;
import view.MainView;

public class MainViewController {
    private MainView mainView;
    private BoardController boardController;

    public MainViewController(MainView mainView) {
        this.mainView = mainView;
        boardController = new BoardController(mainView.getBoardStackPane());

    }

    public void newGame(PlayerType whitePlayerType, PlayerType blackPlayerType, int boardSize) {
        Game game = new Game(boardController, this);
        MoveFinderSettings moveFinderSettings = new MoveFinderSettings(mainView.isFlyingKingEnabled(), mainView.isCheckerBeatingBackwardEnabled());
        game.newGame(whitePlayerType, blackPlayerType, boardSize, moveFinderSettings);
    }

    public void setResult(String resultText) {
        mainView.setResult(resultText);
    }

    public void updateMovesTillDraw(int movesTillDraw) {
        mainView.setMovesTillDraw(movesTillDraw);
    }

    public void setShowingTextOnTiles(boolean shouldBeShown) {
        boardController.setShowingTextOnTiles(shouldBeShown);
    }
}
