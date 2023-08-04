import javafx.util.Pair;

import java.util.*;

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

    private Map<Pair, Integer> history;
    private Pair<Integer, Integer> revertPoint;

    private int maxRevertSteps = 0;


    public SudokuGenerator() {
        // Initialize possibilities
        rowsRandomPool = new HashMap<>();
        colsRandomPool = new HashMap<>();
        boxesRandomPool = new HashMap<>();
        history = new HashMap<>();
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

    public int[][] generateSudoku() {
        int[][] board = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Pair<Integer, Integer> currentCell = findNumber(board, row, col);
                row = currentCell.getKey();
                col = currentCell.getValue();
                if (maxRevertSteps >= 10) {
                    maxRevertSteps = 0;
                    revertPoint = null;
                }
            }
        }

        return board;
    }

    private int revert(int[][] board, Pair<Integer, Integer> current) {
        int currentRow = current.getKey();
        int currentCol = current.getValue();

        if (Objects.isNull(revertPoint)) {
            if (currentCol == 0) {
                revertPoint = new Pair<>(currentRow - 1, SIZE - 1);
            } else {
                revertPoint = new Pair<>(currentRow, currentCol - 1);
            }
        } else {
            maxRevertSteps++;
        }

        if (revertPoint.getValue() == 0) {
            revertPoint = new Pair<>(revertPoint.getKey() - 1, SIZE - 1);
        } else {
            revertPoint = new Pair<>(revertPoint.getKey(), revertPoint.getValue() - 1);
        }


        int destRow = revertPoint.getKey();
        int destCol = revertPoint.getValue();

        Integer number = null;
        do {
            if (currentCol == 0) {
                currentCol = SIZE - 1;
                currentRow--;
            } else {
                currentCol--;
            }

            number = history.remove(new Pair(currentRow, currentCol));
            board[currentRow][currentCol] = 0;
            rowsRandomPool.get(currentRow).add(number);
            colsRandomPool.get(currentCol).add(number);
            boxesRandomPool.get(boxIndex(currentRow, currentCol)).add(number);
        } while (destRow != currentRow || destCol != currentCol);
        return number;
    }


    private Pair<Integer, Integer> findNumber(int[][] board, int row, int col) {
        return findNumber(board, row, col, -1);
    }

    private Pair<Integer, Integer> findNumber(int[][] board, int row, int col, int revertedEntry) {
        HashSet<Integer> selectionPool = new HashSet<>(rowsRandomPool.get(row));
        selectionPool.retainAll(colsRandomPool.get(col));
        selectionPool.retainAll(boxesRandomPool.get(boxIndex(row, col)));

        if (selectionPool.isEmpty() || (selectionPool.size() == 1 && selectionPool.contains(revertedEntry))) {
            revertedEntry = revert(board, new Pair<>(row, col));
            return findNumber(board, revertPoint.getKey(), revertPoint.getValue(), revertedEntry);
        }

        boolean validSelection = false;
        while (!validSelection) {
            int randomIndex = random.nextInt(selectionPool.size());
            int i = 0;
            for (Integer entry : selectionPool) {
                if (i == randomIndex) {
                    if (entry == revertedEntry) {
                        break;
                    }
                    rowsRandomPool.get(row).remove(entry);
                    colsRandomPool.get(col).remove(entry);
                    boxesRandomPool.get(boxIndex(row, col)).remove(entry);
                    board[row][col] = entry;
                    history.put(new Pair<>(row, col), entry);
                    validSelection = true;
                    break;  // exit the loop once we've made a selection
                }
                i++;
            }
            // If we've exhausted all possibilities without finding a valid selection, break the loop.
            if (i == selectionPool.size()) {
                break;
            }
        }
        return new Pair<>(row, col);
    }


    private int boxIndex(int i, int j) {
        return (i / 3) * 3 + j / 3;
    }


    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                newBoard[row][col] = board[row][col];
            }
        }
        return newBoard;
    }

}
