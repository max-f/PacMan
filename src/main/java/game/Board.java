package game;

import enums.FieldType;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Field[][] board;
    private int pacDotsOnStart;
    private List<Point> extraItemFields = new ArrayList<>();

    private List<Point> ghostsStarts = new ArrayList<>();
    private Point pacManStart;

    public Field[][] getBoard() {
        return board;
    }

    public List<Point> getGhostsStarts() {
        return ghostsStarts;
    }

    public int getPacDotsOnStart() {
        return pacDotsOnStart;
    }

    public Point getPacManStart() {
        return pacManStart;
    }

    public int getNumberOfRows() {
        return board.length;
    }

    public int getNumberOfColumns() {
        if (board.length == 0) {
            return 0;
        } else {
            return board[0].length;
        }
    }

    public Field getField(int x, int y) {
        if (!isPointValid(x, y)) {
            throw new IllegalArgumentException("specified field is not on the board");
        }
        return board[x][y];
    }

    public Field getField(final Point p) {
        if (!isPointValid(p)) {
            throw new IllegalArgumentException("specified field is not on the board");
        }
        return board[p.x][p.y];
    }

    public List<Point> getExtraItemFields() {
        return extraItemFields;
    }

    public BoardInfo getBoardInfo() {
        return (BoardInfo) this;
    }

    public FieldInfo getFieldInfo(Point p) {
        if (!isPointValid(p)) {
            throw new IllegalArgumentException("specified field not on the board");
        }
        return (FieldInfo) board[p.x][p.y];
    }

    public FieldInfo getFieldInfo(final int x, final int y) {
        if (!isPointValid(x, y)) {
            throw new IllegalArgumentException("specified field not on the board");
        }
        return (FieldInfo) board[x][y];
    }

    private boolean isPointValid(final Point p) {
        return p != null && p.x >= 0 && p.y >= 0 && p.x < board.length
                && p.y < board[p.x].length
                && board[p.x][p.y] != null;
    }

    private boolean isPointValid(final int x, final int y) {
        return x >= 0 && y >= 0 && x < board.length && y < board[x].length && board[x][y] != null;
    }


    /**
     * Creates a new board with given rows and columns from the {@code Field} array. Saves the start position of the
     * ghosts, checks the amount of free fields and Pac-Man starting fields, and populates the list of {@code
     * extraItemFields}.
     *
     * @param board the board that specifies the map.
     * @throws IllegalArgumentException if the given board is null, has no free fields, not exactly one Pac-Man start, a
     *                                  dimension is zero, the rows have different length, or the given board contains a
     *                                  null.
     */
    public Board(Field[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            throw new IllegalArgumentException("invalid board");
        }
        int pacmanStartCounter = 0;
        int freeFieldsCounter = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j].getType()) {
                    case PACMAN_START:
                        pacmanStartCounter++;
                        extraItemFields.add(new Point(i, j));
                        break;
                    case FREE:
                        freeFieldsCounter++;
                        extraItemFields.add(new Point(i, j));
                        break;
                    case POWER_PELLET:
                        extraItemFields.add(new Point(i, j));
                        break;
                    case GHOST_START:
                        ghostsStarts.add(new Point(i, j));
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

    /**
     * Returns a board for given board description.
     * <p>
     * The Following characters are allowed and represent following field types. <br/> '#': WALL <br/> '-': FREE <br/>
     * 'X': POWER_PELLET <br/> 'P': PACMAN_START <br/> 'G': GHOST_START<br/>
     *
     * @param boardDescription a description of the board, each entry represents one row.
     * @return a board for the given description.
     * @throws IllegalArgumentException if the description contains a character that is not allowed.
     */

    public static Board parse(String[] boardDescription) {
        Field[][] tmpBoard = new Field[boardDescription.length][boardDescription[0].length()];

        for (int i = 0; i < boardDescription.length; i++) {
            char[] row = boardDescription[i].toCharArray();
            for (int j = 0; j < row.length; j++) {
                switch (row[j]) {
                    case '#':
                        tmpBoard[i][j] = new Field(FieldType.WALL);
                        break;
                    case '-':
                        tmpBoard[i][j] = new Field(FieldType.FREE);
                        break;
                    case 'X':
                        tmpBoard[i][j] = new Field(FieldType.POWER_PELLET);
                        break;
                    case 'P':
                        tmpBoard[i][j] = new Field(FieldType.PACMAN_START);
                        break;
                    case 'G':
                        tmpBoard[i][j] = new Field(FieldType.GHOST_START);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid character in board description");
                }
            }
        }
        return new Board(tmpBoard);
    }

    /**
     * Should be called when a new game or a new level starts. Initializes the fields, i.e. pac-dots, resets the power
     * pellets (indicated by the original board), removes all actors and extra items.
     */
    public void initBoard() {
        for (Field[] aBoard : board) {
            for (Field field : aBoard) {
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
}
