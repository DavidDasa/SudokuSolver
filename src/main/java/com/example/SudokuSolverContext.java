package com.example;

public class SudokuSolverContext {
    private ISudokuSolver solver;

    public void setSolver(ISudokuSolver solver) {
        this.solver = solver;
    }

    public boolean solve(int[][] board) {
        return solver.solve(board);
    }
}

