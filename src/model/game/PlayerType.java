package model.game;

public enum PlayerType {
    REAL_PLAYER("Real player"),
    RANDOM_BOT("Random bot");

    private String labelForComboBox;

    PlayerType(String labelForComboBox) {
        this.labelForComboBox = labelForComboBox;
    }

    @Override
    public String toString() {
        return labelForComboBox;
    }
}
