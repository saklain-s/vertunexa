import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;

public class Project extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Project() {
        setTitle("Project: Math Quiz and Expense Manager");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        MathQuizPanel mathQuizPanel = new MathQuizPanel(this);
        ExpenseManagerPanel expenseManagerPanel = new ExpenseManagerPanel(this);
        
        mainPanel.add(mainMenuPanel, "mainMenu");
        mainPanel.add(mathQuizPanel, "mathQuiz");
        mainPanel.add(expenseManagerPanel, "expenseManager");
        
        add(mainPanel);
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Project().setVisible(true);
        });
    }
}

class MainMenuPanel extends JPanel {
    public MainMenuPanel(Project frame) {
        setLayout(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        
        JButton quizButton = new JButton("Math Quiz");
        JButton expenseButton = new JButton("Expense Manager");
        JButton exitButton = new JButton("Exit");
        
        quizButton.addActionListener(e -> frame.showPanel("mathQuiz"));
        expenseButton.addActionListener(e -> frame.showPanel("expenseManager"));
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(quizButton);
        buttonPanel.add(expenseButton);
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

class ExpenseManagerPanel extends JPanel {
    private Project frame;
    private java.util.List<Expense> expenses = new ArrayList<>();
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private int nextId = 1;
    
    public ExpenseManagerPanel(Project frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Expense Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Amount", "Category", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        JButton addButton = new JButton("Add Expense");
        JButton editButton = new JButton("Edit Expense");
        JButton deleteButton = new JButton("Delete Expense");
        JButton reportButton = new JButton("Generate Report");
        JButton backButton = new JButton("Back to Main Menu");
        
        addButton.addActionListener(e -> addExpense());
        editButton.addActionListener(e -> editExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        reportButton.addActionListener(e -> generateReport());
        backButton.addActionListener(e -> frame.showPanel("mainMenu"));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JTextField createPlaceholderDateField() {
        JTextField dateField = new JTextField("YYYY-MM-DD");
        dateField.setForeground(Color.GRAY);
        dateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dateField.getText().equals("YYYY-MM-DD")) {
                    dateField.setText("");
                    dateField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (dateField.getText().trim().isEmpty()) {
                    dateField.setText("YYYY-MM-DD");
                    dateField.setForeground(Color.GRAY);
                }
            }
        });
        return dateField;
    }
    
    private void addExpense() {
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField dateField = createPlaceholderDateField();
        
        Object[] message = {
            "Amount:", amountField,
            "Category:", categoryField,
            "Date (YYYY-MM-DD):", dateField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                String category = categoryField.getText().trim();
                String dateStr = dateField.getText().trim();
                if(dateStr.equals("YYYY-MM-DD")) {
                    throw new Exception("Please enter a valid date.");
                }
                LocalDate date = LocalDate.parse(dateStr);
                Expense expense = new Expense(nextId++, amount, category, date);
                expenses.add(expense);
                addExpenseToTable(expense);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        }
    }
    
    private void editExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit.");
            return;
        }
        int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
        Expense expense = findExpenseById(expenseId);
        if (expense == null) {
            JOptionPane.showMessageDialog(this, "Expense not found.");
            return;
        }
        JTextField amountField = new JTextField(String.valueOf(expense.getAmount()));
        JTextField categoryField = new JTextField(expense.getCategory());
        JTextField dateField = createPlaceholderDateField();
        dateField.setText(expense.getDate().toString());
        dateField.setForeground(Color.BLACK);
        
        Object[] message = {
            "Amount:", amountField,
            "Category:", categoryField,
            "Date (YYYY-MM-DD):", dateField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                String category = categoryField.getText().trim();
                String dateStr = dateField.getText().trim();
                if(dateStr.equals("YYYY-MM-DD")) {
                    throw new Exception("Please enter a valid date.");
                }
                LocalDate date = LocalDate.parse(dateStr);
                expense.setAmount(amount);
                expense.setCategory(category);
                expense.setDate(date);
                updateExpenseTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        }
    }
    
    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
            return;
        }
        int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
        Expense expense = findExpenseById(expenseId);
        if (expense != null) {
            expenses.remove(expense);
            updateExpenseTable();
        }
    }
    
    private void generateReport() {
        if (expenses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No expenses to report.");
            return;
        }
        double total = 0;
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            total += expense.getAmount();
            categoryTotals.put(expense.getCategory(),
                categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        StringBuilder report = new StringBuilder();
        report.append("Total Expenses: ").append(total).append("\n");
        report.append("Expenses by Category:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            report.append(entry.getKey()).append(": ")
                  .append(String.format("%.2f", entry.getValue())).append("\n");
        }
        JOptionPane.showMessageDialog(this, report.toString(), "Expense Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Expense findExpenseById(int id) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) return expense;
        }
        return null;
    }
    
    private void addExpenseToTable(Expense expense) {
        tableModel.addRow(new Object[] {expense.getId(), expense.getAmount(), expense.getCategory(), expense.getDate()});
    }
    
    private void updateExpenseTable() {
        tableModel.setRowCount(0);
        for (Expense expense : expenses) {
            addExpenseToTable(expense);
        }
    }
}

class Expense {
    private int id;
    private double amount;
    private String category;
    private LocalDate date;
    
    public Expense(int id, double amount, String category, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    
    public int getId() { return id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    @Override
    public String toString() {
        return "Expense{" +
               "id=" + id +
               ", amount=" + amount +
               ", category='" + category + '\'' +
               ", date=" + date +
               '}';
    }
}
