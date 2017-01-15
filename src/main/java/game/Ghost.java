package game;

import enums.ActorType;
import enums.GhostCharacter;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Ghost extends Actor implements GhostInfo {
    private GhostCharacter character;

    public Ghost(Point2D position, GhostCharacter character) {
        if (position == null || character == null) {
            throw new IllegalArgumentException("Invalid position or character when trying to create new ghost");
        }
        this.actorType = ActorType.GHOST;
        this.position = position;
        this.character = character;
    }

    @Override
    public Color getColor() {
        return character.getColor();
    }

    @Override
    public GhostCharacter getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(this.actorType);
        sb.append(", character: ").append(this.character);
        sb.append(", position: (").append(this.position.getX()).append(",").append(this.position.getX()).append(")");
        sb.append(", color: ").append(this.character.getColor().toString());
        sb.append("]");
        return sb.toString();
    }

}
