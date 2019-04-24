package com.example.tictactoe;

import java.util.Arrays;

import static com.example.tictactoe.GameInstance.CellState.*;
import static com.example.tictactoe.GameInstance.GameState.*;

class GameInstance {
    public enum CellState {
        FREE, CROSS, NOUGHT
    }
    public enum GameState {
        CROSS_MOVE, NOUGHT_MOVE, CROSS_WIN, NOUGHT_WIN;
    }

    private static final int FIELD_SIZE = 3;

    private final CellState[][] field = new CellState[FIELD_SIZE][FIELD_SIZE];
    private int turn = 0;
    private GameState state = CROSS_MOVE;

    GameInstance() {
        Arrays.stream(field).forEach(row -> Arrays.fill(row, FREE));
    }

    CellState get(int x, int y) {
        checkInBounds(x);
        checkInBounds(y);
        return field[x][y];
    }

    /**
     * @return has this move ended the game
     * @throws ImpossibleMoveException
     */
    boolean put(int x, int y) throws ImpossibleMoveException {
        checkInBounds(x);
        checkInBounds(y);
        if (field[x][y] != FREE) {
            throw new ImpossibleMoveException();
        }
        if (state != CROSS_MOVE && state != NOUGHT_MOVE) {
            throw new ImpossibleMoveException();
        }

        

    }

    private void checkInBounds(int coordinate) throws IllegalArgumentException {
        if (coordinate < 0 || FIELD_SIZE <= coordinate) {
            throw new IllegalArgumentException("Coordinate not in range");
        }
    }
}
