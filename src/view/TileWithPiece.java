package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;


class TileWithPiece extends Group {
    private final int INVISIBLE = 0;
    private final int VISIBLE = 1;

    private Circle checkerCircle;
    private Circle promotionCircle;
    private Rectangle square;
    private Text textOnTile;

    TileWithPiece(Rectangle square, int tileNumber) {
        this.square = square;
        square.setStroke(Color.BLACK);
        square.setStrokeType(StrokeType.INSIDE);
        square.setStrokeWidth(0);
        double squareSideLength = square.getHeight();

        checkerCircle = new Circle(squareSideLength / 3);
        checkerCircle.setCenterX(squareSideLength / 2);
        checkerCircle.setCenterY(squareSideLength / 2);
        checkerCircle.setOpacity(INVISIBLE);

        promotionCircle = new Circle(squareSideLength / 6);
        promotionCircle.setFill(Color.BLACK);
        promotionCircle.setCenterX(squareSideLength / 2);
        promotionCircle.setCenterY(squareSideLength / 2);
        promotionCircle.setOpacity(INVISIBLE);

        createText(tileNumber);

        getChildren().addAll(square, checkerCircle, promotionCircle, textOnTile);
    }

    private void createText(int tileNumber) {
        String textToAdd = String.valueOf(tileNumber);

        textOnTile = new Text(textToAdd);
        textOnTile.setX(4);
        textOnTile.setY(13);
        textOnTile.setFill(Color.WHITE);
        textOnTile.setOpacity(INVISIBLE);
    }

    void showChecker(Color color, boolean isPromoted) {
        checkerCircle.setFill(color);
        checkerCircle.setOpacity(VISIBLE);
        if (isPromoted) promotionCircle.setOpacity(1);
        else promotionCircle.setOpacity(0);
    }

    void removeChecker() {
        checkerCircle.setOpacity(INVISIBLE);
        promotionCircle.setOpacity(INVISIBLE);
    }

    void highlight() {
        square.setStrokeWidth(5);
    }

    void removeHighlight() {
        square.setStrokeWidth(0);
    }

    void promote() {
        promotionCircle.setOpacity(VISIBLE);
    }

    void copyPropertiesFrom(TileWithPiece tileToCopyProperties) {
        checkerCircle.setFill(tileToCopyProperties.getCheckerColor());
        checkerCircle.setOpacity(VISIBLE);

        if (tileToCopyProperties.isPromoted()) promotionCircle.setOpacity(VISIBLE);
        else promotionCircle.setOpacity(INVISIBLE);
    }

    public void showText() {
        textOnTile.setOpacity(VISIBLE);
    }

    public void hideText() {
        textOnTile.setOpacity(INVISIBLE);
    }

    private boolean isPromoted() {
        return promotionCircle.getOpacity() == VISIBLE;
    }

    private Paint getCheckerColor() {
        return checkerCircle.getFill();
    }
}
