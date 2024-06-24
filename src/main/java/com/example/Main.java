package com.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Solve Sudoku");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (option == 1) {
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter name: ");
                String name = scanner.nextLine();

                try {
                    userService.registerUser(email, name);
                    System.out.println("User registered successfully!");
                } catch (IOException | IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (option == 2) {
                System.out.print("Enter email: ");
                String email = scanner.nextLine();

                User user = userService.authenticateUser(email);
                if (user != null) {
                    System.out.println("Login successful!");
                    System.out.println("Welcome, " + user.getName());
                } else {
                    System.out.println("Invalid email.");
                }
            } else if (option == 3) {
                System.out.print("Enter email: ");
                String email = scanner.nextLine();

                User user = userService.authenticateUser(email);
                if (user != null) {
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
                        try {
                            userService.saveSolution(email, boardToString(boardCopy));
                            System.out.println("Solution saved successfully!");
                        } catch (IOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
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
                        try {
                            userService.saveSolution(email, boardToString(boardCopy));
                            System.out.println("Solution saved successfully!");
                        } catch (IOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
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
                        try {
                            userService.saveSolution(email, boardToString(boardCopy));
                            System.out.println("Solution saved successfully!");
                        } catch (IOException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No solution exists");
                    }
                } else {
                    System.out.println("Invalid email.");
                }
            } else if (option == 4) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }

        scanner.close();
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

    private static String boardToString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int d = 0; d < 9; d++) {
                sb.append(board[r][d]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
