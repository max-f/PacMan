package game;

import enums.FieldType;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Field[][] board;
    private int pacDotsOnStart;
    private List<Point2D> extraItemFields = new ArrayList<>();

    private List<Point2D> ghostsStarts = new ArrayList<>();
    private Point2D pacManStart;

    /**
     * Creates a new board with given rows and columns from the {@code Field} array. Saves the start position of
     * the ghosts, checks the amount of free fields and Pac-Man starting fields, and populates the list of
     * {@code extraItemFields}.
     *
     * @param board the board that specifies the map.
     * @throws IllegalArgumentException if the given board is null, has no free fields, not exactly one Pac-Man
     * start, a dimension is zero, the rows have different length, or the given board contains a null.
     */
    public Board(Field[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            throw new IllegalArgumentException("invalid board");
        }
        int pacmanStartCounter = 0;
        int freeFieldsCounter = 0;

        for (int i = 0; i< board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j].getType()) {
                    case PACMAN_START:
                        pacmanStartCounter++;
                        extraItemFields.add(new Point2D(i, j));
                        break;
                    case FREE:
                        freeFieldsCounter++;
                        extraItemFields.add(new Point2D(i, j));
                        break;
                    case POWER_PELLET:
                        extraItemFields.add(new Point2D(i, j));
                        break;
                    case GHOST_START:
                        ghostsStarts.add(new Point2D(i, j));
                        break;
                }
            }
        }
        if (pacmanStartCounter != 1 || freeFieldsCounter == 0) {
            throw new IllegalArgumentException("board has more than 1 Pac-Man start position or no free field");
        }
        this.board = board;
        pacDotsOnStart = freeFieldsCounter;
    }

    public static Board parse(String[] boardDescription) {
        Field[][] tmpBoard = new Field[boardDescription.length][boardDescription[0].length()];

        for (int i = 0; i < boardDescription.length; i++) {
            char[] row = boardDescription[i].toCharArray();
            for (int j = 0; j < row.length; j++) {
                switch (row[j]) {
                    case '#' : tmpBoard[i][j] = new Field(FieldType.WALL); break;
                    case '-' : tmpBoard[i][j] = new Field(FieldType.FREE); break;
                    case 'X' : tmpBoard[i][j] = new Field(FieldType.POWER_PELLET); break;
                    case 'P' : tmpBoard[i][j] = new Field(FieldType.PACMAN_START); break;
                    case 'G' : tmpBoard[i][j] = new Field(FieldType.GHOST_START); break;
                    default : throw new IllegalArgumentException("invalid character in board description");
                }
            }
        }
        return new Board(tmpBoard);
    }

    /**
     * Should be called when a new game or a new level starts. Initializes the fields, i.e. pac-dots, resets the
     * power pellets (indicated by the original board), removes all actors and extra items.
     */
    public void initBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Field field = board[i][j];
                switch (field.getType()) {
                    case FREE:
                        field.removeActor();
                        field.setExtraItem(null);
                        field.setPacDot(true);
                        break;
                    case POWER_PELLET:
                        field.removeActor();
                        field.setExtraItem(null);
                        field.setPowerPellet(true);
                        break;
                    case PACMAN_START:
                        field.removeActor();
                        field.setExtraItem(null);
                        break;
                    case GHOST_START:
                        field.removeActor();
                        break;
                }
            }
        }
    }

    public FieldInfo getFieldInfo(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Invalid point to get info from");
        }
        return null;
    }
}
