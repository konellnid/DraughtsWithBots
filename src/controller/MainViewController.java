package controller;

import model.bot.MinimaxBotSettings;
import model.bot.position_rater.PositionRaterSettings;
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

    public MinimaxBotSettings getWhiteMinimaxBotSettings() {
        int searchDepth = mainView.getWhiteSearchDepthSliderValue();
        boolean isAllowedExceedingAtBeatings = mainView.isWhiteAllowExceedingAtBeatingCheckBoxEnabled();

        return new MinimaxBotSettings(searchDepth, isAllowedExceedingAtBeatings);
    }

    public PositionRaterSettings getWhitePositionRaterSettings() {
        PositionRaterSettings positionRaterSettings = new PositionRaterSettings();
        positionRaterSettings.setPointsPerChecker(mainView.getWhiteCheckerScoreSliderValue());
        positionRaterSettings.setPointsPerKing(mainView.getWhiteKingScoreSliderValue());
        positionRaterSettings.setBonusForBeingCloserToPromotionLineActive(mainView.isWhiteBonusForBeingCloserToPromotionLineCheckBoxSelected());
        positionRaterSettings.setBonusForKingControllingMainDiagonalActive(mainView.isWhiteBonusForControllingMainDiagonalCheckBoxSelected());

        return positionRaterSettings;
    }

    public MinimaxBotSettings getBlackMinimaxBotSettings() {
        int searchDepth = mainView.getBlackSearchDepthSliderValue();
        boolean isAllowedExceedingAtBeatings = mainView.isBlackAllowExceedingAtBeatingCheckBoxSelected();

        return new MinimaxBotSettings(searchDepth, isAllowedExceedingAtBeatings);
    }

    public PositionRaterSettings getBlackPositionRaterSettings() {
        PositionRaterSettings positionRaterSettings = new PositionRaterSettings();
        positionRaterSettings.setPointsPerChecker(mainView.getBlackCheckerScoreSliderValue());
        positionRaterSettings.setPointsPerKing(mainView.getBlackKingScoreSliderValue());
        positionRaterSettings.setBonusForBeingCloserToPromotionLineActive(mainView.isBlackBonusForBeingCloserToPromotionLineCheckBox());
        positionRaterSettings.setBonusForKingControllingMainDiagonalActive(mainView.isBlackBonusForControllingMainDiagonalCheckBoxSelected());

        return positionRaterSettings;
    }
}
