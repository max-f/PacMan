package game;

import enums.ActorType;
import enums.Direction;
import enums.ExtraItem;
import enums.GhostCharacter;
import view.ControllerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static enums.ActorType.GHOST;
import static enums.ActorType.PACMAN;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PacManGame {
    private static final long DEFAULT_SEED = 250;

    private static final int PACMAN_LIVES = 3;

    /**
     * Time (in milliseconds) a power pellet will be on the board.
     */
    private static final long POWER_PELLET_TIME = SECONDS.toMillis(20);
    /**
     * Time (in milliseconds) an extra item will be on the board.
     */
    private static final long EXTRA_ITEM_TIME = SECONDS.toMillis(5);
    /**
     * Time (in milliseconds) between the appearance of new extra items.
     */
    private static final long NEW_EXTRA_ITEM_TIME = SECONDS.toMillis(20);

    private Board board;

    private boolean gameOver;
    private boolean powerPelletMode;

    /**
     * The last time the step() method was invoked (as measured by {@link System.currentTimeMillis()})
     */
    private long lastStepInvocation;

    /**
     * Counts the time until the next move for Pac-Man.
     */
    private long pacManMoveDuration;

    /**
     * Counts the time until the next move for the ghosts.
     */
    private long ghostMoveDuration;

    /**
     * Counts the time Pac-Man will still be in power pellet mode. If <= 0 the power pellet mode is over.
     */
    private long powerPelletDuration = POWER_PELLET_TIME;

    /**
     * Counts the number of milliseconds an extra item will be on the board. If <= 0 no extra item will be on the
     * board.
     */
    private long extraItemDuration;

    /**
     * Counts the time since the last placement of an extra item.
     */
    private long newExtraItemDuration;

    private Point extraItemPosition;

    private long score;

    /**
     * Indicates the current stage. The first stage has the value 1.
     */
    private int stageCounter;

    /**
     * Counts the eaten pac-dots.
     */
    private int pacDotCounter;
    private int lives;

    private Random random;

    private List<GameObserver> observers;
    private List<Ghost> ghosts;
    private PacMan pacMan;

    public PacManGame(Board board, ControllerFactory controllerFactory) {
        this(board, DEFAULT_SEED, controllerFactory);
    }

    public PacManGame(Board board, long seed, ControllerFactory controllerFactory) {
        if (board == null || controllerFactory == null) {
            throw new IllegalArgumentException("invalid board or controllerFactory");
        }
        this.board = board;
        this.board.initBoard();

        observers = new ArrayList<GameObserver>();

        extraItemDuration = EXTRA_ITEM_TIME;
        extraItemPosition = null;

        random = new Random(DEFAULT_SEED);

        setPacManOnBoard();
        setGhostsOnBoard();

        lives = PACMAN_LIVES;
        nextLife();
        stageCounter = 1;
    }

    private void setPacManOnBoard() {
        final Point pacManStart = board.getPacManStart();
        pacMan = new PacMan(pacManStart);
        board.getField(pacManStart).placeActor(pacMan);
        notifyObserversActorSet(PACMAN, pacManStart.x, pacManStart.x);
        // pacman.initController
    }

    private void setGhostsOnBoard() {
        ghosts = new ArrayList<>();
        final int startPoints = board.getGhostsStarts().size();
        if (startPoints > 0) {
            for (int i = 0; i < startPoints && i <= 3; i++) {
                final Ghost ghost = new Ghost(board.getGhostsStarts().get(i), getGhostChar(i));
                final Point ghostPosition = ghost.getPosition();
                board.getField(ghostPosition.x, ghostPosition.y).placeActor(ghost);
                notifyObserversActorSet(GHOST, ghostPosition.x, ghostPosition.y);
                // ghost initcontroller
                ghosts.add(ghost);
            }
        }
    }

    private GhostCharacter getGhostChar(final int x) {
        GhostCharacter ghostCharacter;
        switch (x) {
            case 0:
                ghostCharacter = GhostCharacter.OIKAKE;
                break;
            case 1:
                ghostCharacter = GhostCharacter.MACHIBUSE;
                break;
            case 2:
                ghostCharacter = GhostCharacter.KIMAGURE;
                break;
            case 3:
                ghostCharacter = GhostCharacter.OTOBOKE;
                break;
            default:
                throw new IllegalArgumentException("Only 4 ghost characters available");
        }
        return ghostCharacter;
    }

    public synchronized void step() {
        long duration = 1;
        if (lastStepInvocation != 0) {
            duration = System.currentTimeMillis() - lastStepInvocation;
        }
        lastStepInvocation = System.currentTimeMillis();
        step(duration);
    }

    /**
     * Performs one step in the game.
     *
     * @param duration Time past since the last step.
     */
    public synchronized void step(final long duration) {
        if (gameOver) {
            return;
        }
        if (powerPelletMode) {
            if (powerPelletDuration - duration <= 0) {
                powerPelletMode = false;
                powerPelletDuration = POWER_PELLET_TIME;
                notifyObserversEndPowerPelletMode();
            } else {
                powerPelletDuration -= duration;
            }
        }
        if (extraItemPosition != null) {
            if (extraItemDuration - duration <= 0) {
                board.getField(extraItemPosition).setExtraItem(null);
                extraItemDuration = EXTRA_ITEM_TIME;
                extraItemPosition = null;
                notifyObserversExtraItemVanished();
            } else {
                extraItemDuration -= duration;
            }
        }
        if (newExtraItemDuration + duration >= NEW_EXTRA_ITEM_TIME) {
            extraItemDuration = EXTRA_ITEM_TIME;
            newExtraItemDuration = 0;
            placeExtraItem();
        } else {
            newExtraItemDuration += duration;
        }
        int stage = stageCounter;
        if (pacManMoveDuration - duration <= 0) {
            Direction pacmanDirection = pacMan.getMove();
            if (move(pacMan, pacmanDirection)) {
                pacMan.setDirection(pacmanDirection);
            }


        }


    }

    private boolean move(final Actor actor, final Direction direction) {
        if (actor == null) {
            throw new IllegalArgumentException("Invalid actor");
        }
        if (direction == null) {
            return false;
        }

        int newX = actor.getPosition().x;
        int newY = actor.getPosition().y;
        switch (direction) {
            case UP:
                if (newX - 1 < 0) {
                    newX = board.getBoard().length - 1;
                } else {
                    newX -= 1;
                }
                break;
            case DOWN:
                if (newX + 1 == board.getBoard().length) {
                    newX = 0;
                } else {
                    newX += 1;
                }
                break;
            case LEFT:
                if (newY - 1 < 0) {
                    newY = board.getBoard()[0].length - 1;
                } else {
                    newY -= 1;
                }
                break;
            case RIGHT:
                if (newY + 1 == board.getBoard()[0].length) {
                    newY = 0;
                } else {
                    newY += 1;
                }
                break;
        }
        return turn(actor, newX, newY);
    }

    public boolean turn(final Actor actor, final int newX, final int newY) {
        Point currentPos = actor.getPosition();
        final Field newField = board.getField(newX, newY);
        switch (newField.checkActor(actor)) {
            case MOVE_IMPOSSIBLE:
                return false;
            case MOVE_POSSIBLE:
                if (actor instanceof PacMan) {
                    if (newField.hasPacDot()) {
                        board.getField(newX, newY).setPacDot(false);
                        pacDotCounter += 1;
                        score += 1;
                    }
                    if (newField.hasPowerPellet()) {
                        board.getField(newX, newY).setPowerPellet(false);
                        powerPelletMode = true;
                        powerPelletDuration = POWER_PELLET_TIME;
                        notifyObserversStartPowerPelletMode();
                        score += 30;
                    }
                    if (newField.hasExtraItem()) {
                        score += newField.getExtraItem().getPoints();
                        newField.setExtraItem(null);
                        extraItemPosition = null;
                        notifyObserversExtraItemVanished();
                    }
                }
                board.getField(currentPos.x, currentPos.y).removeActor();
                notifyObserversActorRemoved(actor.getActorType(), currentPos.x, currentPos.y);
                actor.setPosition(newX, newY);
                board.getField(currentPos).placeActor(actor);
                notifyObserversActorSet(actor.getActorType(), newX, newY);
                return true;
            case PACMAN_GHOST_COLLISION:
                if (powerPelletMode) {
                    if (actor instanceof Ghost) {

                    }
                }
        }

        return false;
    }

    private void placeExtraItem() {
        Point possibleExtraItemField;
        do {
            possibleExtraItemField = getRandomExtraItemField();
        } while (board.getField(possibleExtraItemField).hasActor());

        board.getField(possibleExtraItemField).setExtraItem(ExtraItem.values()[random.nextInt(4)]);
        extraItemPosition = possibleExtraItemField;
        notifyObserversExtraItemPlaced(extraItemPosition);
    }

    private Point getRandomExtraItemField() {
        final int randomIdx = random.nextInt(board.getExtraItemFields().size());
        return board.getExtraItemFields().get(randomIdx);
    }


    private void notifyObserversActorSet(final ActorType actorType, final int x, final int y) {
        observers.forEach(observer -> observer.actorSet(actorType, x, y));
    }

    private void notifyObserversActorRemoved(final ActorType actorType, final int x, final int y) {
        observers.forEach(observer -> observer.actorRemoved(actorType, x, y));
    }

    private void notifyObserversExtraItemVanished() {
        observers.forEach(GameObserver::extraItemVanished);
    }

    private void notifyObserversExtraItemPlaced(final Point p) {
        observers.forEach(observer -> observer.extraItemPlaced(p));
    }

    private void notifyObserversPacManDied() {
        observers.forEach(GameObserver::pacManDied);
    }

    private void notifyObserversGameOver() {
        observers.forEach(GameObserver::gameOver);
    }

    private void notifyObserversStartPowerPelletMode() {
        observers.forEach(GameObserver::startPowerPelletMode);
    }

    private void notifyObserversEndPowerPelletMode() {
        observers.forEach(GameObserver::endPowerPelletMode);
    }

    /**
     * This method should be called if Pac-Man dies. If at least one life is left, it prepares the game for the next
     * cycle, i.e places Pac-Man and the ghosts on their start fields and updates the counters. Otherwise game has to be
     * set game over. Furthermore, old actors have to be removed and if there is an extraItem, it has to be removed,
     * too.
     */
    private void nextLife() {
        final Point oldPacPos = pacMan.getPosition();
        board.getField(oldPacPos).removeActor();
        notifyObserversActorRemoved(PACMAN, oldPacPos.x, oldPacPos.y);

        ghosts.forEach(ghost -> {
            final Point oldGhostPos = ghost.getPosition();
            board.getField(oldGhostPos).removeActor();
            notifyObserversActorRemoved(GHOST, oldGhostPos.x, oldGhostPos.y);
        });

        if (extraItemPosition != null) {
            board.getField(extraItemPosition).setExtraItem(null);
            extraItemPosition = null;
            notifyObserversExtraItemVanished();
        }

        if (lives > 0) {
            pacMan.setPosition(board.getPacManStart());
            board.getField(board.getPacManStart()).placeActor(pacMan);
            notifyObserversActorSet(PACMAN, board.getPacManStart().x, board.getPacManStart().y);
            pacMan.setDirection(Direction.LEFT);

            for (int i = 0; i < ghosts.size(); i++) {
                final Point ghostStart = board.getGhostsStarts().get(i);
                ghosts.get(i).setPosition(ghostStart);
                board.getField(ghostStart).placeActor(ghosts.get(i));
                notifyObserversActorSet(GHOST, ghostStart.x, ghostStart.y);
            }
            powerPelletDuration = POWER_PELLET_TIME;
            pacManMoveDuration = 0;
            ghostMoveDuration = 0;
            newExtraItemDuration = 0;
            extraItemDuration = NEW_EXTRA_ITEM_TIME;

            notifyObserversPacManDied();
            lives--;
        } else {
            notifyObserversPacManDied();
            notifyObserversGameOver();
        }
    }
}
