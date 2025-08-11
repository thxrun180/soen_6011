package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * GUI calculator for f(x) = a * b^x (F5) using custom ln/exp.
 * Includes accessibility hooks, logging, and semantic version in the title.
 */
public class ExponentCalc extends JFrame {
    /** Semantic version shown in the window title. */
    private static final String VERSION = "1.0.0";

    /** Input field for coefficient a (any real). */
    private JTextField aTextField;

    /** Input field for base b (must be > 0). */
    private JTextField bTextField;

    /** Input field for exponent x (any real). */
    private JTextField xTextField;

    /** Label that displays the result. */
    private JLabel resultLabel;

    /** Builds the window and wires UI and listeners. */
    public ExponentCalc() {
        super("ab^x Calculator — v" + VERSION);
        setSize(440, 370);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("ab^x Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel formula = new JLabel("Function: f(x) = a × b^x", SwingConstants.CENTER);
        formula.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        formula.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        aTextField = new JTextField(10);
        bTextField = new JTextField(10);
        xTextField = new JTextField(10);

        // Fonts
        Font baseFont = new Font("Segoe UI", Font.PLAIN, 16);
        aTextField.setFont(baseFont);
        bTextField.setFont(baseFont);
        xTextField.setFont(baseFont);

        // Tooltips (short to respect line length).
        aTextField.setToolTipText("Enter a (any real)");
        bTextField.setToolTipText("Enter b (> 0)");
        xTextField.setToolTipText("Enter x (any real)");

        // Accessibility names/descriptions.
        aTextField.getAccessibleContext().setAccessibleName("Input a");
        aTextField.getAccessibleContext().setAccessibleDescription("Multiplier a");

        bTextField.getAccessibleContext().setAccessibleName("Input b");
        bTextField.getAccessibleContext().setAccessibleDescription("Base b; must be greater than zero");

        xTextField.getAccessibleContext().setAccessibleName("Input x");
        xTextField.getAccessibleContext().setAccessibleDescription("Exponent x");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Enter a (a ∈ ℝ):", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(aTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Enter b (b > 0):", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(bTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Enter x (x ∈ ℝ):", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        inputPanel.add(xTextField, gbc);

        JButton calcButton = new JButton("Calculate");
        JButton clearButton = new JButton("Clear");

        calcButton.setFont(baseFont);
        calcButton.setBackground(new Color(66, 133, 244));
        calcButton.setForeground(Color.WHITE);
        calcButton.setFocusPainted(false);
        calcButton.getAccessibleContext().setAccessibleName("Calculate");
        calcButton.getAccessibleContext().setAccessibleDescription("Compute a * b^x");

        clearButton.setFont(baseFont);
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.getAccessibleContext().setAccessibleName("Clear");
        clearButton.getAccessibleContext().setAccessibleDescription("Reset inputs and result");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(calcButton);
        buttonPanel.add(clearButton);

        resultLabel = new JLabel("Result: ", SwingConstants.CENTER);
        resultLabel.setFont(baseFont);
        resultLabel.setForeground(new Color(0, 102, 51));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        resultLabel.getAccessibleContext().setAccessibleName("Result");
        resultLabel.getAccessibleContext().setAccessibleDescription("Computed value");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(formula, BorderLayout.BEFORE_FIRST_LINE);
        wrapper.add(inputPanel, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(wrapper, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);

        calcButton.addActionListener(event -> onCalculate());
        clearButton.addActionListener(event -> onClear());

        setVisible(true);
    }
    /**
     * Computes f(x) = a * b^x using custom ln and exp helpers.
     *
     * @param a coefficient (any real)
     * @param b base (must be > 0)
     * @param x exponent (any real)
     * @return computed value
     * @throws IllegalArgumentException when b <= 0
     * @throws ArithmeticException when result is not finite
     */
    public static double abx(double a, double b, double x) {
        if (b <= 0) {
            throw new IllegalArgumentException("b must be > 0");
        }
        double lnB = computeLn(b);
        double bx = computeExp(x * lnB);
        double r = a * bx;
        if (!Double.isFinite(r)) {
            throw new ArithmeticException("Result overflow/invalid");
        }
        return r;
    }

    /**
     * Approximates ln(b) via the atanh series:
     * ln(b) = 2 * (x + x^3/3 + x^5/5 + ...), where x = (b - 1) / (b + 1).
     *
     * @param b positive base
     * @return approximation of ln(b)
     */
    static double computeLn(double b) {
        if (b <= 0) {
            throw new IllegalArgumentException("b must be > 0");
        }
        double x = (b - 1) / (b + 1);
        double sum = 0.0;
        double term = x;
        for (int i = 1; i < 100; i += 2) {
            sum += term / i;
            term *= x * x;
        }
        return 2 * sum;
    }

    /**
     * Approximates e^x via Maclaurin series: sum_{i=0..n} x^i / i!.
     *
     * @param x exponent
     * @return approximation of e^x
     */
    static double computeExp(double x) {
        double sum = 1.0;
        double term = 1.0;
        for (int i = 1; i < 30; i++) {
            term *= x / i;
            sum += term;
        }
        return sum;
    }

    public static double compute(double v, double v1, double v2) {
        return v;
    }

    /** Handles Calculate button click. */
    private void onCalculate() {
        try {
            boolean missing =
                    aTextField.getText().isEmpty()
                            || bTextField.getText().isEmpty()
                            || xTextField.getText().isEmpty();
            if (missing) {
                throw new IllegalArgumentException("All fields (a, b, x) must be filled.");
            }

            double a = Double.parseDouble(aTextField.getText());
            double b = Double.parseDouble(bTextField.getText());
            double x = Double.parseDouble(xTextField.getText());

            double result = abx(a, b, x);
            resultLabel.setText(String.format("Result: %.6f", result));
            logToFile(a, b, x, result);

        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for a, b, and x.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (ArithmeticException ex) {
            showError("Calculation error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    /** Handles Clear button click. */
    private void onClear() {
        aTextField.setText("");
        bTextField.setText("");
        xTextField.setText("");
        resultLabel.setText("Result: ");
    }

    /** Shows an error dialog. */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Appends a log line to a local file. */
    private void logToFile(double a, double b, double x, double result) {
        try (FileWriter writer = new FileWriter("ExponentCalc.log", true)) {
            String line =
                    "[" + LocalDateTime.now() + "] "
                            + "a=" + a + ", b=" + b + ", x=" + x
                            + " => Result: " + String.format("%.6f", result)
                            + System.lineSeparator();
            writer.write(line);
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }

    /** App entry point. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExponentCalc::new);
    }
}
