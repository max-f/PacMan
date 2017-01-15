package game;

import enums.ActorType;
import enums.Direction;
import javafx.geometry.Point2D;

public class PacMan extends Actor {

    private Direction direction;

    public PacMan(Point2D position) {
        if (position == null) {
            throw new IllegalArgumentException("Invalid position to initialize PacMan");
        }
        this.position = position;
        this.actorType = ActorType.PACMAN;
        this.direction = Direction.LEFT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Invalid direction for PacMan");
        }
        this.direction = direction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(this.actorType);
        sb.append(", position: ").append(this.position.getX()).append(",").append(this.position.getY()).append(")");
        sb.append(", direction: ").append(this.direction);
        sb.append("]");
        return sb.toString();
    }

}
