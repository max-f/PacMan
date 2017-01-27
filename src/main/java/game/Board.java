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

    public Board(Field[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {

        }
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

    public FieldInfo getFieldInfo(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Invalid point to get info from");
        }
        return null;
    }
}
