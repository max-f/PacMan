package game;

import enums.ActorType;
import enums.Direction;
import view.ActorController;

public abstract class Actor {

    protected ActorType actorType;
    protected Point position;

    private ActorController autoplayer = null;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.position = position;
    }

    public void setPosition(int x, int y) {
        position = new Point(x, y);
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public void initController(ActorController controller) {
        this.autoplayer = controller;
    }

    public Direction getMove() {
        if (autoplayer != null) {
            return autoplayer.getMove();
        }
        return null;
    }
}
