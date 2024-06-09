package com.example;
public class Main {
    public static void main(String[] args) {
        int[][] board = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        SudokuSolverContext context = new SudokuSolverContext();

        // Naive Solver
        int[][] boardCopy = copyBoard(board);
        context.setSolver(new NaiveSolver());
        System.out.println("Naive Solver:");
        long startTime = System.nanoTime();
        if (context.solve(boardCopy)) {
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime) + " nanoseconds");
            printBoard(boardCopy);
        } else {
            System.out.println("No solution exists");
        }

        // Backtracking Solver
        boardCopy = copyBoard(board);
        context.setSolver(new BacktrackingSolver());
        System.out.println("\nBacktracking Solver:");
        startTime = System.nanoTime();
        if (context.solve(boardCopy)) {
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime) + " nanoseconds");
            printBoard(boardCopy);
        } else {
            System.out.println("No solution exists");
        }

        // DLX Solver
        boardCopy = copyBoard(board);
        context.setSolver(new DLXSolver());
        System.out.println("\nDLX Solver:");
        startTime = System.nanoTime();
        if (context.solve(boardCopy)) {
            long endTime = System.nanoTime();
            System.out.println("Time taken: " + (endTime - startTime) + " nanoseconds");
            printBoard(boardCopy);
        } else {
            System.out.println("No solution exists");
        }
    }

    private static void printBoard(int[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int d = 0; d < board.length; d++) {
                System.out.print(board[r][d]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 9);
        }
        return newBoard;
    }
}
