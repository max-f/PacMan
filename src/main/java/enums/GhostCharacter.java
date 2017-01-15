package enums;

import javafx.scene.paint.Color;

import static javafx.scene.paint.Color.*;

public enum GhostCharacter {

    OIKAKE(RED), MACHIBUSE(PINK), KIMAGURE(CYAN), OTOBOKE(ORANGE);

    private final Color color;

    /**
     * Returns the color of the ghost
     *
     * @return the color of the ghost
     */
    public Color getColor() {
        return color;
    }

    private GhostCharacter(Color color) {
        this.color = color;
    }
}
