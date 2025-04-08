/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author krish
 */
class SudokuSolver {
    public boolean solve(int[][] board) {
        // Find an empty cell
        int[] emptyCell = findEmptyCell(board);
        int row = emptyCell[0];
        int col = emptyCell[1];
        
        // If no empty cell is found, the puzzle is solved
        if (row == -1 && col == -1) {
            return true;
        }
        
        // Try digits 1 to 9
        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                // Place the number
                board[row][col] = num;
                
                // Recursively try to solve the rest of the puzzle
                if (solve(board)) {
                    return true;
                }
                
                // If placing num at (row, col) doesn't lead to a solution, backtrack
                board[row][col] = 0;
            }
        }
        
        // No solution found with current configuration
        return false;
    }
    
    private int[] findEmptyCell(int[][] board) {
        int[] cell = {-1, -1};
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    cell[0] = i;
                    cell[1] = j;
                    return cell;
                }
            }
        }
        
        return cell; // No empty cell found
    }
    
    private boolean isSafe(int[][] board, int row, int col, int num) {
        // Check row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        
        // Check column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        
        // Check 3x3 box
        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxStartRow + i][boxStartCol + j] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
}