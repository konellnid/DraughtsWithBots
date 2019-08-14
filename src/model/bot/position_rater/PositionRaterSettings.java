package model.bot.position_rater;

public class PositionRaterSettings {
    private int pointsPerChecker;
    private int pointsPerKing;
    private boolean isBonusForBeingCloserToPromotionLineActive; // from 0 to 6 points, the closer the checker is to the promotion line, the more points
    private boolean isBonusForKingControllingMainDiagonalActive; // 5 points if player has king on the main diagonal and enemy has no pieces on it

    public PositionRaterSettings() {
        // default
        pointsPerChecker = 10;
        pointsPerKing = 20;
        isBonusForBeingCloserToPromotionLineActive = true;
        isBonusForKingControllingMainDiagonalActive = true;
    }

    public int getPointsPerChecker() {
        return pointsPerChecker;
    }

    public int getPointsPerKing() {
        return pointsPerKing;
    }

    public boolean isBonusForBeingCloserToPromotionLineActive() {
        return isBonusForBeingCloserToPromotionLineActive;
    }

    public boolean isBonusForKingControllingMainDiagonalActive() {
        return isBonusForKingControllingMainDiagonalActive;
    }

    public void setPointsPerChecker(int pointsPerChecker) {
        this.pointsPerChecker = pointsPerChecker;
    }

    public void setPointsPerKing(int pointsPerKing) {
        this.pointsPerKing = pointsPerKing;
    }

    public void setBonusForBeingCloserToPromotionLineActive(boolean bonusForBeingCloserToPromotionLineActive) {
        isBonusForBeingCloserToPromotionLineActive = bonusForBeingCloserToPromotionLineActive;
    }

    public void setBonusForKingControllingMainDiagonalActive(boolean bonusForKingControllingMainDiagonalActive) {
        isBonusForKingControllingMainDiagonalActive = bonusForKingControllingMainDiagonalActive;
    }
}
