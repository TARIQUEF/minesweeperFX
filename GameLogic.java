package game;

import java.util.HashSet;

public class GameLogic {
    private String[][] board;
    private String[][] boardVisible;

    // store coords for array dig()
    private HashSet<Integer> listCoords = new HashSet<>();
    // store coords for gridVisible uncover() method
    private HashSet<Integer> visibleListCoords = new HashSet<>();

    public int flags;
    private int size;
    private int numMines;

    public GameLogic(int numMines, int flags) {
        this.size = 10;
        this.numMines = numMines;
        this.flags = flags;

        board = new String[size][size];
        boardVisible = new String[size][size];

        createBoard();
        insertMines();
    }

    private void insertMines() {
        for (int i = 0; i < numMines; i++) { // random generated
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);
            board[x][y] = "M";
        }
    }

    private void createBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "?";
                boardVisible[i][j] = "?";
            }
        }
    }

    public boolean checkMines(int xpos, int ypos) {
        return board[xpos][ypos].equals("M");
    }

    public void addFlag(int xpos, int ypos) {
        if (boardVisible[xpos][ypos].equals("F")) {
            boardVisible[xpos][ypos] = "?";
            flags++; // give the flag back if removed
        }
        else if (boardVisible[xpos][ypos].equals("?")) {
            if (flags > 0) {
                boardVisible[xpos][ypos] = "F";
                flags--; // less flag count when a flag is placed
            }
            else {
                System.out.println("No flags left!");
            }
        }
        else { // "-"
            System.out.println("Cannot place!");
        }
    }

    public void uncover(int xpos, int ypos) {
        if (xpos < 0 || ypos < 0 || xpos >= size || ypos >= size) {
            // Out of bounds
            return;
        }
        if (!boardVisible[xpos][ypos].equals("?")) {
            // Cell already uncovered
            return;
        }
        if (checkMines(xpos, ypos)) {
            // Hit a mine
            boardVisible[xpos][ypos] = "M";
            return;
        }

        // Count adjcent mines
        int mineCount = countAdjacentMines(xpos, ypos);

        if (mineCount > 0) { // set mine number to visible board
            boardVisible[xpos][ypos] = String.valueOf(mineCount);
        } else {
            boardVisible[xpos][ypos] = "_"; //otherwise put the blank openin
        }

        // when blank opening do recursive on blocks around

        if (mineCount == 0) {
            for (int i = xpos - 1; i <= xpos + 1; i++) {
                for (int j = ypos - 1; j <= ypos + 1; j++) {
                    if (i != xpos || j != ypos) { // skip current one
                        uncover(i, j);
                    }
                }
            }
        }
    }

    private int countAdjacentMines(int xpos, int ypos) { //does the counting helps recursive funvtion
        int count = 0;
        for (int i = xpos - 1; i <= xpos + 1; i++) { // counts around  and adds to count
            for (int j = ypos - 1; j <= ypos + 1; j++) {
                if (i >= 0 && j >= 0 && i < size && j < size && checkMines(i, j)) { // within bounds and mines
                    count++;
                }
            }
        }
        return count;
    }

    public String getBoardVisibleValue(int x, int y) {
        return boardVisible[x][y];
    }


    public int getFlagNum(){
        return flags;
    }
}
