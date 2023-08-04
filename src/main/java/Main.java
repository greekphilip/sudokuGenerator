import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 04/08/2023
 */
public class Main {
    public static final int SIZE = 9;

    public static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        SudokuGenerator sudokuGenerator = new SudokuGenerator();

        long start = System.currentTimeMillis();
        int[][] solvedSudokou = sudokuGenerator.generateSudoku();
        System.out.println("Time it took to generate: " + (System.currentTimeMillis() - start));

        printBoard(solvedSudokou);


        CellRemover cellRemover = new CellRemover();
        int[][] puzzle = cellRemover.removeCells(copyBoard(solvedSudokou), 35);

        printBoard(puzzle);
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                newBoard[row][col] = board[row][col];
            }
        }
        return newBoard;
    }
}