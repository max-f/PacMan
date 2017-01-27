package enums;

public enum ExtraItem {
    CHERRY(50), BANANA(100), ORANGE(200), STRAWBERRY(400);

    private int points;

    private ExtraItem(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
