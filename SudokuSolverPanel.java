import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

class SudokuSolverPanel extends JPanel {
    private JTextField[][] cells = new JTextField[9][9];
    private JButton solveButton;
    private JButton clearButton;
    private JButton backButton;
    private JLabel statusLabel;
    private MainFrame parent;

    public SudokuSolverPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Sudoku Solver");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        // Grid Panel
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setPreferredSize(new Dimension(450, 450));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField(1);
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.PLAIN, 20));
                MatteBorder border = BorderFactory.createMatteBorder(
                    row % 3 == 0 ? 2 : 1,
                    col % 3 == 0 ? 2 : 1,
                    row % 3 == 2 ? 2 : 1,
                    col % 3 == 2 ? 2 : 1,
                    Color.BLACK
                );
                cells[row][col].setBorder(border);
                gridPanel.add(cells[row][col]);
            }
        }

        // Button Panel
        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back to Menu");

        solveButton.addActionListener(e -> solveSudoku());
        clearButton.addActionListener(e -> clearGrid());
        backButton.addActionListener(e -> parent.showMainMenu());

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        // Status Label
        statusLabel = new JLabel("Enter numbers and click Solve");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        // Bottom Panel with Buttons + Status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add everything to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void solveSudoku() {
        try {
            int[][] board = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        board[i][j] = 0;
                    } else {
                        board[i][j] = Integer.parseInt(text);
                        if (board[i][j] < 1 || board[i][j] > 9) {
                            throw new NumberFormatException("Values must be between 1 and 9");
                        }
                    }
                }
            }

            if (!isValidSudoku(board)) {
                statusLabel.setText("Invalid Sudoku puzzle. Please check your input.");
                return;
            }

            SudokuSolver solver = new SudokuSolver();
            boolean solved = solver.solve(board);
            if (solved) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        cells[i][j].setText(String.valueOf(board[i][j]));
                    }
                }
                statusLabel.setText("Sudoku solved successfully!");
            } else {
                statusLabel.setText("No solution exists for this puzzle.");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid input. Please enter numbers 1-9 only.");
        }
    }

    private boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];
            boolean[] box = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    if (row[board[i][j]]) return false;
                    row[board[i][j]] = true;
                }
                if (board[j][i] != 0) {
                    if (col[board[j][i]]) return false;
                    col[board[j][i]] = true;
                }

                int boxRow = 3 * (i / 3) + j / 3;
                int boxCol = 3 * (i % 3) + j % 3;
                if (board[boxRow][boxCol] != 0) {
                    if (box[board[boxRow][boxCol]]) return false;
                    box[board[boxRow][boxCol]] = true;
                }
            }
        }
        return true;
    }

    private void clearGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
            }
        }
        statusLabel.setText("Grid cleared. Enter numbers and click Solve.");
    }
}
