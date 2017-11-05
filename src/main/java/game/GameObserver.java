package game;

import enums.ActorType;

/**
 * GameObserver can register at a {@code GameObservable} to be notified when the
 * state of the observable changes.
 */
public interface GameObserver {

    /**
     * This method is called by the GameObservable when the game is over.
     */
    void gameOver();

    /**
     * This method is called by the GameObservable every time a power pellet was
     * eaten.
     */
    void startPowerPelletMode();

    /**
     * This method is called by the GameObservable every time the time for a
     * power pellet is lapsed.
     */
    void endPowerPelletMode();

    /**
     * This method is called by the GameObservable every time Pac-Man was eaten.
     */
    void pacManDied();

    /**
     * This method is called by the GameObservable every time an extra item is
     * placed on a field.
     *
     * @param p The coordinates of the field where the extra item has been placed.
     */
    void extraItemPlaced(Point p);

    /**
     * This method is called by the GameObservable every time an extra item is
     * eaten or the time, specified by {@code PacManGame#DURATION_EXTRAITEM} is
     * lapsed.
     */
    void extraItemVanished();

    /**
     * This method is called by the GameObservable every time an actor has been
     * set.
     *
     * @param actortype The type of the actor that has been set.
     * @param x         The x-coordinate of the field where the actor has been set.
     * @param y         The y-coordinate of the field where the actor has been set.
     */
    void actorSet(ActorType actortype, int x, int y);

    /**
     * This method is called by the GameObservable every time an actor has been
     * removed.
     *
     * @param actortype The type of the actor that has been removed.
     * @param x         The x-coordinate of the field where the actor has been removed.
     * @param y         The y-coordinate of the field where the actor has been removed.
     */
    void actorRemoved(ActorType actortype, int x, int y);

    /**
     * This method is called by the GameObservable every time a step is done.
     */
    void stepDone();

    /**
     * This method is called by the GameObservable every time a new stage comes
     * up.
     */
    void nextStage();

}

