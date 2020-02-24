package model.bot;

public class MinimaxBotSettings {
    private int searchingDepth;
    private boolean isAllowedExceedingSearchingDepthIfBeatingIsFound;

    public MinimaxBotSettings(int searchingDepth, boolean isAllowedExceedingSearchingDepthIfBeatingIsFound) {
        this.searchingDepth = searchingDepth;
        this.isAllowedExceedingSearchingDepthIfBeatingIsFound = isAllowedExceedingSearchingDepthIfBeatingIsFound;
    }

    public int getSearchingDepth() {
        return searchingDepth;
    }

    public boolean isAllowedExceedingSearchingDepthIfBeatingIsFound() {
        return isAllowedExceedingSearchingDepthIfBeatingIsFound;
    }
}
