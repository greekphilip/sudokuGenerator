package com.papaspyrou.sudoku;

import javafx.util.Pair;

import java.util.*;

import static com.papaspyrou.sudoku.Utils.boxIndex;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 04/08/2023
 */
public class SudokuGenerator {

    public static final int SIZE = 9;
    private Map<Integer, Set<Integer>> rowsRandomPool, colsRandomPool, boxesRandomPool;
    private Random random;
    private Stack<Pair<Integer, Integer>> moves;
    private Map<Pair, Set<Integer>> revertedEntries;

    private CellRemover cellRemover;

    private void initialize() {
        cellRemover = new CellRemover();
        // Initialize possibilities
        rowsRandomPool = new HashMap<>();
        colsRandomPool = new HashMap<>();
        boxesRandomPool = new HashMap<>();
        moves = new Stack<>();
        revertedEntries = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            rowsRandomPool.put(i, new HashSet<>());
            colsRandomPool.put(i, new HashSet<>());
            boxesRandomPool.put(i, new HashSet<>());
        }

        for (int i = 0; i < SIZE; i++) {
            for (int num = 1; num <= SIZE; num++) {
                rowsRandomPool.get(i).add(num);
                colsRandomPool.get(i).add(num);
                boxesRandomPool.get(i).add(num);
            }
        }

        random = new Random();
    }

    public int[][] generateSolvedSudoku() {
        initialize();
        int[][] board = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Pair<Integer, Integer> currentCell = findNumber(board, row, col);
                row = currentCell.getKey();
                col = currentCell.getValue();
            }
        }

        return board;
    }

    public int[][] generateUnsolvedSudoku(int numberOfEmptyCells) {
        int[][] board = generateSolvedSudoku();
        return cellRemover.removeCells(board, numberOfEmptyCells);
    }

    public int[][] generateUnsolvedSudoku(int[][] board, int numberOfEmptyCells) {
        return cellRemover.removeCells(board, numberOfEmptyCells);
    }

    private int revert(int[][] board) {
        Integer number;
        Pair<Integer, Integer> current = moves.peek();
        int currentRow = current.getKey();
        int currentCol = current.getValue();
        number = board[currentRow][currentCol];
        board[currentRow][currentCol] = 0;
        rowsRandomPool.get(currentRow).add(number);
        colsRandomPool.get(currentCol).add(number);
        boxesRandomPool.get(boxIndex(currentRow, currentCol)).add(number);

        if (revertedEntries.containsKey(current)) {
            revertedEntries.get(current).add(number);
        } else {
            revertedEntries.put(current, new HashSet<>());
            revertedEntries.get(current).add(number);
        }
        return number;
    }


    private Pair<Integer, Integer> findNumber(int[][] board, int row, int col) {
        HashSet<Integer> selectionPool = new HashSet<>(rowsRandomPool.get(row));
        selectionPool.retainAll(colsRandomPool.get(col));
        selectionPool.retainAll(boxesRandomPool.get(boxIndex(row, col)));

        if (revertedEntries.containsKey(new Pair(row, col))) {
            selectionPool.removeAll(revertedEntries.get(new Pair(row, col)));
        }

        if (selectionPool.isEmpty()) {
            revert(board);
            Pair<Integer, Integer> revertPoint = moves.pop();
            revertedEntries.remove(new Pair(row, col));
            return findNumber(board, revertPoint.getKey(), revertPoint.getValue());
        }


        int randomIndex = random.nextInt(selectionPool.size());
        int i = 0;
        for (Integer entry : selectionPool) {
            if (i == randomIndex) {
                rowsRandomPool.get(row).remove(entry);
                colsRandomPool.get(col).remove(entry);
                boxesRandomPool.get(boxIndex(row, col)).remove(entry);
                board[row][col] = entry;
                Pair<Integer, Integer> currentMove = new Pair<>(row, col);
                moves.push(currentMove);
                return currentMove;
            }
            i++;
        }
        throw new RuntimeException("Unreachable point");
    }

}
