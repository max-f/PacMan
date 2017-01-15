package game;

import enums.GhostCharacter;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public interface GhostInfo {

    /**
     * Returns the current position of the ghost.
     *
     * @return the position of this ghost.
     */
    Point2D getPosition();

    /**
     * Returns the color of the ghost.
     *
     * @return the color of the ghost
     */
    Color getColor();

    /**
     * Returns the character of this ghost.
     *
     * @return the character of this ghost
     */
    GhostCharacter getCharacter();

}
