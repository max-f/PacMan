package game;

import enums.ActorType;
import enums.Event;
import enums.ExtraItem;
import enums.FieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Field {

    private FieldType type;
    private boolean pacDot;
    private boolean powerPellet;
    private ExtraItem extraItem;
    private Actor actor;

    public Field(FieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("bad fieldtype argument");
        }
        this.type = type;
    }

    public Actor getActor() {
        return actor;
    }

    public boolean hasActor() {
        return actor != null;
    }

    public boolean hasPacDot() {
        return pacDot;
    }

    public boolean hasPowerPellet() {
        return powerPellet;
    }

    public boolean hasExtraItem() {
        return extraItem != null;
    }

    public ExtraItem getExtraItem() {
        return extraItem;
    }

    public FieldType getType() {
        return type;
    }

    public ActorType getActorType() {
        if (actor == null) {
            return null;
        }
        return actor.getActorType();
    }

    public GhostInfo getGhostInfo() {
        if (actor instanceof Ghost) {
            return (GhostInfo) actor;
        }
        return null;
    }

    /**
     * Placing a pac-dot on the {@code Field}. It is only allowed to do so on a field of type FREE.
     *
     * @param pacDot if a pac-dot is to be set or not.
     * @throws IllegalStateException if the {@code Field} is not of type FREE.
     */
    public void setPacDot(boolean pacDot) {
        if (type != FieldType.FREE) {
            throw new IllegalStateException("invalid field type to place a pac-dot");
        }
        this.pacDot = pacDot;
    }

    /**
     * Placing a power pellet on the {@code Field}. It is only allowed to do so on a field of type POWER_PELLET.
     *
     * @param powerPellet if a power pellet is to be set or not.
     * @throws IllegalStateException if the {@code Field} is not of type POWER_PELLET.
     */
    public void setPowerPellet(boolean powerPellet) {
        if (type != FieldType.POWER_PELLET) {
            throw new IllegalStateException("invalid field type to place a power pellet");
        }
        this.powerPellet = powerPellet;
    }

    /**
     * Places an {@code ExtraItem} on the field. It is only allowed to do so on a field which is not of type WALL or
     * GHOST_START.
     *
     * @param extraItem the {@code ExtraItem} to place on the field.
     * @throws IllegalStateException if the {@code Field} is of type WALL or GHOST_START.
     */
    public void setExtraItem(ExtraItem extraItem) {
        if (type == FieldType.WALL || type == FieldType.GHOST_START) {
            throw new IllegalStateException("invalid field type to place an extra item");
        }
        this.extraItem = extraItem;
    }

    /**
     * Placing the {@code Actor} on the {@code Field}.
     *
     * @param actor The {@code Actor} to enter the field
     * @throws IllegalArgumentException if the given {@code Actor} is null.
     * @throws IllegalStateException    if the {@code Actor} can not be placed on the {@code Field}.
     */
    public void placeActor(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("bad actor argument");
        }
        if (this.checkActor(actor) == Event.MOVE_IMPOSSIBLE) {
            throw new IllegalStateException("actor can not be placed here");
        }
        this.actor = actor;
        if (actor.getActorType() == ActorType.PACMAN) {
            this.pacDot = false;
            this.powerPellet = false;
            this.extraItem = null;
        }
    }

    public void removeActor() {
        this.actor = null;
    }

    /**
     * This method checks what happens if the given actor enters this field and returns the corresponding event.
     *
     * @param incomingActor the ${@code Actor} that wants to enter the field.
     * @return the ${@code Event} that occurs if this actor would enter the field.
     * @throws IllegalArgumentException if null is passed as {@code incomingActor}
     */
    public Event checkActor(Actor incomingActor) {
        if (incomingActor == null) {
            throw new IllegalArgumentException("bad incoming actor argument");
        }
        Event e;
        if (incomingActor.getActorType() == ActorType.PACMAN) {
            switch (type) {
                case WALL:
                    e = Event.MOVE_IMPOSSIBLE;
                    break;
                case GHOST_START:
                    e = Event.MOVE_IMPOSSIBLE;
                    break;
                default:
                    if (actor != null) {
                        e = Event.PACMAN_GHOST_COLLISION;

                    } else {
                        e = Event.MOVE_POSSIBLE;
                    }
            }
        } else {
            switch (type) {
                case WALL:
                    e = Event.MOVE_IMPOSSIBLE;
                    break;
                default:
                    if (actor != null) {
                        if (actor.getActorType() == ActorType.GHOST) {
                            e = Event.MOVE_IMPOSSIBLE;
                        } else {
                            e = Event.PACMAN_GHOST_COLLISION;
                        }
                    } else {
                        e = Event.MOVE_POSSIBLE;
                    }
            }
        }
        return e;
    }

    /**
     * The String of a ${@code Field} should have the following format: [Fieldtype, PacDot?, PowerPellet?, ExtraItem,
     * ActorType] The last two entries are optional. If they are null, they don't appear in the result string.
     *
     * @return the string representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        List<String> fields = new ArrayList<String>();
        fields.add(type.toString());
        fields.add(Boolean.toString(pacDot));
        fields.add(Boolean.toString(powerPellet));
        if (extraItem != null) {
            fields.add(extraItem.toString());
        }
        if (actor != null) {
            fields.add(actor.getActorType().toString());
        }
        String delimiter = ", ";
        Object o = fields.stream().collect(Collectors.joining(delimiter));
        sb.append(o.toString());
        sb.append("]");
        return sb.toString();
    }
}
