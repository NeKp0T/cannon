package com.example.tictactoe;

import java.util.Arrays;

import static com.example.tictactoe.GameInstance.CellState.*;
import static com.example.tictactoe.GameInstance.GameState.*;

class GameInstance {
    public enum CellState {
        FREE, CROSS, NOUGHT
    }
    public enum GameState {
        RUNNING, CROSS_WIN, NOUGHT_WIN;
    }

    private enum MoveOrder {
        CROSS_MOVE, NOUGHTS_MOVE;
        public MoveOrder nextMove() {
            switch (this) {
                case CROSS_MOVE: return NOUGHTS_MOVE;
                case NOUGHTS_MOVE: return CROSS_MOVE;
            }
            throw new RuntimeException("Case not implemented");
        }

        public CellState putsOnBoard() {
            switch (this) {
                case CROSS_MOVE: return NOUGHT;
                case NOUGHTS_MOVE: return CROSS;
            }
            throw new RuntimeException("Case not implemented");
        }

        public GameState winState() {
            switch (this) {
                case CROSS_MOVE: return NOUGHT_WIN;
                case NOUGHTS_MOVE: return CROSS_WIN;
            }
            throw new RuntimeException("Case not implemented");
        }
    }

    private static final int FIELD_SIZE = 3;

    private final CellState[][] board = new CellState[FIELD_SIZE][FIELD_SIZE];
    private MoveOrder currentPlayer = MoveOrder.CROSS_MOVE;
    private GameState state = RUNNING;

    GameInstance() {
        Arrays.stream(board).forEach(row -> Arrays.fill(row, FREE));
    }

    CellState get(int x, int y) {
        assertInBounds(x);
        assertInBounds(y);
        return board[x][y];
    }

    GameState getState() {
        return state;
    }

    /**
     * @return has this move ended the game
     * @throws ImpossibleMoveException
     */
    boolean put(int x, int y) throws ImpossibleMoveException {
        assertInBounds(x);
        assertInBounds(y);
        if (board[x][y] != FREE) {
            throw new ImpossibleMoveException();
        }
        if (state != RUNNING) {
            throw new ImpossibleMoveException();
        }

        board[x][y] = currentPlayer.putsOnBoard();
        if (checkWon(x, y)) {
            state = currentPlayer.winState();
            return true;
        }
        return false;
    }

    String getStatus() {
        switch (state) {
            case CROSS_WIN: return "Crosses won!";
            case NOUGHT_WIN: return "Noughts won!";
            case RUNNING:
                switch (currentPlayer) {
                    case CROSS_MOVE: return "Waiting for crosses' move";
                    case NOUGHTS_MOVE: return "Waiting for noughts' move";
                }
        }
    }

    private boolean checkWon(int x, int y) {
        CellState type = board[x][y];
        Direction[] directionsToCheck = {
                new Direction(1, 0),
                new Direction(0, 1),
                new Direction(1, 1),
                new Direction(1, -1),
        };
        for (Direction d : directionsToCheck) {
            int inARow = 0;

            int xNow = x;
            int yNow = y;
            while (checkInBounds(xNow, yNow) && board[xNow][yNow] == type) {
                inARow++;
                xNow += d.dx;
                yNow += d.dy;
            }
            xNow = x;
            yNow = y;
            while (checkInBounds(xNow, yNow) && board[xNow][yNow] == type) {
                inARow++;
                xNow -= d.dx;
                yNow -= d.dy;
            }
            inARow--;

            if (inARow == FIELD_SIZE) {
                return true;
            }
        }

        return false;
    }

    private void assertInBounds(int coordinate) throws IllegalArgumentException {
        if (!checkInBounds(coordinate)) {
            throw new IllegalArgumentException("Coordinate not in range");
        }
    }

    private boolean checkInBounds(int coordinate) throws IllegalArgumentException {
        return (coordinate >= 0 && FIELD_SIZE > coordinate);
    }

    private boolean checkInBounds(int x, int y) throws IllegalArgumentException {
        return checkInBounds(x) && checkInBounds(y);
    }

    private static class Direction {
        public int dx, dy;
        public Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
