/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/**
 *
 * @author krish
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;

class CrosswordSolverPanel extends JPanel {
    private JTextField[][] grid;
    private JSpinner rowsSpinner, colsSpinner;
    private JButton createGridButton;
    private JButton solveButton;
    private JButton clearButton;
    private JButton backButton;
    private JTextArea wordsArea;
    private JPanel gridPanel;
    private JLabel statusLabel;
    private MainFrame parent;
    private int rows = 10;
    private int cols = 10;
    
    public CrosswordSolverPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Crossword Solver");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Grid Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.add(new JLabel("Rows:"));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 3, 20, 1));
        sizePanel.add(rowsSpinner);
        
        sizePanel.add(new JLabel("Columns:"));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 3, 20, 1));
        sizePanel.add(colsSpinner);
        
        createGridButton = new JButton("Create Grid");
        createGridButton.addActionListener(e -> createGrid());
        sizePanel.add(createGridButton);
        
        // Words Panel
        JPanel wordsPanel = new JPanel(new BorderLayout());
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words (one per line)"));
        wordsArea = new JTextArea(10, 15);
        wordsArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(wordsArea);
        wordsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Grid Panel (will be created dynamically)
        gridPanel = new JPanel();
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back to Menu");
        
        solveButton.addActionListener(e -> solveCrossword());
        clearButton.addActionListener(e -> clearGrid());
        backButton.addActionListener(e -> parent.showMainMenu());
        
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        
        // Status Label
        statusLabel = new JLabel("Create a grid, place blocks, and enter words to solve");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(sizePanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(gridPanel, BorderLayout.CENTER);
        centerPanel.add(wordsPanel, BorderLayout.EAST);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Create initial grid
        createGrid();
    }
    
    private void createGrid() {
        rows = (Integer) rowsSpinner.getValue();
        cols = (Integer) colsSpinner.getValue();
        
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, cols, 1, 1));
        
        grid = new JTextField[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new JTextField(1);
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Arial", Font.PLAIN, 16));
                
                final int row = i;
                final int col = j;
                
                // Right-click to toggle black cell
                grid[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (grid[row][col].getBackground().equals(Color.BLACK)) {
                                grid[row][col].setBackground(Color.WHITE);
                                grid[row][col].setEditable(true);
                            } else {
                                grid[row][col].setBackground(Color.BLACK);
                                grid[row][col].setText("");
                                grid[row][col].setEditable(false);
                            }
                        }
                    }
                });
                
                gridPanel.add(grid[i][j]);
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private void solveCrossword() {
        // Get words from the textarea
        String[] wordsList = wordsArea.getText().split("\\s+");
        java.util.List<String> words = new ArrayList<>();

        for (String word : wordsList) {
            word = word.trim().toUpperCase();
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        
        if (words.isEmpty()) {
            statusLabel.setText("Please enter at least one word to solve.");
            return;
        }
        
        // Create a character grid from the current state
        char[][] charGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].getBackground().equals(Color.BLACK)) {
                    charGrid[i][j] = '#'; // Black cell
                } else {
                    String text = grid[i][j].getText().trim().toUpperCase();
                    charGrid[i][j] = text.isEmpty() ? '.' : text.charAt(0); // Empty or filled
                }
            }
        }
        
        // Solve the crossword
        CrosswordSolver solver = new CrosswordSolver();
        boolean solved = solver.solve(charGrid, words);
        
        if (solved) {
            // Update the UI with the solution
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (charGrid[i][j] != '#') {
                        grid[i][j].setText(String.valueOf(charGrid[i][j]));
                    }
                }
            }
            statusLabel.setText("Crossword solved successfully!");
        } else {
            statusLabel.setText("Could not solve the crossword with the given words. Try different words or grid layout.");
        }
    }
    
    private void clearGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].getBackground().equals(Color.BLACK)) {
                    grid[i][j].setText("");
                }
            }
        }
        wordsArea.setText("");
        statusLabel.setText("Grid cleared. Right-click cells to toggle black/white, then enter words and click Solve.");
    }
}