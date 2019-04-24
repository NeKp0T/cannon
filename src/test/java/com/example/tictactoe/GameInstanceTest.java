package com.example.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInstanceTest {
    private GameInstance game;

    @BeforeEach
    void init() {
        game = new GameInstance();
    }

    @Test
    void putsWithoutException() throws ImpossibleMoveException {
        game.put(0, 0);
        game.put(0, 1);
        game.put(0, 2);
        game.put(1, 0);
        game.put(1, 1);
        game.put(1, 2);
    }

    @Test
    void winsHorizontal() throws ImpossibleMoveException {
        makeHorizontalCrossWin();
        assertEquals(GameInstance.GameState.CROSS_WIN,  game.getState());
    }

    private void makeHorizontalCrossWin() throws ImpossibleMoveException {
        game.put(0, 0);
        game.put(1, 0);
        game.put(0, 1);
        game.put(1, 1);
        game.put(0, 2);
    }


    @Test
    void statuses() throws ImpossibleMoveException {
        assertEquals(GameInstance.GameState.RUNNING, game.getState());
        assertEquals(GameInstance.MoveOrder.CROSS_MOVE, game.getMoveOrder());
        game.put(0, 0);
        assertEquals(GameInstance.GameState.RUNNING, game.getState());
        assertEquals(GameInstance.MoveOrder.NOUGHTS_MOVE, game.getMoveOrder());
        game.put(1, 0);
        assertEquals(GameInstance.GameState.RUNNING, game.getState());
        assertEquals(GameInstance.MoveOrder.CROSS_MOVE, game.getMoveOrder());
        game.put(0, 1);
        assertEquals(GameInstance.GameState.RUNNING, game.getState());
        assertEquals(GameInstance.MoveOrder.NOUGHTS_MOVE, game.getMoveOrder());
        game.put(1, 1);
        assertEquals(GameInstance.GameState.RUNNING, game.getState());
        assertEquals(GameInstance.MoveOrder.CROSS_MOVE, game.getMoveOrder());
        game.put(0, 2);
        assertEquals(GameInstance.MoveOrder.CROSS_MOVE, game.getMoveOrder());
        assertEquals(GameInstance.GameState.CROSS_WIN,  game.getState());
    }

    @Test
    void winsDiagonal() throws ImpossibleMoveException {
        game.put(1, 1);
        game.put(0, 1);
        game.put(0, 0);
        game.put(1, 0);
        game.put(2, 2);
        assertEquals(GameInstance.GameState.CROSS_WIN, game.getState());
    }

    @Test
    void testDraw() throws  ImpossibleMoveException {
        makeDraw();

        assertEquals(GameInstance.GameState.DRAW, game.getState());
    }

    private void makeDraw() throws ImpossibleMoveException {
        // xox
        // xox
        // oxo
        game.put(0, 0);
        game.put(0, 1);
        game.put(0, 2);

        game.put(2, 0);
        game.put(2, 1);
        game.put(2, 2);

        game.put(1, 0);
        game.put(1, 1);
        game.put(1, 2);
    }

    @Test
    void testPutOutsideOfBoard() {
        assertThrows(IllegalArgumentException.class, () -> game.put(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> game.put(0, -1));
        assertThrows(IllegalArgumentException.class, () -> game.put(3, 0));
        assertThrows(IllegalArgumentException.class, () -> game.put(0, 3));
    }

    @Test
    void testPutOnUsedCell() throws ImpossibleMoveException {
        game.put(0, 0);
        assertThrows(ImpossibleMoveException.class, () -> game.put(0, 0));
    }

    @Test
    void testThrowsAfterGameEnded() throws ImpossibleMoveException {
        makeDraw();
        assertThrows(ImpossibleMoveException.class, () -> game.put(0, 0));
    }

    @Test
    void getStatusWin() throws ImpossibleMoveException {
        makeHorizontalCrossWin();
        assertEquals(GameInstance.GameState.CROSS_WIN, game.getState());
    }
    @Test
    void getStatusDraw() throws ImpossibleMoveException {
        makeDraw();
        assertEquals(GameInstance.GameState.DRAW, game.getState());
    }
}