package view;

import controller.MainViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.game.PlayerType;

public class MainView {
    @FXML
    private Button newGameButton;

    @FXML
    private Label resultLabel;

    @FXML
    private ComboBox<PlayerType> whiteSettingsComboBox;

    @FXML
    private ComboBox<PlayerType> blackSettingsComboBox;

    @FXML
    private Label movesTillDrawLabel;

    @FXML
    private StackPane boardStackPane;

    @FXML
    private CheckBox showTileNumbersCheckBox;

    @FXML
    private Slider boardSizeSlider;

    private MainViewController mainViewController;

    @FXML
    public void initialize() {
        whiteSettingsComboBox.getItems().setAll(PlayerType.values());
        whiteSettingsComboBox.getSelectionModel().select(1);

        blackSettingsComboBox.getItems().setAll(PlayerType.values());
        blackSettingsComboBox.getSelectionModel().select(1);

        resultLabel.setText("");

        newGameButton.setOnAction(event -> {
            newGameButton.setDisable(true);
            setResult("");

            int whiteChoice = whiteSettingsComboBox.getSelectionModel().getSelectedIndex();
            int blackChoice = blackSettingsComboBox.getSelectionModel().getSelectedIndex();

            int boardSize = (int) boardSizeSlider.getValue();

            mainViewController.newGame(PlayerType.values()[whiteChoice], PlayerType.values()[blackChoice], boardSize);
        });

        showTileNumbersCheckBox.setOnAction(event -> {
            if (showTileNumbersCheckBox.isSelected()) mainViewController.setShowingTextOnTiles(true);
            else mainViewController.setShowingTextOnTiles(false);
        });

    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public StackPane getBoardStackPane() {
        return boardStackPane;
    }

    public void setResult(String resultText) {
        Platform.runLater(() -> {
            resultLabel.setText(resultText);
            newGameButton.setDisable(false);
        });
    }

    public void setMovesTillDraw(int movesTillDraw) {
        Platform.runLater(() -> movesTillDrawLabel.setText(String.valueOf(movesTillDraw)));
    }
}
