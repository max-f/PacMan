package game;

import javafx.geometry.Point2D;

import java.util.List;

public interface BoardInfo {

    FieldInfo getFieldInfo(Point2D point);

    FieldInfo getFieldInfo(int x, int y);

    List<Point2D> getGhostsStart();

    Point2D getPacManStart();

    int getPacDotsOnStart();

    int getNumberOfRows();

    int getNumberOfColumns();

}
