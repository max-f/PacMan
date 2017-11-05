package game;

public class Point {

    /**
     * C style struct
     */

    public final int x;
    public final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point [x = " + x + ", y = " + y + "]";
    }
}
