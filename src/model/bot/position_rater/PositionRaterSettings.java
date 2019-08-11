package model.bot.position_rater;

public class PositionRaterSettings {
    private int pointsPerChecker;
    private int pointsPerKing;
    private boolean isBonusForBeingCloserToPromotionLineActive; // from 0 to 6 points, the closer the checker is to the promotion line, the more points
    private boolean isBonusForKingControllingMainDiagonalActive; // 5 points if player has king on the main diagonal and enemy has no pieces on it
}
