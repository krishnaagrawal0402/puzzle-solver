/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author krish
 */
import java.util.*;

class CrosswordSolver {
    private char[][] grid;
    private int rows;
    private int cols;
    private List<String> wordList;
    private List<String> usedWords;
    
    public boolean solve(char[][] initialGrid, List<String> words) {
        rows = initialGrid.length;
        cols = initialGrid[0].length;
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(initialGrid[i], 0, grid[i], 0, cols);
        }
        
        // Sort words by length (longer words are harder to place)
        wordList = new ArrayList<>(words);
        Collections.sort(wordList, (a, b) -> b.length() - a.length());
        usedWords = new ArrayList<>();
        
        return solveBacktrack(0);
    }
    
    private boolean solveBacktrack(int wordIndex) {
        // Base case: all words have been placed
        if (wordIndex >= wordList.size()) {
            return true;
        }
        
        String word = wordList.get(wordIndex);
        
        // Try to place the word horizontally
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - word.length(); j++) {
                if (canPlaceHorizontally(word, i, j)) {
                    // Place the word
                    char[][] backup = backupGrid();
                    placeWordHorizontally(word, i, j);
                    usedWords.add(word);
                    
                    // Recursively try to place the next word
                    if (solveBacktrack(wordIndex + 1)) {
                        return true;
                    }
                    
                    // Backtrack
                    restoreGrid(backup);
                    usedWords.remove(usedWords.size() - 1);
                }
            }
        }
        
        // Try to place the word vertically
        for (int i = 0; i <= rows - word.length(); i++) {
            for (int j = 0; j < cols; j++) {
                if (canPlaceVertically(word, i, j)) {
                    // Place the word
                    char[][] backup = backupGrid();
                    placeWordVertically(word, i, j);
                    usedWords.add(word);
                    
                    // Recursively try to place the next word
                    if (solveBacktrack(wordIndex + 1)) {
                        return true;
                    }
                    
                    // Backtrack
                    restoreGrid(backup);
                    usedWords.remove(usedWords.size() - 1);
                }
            }
        }
        
        // Couldn't place this word with the current configuration
        return false;
    }
    
    private boolean canPlaceHorizontally(String word, int row, int col) {
        // Check if there's enough space and no black cells
        if (col + word.length() > cols) {
            return false;
        }
        
        // Check if the word can be placed without conflicts
        for (int j = 0; j < word.length(); j++) {
            char gridChar = grid[row][col + j];
            if (gridChar == '#') {
                return false; // Black cell
            }
            if (gridChar != '.' && gridChar != word.charAt(j)) {
                return false; // Conflict with existing letter
            }
        }
        
        return true;
    }
    
    private boolean canPlaceVertically(String word, int row, int col) {
        // Check if there's enough space and no black cells
        if (row + word.length() > rows) {
            return false;
        }
        
        // Check if the word can be placed without conflicts
        for (int i = 0; i < word.length(); i++) {
            char gridChar = grid[row + i][col];
            if (gridChar == '#') {
                return false; // Black cell
            }
            if (gridChar != '.' && gridChar != word.charAt(i)) {
                return false; // Conflict with existing letter
            }
        }
        
        return true;
    }
    
    private void placeWordHorizontally(String word, int row, int col) {
        for (int j = 0; j < word.length(); j++) {
            grid[row][col + j] = word.charAt(j);
        }
    }
    
    private void placeWordVertically(String word, int row, int col) {
        for (int i = 0; i < word.length(); i++) {
            grid[row + i][col] = word.charAt(i);
        }
    }
    
    private char[][] backupGrid() {
        char[][] backup = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, backup[i], 0, cols);
        }
        return backup;
    }
    
    private void restoreGrid(char[][] backup) {
        for (int i = 0; i < rows; i++) {
            System.arraycopy(backup[i], 0, grid[i], 0, cols);
        }
    }
}