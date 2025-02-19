import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TemperatureConverterApp extends JFrame {

    private JTextField tempField;
    private JComboBox<String> fromBox;
    private JComboBox<String> toBox;
    private JLabel resultLabel;

    public TemperatureConverterApp() {
        super("Temperature Converter");

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore and use default look and feel
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel();
        JLabel title = new JLabel("Temperature Converter");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Temperature:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        tempField = new JTextField(15);
        tempField.setForeground(Color.BLACK);
        tempField.setBackground(Color.WHITE);
        tempField.setOpaque(true);
        tempField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(tempField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("From:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        fromBox = new JComboBox<>(new String[] {"Celsius (C)", "Fahrenheit (F)", "Kelvin (K)"});
        mainPanel.add(fromBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("To:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        toBox = new JComboBox<>(new String[] {"Celsius (C)", "Fahrenheit (F)", "Kelvin (K)"});
        mainPanel.add(toBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton convertButton = new JButton("Convert");
        mainPanel.add(convertButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        resultPanel.add(resultLabel);
        add(resultPanel, BorderLayout.SOUTH);

        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertTemperature();
            }
        });
    }

    private void convertTemperature() {
        try {
            double temp = Double.parseDouble(tempField.getText());
            String fromScale = getScale((String) fromBox.getSelectedItem());
            String toScale = getScale((String) toBox.getSelectedItem());
            double convertedTemp = TemperatureConverter.convert(temp, fromScale, toScale);
            if (Double.isNaN(convertedTemp)) {
                resultLabel.setText("Error: Invalid scale provided.");
            } else {
                resultLabel.setText(String.format("Result: %.2f %s", convertedTemp, toScale));
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Error: Please enter a valid number.");
        }
    }

    private String getScale(String s) {
        int start = s.indexOf('(');
        int end = s.indexOf(')');
        if (start != -1 && end != -1) {
            return s.substring(start + 1, end);
        }
        return s;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TemperatureConverterApp().setVisible(true);
            }
        });
    }
}

class TemperatureConverter {
    public static double convert(double temp, String from, String to) {
        double tempInC;
        if (from.equals("C")) {
            tempInC = temp;
        } else if (from.equals("F")) {
            tempInC = (temp - 32) * 5 / 9;
        } else if (from.equals("K")) {
            tempInC = temp - 273.15;
        } else {
            return Double.NaN;
        }

        if (to.equals("C")) {
            return tempInC;
        } else if (to.equals("F")) {
            return tempInC * 9 / 5 + 32;
        } else if (to.equals("K")) {
            return tempInC + 273.15;
        }
        return Double.NaN;
    }
}
