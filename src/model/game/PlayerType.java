package model.game;

public enum PlayerType {
    REAL_PLAYER("Real player"),
    RANDOM_BOT("Random bot"),
    RANDOM_PIECE_BOT("Random piece bot");

    private String labelForComboBox;

    PlayerType(String labelForComboBox) {
        this.labelForComboBox = labelForComboBox;
    }

    @Override
    public String toString() {
        return labelForComboBox;
    }
}
