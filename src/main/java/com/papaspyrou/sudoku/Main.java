package com.papaspyrou.sudoku;

import static com.papaspyrou.sudoku.Utils.*;

/**
 * @Author: Filippos Papaspyrou
 * @version:
 * @Description:
 * @Date: 04/08/2023
 */
public class Main {


    public static void main(String[] args) {
        SudokuSolver sudokuSolver = new SudokuSolver();
        SudokuGenerator sudokuGenerator = new SudokuGenerator();

        long start = System.currentTimeMillis();
        int[][] solvedSudokou = sudokuGenerator.generateSolvedSudoku();
        System.out.println("Time it took to generate: " + (System.currentTimeMillis() - start));

        printBoard(solvedSudokou);

        System.out.println("Valid sudokou: " + isValidSudoku(solvedSudokou));

        int[][] puzzle = sudokuGenerator.generateUnsolvedSudoku(copyBoard(solvedSudokou), 55);

        printBoard(puzzle);


        int[][] solvedPuzzle = sudokuSolver.solveSudoku(copyBoard(puzzle));


        printBoard(solvedPuzzle);

        System.out.println("Valid sudokou: " + isValidSudoku(solvedPuzzle));


        System.out.println("Generated Sudoku and solved one are equal: " + compareBoards(solvedPuzzle, solvedSudokou));
    }



}