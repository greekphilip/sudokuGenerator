package com.papaspyrou.sudoku;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 05/08/2023
 */
public class Utils {
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

    public static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                newBoard[row][col] = board[row][col];
            }
        }
        return newBoard;
    }

    public static boolean compareBoards(int[][] b1, int[][] b2) {
        if (b1.length != b2.length) return false;

        for (int i = 0; i < b1.length; i++) {
            if (b1[i].length != b2[i].length) return false;

            for (int j = 0; j < b1[i].length; j++) {
                if (b1[i][j] != b2[i][j]) return false;
            }
        }

        return true;
    }

    public static int boxIndex(int i, int j) {
        return (i / 3) * 3 + j / 3;
    }

    public static boolean isValidSudoku(int[][] board) {
        // SIZE would be 9 for a standard Sudoku
        int SIZE = 9;

        // Check each row
        for (int i = 0; i < SIZE; i++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 0 && seen[board[i][j]]) {
                    return false;
                }
                seen[board[i][j]] = true;
            }
        }

        // Check each column
        for (int i = 0; i < SIZE; i++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int j = 0; j < SIZE; j++) {
                if (board[j][i] != 0 && seen[board[j][i]]) {
                    return false;
                }
                seen[board[j][i]] = true;
            }
        }

        // Check each box
        for (int i = 0; i < SIZE; i += 3) {
            for (int j = 0; j < SIZE; j += 3) {
                boolean[] seen = new boolean[SIZE + 1];
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (board[i + k][j + l] != 0 && seen[board[i + k][j + l]]) {
                            return false;
                        }
                        seen[board[i + k][j + l]] = true;
                    }
                }
            }
        }

        return true;
    }
}
