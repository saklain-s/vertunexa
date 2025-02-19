import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Project extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Project() {
        setTitle("Project: Math Quiz");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        MathQuizPanel mathQuizPanel = new MathQuizPanel(this);
        
        mainPanel.add(mainMenuPanel, "mainMenu");
        mainPanel.add(mathQuizPanel, "mathQuiz");
        
        add(mainPanel);
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project().setVisible(true));
    }
}

class MainMenuPanel extends JPanel {
    public MainMenuPanel(Project frame) {
        setLayout(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        
        JButton quizButton = new JButton("Math Quiz");
        JButton exitButton = new JButton("Exit");
        
        quizButton.addActionListener(e -> frame.showPanel("mathQuiz"));
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(quizButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, BorderLayout.CENTER);
    }
}

class MathQuizPanel extends JPanel {
    private Project frame;
    private JLabel questionLabel, scoreLabel;
    private JTextField answerField;
    private JButton submitButton, nextButton, backButton;
    private Random random = new Random();
    private int totalQuestions = 5;
    private int currentQuestion = 0;
    private int score = 0;
    private int num1, num2, correctAnswer;
    private char operator;
    
    public MathQuizPanel(Project frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Math Quiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        questionLabel = new JLabel("Click \"Next Question\" to start.", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        centerPanel.add(questionLabel);
        
        answerField = new JTextField();
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(answerField);
        
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(scoreLabel);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit Answer");
        nextButton = new JButton("Next Question");
        backButton = new JButton("Back to Main Menu");
        
        submitButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> nextQuestion());
        backButton.addActionListener(e -> frame.showPanel("mainMenu"));
        
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void nextQuestion() {
        if (currentQuestion >= totalQuestions) {
            JOptionPane.showMessageDialog(this, "Quiz Over! Your final score: " + score + " / " + totalQuestions);
            currentQuestion = 0;
            score = 0;
            scoreLabel.setText("Score: " + score);
        }
        currentQuestion++;
        generateQuestion();
        questionLabel.setText("Question " + currentQuestion + ": " + num1 + " " + operator + " " + num2 + " = ?");
        answerField.setText("");
    }
    
    private void generateQuestion() {
        num1 = random.nextInt(10) + 1;
        num2 = random.nextInt(10) + 1;
        int op = random.nextInt(3);
        switch (op) {
            case 0:
                operator = '+';
                correctAnswer = num1 + num2;
                break;
            case 1:
                operator = '-';
                correctAnswer = num1 - num2;
                break;
            case 2:
                operator = '*';
                correctAnswer = num1 * num2;
                break;
            default:
                operator = '+';
                correctAnswer = num1 + num2;
        }
    }
    
    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText().trim());
            if (userAnswer == correctAnswer) {
                JOptionPane.showMessageDialog(this, "Correct!");
                score++;
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect. The correct answer is " + correctAnswer);
            }
            scoreLabel.setText("Score: " + score);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }
}
