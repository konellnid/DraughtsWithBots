package model.move_finder;

public class MoveFinderSettings {
    private boolean isFlyingKingEnabled;
    private boolean isCheckerBeatingBackwardsEnabled;

    public MoveFinderSettings(boolean isFlyingKingEnabled, boolean isCheckerBeatingBackwardsEnabled) {
        this.isFlyingKingEnabled = isFlyingKingEnabled;
        this.isCheckerBeatingBackwardsEnabled = isCheckerBeatingBackwardsEnabled;
    }

    public boolean isFlyingKingEnabled() {
        return isFlyingKingEnabled;
    }

    public boolean isCheckerBeatingBackwardsEnabled() {
        return isCheckerBeatingBackwardsEnabled;
    }
}
