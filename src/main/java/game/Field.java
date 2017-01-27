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

    public boolean hasPacDot() {
        return pacDot;
    }

    public boolean hasPowerPellet() {
        return powerPellet;
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

    public void setPacDot(boolean pacDot) {
        if (type != FieldType.FREE) {
            throw new IllegalStateException("illegal fieldType");
        }
        this.pacDot = pacDot;
    }

    public void setPowerPellet(boolean powerPellet) {
        if (type != FieldType.POWER_PELLET) {
            throw new IllegalStateException("illegal fieldtype");
        }
    }

    public void placeActor(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("bad actor argument");
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
