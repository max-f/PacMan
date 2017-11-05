package game;

import java.util.List;

public interface BoardInfo {

    FieldInfo getFieldInfo(Point point);

    FieldInfo getFieldInfo(int x, int y);

    List<Point> getGhostsStart();

    Point getPacManStart();

    int getPacDotsOnStart();

    int getNumberOfRows();

    int getNumberOfColumns();

}
