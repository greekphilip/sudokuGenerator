package com.papaspyrou.sudoku;

import java.util.*;

import static com.papaspyrou.sudoku.Utils.*;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 04/08/2023
 */
public class CellRemover {

    private Random random;

    private Map<Integer, Set<Integer>> rowsRandomPool, colsRandomPool, boxesRandomPool;

    private SudokuSolver sudokuSolver;

    private void initialize() {
        sudokuSolver = new SudokuSolver();
        random = new Random();
        // Initialize possibilities
        rowsRandomPool = new HashMap<>();
        colsRandomPool = new HashMap<>();
        boxesRandomPool = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            rowsRandomPool.put(i, new HashSet<>());
            colsRandomPool.put(i, new HashSet<>());
            boxesRandomPool.put(i, new HashSet<>());
        }
    }

    public int[][] removeCells(int[][] board, int numberOfCells) {
        long start = System.currentTimeMillis();
        initialize();
        int[][] originalBoard = copyBoard(board);

        int[] removalIndex = new int[9 * 9];
        for (int i = 0; i < 9 * 9; i++) {
            removalIndex[i] = i;
        }
        int poolSize = 9 * 9;
        int randomIndex;
        int temp;

        for (int i = 0; i < numberOfCells; i++) {
            randomIndex = random.nextInt(poolSize);
            temp = removalIndex[randomIndex];
            int row = temp % 9;
            int col = temp / 9;

            int[][] copy = copyBoard(board);
            copy[row][col] = 0;

            if (!verifyUniqueness(originalBoard, copy, 10)) {
                i--;
                removalIndex[randomIndex] = removalIndex[poolSize - 1];
                removalIndex[poolSize - 1] = temp;
                poolSize--;
                continue;
            }

            board[row][col] = 0;
            removalIndex[randomIndex] = removalIndex[poolSize - 1];
            removalIndex[poolSize - 1] = temp;
            poolSize--;
        }

        System.out.println("Removed cells operation time: " + (System.currentTimeMillis() - start) + "ms");
        return board;
    }

    private boolean verifyUniqueness(int[][] original, int[][] unsolved, int times) {
        for (int i = 0; i < times; i++) {
            int[][] copy1 = copyBoard(unsolved);
            int[][] copy2 = copyBoard(unsolved);
            int[][] copy3 = copyBoard(unsolved);
            int[][] copy4 = copyBoard(unsolved);

            sudokuSolver.solveSudokuRowWiseTopToBottom(copy1);
            if (!Utils.compareBoards(original, copy1)) {
                return false;
            }
            sudokuSolver.solveSudokuRowWiseBottomToTop(copy2);
            if (!Utils.compareBoards(original, copy2)) {
                return false;
            }
            sudokuSolver.solveSudokuColumnWiseTopToBottom(copy3);
            if (!Utils.compareBoards(original, copy3)) {
                return false;
            }
            sudokuSolver.solveSudokuColumnWiseBottomToTop(copy4);
            if (!Utils.compareBoards(original, copy4)) {
                return false;
            }
        }
        return true;
    }
}
