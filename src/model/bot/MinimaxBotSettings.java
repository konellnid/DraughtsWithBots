package model.bot;

public class MinimaxBotSettings {
    private int searchingDepth;
    private boolean isAllowedExceedingSearchingDepthIfBeatingIsFound;
    private int toleranceField; // after finding the best rating, chooses a random

    public MinimaxBotSettings(int searchingDepth, boolean isAllowedExceedingSearchingDepthIfBeatingIsFound, int toleranceField) {
        this.searchingDepth = searchingDepth;
        this.isAllowedExceedingSearchingDepthIfBeatingIsFound = isAllowedExceedingSearchingDepthIfBeatingIsFound;
        this.toleranceField = toleranceField;
    }

    public int getSearchingDepth() {
        return searchingDepth;
    }

    public boolean isAllowedExceedingSearchingDepthIfBeatingIsFound() {
        return isAllowedExceedingSearchingDepthIfBeatingIsFound;
    }

    public int getToleranceField() {
        return toleranceField;
    }
}
