package game;


import enums.ActorType;
import javafx.geometry.Point2D;

public abstract class Actor {

    protected ActorType actorType;
    protected Point2D position;

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        if (position == null) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.position = position;
    }

    public void setPosition(int x, int y) {
        position = new Point2D(x, y);
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }
}
