import javax.swing.*;
import java.awt.*;

public class ABxCalculator extends JFrame {
    private JTextField aField, bField, xField;
    private JLabel resultLabel;

    public ABxCalculator() {
        setTitle("ab^x Calculator");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Title
        JLabel title = new JLabel("ab^x Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Formula
        JLabel formula = new JLabel("Function: f(x) = a × b^x", SwingConstants.CENTER);
        formula.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        formula.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Input Fields
        aField = new JTextField(10);
        bField = new JTextField(10);
        xField = new JTextField(10);

        aField.setFont(font);
        bField.setFont(font);
        xField.setFont(font);

        // Create label+field pairs and center align
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Enter a:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(aField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Enter b:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(bField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Enter x:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(xField, gbc);

        // Calculate Button
        JButton calcButton = new JButton("Calculate");
        calcButton.setFont(font);
        calcButton.setBackground(new Color(66, 133, 244));
        calcButton.setForeground(Color.WHITE);
        calcButton.setFocusPainted(false);

        // Result Label
        resultLabel = new JLabel("Result: ", SwingConstants.CENTER);
        resultLabel.setFont(font);
        resultLabel.setForeground(new Color(0, 102, 51));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add action
        calcButton.addActionListener(e -> {
            try {
                double a = Double.parseDouble(aField.getText());
                double b = Double.parseDouble(bField.getText());
                double x = Double.parseDouble(xField.getText());

                if (b <= 0) throw new IllegalArgumentException("Base b must be > 0.");

                double lnB = computeLn(b);
                double exp = computeExp(x * lnB);
                double result = a * exp;

                resultLabel.setText(String.format("Result: %.6f", result));
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values.");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Unexpected error: " + ex.getMessage());
            }
        });

        // Layout
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(formula, BorderLayout.BEFORE_FIRST_LINE);
        wrapper.add(inputPanel, BorderLayout.CENTER);
        wrapper.add(calcButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(wrapper, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }

    private double computeLn(double b) {
        double x = (b - 1) / (b + 1);
        double sum = 0.0, term = x;
        for (int i = 1; i < 100; i += 2) {
            sum += term / i;
            term *= x * x;
        }
        return 2 * sum;
    }

    private double computeExp(double x) {
        double sum = 1.0, term = 1.0;
        for (int i = 1; i < 30; i++) {
            term *= x / i;
            sum += term;
        }
        return sum;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ABxCalculator::new);
    }
}
