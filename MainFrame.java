/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/**
 *
 * @author krish
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    public MainFrame() {
        setTitle("Puzzle Solver Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Puzzle Solver Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Choose a puzzle type to solve");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton sudokuButton = new JButton("Sudoku Solver");
        JButton crosswordButton = new JButton("Crossword Solver");
        
        sudokuButton.addActionListener(e -> cardLayout.show(mainPanel, "sudoku"));
        crosswordButton.addActionListener(e -> cardLayout.show(mainPanel, "crossword"));
        
        buttonPanel.add(sudokuButton);
        buttonPanel.add(crosswordButton);
        
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(subtitleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        welcomePanel.add(buttonPanel);
        welcomePanel.add(Box.createVerticalGlue());
        
        // Add panels to card layout
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(new SudokuSolverPanel(this), "sudoku");
        mainPanel.add(new CrosswordSolverPanel(this), "crossword");
        
        // Show welcome panel initially
        cardLayout.show(mainPanel, "welcome");
        
        // Add to frame
        add(mainPanel);
    }
    
    public void showMainMenu() {
        cardLayout.show(mainPanel, "welcome");
    }
}