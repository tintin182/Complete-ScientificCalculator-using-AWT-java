import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

// Main class ng Scientific Calculator of app
// JFrame for window, ActionListener to handle actions of buttons
public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField display;  // Text field kung saan makikita yung input at resulta
    private StringBuilder input; // Para sa kasalukuyang input - hindi gaanong ginagamit dito pero pwede pa
    private boolean startNewNumber = true; // Flag para malaman kung mag-start ng bagong numero (para i-clear or lagyan)
    private boolean isRadians = false; // Flag kung radians o degrees ang gamit (default degrees)
    private JToggleButton radToggle; // Button para switch radians/degrees
    private JToggleButton onOffToggle; // Button para ON/OFF ng calc

    // Constructor, dito nilalagay lahat ng GUI setup at event handlers
    public ScientificCalculator() {
        input = new StringBuilder();

        setTitle("Scientific Calculator");
        setSize(400, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Setup display: di pwedeng i-edit, naka-align right, stylized font at kulay
        display = new JTextField("0");
        display.setFont(new Font("Monospaced", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(230, 230, 250)); // light lavender na background
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        display.setPreferredSize(new Dimension(400, 70));
        add(display, BorderLayout.NORTH);

        // Panel para sa ON/OFF at radians toggle buttons
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        togglePanel.setBackground(new Color(230, 230, 250));

        // ON/OFF toggle button setup
        onOffToggle = new JToggleButton("ON");
        onOffToggle.setFont(new Font("Arial", Font.BOLD, 14));
        onOffToggle.setFocusPainted(false);
        onOffToggle.setSelected(true); // Simula ON yung calculator
        onOffToggle.addItemListener(e -> {
            boolean isOn = onOffToggle.isSelected();
            onOffToggle.setText(isOn ? "ON" : "OFF");
            setCalculatorEnabled(isOn); // Enable or disable lahat ng controls depende sa toggle
        });
        togglePanel.add(onOffToggle);

        // Radians/Degrees toggle button setup
        radToggle = new JToggleButton("Radians");
        radToggle.setFont(new Font("Arial", Font.BOLD, 14));
        radToggle.setFocusPainted(false);
        radToggle.addItemListener(e -> {
            isRadians = radToggle.isSelected(); // Update radians flag depende sa toggle state
            radToggle.setText(isRadians ? "Radians" : "Degrees");
        });
        togglePanel.add(radToggle);

        add(togglePanel, BorderLayout.AFTER_LAST_LINE);

        // Top panel para sa display at spacing sa pagitan ng display at buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(230, 230, 250));
        topPanel.add(display);
        topPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Space sa baba ng display
        topPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Additional space
        add(topPanel, BorderLayout.NORTH);

        // Button panel para sa lahat ng calculator functions at numbers
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(10, 1, 1, 5)); // 10 rows na vertically nakaayos
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, -90, 0)); // Adjust sa baba for margin

        // Row 1: Clear (AC), Delete (DEL), Percentage, Divide
        JPanel row1 = new JPanel(new GridLayout(1, 4, 5, 5));
        row1.setBackground(new Color(245, 245, 255));
        row1.add(createButton("AC"));
        row1.add(createButton("DEL"));
        row1.add(createButton("%"));
        row1.add(createButton("/"));
        buttonPanel.add(row1);

        // Row 2: 7, 8, 9, multiplication
        JPanel row2 = new JPanel(new GridLayout(1, 4, 5, 5));
        row2.setBackground(new Color(245, 245, 255));
        row2.add(createButton("7"));
        row2.add(createButton("8"));
        row2.add(createButton("9"));
        row2.add(createButton("*"));
        buttonPanel.add(row2);

        // Row 3: 4, 5, 6, subtraction
        JPanel row3 = new JPanel(new GridLayout(1, 4, 5, 5));
        row3.setBackground(new Color(245, 245, 255));
        row3.add(createButton("4"));
        row3.add(createButton("5"));
        row3.add(createButton("6"));
        row3.add(createButton("-"));
        buttonPanel.add(row3);

        // Row 4: 1, 2, 3, addition
        JPanel row4 = new JPanel(new GridLayout(1, 4, 5, 5));
        row4.setBackground(new Color(245, 245, 255));
        row4.add(createButton("1"));
        row4.add(createButton("2"));
        row4.add(createButton("3"));
        row4.add(createButton("+"));
        buttonPanel.add(row4);

        // Row 5: 0, decimal point, equals, square root
        JPanel row5 = new JPanel(new GridLayout(1, 4, 5, 5));
        row5.setBackground(new Color(245, 245, 255));
        row5.add(createButton("0"));
        row5.add(createButton("."));
        row5.add(createButton("="));
        row5.add(createButton("√"));
        buttonPanel.add(row5);

        // Row 6: sin, cos, tan, cot
        JPanel row6 = new JPanel(new GridLayout(1, 4, 5, 5));
        row6.setBackground(new Color(245, 245, 255));
        row6.add(createButton("sin"));
        row6.add(createButton("cos"));
        row6.add(createButton("tan"));
        row6.add(createButton("cot"));
        buttonPanel.add(row6);

        // Row 7: sec, csc, log, ln
        JPanel row7 = new JPanel(new GridLayout(1, 4, 5, 5));
        row7.setBackground(new Color(245, 245, 255));
        row7.add(createButton("sec"));
        row7.add(createButton("csc"));
        row7.add(createButton("log"));
        row7.add(createButton("ln"));
        buttonPanel.add(row7);

        // Row 8: x^y, square, factorial, exponential
        JPanel row8 = new JPanel(new GridLayout(1, 4, 5, 5));
        row8.setBackground(new Color(245, 245, 255));
        row8.add(createButton("x^y"));
        row8.add(createButton("sqr"));
        row8.add(createButton("fact"));
        row8.add(createButton("exp"));
        buttonPanel.add(row8);

        add(buttonPanel, BorderLayout.CENTER);

        this.buttonPanel = buttonPanel; // Reference for enable/disable buttons

        setCalculatorEnabled(true); // Start enabled

        setVisible(true); // Show window
    }

    private JPanel buttonPanel; // Store buttons container

    // Ito yung nag-e-enable or nagdi-disable ng buong calculator kasi may ON/OFF toggle
    private void setCalculatorEnabled(boolean enabled) {
        display.setEnabled(enabled);
        buttonPanel.setEnabled(enabled);

        // Loop through lahat ng rows and buttons para i-set ang enabled state
        for (Component row : buttonPanel.getComponents()) {
            if (row instanceof Container) {
                for (Component comp : ((Container) row).getComponents()) {
                    comp.setEnabled(enabled);
                }
            }
            row.setEnabled(enabled);
        }

        radToggle.setEnabled(enabled); // Enable radians toggle buttons din

        // ON/OFF toggle palaging enabled para pwede mo siya i-toggle kahit kailan
        onOffToggle.setEnabled(true);
    }

    // creating button na styled and has hover effect
    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(220, 220, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 220), 1));

        // Pag-hover sa button, mag-change color ng konti
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(180, 180, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 255));
            }
        });

        button.addActionListener(this); // Register event handler sa click
        return button;
    }

    // Simpleng factorial function (para sa fact button)
    private double factorial(int n) {
        if (n < 0) return Double.NaN; // Walang factorial sa negative
        double fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    // I-parse yung number galing sa display sa double
    private double parseInput() {
        try {
            return Double.parseDouble(display.getText());
        } catch (NumberFormatException e) {
            return 0; // Default zero pag invalid input
        }
    }

    // Icalculate yung mga scientific functions na direct gamit lang yung display input
    private void calculateScientificFunction(String func) {
        double value = parseInput();
        double result = 0;
        double angle = value;

        // Convert sa radians kung degrees mode ang gamit
        if (!isRadians) {
            angle = Math.toRadians(value);
        }

        // Switch case para sa bawat scientific function
        switch (func) {
            case "sin":
                result = Math.sin(angle);
                break;
            case "cos":
                result = Math.cos(angle);
                break;
            case "tan":
                result = Math.tan(angle);
                break;
            case "cot":
                result = 1.0 / Math.tan(angle);
                break;
            case "sec":
                result = 1.0 / Math.cos(angle);
                break;
            case "csc":
                result = 1.0 / Math.sin(angle);
                break;
            case "log":
                result = Math.log10(value);
                break;
            case "ln":
                result = Math.log(value);
                break;
            case "sqrt":
                result = Math.sqrt(value);
                break;
            case "x²": // square function (di gamit sa buttons but available)
                result = Math.pow(value, 2);
                break;
            case "x!": // factorial (same here)
                result = factorial((int) value);
                break;
            case "exp":
                result = Math.exp(value);
                break;
        }

        display.setText(formatResult(result)); // Format at ipakita sa display
        startNewNumber = true; // Ready na sa bagong input
    }

    // Format ng result para hindi palaging ga-decimal, at para may max decimal places
    private String formatResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Error"; // Ipakita error pag invalid
        }
        // Kapag integer ang resulta, tanggalin decimal zeros
        if (result == (long) result) {
            return String.format("%d", (long) result);
        }
        DecimalFormat df = new DecimalFormat("0.##########"); // 10 decimal places max
        return df.format(result);
    }

    // I-evaluate yung expression na nasa display kapag pinindot ang "="
    private void calculateExpression() {
        try {
            String expr = display.getText();
            expr = expr.replaceAll("×", "*").replaceAll("÷", "/");

            // Count parentheses at dagdagan para balance
            int openParens = 0;
            int closeParens = 0;
            for (char c : expr.toCharArray()) {
                if (c == '(') openParens++;
                else if (c == ')') closeParens++;
            }
            while (closeParens < openParens) {
                expr += ")";
                closeParens++;
            }

            double result = eval(expr); // Gamitin yung recursive parser para sa operations
            display.setText(formatResult(result));
            startNewNumber = true;
        } catch (Exception e) {
            display.setText("Error"); // Kapag may mali
            startNewNumber = true;
        }
    }

    // Recursive descent parser na may support sa arithmetic operations at scientific functions
    private double eval(String expr) throws Exception {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() throws Exception {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Expression grammar for parsing math expressions
            double parseExpression() throws Exception {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() throws Exception {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) {
                        double d = parseFactor();
                        if (d == 0) throw new ArithmeticException("Division by zero");
                        x /= d;
                    }
                    else return x;
                }
            }

            double parseFactor() throws Exception {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expr.substring(startPos, this.pos);
                    if (eat('(')) {
                        double arg = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')'");
                        x = applyFunc(func, arg);
                    } else {
                        x = parseFactor();
                        x = applyFunc(func, x);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                // New handling for postfix percentage operator - handle multiple consecutive '%'
                while (eat('%')) {
                    x = x / 100.0;
                }

                return x;
            }

            // apply ang scientific function (sin, cos, log etc.) sa value x
            double applyFunc(String func, double x) throws Exception {
                boolean useRadians = ScientificCalculator.this.isRadians;
                double angle = x;
                if (!useRadians) {
                    angle = Math.toRadians(x);
                }
                switch (func) {
                    case "sqrt": return Math.sqrt(x);
                    case "sin": return Math.sin(angle);
                    case "cos": return Math.cos(angle);
                    case "tan": return Math.tan(angle);
                    case "cot": return 1.0 / Math.tan(angle);
                    case "sec": return 1.0 / Math.cos(angle);
                    case "csc": return 1.0 / Math.sin(angle);
                    case "log": return Math.log10(x);
                    case "ln": return Math.log(x);
                    case "exp": return Math.exp(x);
                    case "fact": return factorial((int) x);
                    case "sqr": return x * x;
                    default: throw new RuntimeException("Unknown function: " + func);
                }
            }
        }.parse();
    }

    // Handle button presses sa calculator
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // Number or decimal point input check
        if ("0123456789.".contains(cmd)) {
            if (startNewNumber) {
                // Kapag bago lang nagsimula, palitan agad sa pinindot (0. kung decimal point)
                display.setText(cmd.equals(".") ? "0." : cmd);
                startNewNumber = false;
            } else {
                // Kapag may decimal na, huwag magdagdag ng pangalawang decimal
                if (!(cmd.equals(".") && display.getText().contains("."))) {
                    display.setText(display.getText() + cmd);
                }
            }
        } else if (cmd.equals("AC")) {
            // Clear everything, balik sa 0
            display.setText("0");
            startNewNumber = true;
        } else if (cmd.equals("DEL")) {
            // Delete last character, kung isa na lang tinitext set sa 0
            String text = display.getText();
            if (text.length() > 1) {
                display.setText(text.substring(0, text.length() - 1));
            } else {
                display.setText("0");
                startNewNumber = true;
            }
        } else if ("+-*/".contains(cmd)) {
            // Operators lang, dapat hindi nagsisimula ng bagong number para maiwas invalid expression
            if (!startNewNumber) {
                display.setText(display.getText() + cmd);
                startNewNumber = false;
            }
        } else if (cmd.equals("%")) {
            // Append % as postfix operator only if last char is digit or ')'
            String text = display.getText();
            if (!startNewNumber && text.length() > 0) {
                char lastChar = text.charAt(text.length() - 1);
                if (Character.isDigit(lastChar) || lastChar == ')') {
                    display.setText(text + "%");
                    startNewNumber = false;
                }
            }
        } else if (cmd.equals("=")) {
            // Evaluate expression kapag "=" pinindot
            calculateExpression();
        } else if ("sin cos tan cot sec csc log ln sqr exp".contains(cmd)) {
            // Kapag scientific function pinindot, lagyan ng functionName(
            if (startNewNumber && display.getText().equals("0")) {
                display.setText(cmd + "(");
            } else {
                display.setText(display.getText() + cmd + "(");
            }
            startNewNumber = false;
        } else if (cmd.equals("√")) {
            // Square root function, lagyan ng sqrt(
            if (startNewNumber && display.getText().equals("0")) {
                display.setText("sqrt(");
            } else {
                display.setText(display.getText() + "sqrt(");
            }
            startNewNumber = false;
        } else if (cmd.equals("fact")) {
            // Factorial function, lagyan ng fact(
            if (startNewNumber && display.getText().equals("0")) {
                display.setText("fact(");
            } else {
                display.setText(display.getText() + "fact(");
            }
            startNewNumber = false;
        } else if (cmd.equals("x^y")) {
            // Custom power function: tanungin ang user for exponent then calculate na agad
            String baseStr = display.getText();
            String expStr = JOptionPane.showInputDialog(this, "Enter exponent value:", "Exponent", JOptionPane.PLAIN_MESSAGE);
            if (expStr != null) {
                try {
                    double base = Double.parseDouble(baseStr);
                    double exponent = Double.parseDouble(expStr);
                    double result = Math.pow(base, exponent);
                    display.setText(formatResult(result));
                    startNewNumber = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                    startNewNumber = true;
                }
            }
        }
    }

    // Main method para simulan ang program safely sa Event Dispatch Thread
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScientificCalculator());
    }
}

/*
diba diba! huh? pano ngay yun? kami na don hahahha sige na bye!!! oms
*/
