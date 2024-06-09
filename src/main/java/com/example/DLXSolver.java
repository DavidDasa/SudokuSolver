package com.example;

import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class DLXSolver implements ISudokuSolver {
    private static final int SIZE = 9;
    private static final int SUBGRID = 3;
    private ColumnNode header;

    @Override
    public boolean solve(int[][] board) {
        header = createDLXBoard(board);
        return search(0, board);
    }

    private ColumnNode createDLXBoard(int[][] board) {
        int[][] cover = createExactCoverBoard(board);
        return buildDLXList(cover);
    }

    private int[][] createExactCoverBoard(int[][] board) {
        int[][] coverBoard = new int[SIZE * SIZE * SIZE][SIZE * SIZE * 4];

        int header = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                for (int n = 0; n < SIZE; n++) {
                    coverBoard[getIndex(r, c, n)][header] = 1;
                    coverBoard[getIndex(r, c, n)][SIZE * SIZE + r * SIZE + n] = 1;
                    coverBoard[getIndex(r, c, n)][2 * SIZE * SIZE + c * SIZE + n] = 1;
                    coverBoard[getIndex(r, c, n)][3 * SIZE * SIZE + (r / SUBGRID * SUBGRID + c / SUBGRID) * SIZE + n] = 1;
                }
            }
        }

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int n = board[r][c];
                if (n != 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (num != n) {
                            int idx = getIndex(r, c, num - 1);
                            for (int j = 0; j < coverBoard[idx].length; j++) {
                                coverBoard[idx][j] = 0;
                            }
                        }
                    }
                }
            }
        }

        return coverBoard;
    }

    private int getIndex(int r, int c, int n) {
        return r * SIZE * SIZE + c * SIZE + n;
    }

    private ColumnNode buildDLXList(int[][] cover) {
        final int COLS = cover[0].length;
        final int ROWS = cover.length;

        ColumnNode headerNode = new ColumnNode("header");
        ArrayList<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.linkRight(n);
        }

        headerNode = headerNode.right.column;
        for (int i = 0; i < ROWS; i++) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (cover[i][j] == 1) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null)
                        prev = newNode;
                    col.top.linkDown(newNode);
                    prev = prev.linkRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;
        return headerNode;
    }

    private boolean search(int k, int[][] board) {
        if (header.right == header) {
            updateBoard(board);
            return true;
        }

        ColumnNode columnNode = selectColumnNodeHeuristic();
        columnNode.cover();

        for (DancingNode row = columnNode.bottom; row != columnNode; row = row.bottom) {
            solution.add(row);

            for (DancingNode rightNode = row.right; rightNode != row; rightNode = rightNode.right) {
                rightNode.column.cover();
            }

            if (search(k + 1, board))
                return true;

            row = solution.remove(solution.size() - 1);
            columnNode = row.column;

            for (DancingNode leftNode = row.left; leftNode != row; leftNode = leftNode.left) {
                leftNode.column.uncover();
            }
        }

        columnNode.uncover();
        return false;
    }

    private void updateBoard(int[][] board) {
        for (DancingNode n : solution) {
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.column.name);

            for (DancingNode temp = n.right; temp != n; temp = temp.right) {
                int val = Integer.parseInt(temp.column.name);
                if (val < min) {
                    min = val;
                    rcNode = temp;
                }
            }

            int ans1 = Integer.parseInt(rcNode.column.name);
            int ans2 = Integer.parseInt(rcNode.right.column.name);
            int r = ans1 / SIZE;
            int c = ans1 % SIZE;
            int num = (ans2 % SIZE) + 1;
            board[r][c] = num;
        }
    }

    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;

        for (ColumnNode c = (ColumnNode) header.right; c != header; c = (ColumnNode) c.right) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }

        return ret;
    }

    private final List<DancingNode> solution = new ArrayList<>();

    class DancingNode {
        DancingNode left, right, top, bottom;
        ColumnNode column;

        DancingNode() {
            left = right = top = bottom = this;
        }

        DancingNode(ColumnNode c) {
            this();
            column = c;
        }

        DancingNode linkDown(DancingNode node) {
            node.bottom = bottom;
            node.bottom.top = node;
            node.top = this;
            bottom = node;
            return node;
        }

        DancingNode linkRight(DancingNode node) {
            node.right = right;
            node.right.left = node;
            node.left = this;
            right = node;
            return node;
        }

        void unlinkLeftRight() {
            left.right = right;
            right.left = left;
        }

        void relinkLeftRight() {
            left.right = this;
            right.left = this;
        }

        void unlinkUpDown() {
            top.bottom = bottom;
            bottom.top = top;
        }

        void relinkUpDown() {
            top.bottom = this;
            bottom.top = this;
        }
    }

    class ColumnNode extends DancingNode {
        int size;
        String name;

        ColumnNode(String n) {
            super();
            size = 0;
            name = n;
            column = this;
        }

        void cover() {
            unlinkLeftRight();
            for (DancingNode i = bottom; i != this; i = i.bottom) {
                for (DancingNode j = i.right; j != i; j = j.right) {
                    j.unlinkUpDown();
                    j.column.size--;
                }
            }
        }

        void uncover() {
            for (DancingNode i = top; i != this; i = i.top) {
                for (DancingNode j = i.left; j != i; j = j.left) {
                    j.column.size++;
                    j.relinkUpDown();
                }
            }
            relinkLeftRight();
        }
    }
}
