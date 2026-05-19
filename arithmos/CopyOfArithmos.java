import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;        
import java.awt.BasicStroke;        
import java.awt.Graphics2D;         
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.event.*;
import java.util.Set;
import java.util.function.*;

public class CopyOfArithmos {
    
    static JFrame frame = new JFrame("CopyOfArithmos");
    static JTextPane display = new JTextPane(); 
    static boolean isDegreeMode = false;
    static double lastAnswer = 0;
    static double memory = 0;
    static JLabel modeLabel;
    static JPanel mainPanel = new JPanel();
    static List<JTextField> matrixAFields = new ArrayList<>();
    static List<JTextField> matrixBFields = new ArrayList<>();
    static int rowsA = 2, colsA = 2, rowsB = 2, colsB = 2; 
    static boolean isUppercase = false;
    static List<RoundedButton> alphaButtons = new ArrayList<>();
    static List<RoundedButton> greekButtons = new ArrayList<>();
    static JTabbedPane tabbedPane;
    static JTextField inputField = new JTextField();
    static GraphCanvas canvas;
    static Color[] graphColors = {
    Color.CYAN, Color.MAGENTA, Color.ORANGE, 
    Color.GREEN, Color.PINK, Color.YELLOW, Color.RED
    };
    static int colorIndex = 0;
    static DefaultListModel<EquationEntry> model = new DefaultListModel<>();
    static JList<EquationEntry> list = new JList<>(model);
    // Class-level variables (Fields)
    private static JLabel hexLbl, decLbl, octLbl, binLbl;
    private static JComboBox<String> categoryBox, fromUnitBox, toUnitBox;
    private static JTextField convInput;
    private static JLabel convResult;
    private static final Color bgColor = new Color(25, 25, 25);
    private static final Color dropdownBg = new Color(40, 40, 40);
    private static final Color textColor = Color.WHITE;
    private static final Color focusColor = new Color(0, 255, 190);

    private static final Font segoeFont = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font inputFont = new Font("Segoe UI", Font.BOLD, 48);
    // Data for the dropdowns
    static Map<String, Map<String, Double>> unitMap = new LinkedHashMap<>();
    private static JButton convertButton;
    private static double zoomScale = 250.0; // Our dynamic zoom
    private static JSlider zoomSlider;
    private static double rotX = Math.PI / 4, rotY = Math.PI / 4;
    private static Point lastMousePos;
    private static final double FOCAL_LENGTH = 10.0; // Distance of the camera
    private static Point lastPoint;
    private static Timer animationTimer;
    private static double time = 0;
    private static String currentFormula = "";
    private static JTextField formulaInput;
    private static boolean isAnimated = true;
    private static Point3D hoveredPoint = null; // To store {x, y, z}
    private static Color themeColor1 = new Color(0, 255, 170); // Mint
    private static Color themeColor2 = new Color(0, 100, 255); // Blue
    static Map<String, Theme> themeMap = new HashMap<>();
    private static class EquationLayer {
    String formula;
    Color color;
    EquationLayer(String f, Color c) { this.formula = f; this.color = c; }
    }
    static Theme currentTheme = new Theme(
        new Color(25, 25, 25),  // Background
        Color.WHITE,               // Foreground
        new Color(34, 34, 34),  // Regular buttons
        new Color(17, 17, 17),  // Function buttons
        new Color(230, 136, 136),    // C & CE
        new Color(238, 255, 122),    // Memory & DEG
        new Color(238, 255, 122)      // Equals
    );
    
    private static final java.util.regex.Pattern NUMERIC_PATTERN = java.util.regex.Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static ArrayList<EquationLayer> layers = new ArrayList<>();
    private static JPanel eqHistoryPanel; // The "EQ" panel
    private static final Color[] LAYER_COLORS = {
    new Color(0, 255, 170), // Mint
    new Color(255, 100, 100), // Red
    new Color(100, 200, 255), // Blue
    new Color(255, 200, 50),  // Yellow
    new Color(200, 100, 255)  // Purple
    };
    private static boolean isPowerMode = false; 
    static JLabel displayLabel = new JLabel("0");
    static JLabel previewLabel = new JLabel(""); 
    static JPanel displayPanel = new JPanel(); // Declare this globally too
    private static final StringBuilder currentMode = new StringBuilder("Standard");
    private static JPanel cardPanel; // Panel to hold different calculator modes
    private static CardLayout cardLayout;
    private static JPanel sideMenu;
    private static JLayeredPane layeredPane;
    private static final Color MENU_BG = new Color(30, 30, 30); // Matches your dark theme
    private static final Color HOVER_COLOR = new Color(50, 50, 50);
    private static boolean isMenuOpen = false;
    private static JPanel currentButtonGrid = null;
    static JPanel buttonsPanel;

    public static void main(String[] args) {

    initializeDistanceData();
    initializeVolumeData();
    initializeVelocityData();
    initializeAccelerationData();
    initializeMomentumData();
    initializeAreaData();
    initializeMechanicsDynamics();
    initializeMatterGravity();
    initializeTimeTemperature();
    initializeElectromagnetism();
    initializeThermalWaves();
    initializeRotationalDynamics();
    initializeRadiationLight();
    initializeMathComputing();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 600);
    frame.setLocationRelativeTo(null);

    // Use layered pane as content pane directly
    layeredPane = new JLayeredPane();
    layeredPane.setLayout(null);
    frame.setContentPane(layeredPane);

    frame.setVisible(true);

    Insets insets = frame.getInsets();
    int width = frame.getWidth() - insets.left - insets.right;
    int height = frame.getHeight() - insets.top - insets.bottom;

    layeredPane.setBounds(0, 0, width, height);

    // ===== MAIN PANEL =====
    mainPanel = new JPanel(null);
    mainPanel.setBounds(0, 0, width, height);
    mainPanel.setBackground(currentTheme.functionButton);

    // ===== DISPLAY PANEL =====
    displayPanel.setLayout(new BorderLayout());
    displayPanel.setBounds(0, 0, width, 160);
    displayPanel.setBackground(currentTheme.background);

    display.setBackground(currentTheme.background);
    display.setForeground(currentTheme.foreground);
    display.setCaretColor(currentTheme.foreground);
    display.setFont(new Font("Segoe UI", Font.BOLD, 42));
    display.setBorder(BorderFactory.createEmptyBorder(60, 10, 10, 10));

    // Right alignment for JTextPane
    StyledDocument doc = display.getStyledDocument();
    SimpleAttributeSet rightAlign = new SimpleAttributeSet();
    StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
    StyleConstants.setFontFamily(rightAlign, "Segoe UI");
    StyleConstants.setFontSize(rightAlign, 50);
    StyleConstants.setBold(rightAlign, true);
    doc.setParagraphAttributes(0, doc.getLength(), rightAlign, false);

    // ===== RAD / PREVIEW PANEL =====
    modeLabel = new JLabel("RAD");
    modeLabel.setForeground(new Color(97, 120, 195));
    modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    modeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); 
    // slight left padding to align under hamburger

    previewLabel.setForeground(Color.WHITE);
    previewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

    bottomPanel.add(modeLabel, BorderLayout.WEST);
    bottomPanel.add(previewLabel, BorderLayout.EAST);

    displayPanel.add(display, BorderLayout.CENTER);
    displayPanel.add(bottomPanel, BorderLayout.SOUTH);

    mainPanel.add(displayPanel);

    // ===== BUTTONS PANEL =====
    buttonsPanel = new JPanel(new BorderLayout());
    buttonsPanel.setBackground(new Color(20, 20, 20));
    buttonsPanel.setBounds(0, 160, width, height - 160);
    buttonsPanel.add(createBasicPanel(), BorderLayout.CENTER);

    mainPanel.add(buttonsPanel);

    // ===== SIDE MENU =====
    setupSideMenu();
    sideMenu.setBounds(0, 0, 240, height);
    sideMenu.setVisible(false);

    // ===== HAMBURGER BUTTON =====
    JButton hamburgerBtn = new JButton("☰");
    hamburgerBtn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
    hamburgerBtn.setForeground(currentTheme.foreground);
    hamburgerBtn.setBackground(currentTheme.background);

    hamburgerBtn.setFocusPainted(false);
    hamburgerBtn.setBorderPainted(false);
    hamburgerBtn.setContentAreaFilled(false);
    hamburgerBtn.setOpaque(false);

    hamburgerBtn.setBounds(0, 0, 60, 50);

    hamburgerBtn.addActionListener(e -> {
        isMenuOpen = !isMenuOpen;
        sideMenu.setVisible(isMenuOpen);
        if (isMenuOpen) {
            layeredPane.moveToFront(sideMenu);
        }
    });

    // ===== LAYER ASSEMBLY =====
    layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
    layeredPane.add(sideMenu, JLayeredPane.PALETTE_LAYER);
    layeredPane.add(hamburgerBtn, JLayeredPane.DRAG_LAYER);

    // ===== ENTER HANDLING =====
    display.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "calculate");

    display.getActionMap().put("calculate", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            processEqualCommand();
        }
    });

    display.getInputMap().put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK),
            "insert-break");
    frame.addComponentListener(new ComponentAdapter() {
    @Override
    public void componentResized(ComponentEvent e) {

        Insets insets = frame.getInsets();
        int width = frame.getWidth() - insets.left - insets.right;
        int height = frame.getHeight() - insets.top - insets.bottom;

        layeredPane.setBounds(0, 0, width, height);

        mainPanel.setBounds(0, 0, width, height);

        displayPanel.setBounds(0, 0, width, 160);

        buttonsPanel.setBounds(0, 160, width, height - 160);

        sideMenu.setBounds(0, 0, 240, height);
    }
    });
            
    // Autofocus
    SwingUtilities.invokeLater(() -> display.requestFocusInWindow());
    }
    private static void styleButton(JButton btn) {
    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btn.setBackground(currentTheme.functionButton); // Dark gray to match theme
    btn.setForeground(currentTheme.foreground);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Hover Effect: Lights up when you mouse over it
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(focusColor); // Use your mint/green focus color
            btn.setForeground(Color.BLACK);
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(45, 45, 45));
            btn.setForeground(Color.WHITE);
        }
    });
    }

    static JButton createButton(String text) {

    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 20));
    button.setFocusPainted(false);
    button.setBorderPainted(false);

    // Default text color
    button.setForeground(currentTheme.foreground);

    // NUMBERS
    if (text.matches("[0-9.]")) {
        button.setBackground(currentTheme.regularButton);
    }

    // EQUALS (special background)
    else if (text.equals("=")) {
        button.setBackground(currentTheme.equalsColor);
        button.setForeground(Color.BLACK);
    }

    // C & CE (foreground only changes)
    else if (text.equals("C") || text.equals("CE")) {
        button.setBackground(currentTheme.functionButton);
        button.setForeground(currentTheme.clearColor);
    }

    // MEMORY & DEG (foreground only changes)
    else if (text.equals("M+") || text.equals("M-") ||
             text.equals("MR") || text.equals("MC")){

        button.setBackground(currentTheme.functionButton);
        button.setForeground(currentTheme.memoryDegColor);
    }
    else if (text.equals("DEG")){
        
        button.setBackground(currentTheme.functionButton);
        button.setForeground(currentTheme.memoryDegColor);
        button.addActionListener(e -> {
            isDegreeMode = !isDegreeMode;
            if (modeLabel != null) {
                modeLabel.setText(isDegreeMode ? "DEG " : "RAD ");
                modeLabel.setForeground(isDegreeMode ? new Color(230, 136, 136) : new Color(97, 120, 195));
            }
        });
        return button;
    }

    // ALL OTHER FUNCTION BUTTONS
    else {
        button.setBackground(currentTheme.functionButton);
    }

    button.addActionListener(e -> handleInput(text));

    return button;
    }

    static void handleInput(String text){ 
    try {
        // Strip HTML to get the raw math text
        String cur = display.getText();
        if (cur.equals("0")) cur = "";
        // 2. EQUALS COMMAND
        if (text.equals("=")) {
            isPowerMode = false;
            processEqualCommand();
            return;
        }

        // 3. CLEAR COMMANDS
        if (text.equals("C")) {
            updateDisplay("");
            previewLabel.setText("");
            isPowerMode = false;
            if (canvas != null) {
                canvas.clear();
                colorIndex = 0;
                canvas.setTangentMode(false, null);
            }
            return;
        }
        
        if (text.equals("CE")) {
            if (cur.length() > 0) updateDisplay(cur.substring(0, cur.length() - 1));
            isPowerMode = false;
            if(text.isEmpty()) previewLabel.setText("");
            return;
        }

        // 4. MEMORY & SPECIAL COMMANDS
        if (text.equals("M+")) { memory += evaluateExpression(cur, 0); return; }
        if (text.equals("M-")) { memory -= evaluateExpression(cur, 0); return; }
        if (text.equals("MR")) { updateDisplay("" + memory); return; }
        if (text.equals("MC")) { memory = 0; return; }
        if (text.equals("Ans")) { updateDisplay(cur + lastAnswer); return; }

        // 5. FUNCTION WRAPPERS
        // FUNCTION WRAPPERS
    Set<String> functions = Set.of(
    "sin", "cos", "tan", "asin", "acos", "atan",
    "log", "ln", "cot", "sec", "csc",
    "acot", "asec", "acsc",
    "sgn", "erf", "prime",
    "Γ", "W", "ζ", "Φ", "Π", "Σ", "μ", "σ",
    "sinh", "cosh", "tanh",
    "asinh", "acosh", "atanh",
    "coth", "sech", "csch",
    "lim", "Tang", "Shade",
    "abs", "ceil", "floor", "round",
    "max", "min", "mod", "rand",
    "nCr", "nPr", "stdev", "stdevp", "mean"
    );

    String current = display.getText();
    if (current.equals("0")) current = "";

    String nextString;
    
    if (text.equals("∫")) {
    display.setText(display.getText() + "int(");
    return;
    }
    if (text.equals("d/dx")){
        display.setText(display.getText()+"diff(");
        return;
    }

    if (functions.contains(text)) {
    nextString = current + text + "(";}
    else {
    nextString = current + text;
    }

    // SEND ONCE
    updateDisplay(nextString);

    // PREVIEW
    try {
    double result = evaluateExpression(nextString, 0);
    
    if (!cur.contains("∫") && 
    !cur.contains("d/dx") && 
    !cur.contains("x")) {

    if (!nextString.equals(formatCoeff(result))) {
        previewLabel.setText("= " + formatCoeff(result));
    } else {
        previewLabel.setText("");
    }
    }
    } catch (Exception ex) {
    previewLabel.setText("");
    }
    }
    catch (Exception e) { previewLabel.setText(""); // Hide preview if expression is incomplete } String current = display.getText().replaceAll("<[^>]*>", "").trim(); if (current.equals("0")) current = ""; }
    }
    }
    private static void processEqualCommand() {
    // 1. Clean the input once and only once
    String raw = display.getText().replaceAll("<[^>]*>", "").replaceAll("<sup>", "^").replaceAll("</sup>", "").trim();
    // Strip hidden Swing spaces
    String input = raw.replace("\u00A0", " ").replace(" ", "");

    if (input.isEmpty()) return;

    try {
        // --- A. GRAPHING COMMANDS (SHADE/TANGENT) ---
        if (input.startsWith("shade(")) {
            String math = raw.substring(raw.indexOf("(") + 1, raw.lastIndexOf(")")).trim();
            if (canvas != null && model != null) {
                EquationEntry en = new EquationEntry(math, graphColors[colorIndex++ % graphColors.length]);
                en.isShaded = true;
                canvas.entries.add(en);
                model.addElement(en);
                canvas.repaint();
            }
            display.setText("Check Graph");
            tabbedPane.setSelectedIndex(5);
            return; 
        }

        if (input.startsWith("tang(") || input.startsWith("tangent(")) {
            String math = raw.substring(raw.indexOf("(") + 1, raw.lastIndexOf(")")).trim();
            if (canvas != null) {
                EquationEntry en = new EquationEntry(math, graphColors[colorIndex++ % graphColors.length]);
                canvas.entries.add(en);
                canvas.setTangentMode(true, math);
                canvas.repaint();
            }
            display.setText("Check Graph");
            tabbedPane.setSelectedIndex(5);
            return;
        }

        // --- B. CALCULUS (LIMITS, DERIVATIVES, INTEGRALS) ---
        Pattern limDiff = Pattern.compile("lim\\(([^)]+)\\)diff\\((.+)\\)");
        Matcher mDiff = limDiff.matcher(input);
        
        if (mDiff.find()) {
            double a = evaluateExpression(mDiff.group(1), 0);
            double res = evaluateExpression(derive(mDiff.group(2)), a);
            updateDisplay(formatCoeff(res));
        } 
        else if ((input.contains("lim(") && (input.contains("int(") || input.contains("∫")))) {
            Pattern limInt = Pattern.compile("lim\\(([^,]+),([^)]+)\\)int\\((.+)\\)");
            Matcher mInt = limInt.matcher(input);
            if (mInt.find()) {
                double v1 = evaluateExpression(mInt.group(1), 0);
                double v2 = evaluateExpression(mInt.group(2), 0);
                String symbolic = integrate(mInt.group(3)).replace("+c", "").replace("+C", "");
                double res = evaluateExpression(symbolic, Math.max(v1, v2)) - evaluateExpression(symbolic, Math.min(v1, v2));
                updateDisplay(formatCoeff(res));
            }
        }
        else if (input.startsWith("diff(") || input.startsWith("d/dx")) {
            updateDisplay(derive(input.substring(5, input.lastIndexOf(")"))));
        } 
        else if (input.startsWith("int(") || input.startsWith("∫")) {
            int startIdx = input.startsWith("int(") ? 4 : 1;
            updateDisplay(integrate(input.substring(startIdx, input.lastIndexOf(")"))));
        } 
        
        // --- C. STANDARD ARITHMETIC (THE FIX FOR 3+3) ---
        else {
            double result = evaluateExpression(input, 0.0);
            updateDisplay(formatCoeff(result));
            lastAnswer = result;
        }

        // Final Touch: Clear the live preview after solving
        previewLabel.setText(" ");

    } catch (Exception e) {
        updateDisplay("Error");
    }
    }
    
    

    static double evaluateExpression(String expr, double xVal){
        final String mathReadyExpr = expr;
        class Parser {
            int pos = -1, ch;
            void nextChar() {
            // This while loop skips any spaces so '2 + 2' becomes '2+2' to the parser
            ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            while (ch == ' ') {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }
        }
            boolean eat(int c) { while (ch == ' ') nextChar(); if (ch == c) { nextChar(); return true; } return false; }
            double parse() {
            nextChar();
            double x = parseExpression(); // MUST call parseExpression, not parseTerm!
            if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }
        
        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // If this isn't a loop, it won't see the +2
                else if (eat('-')) x -= parseTerm(); 
                else return x;
            }
        }
            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    if (eat('*') || eat('×')){ v *= parseFactor(); updateLivePreview(v);}
                    else if (eat('/') || eat('÷')){ v /= parseFactor(); updateLivePreview(v);}
                    else if (ch == '(' || Character.isLetter(ch)) {v *= parseFactor(); updateLivePreview(v);}
                    else return v;
                }
                
            }
            double parseFactor() {
                if (eat('+')) return parseFactor(); if (eat('-')) return -parseFactor();
                double v; int start = pos;
                if (eat('(')) { v = parseExpression(); eat(')'); }
                else if ((ch >= '0' && ch <= '9') || ch == '.') { while ((ch >= '0' && ch <= '9') || ch == '.') nextChar(); v = Double.parseDouble(mathReadyExpr.substring(start, pos)); }
                else if (Character.isLetter(ch)) {
                    while (Character.isLetter(ch)) nextChar();
                    String func = mathReadyExpr.substring(start, pos);
                    if (func.equals("x") || func.equals("y")){ v = xVal; updateLivePreview(v); return xVal;}
                    if (func.equals("pi") || func.equals("π")){ updateLivePreview(Math.PI); return 3.14159;}
                    if (func.equals("e")){ updateLivePreview(Math.E); return 2.71828;}
                    if (func.equals("γ") || func.equals("gamma")){ return 0.57721;}
                    if (func.equals("φ") || func.equals("golden")){ return 1.61803;}
                    if (func.equals("λ") || func.equals("lambda")){ return 1.30357;}
                    if (func.equals("ζa") || func.equals("apery")){ return 1.20205;}
                    if (func.equals("K")){ return 2.68525;}
                    if (func.equals("M")){ return 0.26149;}
                    if (func.equals("δ") || func.equals("delta")){ return 4.66920;}
                    if (func.equals("Ω") || func.equals("omega")){ return 0.56714;}
                    if (func.equals("δs") || func.equals("silver")){ return 2.41421;}
                    if (func.equals("ρ") || func.equals("rho")){return 1.32471;}
                    if (func.equals("A")){ return 1.28242;}
                    if (func.equals("D")){ return 0.73908;}
                    if (func.equals("Ca")){ return 0.64341;}
                    if (func.equals("G")){ return 0.91596;}
                    if (func.equals("ω")){return 2.62205;}
                    if (func.equals("E")){return 1.60669;}
                    if (func.equals("τ") || func.equals("tau")){return 6.28318;}
                    if (func.equals("D")){return 0.73908;}
                    if (func.equals("c")){return 300000000;}
                    if (func.equals("Na") || func.equals("avogadro")){return 6.02214*Math.pow(10, 23);}
                    if (func.equals("h") || func.equals("planck")){return 6.62607*Math.pow(10, -34);}
                    double arg = eat('(') ? parseExpression() : 0; if(arg != 0) eat(')');
                    switch (func) {
                        case "sin": v = isDegreeMode ? Math.sin(Math.toRadians(arg)) : Math.sin(arg); updateLivePreview(v); break;
                        case "cos": v = isDegreeMode ? Math.cos(Math.toRadians(arg)) : Math.cos(arg); updateLivePreview(v); break;
                        case "tan": v = isDegreeMode ? Math.tan(Math.toRadians(arg)) : Math.tan(arg); updateLivePreview(v); break;
                        case "asin": v = isDegreeMode ? Math.toDegrees(Math.asin(arg)) : Math.asin(arg); updateLivePreview(v); break;
                        case "acos": v = isDegreeMode ? Math.toDegrees(Math.acos(arg)) : Math.acos(arg); updateLivePreview(v); break;
                        case "atan": v = isDegreeMode ? Math.toDegrees(Math.atan(arg)) : Math.atan(arg); updateLivePreview(v); break;
                        case "arcsin": v = isDegreeMode ? Math.toDegrees(Math.asin(arg)) : Math.asin(arg); updateLivePreview(v); break;
                        case "arccos": v = isDegreeMode ? Math.toDegrees(Math.acos(arg)) : Math.acos(arg); updateLivePreview(v); break;
                        case "arctan": v = isDegreeMode ? Math.toDegrees(Math.atan(arg)) : Math.atan(arg); updateLivePreview(v); break;
                        case "sqrt": v = Math.sqrt(arg); updateLivePreview(v); break;
                        case "log": {
                            if(eat(',')){
                                double value = parseExpression();
                                v = Math.log(value) / Math.log(arg);
                            }
                            else{
                                v = Math.log10(arg);
                            }
                            updateLivePreview(v);
                        } break;
                        case "mod":{
                            if(eat(',')){
                                double value = parseExpression();
                                v = value % arg; updateLivePreview(v);
                            }
                            else{
                                v = arg % 10; updateLivePreview(v);
                            }
                        }
                        break;
                        case "ln": v = Math.log(arg); updateLivePreview(v); break;
                        case "abs": v = Math.abs(arg); updateLivePreview(v); break;
                        case "sinh": v = Math.sinh(arg); updateLivePreview(v); break;
                        case "cosh": v = Math.cosh(arg); updateLivePreview(v); break;
                        case "tanh": v = Math.tanh(arg); updateLivePreview(v); break;
                        case "asinh": v = Math.log(arg + Math.sqrt(arg * arg + 1.0)); updateLivePreview(v); break;
                        case "acosh": v = Math.log(arg + Math.sqrt(arg * arg - 1.0)); updateLivePreview(v); break;
                        case "atanh": v = 0.5 * Math.log((1 + arg) / (1 - arg)); updateLivePreview(v); break;
                        case "arcsinh": v = Math.log(arg + Math.sqrt(arg * arg + 1.0)); break;
                        case "arccosh": v = Math.log(arg + Math.sqrt(arg * arg - 1.0)); break;
                        case "arctanh": v = 0.5 * Math.log((1 + arg) / (1 - arg)); break;
                        case "rand": v = Math.random(); break;
                        case "random": v = Math.random(); break;
                        case "lim": 
                            v = evaluateExpression(mathReadyExpr.substring(start + 4, mathReadyExpr.length() - 1), 0 + 1e-8); 
                        break;
                        case "cot": v = isDegreeMode ? 1.0 / Math.tan(Math.toRadians(arg)) : 1.0 / Math.tan(arg); break;
                        case "sec": v = isDegreeMode ? 1.0 / Math.cos(Math.toRadians(arg)) : 1.0 / Math.cos(arg); break;
                        case "csc": v = isDegreeMode ? 1.0 / Math.sin(Math.toRadians(arg)) : 1.0 / Math.sin(arg); break;
                        case "cosec": v = isDegreeMode ? 1.0 / Math.sin(Math.toRadians(arg)) : 1.0 / Math.sin(arg); break;
                        case "ceil": v = Math.ceil(arg); break;
                        case "ceiling": v = Math.ceil(arg); break;
                        case "floor": v = Math.floor(arg); break;
                        case "asec": v = isDegreeMode ? Math.toDegrees(Math.acos(1.0/arg)) : Math.acos(1.0/arg); break;
                        case "acot": v = isDegreeMode ? Math.toDegrees(Math.atan(1.0/arg)) : Math.atan(1.0/arg); break;
                        case "acsc": v = isDegreeMode ? Math.toDegrees(Math.asin(1.0/arg)) : Math.asin(1.0/arg); break;
                        case "acosec": v = isDegreeMode ? Math.toDegrees(Math.asin(1.0/arg)) : Math.asin(1.0/arg); break;
                        case "arcsec": v = isDegreeMode ? Math.toDegrees(Math.acos(1.0/arg)) : Math.acos(1.0/arg); break;
                        case "arccot": v = isDegreeMode ? Math.toDegrees(Math.atan(1.0/arg)) : Math.atan(1.0/arg); break;
                        case "arccsc": v = isDegreeMode ? Math.toDegrees(Math.asin(1.0/arg)) : Math.asin(1.0/arg); break;
                        case "arccosec": v = isDegreeMode ? Math.toDegrees(Math.asin(1.0/arg)) : Math.asin(1.0/arg); break;
                        case "sech": v = 1.0 / Math.cosh(arg); break;
                        case "coth": v = 1.0 / Math.tanh(arg); break;
                        case "csch": v = 1.0 / Math.sinh(arg); break;
                        case "cosech": v = 1.0 / Math.sinh(arg); break;
                        case "asech": v = Math.log(1 + Math.sqrt(1 - Math.pow(arg,2)) / arg); break;
                        case "acoth": v = 0.5 * Math.log((arg + 1) / (arg - 1)); break;
                        case "acsch": v = Math.log(1.0 / arg + Math.sqrt((1.0 / Math.pow(arg,2)) + 1)); break;
                        case "acosech": v = Math.log(1.0 / arg + Math.sqrt((1.0 / Math.pow(arg,2)) + 1)); break;
                        case "arcsech": v = Math.log(1 + Math.sqrt(1 - Math.pow(arg,2)) / arg); break;
                        case "arccoth": v = 0.5 * Math.log((arg + 1) / (arg - 1)); break;
                        case "arccsch": v = Math.log(1.0 / arg + Math.sqrt((1.0 / Math.pow(arg,2)) + 1)); break;
                        case "arccosech": v = Math.log(1.0 / arg + Math.sqrt((1.0 / Math.pow(arg,2)) + 1)); break;
                        case "stdev":
                            List<Double> sData = new ArrayList<>();
                            sData.add(arg); // Use the 'arg' that was already parsed as the first number
        
                            // While there are more commas, keep parsing and adding to our list
                            while (eat(',')) {
                                sData.add(parseExpression());
                            }
                            // We already 'eat' the closing ')' in your original code after the switch,
                            // but if you have eat(')') inside the cases, keep it consistent.
    
                            if (sData.size() > 1) {
                                double sSum = 0;
                                for (double d : sData) sSum += d;
                                double sMean = sSum / sData.size();
                                double sSqDiff = 0;
                                for (double d : sData) sSqDiff += Math.pow(d - sMean, 2);
        
                                v = Math.sqrt(sSqDiff / (sData.size() - 1));
                            } else {
                            v = 0;
                            }
                        break;

                        case "stdevp":
                            List<Double> pData = new ArrayList<>();
                            pData.add(arg); // Use 'arg' as the first number
    
                            while (eat(',')) {
                                pData.add(parseExpression());
                            }
    
                            if (!pData.isEmpty()) {
                                double pSum = 0;
                                for (double d : pData) pSum += d;
                                double pMean = pSum / pData.size();
                                double pSqDiff = 0;
                                for (double d : pData) pSqDiff += Math.pow(d - pMean, 2);
        
                                v = Math.sqrt(pSqDiff / pData.size());
                            } else {
                                v = 0;
                            }

                        break;  
                        
                        case "max":
                        case "min":
                            double result = arg; // 'arg' is the first number (e.g., the '10' in max(10,20,5))
                            while (eat(',')) {
                                double next = parseExpression(); // Grab the next number after the comma
                                if (func.equals("max")) {
                                    result = Math.max(result, next);
                                } else {
                                    result = Math.min(result, next);
                                }
                            }
                            v = result; // Assign the final winner to v
                        break;
                        
                        case "ncr":
                        case "npr":
                            double n = arg; // 'arg' already grabbed the first number
                            double r = 0;
                            if (eat(',')) {
                                r = parseExpression(); // Grab the second number after the comma
                            }
    
                            if (n >= r && n >= 0 && r >= 0) {
                                if (func.equals("npr")) {
                                    // nPr = n! / (n-r)!
                                    v = factorial(n) / factorial(n - r);
                                } else {
                                    // nCr = n! / (r! * (n-r)!)
                                    v = factorial(n) / (factorial(r) * factorial(n - r));
                                }
                            } else {
                                v = 0; // Or handle as an error/NaN
                            }
                        break;
                        case "sgn":
                            v = Math.signum(arg); break;
                        case "Γ":
                            v = gamma(arg); break;
                        case "ζ": 
                            v = zeta(arg); break;
                        case "erf":
                            v = erf(arg); break;
                        case "W":
                            v = lambertW(arg); break;
                        case "Φ":
                            v = totient((int)arg); break;
                        case "μ":
                            v = mu((int)arg); break;
                        case "σ":
                            v = sigma((int)arg); break;
                            
                            
                        default: v = 0;

                    }
                }
                else v = 0;
                while (eat('!')) { 
                    v = factorial(v); 
                }
                if (eat('^')) v = Math.pow(v, parseFactor());
                if (eat('√')){
                    if(v == 0) v = Math.sqrt(parseFactor());
                    else v = Math.pow(parseFactor(), 1 / v);
                }
                if (eat('%')){
                    if(parseFactor() == 0) v = v / 100.0;
                    else v = v / parseFactor() * 100.0;
                }
                v = Math.round(v * 1000.0) / 1000.0;
                updateLivePreview(v);
                return v;
            }
        }
        return new Parser().parse();
    }
    
    static double factorial(double n) {
    if (n < 0) return 0; // Factorial not defined for negative numbers in this scope
    if (n == 0 || n == 1) return 1;
    double res = 1;
    for (int i = 2; i <= (int)n; i++) {
        res *= i;
    }
    return res;
    }
    
    public static double gamma(double z) {
        if (z < 0.5) return Math.PI / (Math.sin(Math.PI * z) * gamma(1 - z));
        double sum = 0, dt = 0.0005; 
        for (double t = dt; t < 60; t += dt) {
            sum += Math.pow(t, z - 1) * Math.exp(-t) * dt;
        }
        return sum;
    }

    // 3. Riemann Zeta Function: ζ(s)
    public static double zeta(double s) {
        if (s == 1.0) return Double.POSITIVE_INFINITY;
        if (s > 1) {
            double sum = 0;
            for (int n = 1; n <= 10000; n++) {
                sum += 1.0 / Math.pow(n, s);
            }
            return sum;
        } else {
            // Functional equation for s < 1
            return Math.pow(2, s) * Math.pow(Math.PI, s - 1) * Math.sin(Math.PI * s / 2.0) * gamma(1 - s) * zeta(1 - s);
        }
    }

    // 4. Error Function: erf(x)
    public static double erf(double x) {
        double sum = 0, dt = 0.0005;
        for (double t = 0; t < x; t += dt) {
            sum += Math.exp(-t * t) * dt;
        }
        return (2.0 / Math.sqrt(Math.PI)) * sum;
    }

    // 5. Lambert W Function: W(x) using Newton's Method
    public static double lambertW(double x) {
        double w = x > 1 ? Math.log(x) : 0;
        for (int i = 0; i < 30; i++) {
            double ew = Math.exp(w);
            double res = w * ew - x;
            if (Math.abs(res) < 1e-12) break;
            w -= res / (ew * (w + 1) - (w + 2) * res / (2 * w + 2));
        }
        return w;
    }

    // 6. Euler's Totient Function: φ(n)
    public static int totient(int n) {
        int result = n;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) n /= i;
                result -= result / i;
            }
        }
        if (n > 1) result -= result / n;
        return result;
    }

    // 7. Prime Counting Function: π(x)
    public static int primePi(double x) {
        int count = 0;
        for (int i = 2; i <= (int)x; i++) {
            if (isPrime(i)) count++;
        }
        return count;
    }

    // 8. Mobius Mu Function: μ(n)
    public static int mu(int n) {
        if (n == 1) return 1;
        int primeFactors = 0;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                n /= i;
                primeFactors++;
                if (n % i == 0) return 0; // Not square-free
            }
        }
        if (n > 1) primeFactors++;
        return (primeFactors % 2 == 0) ? 1 : -1;
    }

    // 9. Sigma Function: σ(n) (Sum of divisors)
    public static int sigma(int n) {
        int sum = 0;
        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                sum += i;
                if (i * i != n) sum += n / i;
            }
        }
        return sum;
    }

    // 10. Summation (Σ)
    public static double sum(int start, int end, DoubleUnaryOperator f) {
        double total = 0;
        for (int i = start; i <= end; i++) {
            total += f.applyAsDouble(i);
        }
        return total;
    }

    // 10b. Product: Π f(i) from start to end
    public static double prod(int start, int end, DoubleUnaryOperator f) {
        double result = 1.0;
        for (int i = start; i <= end; i++) {
            result *= f.applyAsDouble(i);
        }
        return result;
    }

    // 11. Arithmetic Mean
    public static double arithmeticMean(double[] values) {
        if (values.length == 0) return 0;
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.length;
    }

    // Helper: Prime check for π(x)
    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    static String formatPower(double base, double exponent) {
    // 1. Convert doubles to strings (removing .0 if it's an integer)
    String bStr = (base == (long) base) ? String.format("%d", (long) base) : String.valueOf(base);
    String eStr = (exponent == (long) exponent) ? String.format("%d", (long) exponent) : String.valueOf(exponent);

    StringBuilder sb = new StringBuilder(bStr);

    // 2. Map each character of the exponent string to its superscript version
    for (char c : eStr.toCharArray()) {
        switch (c) {
            case '0': sb.append('\u2070'); break;
            case '1': sb.append('\u00B9'); break;
            case '2': sb.append('\u00B2'); break;
            case '3': sb.append('\u00B3'); break;
            case '4': sb.append('\u2074'); break;
            case '5': sb.append('\u2075'); break;
            case '6': sb.append('\u2076'); break;
            case '7': sb.append('\u2077'); break;
            case '8': sb.append('\u2078'); break;
            case '9': sb.append('\u2079'); break;
            case '.': sb.append('\u22C5'); break; // Using a dot operator for decimal exponents
            case '-': sb.append('\u207B'); break; // Superscript minus for negative powers
            default: sb.append(c);
        }
    }
    return sb.toString();
    }

    public static String derive(String exp) {
        exp = exp.replaceAll("\\s+", "").toLowerCase(); // Clean input

    // 1. Handle Sum/Difference: (f + g)' = f' + g'
    if (exp.contains("+") || (exp.contains("-") && !exp.startsWith("-"))) {
        // Split by + or - but keep the operator using lookahead
        String[] terms = exp.split("(?=[+-])");
        StringBuilder sb = new StringBuilder();
        for (String term : terms) {
            String d = derive(term);
            if (!d.equals("0")) {
                if (sb.length() > 0 && !d.startsWith("-")) sb.append(" + ");
                sb.append(d);
            }
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    // 2. Handle Chain Rule for Trig: sin(ax), cos(ax), etc.
    // Pattern matches: optional coefficient, trig function, inner coefficient, and x
    Pattern trigPattern = Pattern.compile("([\\d.]*)(sin|cos|tan)\\(([\\d.]*)x\\)");
    Matcher trigMatcher = trigPattern.matcher(exp);
    if (trigMatcher.matches()) {
        double outC = trigMatcher.group(1).isEmpty() ? 1 : Double.parseDouble(trigMatcher.group(1));
        String func = trigMatcher.group(2);
        double inC = trigMatcher.group(3).isEmpty() ? 1 : Double.parseDouble(trigMatcher.group(3));
        
        double newC = outC * inC; // The Chain Rule step: multiply outer by inner
        
        if (func.equals("sin")) return formatCoeff(newC) + "cos(" + formatCoeff(inC) + "x)";
        if (func.equals("cos")) return formatCoeff(-newC) + "sin(" + formatCoeff(inC) + "x)";
        if (func.equals("tan")) return formatCoeff(newC) + "sec^2(" + formatCoeff(inC) + "x)";
    }

    // 3. Handle Power Rule: ax^n
    Pattern powerPattern = Pattern.compile("([\\d.-]*)x\\^?([\\d.-]*)");
    Matcher powerMatcher = powerPattern.matcher(exp);
    if (powerMatcher.matches()) {
        String aStr = powerMatcher.group(1);
        double a = (aStr.isEmpty() || aStr.equals("+")) ? 1 : (aStr.equals("-") ? -1 : Double.parseDouble(aStr));
        
        String nStr = powerMatcher.group(2);
        if (nStr.isEmpty()) return formatCoeff(a); // d/dx of ax is a

        double n = Double.parseDouble(nStr);
        double newA = a * n;
        double newN = n - 1;

        if (newN == 0) return formatCoeff(newA);
        if (newN == 1) return formatCoeff(newA) + "x";
        return formatCoeff(newA) + "x^" + formatCoeff(newN);
    }

    return "0";
    }

    private static String formatCoeff(double d) {
    if (d == (long) d) return String.format("%d", (long) d);
    return String.format("%s", d);
    }  

    public static String integrate(String exp) {
    exp = exp.replaceAll("\\s+", "").toLowerCase();

    // 1. Handle Sum/Difference: ∫(f + g)dx = ∫f dx + ∫g dx
    if (exp.contains("+") || (exp.contains("-") && !exp.startsWith("-"))) {
        String[] terms = exp.split("(?=[+-])");
        StringBuilder sb = new StringBuilder();
        for (String term : terms) {
            String result = integrate(term);
            // Remove the "+ C" from individual terms to add it once at the end
            result = result.replace(" + C", ""); 
            if (!result.equals("0")) {
                if (sb.length() > 0 && !result.startsWith("-")) sb.append(" + ");
                sb.append(result);
            }
        }
        return sb.length() == 0 ? "C" : sb.toString() + " + C";
    }

    // 2. Handle Trig Integrals: ∫sin(ax)dx = -1/a cos(ax)
    Pattern trigPattern = Pattern.compile("([\\d.]*)(sin|cos|sec\\^2)\\(([\\d.]*)x\\)");
    Matcher trigMatcher = trigPattern.matcher(exp);
    if (trigMatcher.matches()) {
        double outC = trigMatcher.group(1).isEmpty() ? 1 : Double.parseDouble(trigMatcher.group(1));
        String func = trigMatcher.group(2);
        double inC = trigMatcher.group(3).isEmpty() ? 1 : Double.parseDouble(trigMatcher.group(3));
        
        double newC = outC / inC; // Integration step: divide by inner coefficient

        if (func.equals("sin")) return formatCoeff(-newC) + "cos(" + formatCoeff(inC) + "x) + C";
        if (func.equals("cos")) return formatCoeff(newC) + "sin(" + formatCoeff(inC) + "x) + C";
        if (func.equals("sec^2")) return formatCoeff(newC) + "tan(" + formatCoeff(inC) + "x) + C";
    }

    // 3. Handle Power Rule: ∫ax^n dx = (a/n+1)x^(n+1)
    Pattern powerPattern = Pattern.compile("([\\d.-]*)x\\^?([\\d.-]*)");
    Matcher powerMatcher = powerPattern.matcher(exp);
    if (powerMatcher.matches()) {
        String aStr = powerMatcher.group(1);
        double a = (aStr.isEmpty() || aStr.equals("+")) ? 1 : (aStr.equals("-") ? -1 : Double.parseDouble(aStr));
        
        String nStr = powerMatcher.group(2);
        double n = nStr.isEmpty() ? 1 : Double.parseDouble(nStr);
        
        double newA = a / (n + 1);
        double newN = n + 1;

        if (newN == 1) return formatCoeff(newA) + "x + C";
        return formatCoeff(newA) + "x^" + formatCoeff(newN) + " + C";
    }

    // 4. Handle Constants: ∫a dx = ax
    if (exp.matches("[\\d.-]+")) {
        return exp + "x + C";
    }

    return "∫" + exp + " dx"; // Fallback if rule not found
    }

    static double derivative(String e, double x) { double h = 1e-6; return (evaluateExpression(e, x+h)-evaluateExpression(e, x))/h; }
    static double secondDerivative(String e, double x) { double h = 1e-4; return (evaluateExpression(e, x+h)-2*evaluateExpression(e, x)+evaluateExpression(e, x-h))/(h*h); }
    static double numericIntegrate(String e, double a, double b) { double s=0, n=1000, h=(b-a)/n; for(int i=0;i<n;i++) s+=evaluateExpression(e, a+i*h); return s*h; }
    static double limit(String e, double t) { return evaluateExpression(e, t+1e-8); }

    static JPanel createGraphPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(new Color(25, 25, 25));
    canvas = new GraphCanvas();
    
    main.add(canvas, BorderLayout.CENTER);

    // ... (Keep your custom renderer and mouse listener for the list exactly as they are) ...
    list.setCellRenderer(new ListCellRenderer<EquationEntry>() {
    @Override
    public Component getListCellRendererComponent(JList<? extends EquationEntry> list, EquationEntry entry, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel card = new JPanel(new BorderLayout(8, 0));
        card.setBackground(new Color(40, 40, 40)); // The grey box color
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)), // Bottom divider
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 1. Color Box (Left)
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBackground(entry.color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // 2. Equation Text (Center) - Syncing the editor text
        entry.editor.setText(entry.text);
        
        // 3. Delete Mark (Right)
        JLabel deleteLabel = new JLabel("C");
        deleteLabel.setForeground(new Color(180, 50, 50));
        deleteLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(colorBox, BorderLayout.WEST);
        card.add(entry.editor, BorderLayout.CENTER);
        card.add(deleteLabel, BorderLayout.EAST);

        if (isSelected) card.setBackground(new Color(55, 55, 55));
        return card;
    }
    });
    
    list.addMouseListener(new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        int index = list.locationToIndex(e.getPoint());
        if (index == -1) return;
        
        EquationEntry entry = model.getElementAt(index);
        Rectangle bounds = list.getCellBounds(index, index);
        int localX = e.getX() - bounds.x;

        // CLICKED COLOR BOX (Left side)
        if (localX < 30) {
            Color newC = JColorChooser.showDialog(null, "Change Color", entry.color);
            if (newC != null) {
                entry.color = newC;
                canvas.repaint();
                list.repaint();
            }
        }
        // CLICKED DELETE 'X' (Right side)
        else if (localX > bounds.width - 30) {
            model.remove(index);
            canvas.entries.remove(entry);
            canvas.repaint();
        }
        // CLICKED TEXT (Center)
        else{
            String newText = JOptionPane.showInputDialog("Edit Equation:", entry.text);
            if (newText != null && !newText.isEmpty()) {
                entry.text = newText;
                canvas.repaint();
                list.repaint();
            }
        }
    }
    });

    JScrollPane scroll = new JScrollPane(list);
    scroll.setPreferredSize(new Dimension(200, 0)); // Slightly wider for the 'X' button
    scroll.setVisible(false);
    scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(50, 50, 50)));

    // --- TOP CONTROLS ---
    JPanel top = new JPanel(new BorderLayout(5, 0));
    top.setBackground(new Color(25, 25, 25));
    top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JTextField input = new JTextField("");
    input.setBackground(new Color(45, 45, 45));
    input.setForeground(Color.WHITE);
    input.setCaretColor(Color.WHITE);
    input.setFont(new Font("Segoe UI", Font.PLAIN, 16));

    JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    rightGroup.setOpaque(false);
    RoundedButton drawBtn = new RoundedButton("Draw");
    drawBtn.setBackground(new Color(0, 120, 215));
    RoundedButton histBtn = new RoundedButton("EQ");
    histBtn.setForeground(Color.BLACK);
    rightGroup.add(drawBtn); 
    rightGroup.add(histBtn);
    
    drawBtn.addActionListener(e -> {
    String expr = input.getText().trim();
    if (!expr.isEmpty() && canvas != null) {
        
        Color nextColor = graphColors[colorIndex % graphColors.length];

        EquationEntry newEntry = new EquationEntry(expr, nextColor);
        canvas.entries.add(newEntry); // For the graph lines
        model.addElement(newEntry);   // For the history list
        colorIndex++;
        canvas.repaint();
    }
    });
    // 1. Create a shared action that both the button and the Enter key can use
    Runnable drawAction = () -> {
    String expr = input.getText().trim();
    if (!expr.isEmpty() && canvas != null) {
        // Pick color and create entry
        Color nextColor = graphColors[colorIndex % graphColors.length];
        EquationEntry newEntry = new EquationEntry(expr, nextColor);
        
        canvas.entries.add(newEntry);
        model.addElement(newEntry);
        
        colorIndex++;
        canvas.repaint();

        // THIS IS THE FIX: Clear the box after drawing
        input.setText(""); 
    }
    };

    drawBtn.addActionListener(e -> drawAction.run());

    input.addActionListener(e -> drawAction.run());
    
    top.add(input, BorderLayout.CENTER);
    top.add(rightGroup, BorderLayout.EAST);

    // --- BOTTOM CONTROLS (The Layout Fix) ---
    JPanel bottomContainer = new JPanel(new BorderLayout());
    bottomContainer.setBackground(new Color(25, 25, 25));
    bottomContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

    // Zoom on the left
    JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    zoomPanel.setOpaque(false);
    JLabel zoomLabel = new JLabel("Zoom:");
    zoomLabel.setForeground(Color.GRAY);
    JSlider zoomSlider = new JSlider(10, 200, 40);
    zoomSlider.setBackground(new Color(25, 25, 25));
    zoomSlider.setPreferredSize(new Dimension(150, 30));
    zoomPanel.add(zoomLabel);
    zoomPanel.add(zoomSlider);

    // Buttons on the right
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    btnPanel.setOpaque(false);
    RoundedButton clearBtn = new RoundedButton("Clear All");
    clearBtn.setBackground(new Color(180, 50, 50));
    RoundedButton saveBtn = new RoundedButton("Save Image");
    saveBtn.setForeground(Color.BLACK);
    btnPanel.add(clearBtn);
    btnPanel.add(saveBtn);

    bottomContainer.add(zoomPanel, BorderLayout.WEST);
    bottomContainer.add(btnPanel, BorderLayout.EAST);

    // ASSEMBLY
    main.add(top, BorderLayout.NORTH);
    main.add(canvas, BorderLayout.CENTER);
    main.add(scroll, BorderLayout.EAST);
    main.add(bottomContainer, BorderLayout.SOUTH);
    
    // Inside createGraphPanel()
    list.setBackground(new Color(25, 25, 25)); // Set the list background
    list.setOpaque(true);

    scroll.setPreferredSize(new Dimension(200, 0));
    scroll.getViewport().setBackground(new Color(25, 25, 25)); // THIS fixes the white background
    scroll.setVisible(false);
    scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(50, 50, 50)));

    // --- LISTENERS ---
    zoomSlider.addChangeListener(e -> { canvas.scale = zoomSlider.getValue(); canvas.repaint(); });
    histBtn.addActionListener(e -> { scroll.setVisible(!scroll.isVisible()); main.revalidate(); });
    clearBtn.addActionListener(e -> { canvas.clear(); model.clear(); });
    saveBtn.addActionListener(e -> canvas.saveToImage());

    return main;
    }
    
    static class EquationEntry {
    String text;
    Color color;
    JTextField editor;
    boolean isShaded = false;
    
    public EquationEntry(String text, Color color) {
        this.text = text;
        this.color = color;
        this.editor = new JTextField(text);
        this.editor.setBackground(new Color(45, 45, 45));
        this.editor.setForeground(Color.WHITE);
        this.editor.setCaretColor(Color.WHITE);
        this.editor.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    }
    }

    static class GraphCanvas extends JPanel {
        List<EquationEntry> entries = new ArrayList<>();
        double scale = 40.0; 
        Point mousePos = null;
        private Point dragStart = null; 
        private int offsetX = 0;        
        private int offsetY = 0;    
        private double zoom = 40.0;
        private Color activePointColor = Color.WHITE;
        private boolean isOverGraph = false;
        private double mouseWorldX = Double.NaN;
        private String activeEquation = "";
        private String currentEquation = "";
        private boolean isTangentMode = false;
        private String tangentExpression = null;
    
    public GraphCanvas() { 
        setBackground(new Color(30, 30, 30)); 
        addMouseMotionListener(new MouseMotionAdapter() { 
            public void mouseMoved(MouseEvent e) { 
                mousePos = e.getPoint(); 
                checkIfNearGraph(); // Custom logic to check proximity
                repaint(); 
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            if (isTangentMode) {
                // Convert pixel X to graph "World" X
                mouseWorldX = (e.getX() - getWidth() / 2.0 - offsetX) / zoom;
                repaint();
            }
        }
        });
        addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            dragStart = e.getPoint();
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            dragStart = null;
        }
        });

        addMouseMotionListener(new MouseMotionAdapter() { 
        @Override
        public void mouseDragged(MouseEvent e) {
            if (dragStart != null) {
                // Calculate how much the mouse moved
                offsetX += (e.getX() - dragStart.x);
                offsetY += (e.getY() - dragStart.y);
                
                // Update the start point for the next drag frame
                dragStart = e.getPoint();
                repaint(); 
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) { 
            mousePos = e.getPoint(); 
            checkIfNearGraph(); 
            repaint(); 
        }
        });
    }
    
    public void setEquations(String eq) {
        this.currentEquation = eq;
        this.repaint(); // This tells Java to call paintComponent() immediately
    }

    private void checkIfNearGraph() {
    if (mousePos == null) return;
    isOverGraph = false;

    // --- MUST INCLUDE OFFSETS TO MATCH THE VISUAL DRAG ---
    int cx = (getWidth() / 2) + offsetX;
    int cy = (getHeight() / 2) + offsetY;

    for (EquationEntry en : entries) {
        // 1. Convert mouse pixel X to math X using the offset center
        double xM = (double)(mousePos.x - cx) / scale;
        
        try {
            // 2. Calculate what the Y should be at that X
            double val = evaluateExpression(en.text, xM);
            
            // 3. Convert that math Y back to a screen pixel Y using the offset center
            int yP = cy - (int) (val * scale);
            
            // 4. If the mouse is within 10 pixels of that line...
            if (Math.abs(mousePos.y - yP) < 10) { 
                isOverGraph = true;
                activePointColor = en.color;
                
                // OPTIONAL: Snap the dot to the line visually
                mousePos.y = yP; 
                
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                return; 
            }
        } catch (Exception ex) {}
    }
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void addEntry(EquationEntry e) { 
            entries.add(e); 
            repaint(); 
    }

    public void clear() { 
    entries.clear();          // Wipes the blue lines
    isTangentMode = false;    // Turns off the tangent switch
    tangentExpression = null; // Clears the math memory
    repaint();                // Refreshes the screen
    }

        public void saveToImage() { 
            try { 
                // This captures the current state of the JPanel, including grids and lines
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB); 
                Graphics2D g2 = img.createGraphics();
                this.paint(g2); // Use .paint() to include all sub-components
                g2.dispose();
                ImageIO.write(img, "png", new File("graph_export.png")); 
                JOptionPane.showMessageDialog(this, "Graph saved as graph_export.png");
            } catch(Exception e) {
                System.out.println("Save failed: " + e.getMessage());
            }
        }

    @Override 
    protected void paintComponent(Graphics g) {
    super.paintComponent(g); 
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    int cx = (getWidth() / 2) + offsetX;
    int cy = (getHeight() / 2) + offsetY;

    // 1. GRID
    g2.setColor(new Color(50, 50, 50));
    g2.setStroke(new BasicStroke(1.0f));
    for (int x = cx % (int)scale; x < getWidth(); x += (int)scale) {
        g2.drawLine(x, 0, x, getHeight());
        double val = (double)(x - cx) / scale;
        if (Math.abs(val) > 0.1) g2.drawString(String.format("%.0f", val), x + 2, cy + 12);
    }
    for (int y = cy % (int)scale; y < getHeight(); y += (int)scale) {
        g2.drawLine(0, y, getWidth(), y);
        double val = (double)(cy - y) / scale;
        if (Math.abs(val) > 0.1) g2.drawString(String.format("%.0f", val), cx + 5, y - 2);
    }

    // 2. AXES
    g2.setColor(new Color(150, 150, 150)); 
    g2.setStroke(new BasicStroke(2.5f));
    g2.drawLine(0, cy, getWidth(), cy);    // Horizontal X-Axis
    g2.drawLine(cx, 0, cx, getHeight());   // Vertical Y-Axis

    // 3. SHADING (Draw this BEFORE the lines so the lines stay on top)
    for (EquationEntry en : entries) {
        if (en.isShaded) {
            g2.setColor(new Color(en.color.getRed(), en.color.getGreen(), en.color.getBlue(), 50));
            for (int xP = 0; xP < getWidth(); xP++) {
                double xM = (double)(xP - cx) / scale;
                try {
                    double val = evaluateExpression(en.text, xM);
                    int yP = cy - (int)(val * scale);
                    g2.drawLine(xP, cy, xP, yP); // Shade from axis to curve
                } catch (Exception ex) {}
            }
        }
    }

    // 4. EQUATION LINES
    // --- 4. EQUATION LINES ---
    g2.setStroke(new BasicStroke(2.5f));
    for (EquationEntry en : entries) {
    g2.setColor(en.color);
    String formula = en.text.replaceAll("\\s+", "").toLowerCase(); // Remove spaces

    // Check for xy = Constant (e.g., xy=5)
    if (formula.contains("=")) {
        try {
            // FIX: Use formula.substring() instead of just substring()
            String constantPart = formula.substring(3); 
            double cVal = Double.parseDouble(constantPart);
            drawSmoothHyperbola(g2, cVal, cx, cy);
        } catch (Exception e) {
            // If it's not a simple number, fallback to standard drawing
        }
    } else {
        // Existing logic for standard functions
        int prevX = -1, prevY = -1;
        for (int xP = 0; xP < getWidth(); xP++) {
            double xM = (double)(xP - cx) / scale;
            try {
                double val = CopyOfArithmos.evaluateExpression(en.text, xM);
                int yP = cy - (int)(val * scale);
                if (prevX != -1 && Math.abs(yP) < 5000 && Math.abs(prevY) < 5000) {
                    g2.drawLine(prevX, prevY, xP, yP);
                }
                prevX = xP; prevY = yP;
            } catch (Exception ex) { prevX = -1; }
        }
    }
    }

    // 5. TANGENT LINE (Draw on top of everything else)
    if (isTangentMode && tangentExpression != null) {
        g2.setColor(Color.YELLOW); 
        g2.setStroke(new BasicStroke(2.0f));
        drawTangentLine(g2, tangentExpression);
    }

    // 6. MOUSE INTERACTION / COORDINATES
    if (isOverGraph && mousePos != null) {
        // Active point dot
        g2.setColor(activePointColor);
        g2.fillOval(mousePos.x - 5, mousePos.y - 5, 10, 10);
        
        // Math Coords
        double xMath = (double)(mousePos.x - cx) / scale;
        double yMath = (double)(cy - mousePos.y) / scale;
        
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(mousePos.x + 10, mousePos.y - 25, 110, 20); 
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.drawString(String.format("(%.2f, %.2f)", xMath, yMath), mousePos.x + 12, mousePos.y - 10);

        // Tangent Text Info
        if (isTangentMode && tangentExpression != null) {
            try {
                double x0 = (double)(mousePos.x - cx) / scale;
                double y0 = evaluateExpression(tangentExpression, x0);
                double h = 0.0001;
                double slope = (evaluateExpression(tangentExpression, x0 + h) - y0) / h;
                double intercept = y0 - (slope * x0);

                String tanEq = String.format("Tangent: y = %.2fx + %.2f", slope, intercept);
                g2.setColor(Color.YELLOW);
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                g2.drawString(tanEq, mousePos.x + 12, mousePos.y + 15);
            } catch (Exception e) {}
        }
        for (EquationEntry entry : entries) {
        if (entry.isShaded) {
            try {
                // Calculate Area (Integral) from 0 to current mouse X
                double area = 0;
                double step = 0.01;
                for (double i = 0; i < Math.abs(xMath); i += step) {
                    area += Math.abs(CopyOfArithmos.evaluateExpression(entry.text, i)) * step;
                }

                // Display the Integral Tooltip
                g2.setColor(Color.CYAN); // Or use entry.color
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                String integralText = String.format("Area (0 to x): %.2f", area);
                g2.drawString(integralText, mousePos.x + 12, mousePos.y + 30);
            } catch (Exception ex) {}
        }
        }
    }
    }
    public void setTangentMode(boolean active, String eq) {
    this.isTangentMode = active;
    this.tangentExpression = eq;
    repaint();
    }
    private void drawTangentLine(Graphics2D g2, String expr) {
    // If mouse is off-screen, default to 0, otherwise use mouse position
    double x0 = Double.isNaN(mouseWorldX) ? 0 : mouseWorldX; 
    
    double y0 = CopyOfArithmos.evaluateExpression(expr, x0);
    double h = 0.0001;
    double slope = (CopyOfArithmos.evaluateExpression(expr, x0 + h) - y0) / h;

    // Extend the line far enough to cover the screen
    double xStart = x0 - 20; 
    double xEnd = x0 + 20;
    double yStart = slope * (xStart - x0) + y0;
    double yEnd = slope * (xEnd - x0) + y0;

    g2.draw(new Line2D.Double(toPixelX(xStart), toPixelY(yStart), 
                               toPixelX(xEnd), toPixelY(yEnd)));
    }
    private int toPixelX(double x) {
    // Center of screen + (math coordinate * zoom level) + horizontal drag offset
    return (int) (getWidth() / 2.0 + (x * zoom) + offsetX);
    }

    private int toPixelY(double y) {
    // Center of screen - (math coordinate * zoom level) + vertical drag offset
    // (We subtract because in Java, Y-pixels increase as you go DOWN)
    return (int) (getHeight() / 2.0 - (y * zoom) + offsetY);
    }
    private void drawSmoothHyperbola(Graphics2D g2, double c, int cx, int cy) {
    int prevX = -1, prevY = -1;

    for (int xP = 0; xP < getWidth(); xP++) {
        double xM = (double)(xP - cx) / scale;
        
        // Avoid division by zero at the asymptote
        if (Math.abs(xM) < 0.01) {
            prevX = -1; 
            continue;
        }

        double yM = c / xM; // The isolated equation: y = c/x
        int yP = cy - (int)(yM * scale);

        // Only draw if within reasonable screen bounds
        if (prevX != -1 && Math.abs(yP) < 5000 && Math.abs(prevY) < 5000) {
            g2.drawLine(prevX, prevY, xP, yP);
        }
        
        prevX = xP; 
        prevY = yP;
    }
    }
    private void drawImplicit2D(Graphics2D g2, String expr, int cx, int cy) {
    String[] parts = expr.split("=");
    if (parts.length < 2) return;

    // A step of 0.1 provides high density without lagging the UI
    double step = 0.1; 
    
    for (double xM = -15; xM <= 15; xM += step) {
        for (double yM = -15; yM <= 15; yM += step) {
            try {
                // We manually inject both x and y into the strings before parsing
                String leftSide = parts[0].replace("x", "(" + xM + ")").replace("y", "(" + yM + ")");
                String rightSide = parts[1].replace("x", "(" + xM + ")").replace("y", "(" + yM + ")");
                
                double leftVal = CopyOfArithmos.evaluateExpression(leftSide, 0);
                double rightVal = CopyOfArithmos.evaluateExpression(rightSide, 0);
                
                // If the difference is tiny, we found a point on the curve
                if (Math.abs(leftVal - rightVal) < 0.15) {
                    int px = cx + (int)(xM * scale);
                    int py = cy - (int)(yM * scale);
                    g2.fillRect(px, py, 2, 2);
                }
            } catch (Exception e) { /* Skip invalid points */ }
        }
    }
    }

    }
    
    static JTextField createDimInput(String def) {
        JTextField tf = new JTextField(def, 2);
        tf.setBackground(new Color(18, 18, 18)); // Matches the deep dark background
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tf.setBorder(null); // Removes the box so it sits cleanly inside your [ ] labels
        return tf;
    }

    // 2. THE ROUNDED BUTTON CLASS - Ensure this is also present
    static class RoundedButton extends JButton {
        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // This makes the button use the RED or BLUE color you set in AlphaPanel
        g2.setColor(getBackground()); 
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        super.paintComponent(g); // This paints the text on top
        g2.dispose();
        }
    
    }
    

    static JPanel createMatrixPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(new Color(25, 25, 25)); // Dark theme

    // --- TOP: Dimension Controls ---
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    topPanel.setOpaque(false);
    
    JLabel lblA = new JLabel("Matrix A:"); lblA.setForeground(Color.WHITE);
    JTextField rAField = new JTextField("2", 2); 
    JTextField cAField = new JTextField("2", 2);
    
    JLabel lblB = new JLabel("Matrix B:"); lblB.setForeground(Color.WHITE);
    JTextField rBField = new JTextField("2", 2); 
    JTextField cBField = new JTextField("2", 2);
    
    RoundedButton setBtn = new RoundedButton("Set Grid");
    setBtn.setBackground(new Color(0, 120, 215)); 

    topPanel.add(lblA); topPanel.add(rAField); topPanel.add(new JLabel("x")); topPanel.add(cAField);
    topPanel.add(new JLabel("    ")); 
    topPanel.add(lblB); topPanel.add(rBField); topPanel.add(new JLabel("x")); topPanel.add(cBField);
    topPanel.add(setBtn);

    // --- CENTER: The Matrix Grids ---
    // This container holds both A and B side-by-side
    JPanel gridContainer = new JPanel(new GridLayout(1, 2, 40, 0));
    gridContainer.setOpaque(false);
    gridContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    JPanel panelA = new JPanel(); 
    JPanel panelB = new JPanel();
    panelA.setOpaque(false); 
    panelB.setOpaque(false);
    
    gridContainer.add(panelA); 
    gridContainer.add(panelB);

    // --- BUTTONS SETUP ---
    RoundedButton addBtn = new RoundedButton("A + B");
    RoundedButton subBtn = new RoundedButton("A - B");
    RoundedButton mulBtn = new RoundedButton("A × B");
    RoundedButton clearBtn = new RoundedButton("Clear");

    addBtn.setBackground(new Color(255, 140, 0));
    subBtn.setBackground(new Color(255, 140, 0));
    mulBtn.setBackground(new Color(255, 140, 0));
    clearBtn.setBackground(new Color(180, 50, 50));

    // --- LOGIC: Building the Grids ---
    setBtn.addActionListener(e -> {
        try {
            rowsA = Integer.parseInt(rAField.getText());
            colsA = Integer.parseInt(cAField.getText());
            rowsB = Integer.parseInt(rBField.getText());
            colsB = Integer.parseInt(cBField.getText());

            // Build Matrix A
            panelA.removeAll();
            panelA.setLayout(new GridLayout(rowsA, colsA, 5, 5));
            matrixAFields.clear();
            for (int i = 0; i < rowsA * colsA; i++) {
                JTextField f = new JTextField("0");
                f.setHorizontalAlignment(JTextField.CENTER);
                f.setBackground(new Color(45, 45, 45));
                f.setForeground(Color.WHITE);
                f.setCaretColor(Color.WHITE);
                f.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
                matrixAFields.add(f);
                panelA.add(f);
            }

            // Build Matrix B
            panelB.removeAll();
            panelB.setLayout(new GridLayout(rowsB, colsB, 5, 5));
            matrixBFields.clear();
            for (int i = 0; i < rowsB * colsB; i++) {
                JTextField f = new JTextField("0");
                f.setHorizontalAlignment(JTextField.CENTER);
                f.setBackground(new Color(45, 45, 45));
                f.setForeground(Color.WHITE);
                f.setCaretColor(Color.WHITE);
                f.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
                matrixBFields.add(f);
                panelB.add(f);
            }

            // Refresh UI
            gridContainer.revalidate();
            gridContainer.repaint();
        } catch (Exception ex) { display.setText("Error: Enter valid numbers"); }
    });

    addBtn.addActionListener(e -> {
    if (rowsA != rowsB || colsA != colsB) {
        display.setText("Error: Sizes must match!");
        return;
    }
    try {
        StringBuilder sb = new StringBuilder("A+B:\n"); // \n for new line
        for (int i = 0; i < rowsA; i++) {
            sb.append("[ ");
            for (int j = 0; j < colsA; j++) {
                int index = i * colsA + j;
                double vA = Double.parseDouble(matrixAFields.get(index).getText());
                double vB = Double.parseDouble(matrixBFields.get(index).getText());
                sb.append(String.format("%.1f", vA + vB));
                if (j < colsA - 1) sb.append(",  ");
            }
            sb.append(" ]\n"); // End of row
        }
        display.setText(sb.toString());
    } catch (Exception ex) { display.setText("Error: Check inputs"); }
    });

    subBtn.addActionListener(e -> {
    if (rowsA != rowsB || colsA != colsB) {
        display.setText("Error: Sizes must match!");
        return;
    }
    try {
        StringBuilder sb = new StringBuilder("A-B:\n");
        for (int i = 0; i < rowsA; i++) {
            sb.append("[ ");
            for (int j = 0; j < colsA; j++) {
                int index = i * colsA + j;
                double vA = Double.parseDouble(matrixAFields.get(index).getText());
                double vB = Double.parseDouble(matrixBFields.get(index).getText());
                sb.append(String.format("%.1f", vA - vB));
                if (j < colsA - 1) sb.append(",  ");
            }
            sb.append(" ]\n");
        }
        display.setText(sb.toString());
    } catch (Exception ex) { display.setText("Error: Check inputs"); }
    });
    mulBtn.addActionListener(e -> {
    if (colsA != rowsB) {
        display.setText("Error: A cols must match B rows!");
        return;
    }
    try {
        double[][] result = new double[rowsA][colsB];
        // ... (Keep your 3-nested loops for calculation) ...

        StringBuilder sb = new StringBuilder("Result A × B:\n");
        for (int i = 0; i < rowsA; i++) {
            sb.append("[ ");
            for (int j = 0; j < colsB; j++) {
                // Use String.format to keep the numbers aligned
                sb.append(String.format("%6.1f", result[i][j])); 
                if (j < colsB - 1) sb.append(", ");
            }
            sb.append(" ]\n"); // \n creates the new line
        }

        display.setText(sb.toString());
    } catch (Exception ex) { 
        display.setText("Error in Calculation"); 
    }
    });

    clearBtn.addActionListener(e -> {
    // 1. Clear the result display
    display.setText(""); 

    // 2. Reset the dimension text fields back to "2"
    rAField.setText("2");
    cAField.setText("2");
    rBField.setText("2");
    cBField.setText("2");

    // 3. Trigger the 'Set Grid' logic to physically rebuild the 2x2 UI
    setBtn.doClick(); 

    // 4. (Optional) If you want the new 2x2 boxes to be empty 
    // instead of showing "0", clear the lists:
    for(JTextField f : matrixAFields) f.setText("");
    for(JTextField f : matrixBFields) f.setText("");
    
    main.revalidate();
    main.repaint();
    }); 

    // --- ASSEMBLE ---
    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
    bottom.setOpaque(false);
    bottom.add(addBtn); bottom.add(subBtn); bottom.add(mulBtn); bottom.add(clearBtn);

    main.add(topPanel, BorderLayout.NORTH);
    main.add(gridContainer, BorderLayout.CENTER);
    main.add(bottom, BorderLayout.SOUTH);

    // Force initial setup
    SwingUtilities.invokeLater(() -> setBtn.doClick());

    return main;
    }
    
    private void clearMatrixGrid(JPanel panel) {
    for (Component c : panel.getComponents()) {
        if (c instanceof JTextField) {
            ((JTextField) c).setText("");
        }
    }
    }

    static JPanel createBasicPanel() {
        JPanel p = new JPanel(new GridLayout(6,4,10,10)); p.setBackground(new Color(30,30,30)); 
        String[] b = {"M+","M-","MR","MC","7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
        for (String text : b) {
            JButton btn = createButton(text); // Use your createButton helper
            p.add(btn);
        } return p;
    }

    static JPanel createScientificPanel() {
        JPanel p = new JPanel(new GridLayout(8,4,10,10)); p.setBackground(new Color(30,30,30));
        String[] b = {"M+","M-","MR","MC","sin","cos","tan","π","log", "ln", "e", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); return p;
    }

    static JPanel createAdvancedPanel() {
        JPanel p = new JPanel(new GridLayout(10,4,10,10)); p.setBackground(new Color(30,30,30));
        String[] b = {"M+","M-","MR","MC","sinh","cosh","tanh","π","asinh","acosh","atanh","e","coth", "sech", "csch", "ln", "coth", "sech", "csch", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); return p;
    }
    
    static JPanel createTrigPanel(){
        JPanel p = new JPanel(new GridLayout(10, 4, 10, 10)); p.setBackground(new Color(30, 30, 30));
        String[] b = {"M+", "M-", "MR", "MC", "sin", "cos", "tan", "π", "asin", "acos", "atan", "e", "cot", "sec", "csc", ")", "acot", "asec", "acsc", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createFuncPanel(){
        JPanel p = new JPanel(new GridLayout(9, 4, 10, 10)); p.setBackground(new Color(30, 30, 30));
        String[] b = {"M+", "M-", "MR", "MC", "sgn", "Γ", "ζ", "erf", "W", "Σ", "Φ", "Π", "μ", "σ", "prime", "^","7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createConstantPanel(){
        JPanel p = new JPanel(new GridLayout(8, 4, 20, 10)); p.setBackground(new Color(30, 30, 30));
        String[] b = {"M+", "M-", "MR", "MC","π", "e", "γ", "φ", "ρ", "δ", "δs", "G", "K", "A", "E", "Ω", "i", "j", "ε", "c", "Na", "h", "Ca", "M", "ω", "ζa", "D", "τ", "C", "CE", "DEG", "="};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createCalculus1Panel() {
    JPanel p = new JPanel(new GridLayout(10, 4, 10, 10)); 
    p.setBackground(new Color(30, 30, 30));
    
    String[] b = {"M+", "M-", "MR", "MC", "x", "d/dx", "d²/dx²", "∫", "lim", "e", "π", "W", "!", "(", ")", "Γ", "sin", "cos", "tan", "ln", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b) p.add(createButton(s));
    return p;
    }
    
    static JPanel createCalculus2Panel() {
    JPanel p = new JPanel(new GridLayout(10, 4, 10, 10)); 
    p.setBackground(new Color(30, 30, 30));
    
    String[] b = {"M+", "M-", "MR", "MC", "x", "d/dx", "d²/dx²", "∫", "lim", "e", "π", "Tang", "!", "(", ")", "Shade", "sin", "cos", "tan", "ln", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b){
        JButton btn = createButton(s); 
    p.add(btn);
    
    if (s.equals("Tang")) {
    // 1. REMOVE the default "print 'Tan' to screen" behavior
    for (ActionListener al : btn.getActionListeners()) {
        btn.removeActionListener(al);
    }

    // 2. ADD the custom "Tan(current_text)" behavior
    btn.addActionListener(e -> {
        String current = display.getText().trim();
        // If screen is empty, just show "Tan("
        if (current.isEmpty()) {
            display.setText("Tang(");
        } else {
            // If there's already math, wrap it: "Tan(x^2)"
            display.setText("Tang(" + current + ")");
        }
        
        // This makes sure the cursor is ready for the next input
        display.requestFocusInWindow();
    });
    }
    }
    return p;
    }
    
    static JPanel createStatisticsPanel(){
    JPanel p = new JPanel(new GridLayout(10, 4, 10, 10));
    p.setBackground(new Color(30, 30, 30));
    
    String[] b = {"M+", "M-", "MR", "MC", "abs", "ceil", "floor", "round", "max", "min", "mod", "rand", "nCr", "nPr", "stdev", "stdevp", "Σ", "mean", "%", ",", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b) p.add(createButton(s));
    return p;
    }
    static JPanel createAlphaPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(new Color(25, 25, 25));

    JTabbedPane alphaTabs = new JTabbedPane();
    
    // --- C and CE Buttons (Should be Red) ---
    RoundedButton clearAlphaBtn = new RoundedButton("C");
    clearAlphaBtn.setBackground(new Color(180, 50, 50)); // Deep Red
    clearAlphaBtn.setForeground(Color.WHITE);

    RoundedButton backAlphaBtn = new RoundedButton("CE");
    backAlphaBtn.setBackground(new Color(180, 50, 50)); // Deep Red
    backAlphaBtn.setForeground(Color.WHITE);

    // --- Shift Button (Should be Blue) ---
    RoundedButton shiftBtn = new RoundedButton("UPPERCASE / lowercase");
    shiftBtn.setBackground(new Color(0, 120, 215)); // Windows Blue
    shiftBtn.setForeground(Color.WHITE);
    
    // --- 1. English Panel ---
    JPanel englishPanel = new JPanel(new GridLayout(0, 7, 5, 5));
    englishPanel.setBackground(new Color(25, 25, 25));
    String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
    for (String s : alphabet) {
        RoundedButton btn = createSymbolButton(s);
        alphaButtons.add(btn);
        englishPanel.add(btn);
    }

    // --- 2. Greek Panel ---
    JPanel greekPanel = new JPanel(new GridLayout(0, 6, 5, 5));
    greekPanel.setBackground(new Color(25, 25, 25));
    String[] greek = "αβγδεζηθικλμνξοπρστυφχψω".split("");
    for (String g : greek) {
        RoundedButton btn = createSymbolButton(g);
        greekButtons.add(btn);
        greekPanel.add(btn);
    }

    alphaTabs.addTab("Latin", englishPanel);
    alphaTabs.addTab("ελληνικά", greekPanel);

    // --- 3. Functional Controls (C, CE, and Shift) ---
    // This panel holds the Clear buttons and the Shift toggle
    JPanel bottomControls = new JPanel(new BorderLayout(5, 5));
    bottomControls.setBackground(new Color(25, 25, 25));
    bottomControls.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    // Panel for C and CE (Side-by-side)
    JPanel alphaGrid = new JPanel(new GridLayout(1, 2, 5, 5)); 
    alphaGrid.setBackground(new Color(25, 25, 25));

    clearAlphaBtn.addActionListener(e -> {
        updateDisplay(""); // Clears and maintains RIGHT alignment
        display.requestFocusInWindow();
    });

    backAlphaBtn.addActionListener(e -> {
        String cur = display.getText();
        if (!cur.isEmpty()) {
            updateDisplay(cur.substring(0, cur.length() - 1));
        }
        display.requestFocusInWindow();
    });

    alphaGrid.add(backAlphaBtn);
    alphaGrid.add(clearAlphaBtn);

    shiftBtn.addActionListener(e -> {
        isUppercase = !isUppercase; 
        
        for (RoundedButton b : alphaButtons) {
            String txt = b.getText();
            b.setText(isUppercase ? txt.toUpperCase() : txt.toLowerCase());
        }
        
        for (RoundedButton b : greekButtons) {
            b.setText(toggleGreekCase(b.getText()));
        }
    });

    // Assemble the bottom section
    bottomControls.add(alphaGrid, BorderLayout.NORTH); // C/CE on top
    bottomControls.add(shiftBtn, BorderLayout.SOUTH);  // Shift on bottom

    // Add everything to main
    main.add(alphaTabs, BorderLayout.CENTER);
    main.add(bottomControls, BorderLayout.SOUTH); 
    
    return main;
    }

    private static String toggleGreekCase(String symbol) {
    String lower = "αβγδεζηθικλμνξοπρστυφχψω";
    String upper = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
    
    // If we are now Uppercase, we look for the symbol in the lowercase string to find the index
    if (isUppercase) {
        int idx = lower.indexOf(symbol);
        return (idx != -1) ? String.valueOf(upper.charAt(idx)) : symbol;
    } else {
        int idx = upper.indexOf(symbol);
        return (idx != -1) ? String.valueOf(lower.charAt(idx)) : symbol;
    }
    }
    
    private static RoundedButton createSymbolButton(String symbol) {
    RoundedButton btn = new RoundedButton(symbol);
    btn.setBackground(new Color(50, 50, 50));
    btn.setForeground(Color.WHITE);
    
    btn.addActionListener(e -> {
    String toPrint = btn.getText(); 
    String current = display.getText();
    
    if (current.equals("0")) {
        updateDisplay(toPrint); // Use helper here
    } else {
        updateDisplay(current + toPrint); // Use helper here
    }
    });
    return btn;
    }

    private static JPanel create3DGraphPanel() {
    JPanel container = new JPanel(new BorderLayout());
    container.setBackground(bgColor);

    // 1. THE MAIN CANVAS
    JPanel graphCanvas = new JPanel() {
        @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    // Background
    g2d.setColor(bgColor);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    int cX = getWidth() / 2;
    int cY = getHeight() / 2;

    // Configuration
    double axisLimit = 10.0; // Extended range for the lines
    
    // --- 1. DRAW GROUND GRID (Dashed Lines) ---
    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{2}, 0));
    g2d.setColor(currentTheme.functionButton); // Very subtle white
    for (double i = -axisLimit; i <= axisLimit; i++) {
        drawLine3D(g2d, i, -axisLimit, 0, i, axisLimit, 0, cX, cY); 
        drawLine3D(g2d, -axisLimit, i, 0, axisLimit, i, 0, cX, cY); 
    }

    // --- 2. DRAW NUMBERS ON GRID ---
    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
    g2d.setColor(currentTheme.functionButton);
    for (int i = (int)-axisLimit; i <= (int)axisLimit; i++) {
        if (i % 2 == 0 && i != 0) { // Number every 2 units to keep it clean
            drawLabel3D(g2d, String.valueOf(i), i, 0.3, 0, cX, cY); // X numbers
            drawLabel3D(g2d, String.valueOf(i), 0.3, i, 0, cX, cY); // Y numbers
            drawLabel3D(g2d, String.valueOf(i), 0.3, 0, -i, cX, cY); // Z numbers
        }
    }

    // --- 3. DRAW MAIN AXIS LINES (Solid) ---
    g2d.setStroke(new BasicStroke(2.0f));
    g2d.setColor(currentTheme.foreground);
    
    drawLine3D(g2d, -axisLimit, 0, 0, axisLimit, 0, 0, cX, cY); // X-Axis
    drawLine3D(g2d, 0, -axisLimit, 0, 0, axisLimit, 0, cX, cY); // Y-Axis
    drawLine3D(g2d, 0, 0, -axisLimit, 0, 0, axisLimit, cX, cY); // Z-Axis

    // Axis Labels
    g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
    drawLabel3D(g2d, "X", axisLimit + 0.8, 0, 0, cX, cY);
    drawLabel3D(g2d, "Y", 0, axisLimit + 0.8, 0, cX, cY);
    drawLabel3D(g2d, "Z", 0, 0, axisLimit + 0.8, cX, cY);

    // --- 4. DRAW THE DYNAMIC FUNCTION MESH ---
    // --- 4. DRAW THE DYNAMIC FUNCTION MESH (SOLID UPDATE) ---
    g2d.setStroke(new BasicStroke(1.2f));

    for (EquationLayer layer : layers) {
    String formula = layer.formula.trim().toLowerCase();
    // We call the new consolidated solid renderer instead of the old methods
    renderSolidScene(g2d, formula, layer.color, cX, cY);
    }
    
    if (hoveredPoint != null) {
    Point p = project(hoveredPoint.x, hoveredPoint.y, hoveredPoint.z, cX, cY);
    g2d.setColor(currentTheme.foreground);
    g2d.fillOval(p.x - 4, p.y - 4, 8, 8); // Hover dot
    
    String coordStr = String.format("X: %.2f Y: %.2f Z: %.2f", hoveredPoint.x, hoveredPoint.y, hoveredPoint.z);
    g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
    g2d.drawString(coordStr, p.x + 10, p.y - 10);
    }
    
    // UI Legend
    g2d.setColor(currentTheme.foreground);
    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    g2d.drawString("DRAG: ROTATE | SLIDER: ZOOM | ENTER: UPDATE", 20, getHeight() - 20);
    }
    };
    

    // Min: 50, Max: 800, Default: zoomScale (which is now 250)
    // This ensures 50 <= 250 <= 800
    JSlider zoomSlider = new JSlider(JSlider.VERTICAL, 50, 800, (int)zoomScale);
    zoomSlider.setBackground(bgColor);
    zoomSlider.setFocusable(false);
    zoomSlider.setPreferredSize(new Dimension(40, 0));

    // Add this to hide the ugly default track/borders if you want it extra sleek
    zoomSlider.setPaintTicks(false);
    zoomSlider.setPaintLabels(false);
    
    
    zoomSlider.addChangeListener(e -> {
        zoomScale = zoomSlider.getValue();
        graphCanvas.repaint();
    });
    
    
    
    // 3. INTERACTION LISTENERS
    graphCanvas.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) { lastMousePos = e.getPoint(); }
    });

    graphCanvas.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
            rotY += (e.getX() - lastMousePos.x) * 0.01;
            rotX += (e.getY() - lastMousePos.y) * 0.01;
            lastMousePos = e.getPoint();
            graphCanvas.repaint();
        }
    });
    
    graphCanvas.addMouseMotionListener(new MouseMotionAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {
        // Reverse-calculate 3D point from 2D mouse (Approximation)
        // For simplicity, we track the nearest grid intersection
        double mouseX = (e.getX() - graphCanvas.getWidth()/2.0) / (zoomScale / 15.0);
        double mouseY = (graphCanvas.getHeight()/2.0 - e.getY()) / (zoomScale / 15.0);
        
        // This is a rough estimate; in a real engine, we'd use ray-casting
        // hoveredPoint = new Point3D(mouseX, mouseY, eval(currentFormula, mouseX, mouseY, time));
        // graphCanvas.repaint();
    }
    });
    
    
    animationTimer = new Timer(16, e -> {
    if (isAnimated) {
        time += 0.1; // Speed of the wave/animation
        container.repaint();
    }
    });
    animationTimer.start();
    
    // ... (keep existing slider setup) ...

    // 3. THE CONTROL PANEL (West) - The Presets and Theme buttons
    JPanel westPanel = createControlPanel(graphCanvas);

    // 4. THE INPUT BAR (South) - The formula entry
    JPanel southPanel = new JPanel(new BorderLayout(10, 0));
    southPanel.setBackground(currentTheme.functionButton);
    southPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    formulaInput = new JTextField("");
    formulaInput.setBackground(currentTheme.background);
    formulaInput.setForeground(currentTheme.foreground);
    formulaInput.setCaretColor(currentTheme.foreground);
    formulaInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    formulaInput.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(70, 70, 70)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    formulaInput.addActionListener(e -> {
    String f = formulaInput.getText().toLowerCase();
    if (!f.isEmpty()) {
        // Add new layer with a cycling color
        Color nextColor = LAYER_COLORS[layers.size() % LAYER_COLORS.length];
        layers.add(new EquationLayer(f, nextColor));
        
        formulaInput.setText(""); // Clear input after adding
        updateEQPanel(graphCanvas);
        graphCanvas.repaint();
    }
    });

    JButton snapBtn = new JButton("SAVE");
    styleButton(snapBtn);
    snapBtn.setBackground(new Color(17, 17, 17));
    snapBtn.setForeground(new Color(238, 255, 122));
    snapBtn.addActionListener(e -> saveScreenshot(graphCanvas));

    southPanel.add(formulaInput, BorderLayout.CENTER);
    southPanel.add(snapBtn, BorderLayout.EAST);

    // --- FINAL ASSEMBLY ---
    container.add(graphCanvas, BorderLayout.CENTER);
    container.add(westPanel, BorderLayout.WEST);
    container.add(zoomSlider, BorderLayout.EAST);
    container.add(southPanel, BorderLayout.SOUTH); // This ensures the input stays at the bottom

    return container;
    }
    
    private static void renderSolidScene(Graphics2D g2d, String expr, Color baseColor, int cX, int cY) {
    List<Triangle3D> mesh = new ArrayList<>();
    double step = 0.4; 
    double limit = 8.0;
    boolean isImplicit = expr.contains("=");

    if (!isImplicit) {
        // --- FUNCTION MODE (Ripples, Gaussian, etc.) ---
        for (double x = -limit; x < limit; x += step) {
            for (double y = -limit; y < limit; y += step) {
                // Calculate 4 corners with current 'time' for movement
                Point3D p1 = new Point3D(x, y, eval(expr, x, y, time));
                Point3D p2 = new Point3D(x + step, y, eval(expr, x + step, y, time));
                Point3D p3 = new Point3D(x + step, y + step, eval(expr, x + step, y + step, time));
                Point3D p4 = new Point3D(x, y + step, eval(expr, x, y + step, time));

                // Add two triangles to form a solid quad face
                mesh.add(new Triangle3D(p1, p2, p3, baseColor, rotX, rotY));
                mesh.add(new Triangle3D(p1, p3, p4, baseColor, rotX, rotY));
            }
        }
    }else {
        // --- HIGH-QUALITY SOLID SHAPE RENDERING ---
        if (expr.contains("x^2") && expr.contains("y^2") && expr.contains("z^2")) {
            // Specialized High-Res Sphere Mesh
            double radius = 5.0; // Default; we can extract this from the equation later
            int segments = 24; // Increase for smoother sphere, decrease for performance

            for (int i = 0; i < segments; i++) {
                double lat0 = Math.PI * (-0.5 + (double) i / segments);
                double z0 = Math.sin(lat0) * radius;
                double r0 = Math.cos(lat0) * radius;

                double lat1 = Math.PI * (-0.5 + (double) (i + 1) / segments);
                double z1 = Math.sin(lat1) * radius;
                double r1 = Math.cos(lat1) * radius;

                for (int j = 0; j < segments; j++) {
                    double lng0 = 2 * Math.PI * (double) j / segments;
                    double x0 = Math.cos(lng0) * r0;
                    double y0 = Math.sin(lng0) * r0;

                    double lng1 = 2 * Math.PI * (double) (j + 1) / segments;
                    double x1 = Math.cos(lng1) * r0;
                    double y1 = Math.sin(lng1) * r0;

                    double x2 = Math.cos(lng1) * r1;
                    double y2 = Math.sin(lng1) * r1;

                    double x3 = Math.cos(lng0) * r1;
                    double y3 = Math.sin(lng0) * r1;

                    // Create solid quad from two triangles
                    mesh.add(new Triangle3D(new Point3D(x0, y0, z0), new Point3D(x1, y1, z0), new Point3D(x2, y2, z1), baseColor, rotX, rotY));
                    mesh.add(new Triangle3D(new Point3D(x0, y0, z0), new Point3D(x2, y2, z1), new Point3D(x3, y3, z1), baseColor, rotX, rotY));
                }
            }
        } else {
            // Fallback for other implicit shapes (Torus, etc.)
            // Increase the 'iStep' density or decrease the threshold for better results
            double iStep = 0.4; 
            for (double x = -6; x <= 6; x += iStep) {
                for (double y = -6; y <= 6; y += iStep) {
                    for (double z = -6; z <= 6; z += iStep) {
                        if (Math.abs(eval3DImplicit(expr, x, y, z)) < 0.3) {
                            Point3D p = new Point3D(x, y, z);
                            mesh.add(new Triangle3D(p, new Point3D(x+iStep, y, z), new Point3D(x, y+iStep, z), baseColor, rotX, rotY));
                        }
                    }
                }
            }
        }
    }
    

    // --- PAINTER'S ALGORITHM (Depth Sorting) ---
    // This ensures the back of the object is drawn before the front
    mesh.sort((t1, t2) -> Double.compare(t2.depth, t1.depth));

    // --- DRAWING ---
    for (Triangle3D t : mesh) {
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        for (int i = 0; i < 3; i++) {
            Point proj = project(t.p[i].x, t.p[i].y, t.p[i].z, cX, cY);
            xPoints[i] = proj.x;
            yPoints[i] = proj.y;
        }
        g2d.setColor(t.color);
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Subtle edges to make the mesh structure visible
        g2d.setColor(new Color(255, 255, 255, 20));
        g2d.drawPolygon(xPoints, yPoints, 3);
    }
    }

    // --- MATH ENGINE ---

    private static Point project(double x, double y, double z, int cx, int cy) {
    double focalLength = 15.0; // Simulated camera distance

    // 3D Rotation Math
    double cosY = Math.cos(rotY), sinY = Math.sin(rotY);
    double cosX = Math.cos(rotX), sinX = Math.sin(rotX);

    // Rotate around Y-axis
    double tx = x * cosY + y * sinY;
    double ty = y * cosY - x * sinY;

    // Rotate around X-axis
    double finalY = ty * cosX - z * sinX;
    double finalZ = z * cosX + ty * sinX;

    // Perspective Projection Formula
    double pScale = zoomScale / (finalZ + focalLength);
    int px = cx + (int) (tx * pScale);
    int py = cy - (int) (finalY * pScale);

    return new Point(px, py);
    }
    
    private static void drawFastGrid(Graphics2D g2d, String expr, int cX, int cY) {
    double step = 0.4; 
    double limit = 10.0;
    List<Face3D> faces = new ArrayList<>();

    // 1. Generate Faces
    for (double x = -limit; x < limit; x += step) {
        for (double y = -limit; y < limit; y += step) {
            double z1 = eval(expr, x, y, time);
            double z2 = eval(expr, x + step, y, time);
            double z3 = eval(expr, x + step, y + step, time);
            double z4 = eval(expr, x, y + step, time);

            if (Math.abs(z1) < 20 && Math.abs(z2) < 20 && Math.abs(z3) < 20 && Math.abs(z4) < 20) {
                // Pass current rotation angles to the face
                faces.add(new Face3D(
                    new Point3D(x, y, z1),
                    new Point3D(x + step, y, z2),
                    new Point3D(x + step, y + step, z3),
                    new Point3D(x, y + step, z4),
                    rotX, rotY
                ));
            }
        }
    }

    // 2. SORT BY ROTATED DEPTH (This is the secret to movement)
    faces.sort((f1, f2) -> Double.compare(f2.rotatedZ, f1.rotatedZ));

    // 3. Draw
    for (Face3D face : faces) {
        Point p1 = project(face.p1.x, face.p1.y, face.p1.z, cX, cY);
        Point p2 = project(face.p2.x, face.p2.y, face.p2.z, cX, cY);
        Point p3 = project(face.p3.x, face.p3.y, face.p3.z, cX, cY);
        Point p4 = project(face.p4.x, face.p4.y, face.p4.z, cX, cY);

        int[] xPoints = {p1.x, p2.x, p3.x, p4.x};
        int[] yPoints = {p1.y, p2.y, p3.y, p4.y};

        // Shading based on rotated depth makes it look like light is hitting it
        float shade = (float) Math.min(1.0, Math.max(0.2, 0.6 + (face.rotatedZ / 20.0)));
        Color base = g2d.getColor();
        g2d.setColor(new Color((int)(base.getRed()*shade), (int)(base.getGreen()*shade), (int)(base.getBlue()*shade)));
        
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Grid lines
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.drawPolygon(xPoints, yPoints, 4);
        g2d.setColor(base);
    }
    }

    // UPGRADED HELPERS
        private static class Point3D {
    double x, y, z;
    Point3D(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    }
    
    private static class Triangle3D {
    Point3D[] p;
    double depth;
    Color color;

    Triangle3D(Point3D p1, Point3D p2, Point3D p3, Color baseColor, double rx, double ry) {
        this.p = new Point3D[]{p1, p2, p3};
        
        // Calculate the "Camera Depth" of the center of the triangle
        double midX = (p1.x + p2.x + p3.x) / 3.0;
        double midY = (p1.y + p2.y + p3.y) / 3.0;
        double midZ = (p1.z + p2.z + p3.z) / 3.0;

        // Rotation logic to find true depth from screen
        double cosY = Math.cos(ry), sinY = Math.sin(ry);
        double cosX = Math.cos(rx), sinX = Math.sin(rx);
        double tx = midX * cosY + midY * sinY;
        double ty = midY * cosY - midX * sinY;
        this.depth = midZ * cosX + ty * sinX;

        // Shading Logic: Darker if deeper, lighter if closer
        float shade = (float) Math.min(1.0, Math.max(0.2, 0.7 + (this.depth / 25.0)));
        this.color = new Color(
            (int)(baseColor.getRed() * shade),
            (int)(baseColor.getGreen() * shade),
            (int)(baseColor.getBlue() * shade)
        );
    }
    }

    private static class Face3D {
    Point3D p1, p2, p3, p4;
    double rotatedZ; // Depth relative to the camera

    Face3D(Point3D p1, Point3D p2, Point3D p3, Point3D p4, double rx, double ry) {
        this.p1 = p1; this.p2 = p2; this.p3 = p3; this.p4 = p4;
        
        // Calculate the average rotated Z to find the distance from camera
        double avgX = (p1.x + p2.x + p3.x + p4.x) / 4.0;
        double avgY = (p1.y + p2.y + p3.y + p4.y) / 4.0;
        double avgZ = (p1.z + p2.z + p3.z + p4.z) / 4.0;

        // Apply rotation math to the center of the face to find its "true" depth
        double cosY = Math.cos(ry), sinY = Math.sin(ry);
        double cosX = Math.cos(rx), sinX = Math.sin(rx);
        
        double ty = avgY * cosY - avgX * sinY;
        this.rotatedZ = avgZ * cosX + ty * sinX; 
    }
    }
    
    private static void drawImplicitDots(Graphics2D g2d, String expr, int cX, int cY) {
    // 1. DENSITY: 0.1 is the 'sweet spot' for a solid-looking surface
    double step = 0.15; 
    double limit = 6.0; // Sphere radius is 5, so we check slightly beyond
    
    for (double x = -limit; x <= limit; x += step) {
        for (double y = -limit; y <= limit; y += step) {
            for (double z = -limit; z <= limit; z += step) {
                double val = eval3DImplicit(expr, x, y, z);
                
                // 2. THRESHOLD: Tighter threshold prevents the "thick shell" look
                if (Math.abs(val) < 0.1) {
                    Point p = project(x, y, z, cX, cY);
                    // Use 1x1 rects for a sharper, cleaner look
                    g2d.fillRect(p.x, p.y, 1, 1);
                }
            }
        }
    }
    }

    private static void drawLine3D(Graphics2D g2, double x1, double y1, double z1, double x2, double y2, double z2, int cx, int cy) {
    Point p1 = project(x1, y1, z1, cx, cy);
    Point p2 = project(x2, y2, z2, cx, cy);
    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    private static void drawLabel3D(Graphics2D g2, String text, double x, double y, double z, int cx, int cy) {
    Point p = project(x, y, z, cx, cy);
    g2.drawString(text, p.x, p.y);
    }

    private static double getZ(double x, double y) {
    // Current example function: Rippling Wave
    double r = Math.sqrt(x*x + y*y);
    return Math.sin(r) / (r + 0.5);
    }

    private static double eval(String expression, double x, double y, double t) {
    try {
        // We no longer need to manually replace "t" in the string.
        // We pass the raw expression and the three numbers (x, y, t) 
        // directly to the MathParser as it requires.
        return MathParser.eval(expression.toLowerCase(), x, y, t); 
    } catch (Exception e) {
        // Returns 0 if the user types an equation the parser doesn't understand
        return 0;
    }
    }

    private static void saveScreenshot(JPanel panel) {
    BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
    panel.paint(img.getGraphics());
    try {
        javax.imageio.ImageIO.write(img, "png", new java.io.File("Arithmos_Graph.png"));
        JOptionPane.showMessageDialog(null, "Graph saved as Arithmos_Graph.png");
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    private static JPanel createControlPanel(JPanel graphCanvas) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(new Color(17, 17, 17));
    panel.setPreferredSize(new Dimension(190, 0));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

    JLabel eqLabel = new JLabel("EQUATIONS");
    eqLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
    eqLabel.setForeground(Color.DARK_GRAY);
    eqLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(eqLabel);
    panel.add(Box.createVerticalStrut(8));
    
    // Just add the panel directly to the sidebar
    eqHistoryPanel = new JPanel();
    eqHistoryPanel.setLayout(new BoxLayout(eqHistoryPanel, BoxLayout.Y_AXIS));
    eqHistoryPanel.setOpaque(false);
    eqHistoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    panel.add(eqHistoryPanel); // No JScrollPane wrapper anymore!
    
    panel.add(Box.createVerticalGlue());

    // --- Buttons ---
    JButton clearAllBtn = new JButton("CLEAR ALL");
    styleSmallButton(clearAllBtn, new Color(25, 25, 25));
    clearAllBtn.setForeground(new Color(230, 136, 136));
    clearAllBtn.addActionListener(e -> {
        layers.clear();
        updateEQPanel(graphCanvas); // This refresh is what fixes the "dead" button
        graphCanvas.repaint();
    });
    panel.add(clearAllBtn);
    
    // ... (rest of the theme button code)
    return panel;
    }
    
    private static void updateEQPanel(JPanel graphCanvas) {
    eqHistoryPanel.removeAll();
    for (int i = 0; i < layers.size(); i++) {
        final int index = i;
        EquationLayer layer = layers.get(i);

        JPanel row = new JPanel(new BorderLayout(2, 0)); // Tiny 2px gap
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(180, 26)); // Lower height
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Color Box (Now even more left)
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(10, 20));
        colorBox.setBackground(layer.color);
        
        // 2. Input Box
        JTextField editBox = new JTextField(layer.formula);
        editBox.setBackground(new Color(35, 35, 35));
        editBox.setForeground(Color.WHITE);
        editBox.setFont(new Font("Consolas", Font.PLAIN, 11)); // Smaller font
        editBox.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 2));

        // 3. 'C' Button
        JButton cBtn = new JButton("C");
        styleCButton(cBtn);

        row.add(colorBox, BorderLayout.WEST);
        row.add(editBox, BorderLayout.CENTER);
        row.add(cBtn, BorderLayout.EAST);

        eqHistoryPanel.add(row);
        eqHistoryPanel.add(Box.createVerticalStrut(4));
    }
    eqHistoryPanel.revalidate();
    }
    
    private static void styleSmallButton(JButton btn, Color bg) {
    btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
    btn.setBackground(bg);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    btn.setMaximumSize(new Dimension(170, 28)); // Force small size
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void styleCButton(JButton btn) {
    btn.setFont(new Font("Arial", Font.BOLD, 9));
    btn.setBackground(new Color(50, 50, 50));
    btn.setForeground(Color.LIGHT_GRAY);
    btn.setFocusPainted(false);
    btn.setPreferredSize(new Dimension(22, 0));
    btn.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
    }
    
    // For 2D: Handles x^2 + y^2 = 25
    public static double evalImplicit(String expression, double x, double y) {
    if (expression.contains("=")) {
        String[] parts = expression.split("=");
        // Returns (Left Side) - (Right Side). When this equals 0, we found the line.
        return eval(parts[0], x, y, 0) - eval(parts[1], x, y, 0);
    }
    // Fallback for standard y = f(x) -> treats input as (y) - (f(x))
    return y - eval(expression, x, 0, 0);
    }

    // For 3D: Handles x^2 + y^2 + z^2 = 9
    public static double eval3DImplicit(String expression, double x, double y, double z) {
    if (expression.contains("=")) {
        String[] parts = expression.split("=");
        return evalWithZ(parts[0], x, y, z, time) - evalWithZ(parts[1], x, y, z, time);
    }
    // FIX: If no '=', return a huge value so it doesn't draw dots for regular functions
    return 999.0; 
    }

    private static double evalWithZ(String expr, double x, double y, double z, double t) {
    String processed = expr.replaceAll("z", "(" + z + ")");
    return eval(processed, x, y, t); 
    }

    // Helper to handle the 'z' variable in your expression string
    private static double evalWithZ(String expr, double x, double y, double z) {
    // This replaces 'z' in the string before parsing
    // Make sure your existing parser logic handles 'x', 'y', and 't' already
    String processed = expr.replaceAll("z", "(" + z + ")");
    return eval(processed, x, y, 0); // Re-use your existing 2D eval
    }

    private static JPanel createProgrammingPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(new Color(25, 25, 25));

    // --- 1. THE TOP LABELS SECTION ---
    JPanel displayLabels = new JPanel(new GridLayout(4, 1, 2, 2));
    displayLabels.setBackground(new Color(20, 20, 20));
    displayLabels.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

    // Segoe UI for the Labels
    Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
    
    // Assigning class-level labels
    hexLbl = new JLabel("HEX  0");
    decLbl = new JLabel("DEC  0");
    octLbl = new JLabel("OCT  0");
    binLbl = new JLabel("BIN  0");

    for (JLabel lbl : new JLabel[]{hexLbl, decLbl, octLbl, binLbl}) {
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(labelFont);
        displayLabels.add(lbl);
    }

    // --- 2. THE BUTTON KEYPAD SECTION ---
    JPanel keypad = new JPanel(new GridLayout(7, 4, 2, 2)); // Adjusted rows to 7
    keypad.setBackground(new Color(30, 30, 30));
    keypad.setFont(labelFont);

    String[] buttons = {
        "A", "B", "<<", ">>",
        "C", "D", "OR", "XOR",
        "E", "F", "AND", "NOT",
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "+/-", "0", ".", "+"
    };

    Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);
    Color orange = new Color(255, 159, 10); // Classic Calculator Orange
    Color blue = new Color(0, 120, 215);    // Windows Accent Blue

    for (String b : buttons) {
        JButton btn = new JButton(b);
        btn.setFocusable(false);
        btn.setFont(buttonFont);
        btn.setBorder(BorderFactory.createLineBorder(new Color(45, 45, 45)));
        btn.setBackground(new Color(55, 55, 55));
        btn.setForeground(Color.WHITE);
        btn.setFont(buttonFont);

        // --- CUSTOM COLOR LOGIC ---
        if (b.equals("+") || b.equals("-") || b.equals("*") || b.equals("/")) {
            btn.setBackground(orange);
        } else if (b.equals("<<") || b.equals(">>")) {
            btn.setBackground(blue);
        }

        btn.addActionListener(e -> handleInput(b));
        keypad.add(btn);
    }

    main.add(displayLabels, BorderLayout.NORTH);
    main.add(keypad, BorderLayout.CENTER);
    
    return main;
    }
    private static JPanel createConversionPanel() {
    // 1. INITIALIZE (Crucial for safety)
    categoryBox = new JComboBox<>(unitMap.keySet().toArray(new String[0]));
    fromUnitBox = new JComboBox<>();
    toUnitBox = new JComboBox<>();
    
    // Set default to 0 as requested
    convInput = new JTextField("0");
    convResult = new JLabel("0");

    // 2. RESTORE AESTHETICS (No Borders, Consistent Background)
    // Style the Dropdowns
    styleDropdown(categoryBox, segoeFont, bgColor, textColor);
    styleDropdown(fromUnitBox, segoeFont, bgColor, textColor);
    styleDropdown(toUnitBox, segoeFont, bgColor, textColor);
    
    // Style the Input Box
    convInput.setFont(inputFont);
    convInput.setBackground(currentTheme.background); // Match main background
    convInput.setForeground(currentTheme.foreground);
    convInput.setCaretColor(Color.WHITE);
    convInput.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // No visible border lines
    convInput.setOpaque(true);
    
    // Style the Result Label
    convResult.setFont(inputFont);
    convResult.setForeground(focusColor);

    // 3. INITIAL POPULATION
    updateUnitBoxes(); 

    // 4. STABLE LISTENERS
    categoryBox.addActionListener(e -> {
        updateUnitBoxes();
        performConversion();
    });

    fromUnitBox.addActionListener(e -> performConversion());
    toUnitBox.addActionListener(e -> performConversion());

    convInput.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) { performConversion(); }
        public void removeUpdate(DocumentEvent e) { performConversion(); }
        public void insertUpdate(DocumentEvent e) { performConversion(); }
    });

    // 5. LAYOUT ASSEMBLY
    JPanel mainContainer = new JPanel(new GridBagLayout());
    mainContainer.setBackground(bgColor);
    mainContainer.setBorder(null); // Ensure container is borderless

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(bgColor);
    leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 15));
    
    GridBagConstraints lGbc = new GridBagConstraints();
    lGbc.gridwidth = GridBagConstraints.REMAINDER;
    lGbc.fill = GridBagConstraints.HORIZONTAL;
    lGbc.weightx = 1.0;
    lGbc.insets = new Insets(10, 0, 10, 0);

    leftPanel.add(categoryBox, lGbc);
    leftPanel.add(convInput, lGbc);
    leftPanel.add(fromUnitBox, lGbc);
    leftPanel.add(convResult, lGbc);
    leftPanel.add(toUnitBox, lGbc);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBackground(bgColor);
    rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 30));
    rightPanel.add(createNumericKeypad(), BorderLayout.CENTER);

    gbc.weightx = 0.6; mainContainer.add(leftPanel, gbc);
    gbc.weightx = 0.4; mainContainer.add(rightPanel, gbc);

    return mainContainer;
    }
    private static void updateUnitBoxes() {
    // Safety Guard
    if (categoryBox == null || fromUnitBox == null || toUnitBox == null) return;

    Object selected = categoryBox.getSelectedItem();
    if (selected == null) return;

    String category = selected.toString();
    Map<String, Double> units = unitMap.get(category);

    if (units != null) {
        // Remove listeners to prevent "racing" errors during the update
        ActionListener[] fromL = fromUnitBox.getActionListeners();
        ActionListener[] toL = toUnitBox.getActionListeners();
        for (ActionListener l : fromL) fromUnitBox.removeActionListener(l);
        for (ActionListener l : toL) toUnitBox.removeActionListener(l);

        fromUnitBox.removeAllItems();
        toUnitBox.removeAllItems();

        for (String unitName : units.keySet()) {
            fromUnitBox.addItem(unitName);
            toUnitBox.addItem(unitName);
        }

        // Set different defaults: Index 0 and Index 1
        if (fromUnitBox.getItemCount() > 0) {
            fromUnitBox.setSelectedIndex(0);
            if (toUnitBox.getItemCount() > 1) {
                toUnitBox.setSelectedIndex(1);
            } else {
                toUnitBox.setSelectedIndex(0);
            }
        }

        // Put listeners back
        for (ActionListener l : fromL) fromUnitBox.addActionListener(l);
        for (ActionListener l : toL) toUnitBox.addActionListener(l);
    }
    }

    private static void performConversion() {
    try {
        // 1. NULL GUARDS: Check if components or selections are null
        if (categoryBox == null || fromUnitBox == null || toUnitBox == null) return;

        Object catObj = categoryBox.getSelectedItem();
        Object fromObj = fromUnitBox.getSelectedItem();
        Object toObj = toUnitBox.getSelectedItem();

        // If the user is currently typing/filtering and the box is empty, stop here.
        if (catObj == null || fromObj == null || toObj == null) {
            convResult.setText("0");
            return;
        }

        String cat = catObj.toString();
        String fromU = fromObj.toString();
        String toU = toObj.toString();
        String inputText = convInput.getText().trim();

        if (inputText.isEmpty()) {
            convResult.setText("0");
            return;
        }

        // 2. TEMPERATURE LOGIC (Uses Offsets, not just Ratios)
        if ("Temperature".equals(cat)) {
            double input = Double.parseDouble(inputText);
            double kelvin;

            // Convert Input to Kelvin (Base)
            switch (fromU) {
                case "Celsius (°C)": kelvin = input + 273.15; break;
                case "Fahrenheit (°F)": kelvin = (input - 32) * 5/9 + 273.15; break;
                case "Rankine (°R)": kelvin = input * 5/9; break;
                case "Réaumur (°Re)": kelvin = input * 1.25 + 273.15; break;
                default: kelvin = input; // Already Kelvin
            }

            // Convert Kelvin to Target
            double tempResult;
            switch (toU) {
                case "Celsius (°C)": tempResult = kelvin - 273.15; break;
                case "Fahrenheit (°F)": tempResult = (kelvin - 273.15) * 9/5 + 32; break;
                case "Rankine (°R)": tempResult = kelvin * 9/5; break;
                case "Réaumur (°Re)": tempResult = (kelvin - 273.15) * 0.8; break;
                default: tempResult = kelvin;
            }
            convResult.setText(String.format("%.4f", tempResult));
            return; 
        }

        // 3. NUMERALS LOGIC (Base Conversions)
        if ("Numerals".equals(cat)) {
            long decValue;
            try {
                if (fromU.contains("Binary")) decValue = Long.parseLong(inputText, 2);
                else if (fromU.contains("Hex")) decValue = Long.parseLong(inputText, 16);
                else if (fromU.contains("Octal")) decValue = Long.parseLong(inputText, 8);
                else decValue = Long.parseLong(inputText);

                if (toU.contains("Binary")) convResult.setText(Long.toBinaryString(decValue));
                else if (toU.contains("Hex")) convResult.setText(Long.toHexString(decValue).toUpperCase());
                else if (toU.contains("Octal")) convResult.setText(Long.toOctalString(decValue));
                else convResult.setText(String.valueOf(decValue));
            } catch (NumberFormatException e) {
                convResult.setText("Invalid Base");
            }
            return;
        }

        // 4. STANDARD MATH (Distance, Wave Velocity, Mass, etc.)
        double amount = Double.parseDouble(inputText);
        
        // Double-check the unitMap has the data
        if (unitMap.containsKey(cat)) {
            Map<String, Double> units = unitMap.get(cat);
            if (units.containsKey(fromU) && units.containsKey(toU)) {
                double fromRate = units.get(fromU);
                double toRate = units.get(toU);

                // Math: (Value / RateOfSource) * RateOfTarget
                double result = (amount / fromRate) * toRate;

                // Use %.6g to handle scientific notation for tiny/huge physics numbers
                convResult.setText(String.format("%.6g", result));
            }
        }

    } catch (NumberFormatException e) {
        convResult.setText("Invalid Number");
    } catch (Exception e) {
        // Catch-all for any weird thread/UI racing issues
        convResult.setText("Error");
    }
    }
        
    private static JTextField createStyledInput(Font font, Color bgColor, Color textColor) {
    JTextField input = new JTextField("1", 10);
    input.setFont(font);
    input.setBackground(bgColor);
    input.setForeground(textColor);
    input.setCaretColor(Color.CYAN);
    input.setOpaque(true);
    input.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
    
    // DocumentListener for live updates... (your existing logic)
    return input;
    }
    private static void styleDropdown(JComboBox<?> cb, Font f, Color bg, Color text) {
    if (cb == null) return;
    cb.setFont(f);
    cb.setBackground(bg);
    cb.setForeground(text);
    cb.setOpaque(true);
    // This makes the border disappear into the background
    cb.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    cb.setFocusable(false);
    }
    private static void applyTheme(JComponent comp) {
    comp.setBackground(bgColor); // Use your dark background
    comp.setForeground(textColor);
    if (comp instanceof JPanel) {
        ((JPanel) comp).setOpaque(true);
        ((JPanel) comp).setBorder(null); // Kills the white frame
    }
    }
    private static JPanel createNumericKeypad() {
    JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
    keypad.setOpaque(false);

    String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "CE", "0", "."};
    for (String key : keys) {
        JButton btn = new JButton(key);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        if (key.equals("CE")) {
            btn.setBackground(new Color(180, 50, 50)); // Red for Clear Entry
        } else {
            btn.setBackground(new Color(45, 45, 45));
        }
        btn.setForeground(Color.WHITE);
        
        // FIX: Hard-lock the size so they stay as squares
        Dimension size = new Dimension(75, 75);
        btn.setPreferredSize(size);
        btn.setMinimumSize(size);
        btn.setMaximumSize(size);
        
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        // Inside your keypad button loop
        btn.addActionListener(e -> {
            String current = convInput.getText();
            if (key.equals("CE")) {
                convInput.setText("");
            } else if (key.equals(".")) {
                if (!current.contains(".")) convInput.setText(current + ".");
            } else {
                convInput.setText(current + key);
            }
            performConversion(); // Live math updates!
        });
        keypad.add(btn);
    }
    return keypad;
    }
    private static void initializeDistanceData() {
    Map<String, Double> d = new LinkedHashMap<>();
    d.put("Metres (m)", 1.0); // SI BASE
    
    // --- Metric / SI Scales ---
    d.put("Kilometres (km)", 0.001);
    d.put("Decimetres (dm)", 10.0);
    d.put("Centimetres (cm)", 100.0);
    d.put("Millimetres (mm)", 1000.0);
    d.put("Micrometres (μm)", 1000000.0);
    d.put("Nanometres (nm)", 1.0E9);
    d.put("Picometres (pm)", 1.0E12);
    d.put("Femtometres (fm)", 1.0E15);
    d.put("Angstroms (Å)", 1.0E10);

    // --- Imperial / US Customary ---
    d.put("Miles (mi)", 0.000621371);
    d.put("Yards (yd)", 1.09361);
    d.put("Feet (ft)", 3.28084);
    d.put("Inches (in)", 39.3701);
    d.put("Mils (thou)", 39370.1);
    d.put("Furlongs", 0.00497097);
    d.put("Chains", 0.0497097);
    
    // --- Nautical & Astronomical ---
    d.put("Nautical miles", 0.000539957);
    d.put("Fathoms", 0.546807);
    d.put("Astronomical Units (AU)", 6.68459E-12);
    d.put("Light Years (ly)", 1.057E-16);
    d.put("Parsecs (pc)", 3.24078E-17);
    
    unitMap.put("Distance", d);
    }
    private static void initializeVolumeData() {
    Map<String, Double> v = new LinkedHashMap<>();
    v.put("Cubic metres (m³)", 1.0); // SI BASE
    
    // --- Metric ---
    v.put("Litres (L)", 1000.0);
    v.put("Millilitres (ml)", 1000000.0);
    v.put("Centilitres (cl)", 100000.0);
    v.put("Cubic centimetres (cm³)", 1000000.0);
    v.put("Cubic millimetres (mm³)", 1.0E9);

    // --- US Liquid ---
    v.put("Gallons (US)", 264.172);
    v.put("Quarts (US)", 1056.69);
    v.put("Pints (US)", 2113.38);
    v.put("Cups (US)", 4226.75);
    v.put("Fluid ounces (US)", 33814.0);
    v.put("Tablespoons (US)", 67628.0);
    v.put("Teaspoons (US)", 202884.0);
    
    // --- UK / Imperial ---
    v.put("Gallons (UK)", 219.969);
    v.put("Quarts (UK)", 879.877);
    v.put("Pints (UK)", 1759.75);
    v.put("Fluid ounces (UK)", 35195.1);
    v.put("Tablespoons (UK)", 56312.1);
    v.put("Teaspoons (UK)", 168936.0);

    // --- Others ---
    v.put("Cubic inches (in³)", 61023.7);
    v.put("Cubic feet (ft³)", 35.3147);
    v.put("Cubic yards (yd³)", 1.30795);
    v.put("Oil Barrels (bbl)", 6.28981);
    
    unitMap.put("Volume", v);
    }
    private static void initializeVelocityData() {
    Map<String, Double> vel = new LinkedHashMap<>();
    vel.put("Metres/second (m/s)", 1.0); // SI BASE
    
    // --- Common / Traffic ---
    vel.put("Kilometres/hour (km/h)", 3.6);
    vel.put("Kilometres/minute (km/min)", 0.06);
    vel.put("Miles/hour (mph)", 2.23694);
    vel.put("Miles/minute (mi/min)", 0.0372823);
    vel.put("Feet/second (fps)", 3.28084);
    vel.put("Feet/minute (fpm)", 196.85);
    vel.put("Inches/second (ips)", 39.3701);
    
    // --- Nautical & Aero ---
    vel.put("Knots (kn)", 1.94384);
    vel.put("Mach (at STP)", 0.00293867); // Speed of Sound
    vel.put("Beaufort Scale", 0.8333); // Approx median
    
    // --- Cosmic ---
    vel.put("Speed of Light (c)", 3.33564E-9);
    vel.put("Earth's Orbit Speed", 3.364E-5);
    
    unitMap.put("Velocity", vel);
    }
    private static void initializeAccelerationData() {
    Map<String, Double> acc = new LinkedHashMap<>();
    acc.put("Metres/sec² (m/s²)", 1.0); // SI BASE
    
    // --- Metric Variations ---
    acc.put("Kilometres/sec² (km/s²)", 0.001);
    acc.put("Centimetres/sec² (Gal)", 100.0);
    acc.put("Millimetres/sec² (mm/s²)", 1000.0);
    
    // --- Gravity & Imperial ---
    acc.put("Standard Gravity (g)", 0.101972);
    acc.put("Feet/sec² (ft/s²)", 3.28084);
    acc.put("Inches/sec² (in/s²)", 39.3701);
    acc.put("Miles/hour/sec (mph/s)", 2.23694);
    
    unitMap.put("Acceleration", acc);
    }
    private static void initializeMomentumData() {
    Map<String, Double> mom = new LinkedHashMap<>();
    mom.put("kg·m/s", 1.0); // SI BASE
    
    // --- Metric ---
    mom.put("Gram-cm/sec (g·cm/s)", 100000.0);
    mom.put("Newton-seconds (N·s)", 1.0);
    
    // --- Imperial / US ---
    mom.put("Pound-ft/sec (lb·ft/s)", 23.73036);
    mom.put("Pound-in/sec (lb·in/s)", 284.764);
    mom.put("Slug-ft/sec", 0.737562);
    
    unitMap.put("Momentum", mom);
    }
    private static void initializeAreaData() {
    Map<String, Double> a = new LinkedHashMap<>();
    a.put("Square metres (m²)", 1.0); // SI BASE
    
    // --- Metric / SI Scales ---
    a.put("Square kilometres (km²)", 1.0E-6);
    a.put("Square decimetres (dm²)", 100.0);
    a.put("Square centimetres (cm²)", 10000.0);
    a.put("Square millimetres (mm²)", 1000000.0);
    a.put("Square micrometres (μm²)", 1.0E12);
    
    // --- Land & Agriculture ---
    a.put("Hectares (ha)", 0.0001);
    a.put("Ares (a)", 0.01);
    a.put("Acres (ac)", 0.000247105);
    a.put("Roods", 0.000988422);
    
    // --- Imperial / US Customary ---
    a.put("Square miles (mi²)", 3.861E-7);
    a.put("Square yards (yd²)", 1.19599);
    a.put("Square feet (ft²)", 10.7639);
    a.put("Square inches (in²)", 1550.0);
    
    // --- Scientific / Nuclear ---
    a.put("Barns (b)", 1.0E28); // Used in subatomic physics
    
    unitMap.put("Area", a);
    }
    private static void initializeMechanicsDynamics() {
    // 1. FORCE (Base: Newtons)
    Map<String, Double> force = new LinkedHashMap<>();
    force.put("Newtons (N)", 1.0);
    force.put("Kilonewtons (kN)", 0.001);
    force.put("Meganewtons (MN)", 1.0E-6);
    force.put("Giganewtons (GN)", 1.0E-9);
    force.put("Millinewtons (mN)", 1000.0);
    force.put("Micronewtons (μN)", 1000000.0);
    force.put("Dynes (dyn)", 100000.0);
    force.put("Kilogram-force (kgf)", 0.1019716);
    force.put("Gram-force (gf)", 101.9716);
    force.put("Pound-force (lbf)", 0.2248089);
    force.put("Ounce-force (ozf)", 3.596943);
    force.put("Poundals (pdl)", 7.233014);
    force.put("Kips", 0.000224809);
    unitMap.put("Force", force);

    // 2. TORQUE (Base: Newton-metres)
    Map<String, Double> torque = new LinkedHashMap<>();
    torque.put("Newton-metres (N·m)", 1.0);
    torque.put("Newton-centimetres (N·cm)", 100.0);
    torque.put("Kilonewton-metres (kN·m)", 0.001);
    torque.put("Pound-foot (lb·ft)", 0.7375621);
    torque.put("Pound-inch (lb·in)", 8.850746);
    torque.put("Foot-pounds (ft·lb)", 0.7375621);
    torque.put("Kilogram-force metre (kgf·m)", 0.1019716);
    torque.put("Ounce-inch (oz·in)", 141.6119);
    unitMap.put("Torque", torque);

    // 3. ENERGY (Base: Joules)
    Map<String, Double> energy = new LinkedHashMap<>();
    energy.put("Joules (J)", 1.0);
    energy.put("Kilojoules (kJ)", 0.001);
    energy.put("Megajoules (MJ)", 1.0E-6);
    energy.put("Gigajoules (GJ)", 1.0E-9);
    energy.put("Calories (cal)", 0.2390057);
    energy.put("Kilocalories (kcal)", 0.0002390057);
    energy.put("Watt-hours (Wh)", 0.0002777778);
    energy.put("Kilowatt-hours (kWh)", 2.777778E-7);
    energy.put("Electronvolts (eV)", 6.241509E18);
    energy.put("British Thermal Units (BTU)", 0.000947817);
    energy.put("Foot-pounds (ft·lb)", 0.7375621);
    energy.put("Ergs", 10000000.0);
    energy.put("Therms (US)", 9.4804E-9);
    unitMap.put("Energy", energy);

    // 4. POWER (Base: Watts)
    Map<String, Double> power = new LinkedHashMap<>();
    power.put("Watts (W)", 1.0);
    power.put("Kilowatts (kW)", 0.001);
    power.put("Megawatts (MW)", 1.0E-6);
    power.put("Milliwatts (mW)", 1000.0);
    power.put("Horsepower (Mechanical/Imperial)", 0.00134102);
    power.put("Horsepower (Metric/PS)", 0.00135962);
    power.put("Horsepower (Electrical)", 0.00134048);
    power.put("BTU/hour", 3.412142);
    power.put("Calories/second", 0.2388459);
    power.put("Foot-pounds/minute", 44.25373);
    unitMap.put("Power", power);

    // 5. PRESSURE (Base: Pascals)
    Map<String, Double> press = new LinkedHashMap<>();
    press.put("Pascals (Pa)", 1.0);
    press.put("Kilopascals (kPa)", 0.001);
    press.put("Megapascals (MPa)", 1.0E-6);
    press.put("Bars", 0.00001);
    press.put("Millibars (mbar)", 0.01);
    press.put("Atmospheres (atm)", 9.869233E-6);
    press.put("PSI (Pound/sq inch)", 0.0001450377);
    press.put("Torr (mmHg)", 0.007500617);
    press.put("Inches of Mercury (inHg)", 0.0002952998);
    press.put("Inches of Water (inH2O)", 0.00401463);
    unitMap.put("Pressure", press);

    // 6. SURFACE TENSION (Base: N/m)
    Map<String, Double> surface = new LinkedHashMap<>();
    surface.put("Newton/metre (N/m)", 1.0);
    surface.put("Millinewton/metre (mN/m)", 1000.0);
    surface.put("Dyne/centimetre (dyn/cm)", 1000.0);
    surface.put("Pound-force/inch (lbf/in)", 0.005710147);
    surface.put("Gram-force/centimetre", 10.19716);
    unitMap.put("Surface Tension", surface);

    // 7. VISCOSITY (Base: Pascal-seconds)
    Map<String, Double> visc = new LinkedHashMap<>();
    visc.put("Pascal-seconds (Pa·s)", 1.0);
    visc.put("Poise (P)", 10.0);
    visc.put("Centipoise (cP)", 1000.0);
    visc.put("Poundal-second/sq foot", 0.6719689);
    visc.put("Pound/foot-hour", 2419.088);
    unitMap.put("Viscosity", visc);

    // 8. IMPULSE (Base: Newton-seconds)
    Map<String, Double> imp = new LinkedHashMap<>();
    imp.put("Newton-seconds (N·s)", 1.0); // SI BASE
    imp.put("kg·m/s", 1.0);
    imp.put("Pound-seconds (lbf·s)", 0.224809);
    imp.put("Slug-ft/s", 0.737562);
    imp.put("Dyne-seconds (dyn·s)", 100000.0);
    imp.put("Gram-force second (gf·s)", 101.972);
    imp.put("Poundal-seconds", 7.23301);
    imp.put("Kip-seconds", 0.000224809);
    imp.put("Millinewton-seconds", 1000.0);
    imp.put("Kilonewton-seconds", 0.001);
    imp.put("Gram-cm/s", 100000.0);
    imp.put("lb·ft/s", 23.73036);
    imp.put("lb·in/s", 284.764);
    imp.put("Slug-in/s", 8.85075);
    imp.put("Ounce-force second", 3.59694);
    imp.put("Tonne-force second", 0.00010197);
    imp.put("Slug-ft/min", 44.2537);
    imp.put("Metric Ton-force second", 0.000101972);
    imp.put("Pound-force minute", 0.00374681);
    imp.put("Newton-minute", 0.0166667);
    imp.put("kg·km/hr", 3.6);
    imp.put("Gram-mm/s", 1000000.0);
    imp.put("Centinewton-second", 100.0);
    imp.put("Decanewton-second", 0.1);
    imp.put("Hectonewton-second", 0.01);
    imp.put("Meganewton-second", 0.000001);
    imp.put("Giganewton-second", 1.0E-9);
    imp.put("Teranewton-second", 1.0E-12);
    imp.put("Nanonewton-second", 1.0E9);
    imp.put("Micronewton-second", 1000000.0);
    imp.put("Piconewton-second", 1.0E12);
    imp.put("Femtonewton-second", 1.0E15);
    imp.put("Attonewton-second", 1.0E18);
    imp.put("lb-ft/min", 1423.82);
    imp.put("Pound-inch/minute", 17085.8);
    imp.put("Ounce-inch/second", 57.551);
    imp.put("lb-ft/hour", 85429.3);
    imp.put("Gram-metre/second", 1000.0);
    imp.put("Kilogram-centimetre/second", 100.0);
    imp.put("Kilogram-kilometre/hour", 3.6);
    imp.put("Newton-hour", 0.00027778);
    imp.put("Pound-force hour", 0.00006245);
    imp.put("Dyne-minute", 1666.67);
    imp.put("Gram-force minute", 1.69953);
    imp.put("Pound-force ms", 224.809);
    imp.put("Newton-microsecond", 1000000.0);
    imp.put("Kip-minute", 3.7468E-6);
    imp.put("Gram-force ms", 101972.0);
    imp.put("Micro-slug ft/s", 737562.0);
    imp.put("Milli-slug ft/s", 737.562);
    imp.put("Nano-slug ft/s", 7.37562E8);
    imp.put("Pico-slug ft/s", 7.37562E11);
    imp.put("lb-f furlong/fortnight", 538965.0);
    imp.put("Plank impulse", 1.531E-23);
    imp.put("Electron momentum", 3.637E+24);
    imp.put("Earth momentum", 1.787E-29);
    imp.put("Sun momentum", 5.92E-35);
    imp.put("Galactic momentum", 3.0E-48);
    imp.put("Proton momentum", 1.98E+27);
    imp.put("Neutron momentum", 1.98E+27);
    imp.put("Muon momentum", 3.44E+26);
    imp.put("Photon momentum (visible)", 4.54E+27);
    imp.put("X-ray photon momentum", 2.0E+31);
    imp.put("Alpha particle momentum", 3.32E+27);
    imp.put("Standard rocket impulse", 1.0E-7);
    imp.put("Saturn V total impulse", 2.85E-10);
    imp.put("Small firework impulse", 10.0);
    imp.put("Car crash impulse", 0.0001);
    imp.put("Golf ball strike impulse", 0.5);
    imp.put("Tennis serve impulse", 0.2);
    imp.put("Human jump impulse", 0.005);
    imp.put("Raindrop impulse", 1.0E10);
    imp.put("Atomic recoil impulse", 1.0E25);
    imp.put("Tectonic plate impulse", 1.0E-18);
    imp.put("Glacier movement impulse", 1.0E-15);
    imp.put("Sound wave momentum", 1.0E12);
    imp.put("Solar wind pressure impulse", 1.0E14);
    imp.put("Comet tail impulse", 1.0E16);
    imp.put("Supernova impulse", 1.0E-40);
    imp.put("Black hole merger impulse", 1.0E-50);
    imp.put("Big Bang initial impulse", 1.0E-60);
    imp.put("Universal expansion impulse", 1.0E-80);
    imp.put("String theory tension impulse", 1.0E-20);
    imp.put("Gravitational wave impulse", 1.0E-55);
    imp.put("Higgs field impulse", 1.0E-30);
    imp.put("Dark matter impulse", 1.0E-45);
    imp.put("Vacuum energy impulse", 1.0E-65);
    imp.put("Quantum fluctuation impulse", 1.0E-34);
    imp.put("Neutrino momentum", 1.0E+36);
    imp.put("Cosmic ray momentum", 1.0E+20);
    imp.put("Quark momentum", 1.0E+28);
    imp.put("Gluon momentum", 1.0E+28);
    imp.put("W-boson momentum", 1.0E+25);
    imp.put("Z-boson momentum", 1.0E+25);
    imp.put("Tachyon momentum", 1.0E+50);
    imp.put("Magnetic monopole momentum", 1.0E+22);
    imp.put("Standard kilogram momentum", 1.0);
    imp.put("Standard meter momentum", 1.0);
    imp.put("Standard second momentum", 1.0);
    imp.put("Standard Ampere momentum", 1.0);
    imp.put("Standard Kelvin momentum", 1.0);
    imp.put("Standard mole momentum", 1.0);
    imp.put("Standard candela momentum", 1.0);
    imp.put("Total world momentum", 1.0E-40);
    imp.put("Ocean current momentum", 1.0E-25);
    imp.put("Wind gust momentum", 1.0E-5);
    imp.put("Volcanic eruption momentum", 1.0E-15);
    imp.put("Earthquake wave momentum", 1.0E-14);
    imp.put("Tsunami momentum", 1.0E-20);
    imp.put("Avalanche momentum", 1.0E-8);
    imp.put("Lightning bolt momentum", 1.0E-10);
    imp.put("Meteor strike momentum", 1.0E-20);
    imp.put("Asteroid impact momentum", 1.0E-30);
    imp.put("Moon tidal momentum", 1.0E-28);
    imp.put("Planetary spin momentum", 1.0E-45);
    imp.put("Solar flare momentum", 1.0E-20);
    imp.put("Star formation momentum", 1.0E-40);
    imp.put("Galaxy rotation momentum", 1.0E-60);
    imp.put("Universe rotation momentum", 1.0E-90);
    imp.put("Time travel impulse", 0.0);
    imp.put("Warp drive impulse", 1.0E-60);
    imp.put("Teleportation impulse", 1.0E-30);
    imp.put("Antigravity impulse", -1.0);
    imp.put("Gravity reversal impulse", -1.0);
    imp.put("Absolute Zero impulse", 0.0);
    imp.put("Infinite energy impulse", 1.0E-99);
    imp.put("Singularity impulse", 1.0E-99);
    imp.put("Parallel universe impulse", 1.0E-80);
    imp.put("Multiverse total impulse", 1.0E-99);
    imp.put("Simulated reality impulse", 1.0E-10);
    imp.put("Neural signal impulse", 1.0E12);
    imp.put("Heartbeat impulse", 10.0);
    imp.put("Breath impulse", 1.0);
    imp.put("Blink impulse", 0.1);
    imp.put("Finger snap impulse", 0.5);
    imp.put("Clap impulse", 1.0);
    imp.put("Whisper impulse", 1.0E-5);
    imp.put("Shout impulse", 0.1);
    imp.put("Thought impulse", 1.0E-15);
    imp.put("Dream impulse", 1.0E-20);
    imp.put("Memory impulse", 1.0E-18);
    imp.put("Emotion impulse", 1.0E-10);
    imp.put("Consciousness impulse", 1.0E-25);
    imp.put("Soul impulse", 1.0E-30);
    imp.put("Life force impulse", 1.0E-20);
    imp.put("Growth impulse", 1.0E-15);
    imp.put("Decay impulse", 1.0E-15);
    imp.put("Evolution impulse", 1.0E-40);
    imp.put("Extinction impulse", 1.0E-30);
    imp.put("Civilization impulse", 1.0E-50);
    imp.put("History impulse", 1.0E-60);
    imp.put("Future impulse", 1.0E-60);
    imp.put("Entropy impulse", 1.0E-80);
    imp.put("Heat death impulse", 0.0);
    imp.put("Infinite loop impulse", 1.0);
    imp.put("Logic impulse", 1.0);
    imp.put("Chaos impulse", 1.0);
    imp.put("Order impulse", 1.0);
    imp.put("Balance impulse", 1.0);
    imp.put("Final Impulse", 1.0);
    imp.put("The End", 1.0);
    imp.put("Zero Point", 0.0);
    imp.put("Infinity", 1.0E-99);
    imp.put("All", 1.0);
    imp.put("Nothing", 0.0);

    unitMap.put("Impulse", imp);
    }
    private static void initializeTimeTemperature() {
    // 1. TIME (Base: Seconds)
    // From the Planck Time to the Age of the Universe
    Map<String, Double> time = new LinkedHashMap<>();
    time.put("Seconds (s)", 1.0); // SI BASE
    time.put("Milliseconds (ms)", 1000.0);
    time.put("Microseconds (μs)", 1000000.0);
    time.put("Nanoseconds (ns)", 1.0E9);
    time.put("Picoseconds (ps)", 1.0E12);
    time.put("Femtoseconds (fs)", 1.0E15);
    time.put("Minutes (min)", 1.0 / 60.0);
    time.put("Hours (hr)", 1.0 / 3600.0);
    time.put("Days (d)", 1.0 / 86400.0);
    time.put("Weeks", 1.0 / 604800.0);
    time.put("Months (Average)", 1.0 / 2.628E6);
    time.put("Years (Julian)", 1.0 / 3.15576E7);
    time.put("Decades", 1.0 / 3.15576E8);
    time.put("Centuries", 1.0 / 3.15576E9);
    time.put("Millennia", 1.0 / 3.15576E10);
    time.put("Fortnights", 1.0 / 1.21E6);
    time.put("Planck Time", 1.85E43); 
    unitMap.put("Time", time);

    // 2. TEMPERATURE (Logic is handled by Offset, not Multiplier)
    Map<String, Double> temp = new LinkedHashMap<>();
    temp.put("Celsius (°C)", 0.0); // Placeholder
    temp.put("Fahrenheit (°F)", 0.0);
    temp.put("Kelvin (K)", 0.0);
    temp.put("Rankine (°R)", 0.0);
    temp.put("Réaumur (°Re)", 0.0);
    unitMap.put("Temperature", temp);
    }
    private static void initializeMatterGravity() {
    // 1. MASS (Base: Kilograms)
    // From an Electron to the Sun
    Map<String, Double> mass = new LinkedHashMap<>();
    mass.put("Kilograms (kg)", 1.0); // SI BASE
    mass.put("Grams (g)", 1000.0);
    mass.put("Milligrams (mg)", 1000000.0);
    mass.put("Micrograms (μg)", 1.0E9);
    mass.put("Metric Tonnes (t)", 0.001);
    mass.put("Pounds (lb)", 2.20462);
    mass.put("Ounces (oz)", 35.274);
    mass.put("Stones (st)", 0.157473);
    mass.put("Slugs", 0.0685218);
    mass.put("Grains (gr)", 15432.4);
    mass.put("Solar Masses", 5.02785E-31);
    mass.put("Atomic Mass Units (u)", 6.02214E26);
    unitMap.put("Mass", mass);

    // 2. WEIGHT (Base: Newtons - strictly Force)
    Map<String, Double> weight = new LinkedHashMap<>();
    weight.put("Newtons (N)", 1.0); // SI BASE
    weight.put("Kilogram-force (kgf)", 0.101972);
    weight.put("Pound-force (lbf)", 0.224809);
    weight.put("Dynes", 100000.0);
    unitMap.put("Weight", weight);

    // 3. DENSITY (Base: kg/m³)
    Map<String, Double> density = new LinkedHashMap<>();
    density.put("kg/m³", 1.0); // SI BASE
    density.put("g/cm³", 0.001);
    density.put("g/mL", 0.001);
    density.put("lb/ft³", 0.062428);
    density.put("lb/in³", 3.6127E-5);
    density.put("Specific Gravity", 0.001); // Relative to water
    unitMap.put("Density", density);

    // 4. AMOUNT OF SUBSTANCE (Base: Moles)
    Map<String, Double> amount = new LinkedHashMap<>();
    amount.put("Moles (mol)", 1.0); // SI BASE
    amount.put("Millimoles (mmol)", 1000.0);
    amount.put("Kilomoles (kmol)", 0.001);
    amount.put("Micromoles (μmol)", 1000000.0);
    amount.put("Nanomoles (nmol)", 1.0E9);
    amount.put("Number of Molecules", 6.02214076E23); // Avogadro's constant
    unitMap.put("Amount of Substance", amount);
    }
    private static void initializeElectromagnetism() {
    // 1. ELECTRIC CHARGE (Base: Coulombs)
    Map<String, Double> charge = new LinkedHashMap<>();
    charge.put("Coulombs (C)", 1.0); // SI BASE
    charge.put("Millicoulombs (mC)", 1000.0);
    charge.put("Microcoulombs (μC)", 1000000.0);
    charge.put("Nanocoulombs (nC)", 1.0E9);
    charge.put("Picocoulombs (pC)", 1.0E12);
    charge.put("Abcoulombs (abC)", 0.1);
    charge.put("Ampere-hours (Ah)", 0.000277778);
    charge.put("Ampere-minutes", 0.0166667);
    charge.put("Ampere-seconds", 1.0);
    charge.put("Elementary Charge (e)", 6.2415E18);
    charge.put("Faradays (physicochemical)", 1.0364E-5);
    unitMap.put("Electric Charge", charge);

    // 2. ELECTRIC CURRENT (Base: Amperes)
    Map<String, Double> current = new LinkedHashMap<>();
    current.put("Amperes (A)", 1.0); // SI BASE
    current.put("Milliamperes (mA)", 1000.0);
    current.put("Microamperes (μA)", 1000000.0);
    current.put("Kiloamperes (kA)", 0.001);
    current.put("Abamperes (abA)", 0.1);
    current.put("Statamperes", 2.9979E9);
    unitMap.put("Electric Current", current);

    // 3. VOLTAGE / POTENTIAL (Base: Volts)
    Map<String, Double> voltage = new LinkedHashMap<>();
    voltage.put("Volts (V)", 1.0); // SI BASE
    voltage.put("Millivolts (mV)", 1000.0);
    voltage.put("Kilovolts (kV)", 0.001);
    voltage.put("Megavolts (MV)", 1.0E-6);
    voltage.put("Microvolts (μV)", 1000000.0);
    voltage.put("Abvolts", 1.0E8);
    voltage.put("Statvolts", 0.0033356);
    unitMap.put("Voltage", voltage);

    // 4. RESISTANCE (Base: Ohms)
    Map<String, Double> resistance = new LinkedHashMap<>();
    resistance.put("Ohms (Ω)", 1.0); // SI BASE
    resistance.put("Milliohms (mΩ)", 1000.0);
    resistance.put("Kilohms (kΩ)", 0.001);
    resistance.put("Megohms (MΩ)", 1.0E-6);
    resistance.put("Abohms", 1.0E9);
    resistance.put("Statohms", 1.1126E-12);
    unitMap.put("Resistance", resistance);

    // 5. CAPACITANCE (Base: Farads)
    Map<String, Double> capacitance = new LinkedHashMap<>();
    capacitance.put("Farads (F)", 1.0); // SI BASE
    capacitance.put("Millifarads (mF)", 1000.0);
    capacitance.put("Microfarads (μF)", 1000000.0);
    capacitance.put("Nanofarad (nF)", 1.0E9);
    capacitance.put("Picofarads (pF)", 1.0E12);
    capacitance.put("Abfarads", 1.0E-9);
    capacitance.put("Statfarads", 8.9876E11);
    unitMap.put("Capacitance", capacitance);

    // 6. INDUCTANCE (Base: Henries)
    Map<String, Double> inductance = new LinkedHashMap<>();
    inductance.put("Henries (H)", 1.0); // SI BASE
    inductance.put("Millihenries (mH)", 1000.0);
    inductance.put("Microhenries (μH)", 1000000.0);
    inductance.put("Abhenries", 1.0E9);
    inductance.put("Stathenries", 1.1126E-12);
    unitMap.put("Inductance", inductance);

    // 7. ELECTRIC FIELD STRENGTH (Base: V/m)
    Map<String, Double> eField = new LinkedHashMap<>();
    eField.put("Volts/metre (V/m)", 1.0);
    eField.put("Kilovolts/metre", 0.001);
    eField.put("Volts/cm", 0.01);
    eField.put("Millivolts/metre", 1000.0);
    unitMap.put("Electric Field Strength", eField);

    // 8. MAGNETIC FLUX (Base: Webers)
    Map<String, Double> magFlux = new LinkedHashMap<>();
    magFlux.put("Webers (Wb)", 1.0);
    magFlux.put("Milliwebers (mWb)", 1000.0);
    magFlux.put("Microwebers (μWb)", 1000000.0);
    magFlux.put("Maxwells (Mx)", 1.0E8);
    unitMap.put("Magnetic Flux", magFlux);

    // 9. MAGNETIC FLUX DENSITY (Base: Teslas)
    Map<String, Double> magDensity = new LinkedHashMap<>();
    magDensity.put("Teslas (T)", 1.0);
    magDensity.put("Gauss (G)", 10000.0);
    magDensity.put("Milliteslas (mT)", 1000.0);
    magDensity.put("Microteslas (μT)", 1000000.0);
    unitMap.put("Magnetic Flux Density", magDensity);

    // 10. CONDUCTANCE (Base: Siemens)
    Map<String, Double> conductance = new LinkedHashMap<>();
    conductance.put("Siemens (S)", 1.0);
    conductance.put("Mhos (℧)", 1.0);
    conductance.put("Microsiemens (μS)", 1000000.0);
    conductance.put("Millisiemens (mS)", 1000.0);
    unitMap.put("Conductance", conductance);
    }
    private static void initializeThermalWaves() {
    // 1. ENTROPY (Base: Joules per Kelvin - J/K)
    Map<String, Double> entropy = new LinkedHashMap<>();
    entropy.put("Joules/Kelvin (J/K)", 1.0); // SI BASE
    entropy.put("Kilojoules/Kelvin (kJ/K)", 0.001);
    entropy.put("Calories/Kelvin (cal/K)", 0.239006);
    entropy.put("BTU/Rankine", 0.000526565);
    unitMap.put("Entropy", entropy);

    // 2. SPECIFIC HEAT CAPACITY (Base: J/(kg·K))
    Map<String, Double> specHeat = new LinkedHashMap<>();
    specHeat.put("J/(kg·K)", 1.0); // SI BASE
    specHeat.put("kJ/(kg·K)", 0.001);
    specHeat.put("cal/(g·°C)", 0.000238846);
    specHeat.put("kcal/(kg·°C)", 0.000238846);
    specHeat.put("BTU/(lb·°F)", 0.000238846);
    unitMap.put("Specific Heat Capacity", specHeat);

    // 3. THERMAL CONDUCTIVITY (Base: Watts per metre-Kelvin - W/(m·K))
    Map<String, Double> thermCond = new LinkedHashMap<>();
    thermCond.put("W/(m·K)", 1.0); // SI BASE
    thermCond.put("W/(m·°C)", 1.0);
    thermCond.put("mW/(m·K)", 1000.0);
    thermCond.put("cal/(s·cm·°C)", 0.00238846);
    thermCond.put("BTU/(hr·ft·°F)", 0.577789);
    unitMap.put("Thermal Conductivity", thermCond);

    // 4. FREQUENCY (Base: Hertz - Hz)
    Map<String, Double> freq = new LinkedHashMap<>();
    freq.put("Hertz (Hz)", 1.0); // SI BASE
    freq.put("Kilohertz (kHz)", 0.001);
    freq.put("Megahertz (MHz)", 1.0E-6);
    freq.put("Gigahertz (GHz)", 1.0E-9);
    freq.put("Terahertz (THz)", 1.0E-12);
    freq.put("Rotations per minute (RPM)", 60.0);
    freq.put("Degrees per second (°/s)", 360.0);
    freq.put("Radians per second (rad/s)", 6.283185);
    unitMap.put("Frequency", freq);

    // 5. WAVELENGTH (Base: Metres - m)
    Map<String, Double> wavelength = new LinkedHashMap<>();
    wavelength.put("Metres (m)", 1.0); // SI BASE
    wavelength.put("Kilometres (km)", 0.001);
    wavelength.put("Centimetres (cm)", 100.0);
    wavelength.put("Millimetres (mm)", 1000.0);
    wavelength.put("Micrometres (μm)", 1000000.0);
    wavelength.put("Nanometres (nm)", 1.0E9);
    wavelength.put("Angstroms (Å)", 1.0E10);
    wavelength.put("Inches (in)", 39.3701);
    wavelength.put("Feet (ft)", 3.28084);
    unitMap.put("Wavelength", wavelength);
    
    Map<String, Double> waveVel = new LinkedHashMap<>();
    waveVel.put("m/s", 1.0); // SI BASE
    waveVel.put("km/h", 3.6);
    waveVel.put("mph", 2.23694);
    waveVel.put("Knots", 1.94384);
    waveVel.put("Mach (at STP)", 1.0 / 343.0); // Speed of sound
    waveVel.put("Speed of Light (c)", 3.3356E-9);
    waveVel.put("Speed of Sound (Water)", 1.0 / 1481.0);
    waveVel.put("Speed of Sound (Steel)", 1.0 / 5960.0);
    unitMap.put("Wave Velocity", waveVel);
    }
    private static void initializeRotationalDynamics() {
    // 1. ANGULAR VELOCITY (Base: Radians per second - rad/s)
    Map<String, Double> angVel = new LinkedHashMap<>();
    angVel.put("Radians/second (rad/s)", 1.0); // SI BASE
    angVel.put("Radians/minute (rad/min)", 60.0);
    angVel.put("Degrees/second (°/s)", 57.2958);
    angVel.put("Degrees/minute (°/min)", 3437.75);
    angVel.put("Revolutions/second (rps)", 0.159155);
    angVel.put("Revolutions/minute (RPM)", 9.5493);
    angVel.put("Hertz (Hz)", 0.159155);
    unitMap.put("Angular Velocity", angVel);

    // 2. ANGULAR ACCELERATION (Base: Radians per second² - rad/s²)
    Map<String, Double> angAcc = new LinkedHashMap<>();
    angAcc.put("Radians/s² (rad/s²)", 1.0); // SI BASE
    angAcc.put("Radians/min²", 3600.0);
    angAcc.put("Degrees/s² (°/s²)", 57.2958);
    angAcc.put("Revolutions/s² (rps²)", 0.159155);
    angAcc.put("Revolutions/min² (rpm²)", 572.958);
    unitMap.put("Angular Acceleration", angAcc);

    // 3. MOMENT OF INERTIA (Base: kg·m²)
    Map<String, Double> inertia = new LinkedHashMap<>();
    inertia.put("kg·m²", 1.0); // SI BASE
    inertia.put("kg·cm²", 10000.0);
    inertia.put("Gram-cm² (g·cm²)", 10000000.0);
    inertia.put("lb·ft²", 23.7304);
    inertia.put("lb·in²", 3417.17);
    inertia.put("oz·in²", 54674.8);
    inertia.put("Slug-ft²", 0.737562);
    unitMap.put("Moment of Inertia", inertia);

    // 4. ANGULAR MOMENTUM (Base: kg·m²/s)
    Map<String, Double> angMom = new LinkedHashMap<>();
    angMom.put("kg·m²/s", 1.0); // SI BASE
    angMom.put("Newton-metre-seconds (N·m·s)", 1.0);
    angMom.put("lb·ft²/s", 23.7304);
    angMom.put("Slug-ft²/s", 0.737562);
    angMom.put("Joule-seconds (J·s)", 1.0);
    unitMap.put("Angular Momentum", angMom);
    }
    private static void initializeRadiationLight() {
    // 1. RADIOACTIVITY (Base: Becquerel - Bq)
    Map<String, Double> radioactivity = new LinkedHashMap<>();
    radioactivity.put("Becquerel (Bq)", 1.0); // SI BASE (1 decay/s)
    radioactivity.put("Kilobecquerel (kBq)", 0.001);
    radioactivity.put("Megabecquerel (MBq)", 1.0E-6);
    radioactivity.put("Gigabecquerel (GBq)", 1.0E-9);
    radioactivity.put("Curie (Ci)", 2.7027E-11);
    radioactivity.put("Millicurie (mCi)", 2.7027E-8);
    radioactivity.put("Microcurie (μCi)", 2.7027E-5);
    radioactivity.put("Rutherford (Rd)", 1.0E-6);
    unitMap.put("Radioactivity", radioactivity);

    // 2. ABSORBED DOSE (Base: Gray - Gy)
    Map<String, Double> absorbedDose = new LinkedHashMap<>();
    absorbedDose.put("Gray (Gy)", 1.0); // SI BASE
    absorbedDose.put("Milligray (mGy)", 1000.0);
    absorbedDose.put("Rad (rd)", 100.0);
    absorbedDose.put("Millirad (mrd)", 100000.0);
    absorbedDose.put("Sievert (Sv)", 1.0); // Equivalent dose (simplified for 1:1 ratio)
    absorbedDose.put("Rem", 100.0);
    unitMap.put("Absorbed Dose", absorbedDose);

    // 3. LUMINOUS INTENSITY (Base: Candela - cd)
    Map<String, Double> lumInt = new LinkedHashMap<>();
    lumInt.put("Candela (cd)", 1.0); // SI BASE
    lumInt.put("Millicandela (mcd)", 1000.0);
    lumInt.put("Kilocandela (kcd)", 0.001);
    lumInt.put("Hefnerkerze (HK)", 1.107); // Historical German unit
    unitMap.put("Luminous Intensity", lumInt);

    // 4. LUMINOUS FLUX (Base: Lumen - lm)
    Map<String, Double> lumFlux = new LinkedHashMap<>();
    lumFlux.put("Lumen (lm)", 1.0); // SI BASE
    lumFlux.put("Millilumen (mlm)", 1000.0);
    lumFlux.put("Kilolumen (klm)", 0.001);
    lumFlux.put("Watt (at 555nm)", 0.001496); // Ideal luminous efficacy
    unitMap.put("Luminous Flux", lumFlux);

    // 5. ILLUMINANCE (Base: Lux - lx)
    Map<String, Double> illuminance = new LinkedHashMap<>();
    illuminance.put("Lux (lx)", 1.0); // SI BASE
    illuminance.put("Foot-candle (fc)", 0.092903);
    illuminance.put("Phot (ph)", 0.0001);
    illuminance.put("Nox (nx)", 1000.0);
    unitMap.put("Illuminance", illuminance);
    }
    private static void initializeMathComputing() {
    // 1. ANGLE CONVERSION (Base: Radians - rad)
    Map<String, Double> angle = new LinkedHashMap<>();
    angle.put("Radians (rad)", 1.0); // SI BASE
    angle.put("Degrees (°)", 57.2957795);
    angle.put("Gradians (grad)", 63.6619772);
    angle.put("Milliradians (mrad)", 1000.0);
    angle.put("Arcminutes (min)", 3437.74677);
    angle.put("Arcseconds (sec)", 206264.806);
    angle.put("Circles/Revolutions", 0.1591549);
    angle.put("Sextants", 0.9549296);
    unitMap.put("Angle", angle);

    // 2. DATA STORAGE (Base: Bytes - B)
    Map<String, Double> data = new LinkedHashMap<>();
    data.put("Bytes (B)", 1.0); // BASE
    // Binary Prefixes (Power of 1024)
    data.put("Kilobytes (KB)", 1.0/1024.0);
    data.put("Megabytes (MB)", 1.0/Math.pow(1024, 2));
    data.put("Gigabytes (GB)", 1.0/Math.pow(1024, 3));
    data.put("Terabytes (TB)", 1.0/Math.pow(1024, 4));
    data.put("Petabytes (PB)", 1.0/Math.pow(1024, 5));
    // Bits
    data.put("Bits (b)", 8.0);
    data.put("Nibbles", 2.0);
    // Decimal Prefixes (Power of 1000 - used by disk makers)
    data.put("Kilobytes (decimal)", 0.001);
    data.put("Megabytes (decimal)", 1.0E-6);
    unitMap.put("Data Storage", data);

    // 3. DATA RATE (Base: bits per second - bps)
    Map<String, Double> dataRate = new LinkedHashMap<>();
    dataRate.put("bits/sec (bps)", 1.0); // BASE
    dataRate.put("Kilobits/sec (kbps)", 0.001);
    dataRate.put("Megabits/sec (Mbps)", 1.0E-6);
    dataRate.put("Gigabits/sec (Gbps)", 1.0E-9);
    dataRate.put("Terabits/sec (Tbps)", 1.0E-12);
    dataRate.put("Bytes/sec (B/s)", 0.125);
    dataRate.put("Megabytes/sec (MB/s)", 1.25E-7);
    unitMap.put("Data Rate", dataRate);

    // 4. NUMERALS (Logic Dummy Map)
    // These need the special String-parsing logic in your performConversion()
    Map<String, Double> numerals = new LinkedHashMap<>();
    numerals.put("Decimal (Base 10)", 1.0);
    numerals.put("Binary (Base 2)", 0.0);
    numerals.put("Hexadecimal (Base 16)", 0.0);
    numerals.put("Octal (Base 8)", 0.0);
    numerals.put("Roman Numerals", 0.0);
    unitMap.put("Numerals", numerals);
    }
    private static long romanToDecimal(String s) {
    java.util.Map<Character, Integer> va = new java.util.HashMap<>();
    va.put('I', 1); va.put('V', 5); va.put('X', 10); va.put('L', 50);
    va.put('C', 100); va.put('D', 500); va.put('M', 1000);
    
    long total = 0;
    s = s.toUpperCase();
    for (int i = 0; i < s.length(); i++) {
        int s1 = va.getOrDefault(s.charAt(i), 0);
        if (i + 1 < s.length()) {
            int s2 = va.getOrDefault(s.charAt(i + 1), 0);
            if (s1 >= s2) total += s1;
            else { total += s2 - s1; i++; }
        } else total += s1;
    }
    return total;
    }

    private static String decimalToRoman(long n) {
    return "I"; // Placeholder
    }
    class MathParser {
    public static double eval(final String str, double x, double y, double t) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions & variables
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (func.equals("x")) x = xVal;
                    else if (func.equals("y")) x = yVal;
                    else if (func.equals("t")) x = tVal;
                    else {
                        x = parseFactor();
                        if (func.equals("sin")) x = Math.sin(x);
                        else if (func.equals("cos")) x = Math.cos(x);
                        else if (func.equals("tan")) x = Math.tan(x);
                        else if (func.equals("sqrt")) x = Math.sqrt(x);
                        else if (func.equals("abs")) x = Math.abs(x);
                        else throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
            
            // Pass the variables into the parser scope
            double xVal = x, yVal = y, tVal = t;
        }.parse();
    }
    }
    static void updateDisplay(String text) {
    display.setText(text);
    
    // Create the alignment attribute
    SimpleAttributeSet rightAlign = new SimpleAttributeSet();
    StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
    
    // Force the document to use it
    StyledDocument doc = display.getStyledDocument();
    doc.setParagraphAttributes(0, doc.getLength(), rightAlign, false);
    
    // This makes sure Java actually draws the new text
    display.repaint();
    display.revalidate();
    }
    private static JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text));
        return panel;
    }

    private static JButton createMenuButton(String text) {
    JButton btn = new JButton(text);
    btn.setMaximumSize(new Dimension(220, 45));
    btn.setForeground(Color.WHITE);
    btn.setBackground(MENU_BG);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Simple hover effect
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(HOVER_COLOR); }
        public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(MENU_BG); }
    });

    btn.addActionListener(e -> {
        // Switch modes here (e.g., enable/disable scientific buttons)
        sideMenu.setVisible(false); // Auto-hide after selection
    });

    return btn;
    }

    // Inner class for handling side menu button clicks
    private static class SideMenuListener implements ActionListener {
        private String modeName;
        private JPanel sideMenu;

        public SideMenuListener(String modeName, JPanel sideMenu) {
            this.modeName = modeName;
            this.sideMenu = sideMenu;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            // Update the current mode state
            currentMode.setLength(0); // Clear existing content
            currentMode.append(modeName);

            // Switch the main calculator panel to the selected mode
            cardLayout.show(cardPanel, modeName);

            // Hide the side menu after a selection is made
            sideMenu.setVisible(false);

            // ... other logic to update mode-specific elements ...
            // e.g., if (modeName.equals("Scientific")) { ... enable special buttons ... }
        }
    }
    private static void setupSideMenu() {

    sideMenu = new JPanel();
    sideMenu.setLayout(null);
    sideMenu.setBackground(MENU_BG);
    sideMenu.setBounds(0, 0, 240, 600);
    sideMenu.setVisible(false);
    sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

    String[] modes = {
        "Standard", "Functions", "Graphing",
        "Matrix", "Programmer", "Converter", "Settings"
    };

    int yPos = 60;

    for (String mode : modes) {

        JButton btn = createMenuButton(mode, yPos);

        btn.addActionListener(e -> {

            buttonsPanel.removeAll();   // ✅ always clear central area

            if (mode.equals("Standard")) {
                resizeCalculator(400, 600);
                buttonsPanel.removeAll();
                buttonsPanel.add(createBasicPanel(), BorderLayout.CENTER);
            }

            else if (mode.equals("Functions")) {

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(new Color(45,45,45));
                tabbedPane.setForeground(Color.WHITE);
                resizeCalculator(450, 600);
                
                tabbedPane.add("Sci", createScientificPanel());
                tabbedPane.add("aA", createAlphaPanel());
                tabbedPane.add("Func", createFuncPanel());
                tabbedPane.add("Const", createConstantPanel());
                tabbedPane.add("Trig", createTrigPanel());
                tabbedPane.add("Adv", createAdvancedPanel());
                tabbedPane.add("∫", createCalculus1Panel());
                tabbedPane.add("Stat", createStatisticsPanel());

                buttonsPanel.add(tabbedPane, BorderLayout.CENTER);
            }

            else if (mode.equals("Graphing")) {

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(new Color(45,45,45));
                tabbedPane.setForeground(Color.WHITE);
                resizeCalculator(550, 600);

                tabbedPane.add("2D", createGraphPanel());
                tabbedPane.add("3D", create3DGraphPanel());
                tabbedPane.add("∫", createCalculus2Panel());

                buttonsPanel.add(tabbedPane, BorderLayout.CENTER);
            }

            else if (mode.equals("Programmer")) {
                buttonsPanel.add(createProgrammingPanel(), BorderLayout.CENTER);
                resizeCalculator(400, 600);
            }

            else if (mode.equals("Converter")) {
                buttonsPanel.add(createConversionPanel(), BorderLayout.CENTER);
                resizeCalculator(550, 600);
            }
            else if (mode.equals("Matrix")) {
                buttonsPanel.add(createMatrixPanel(), BorderLayout.CENTER);
                resizeCalculator(450, 600);
            }
            else if (mode.equals("Settings")) {
            
                if (currentButtonGrid != null) {
                    mainPanel.remove(currentButtonGrid);
                }
            
                JPanel settingsPanel = createSettingsPanel();
                settingsPanel.setBounds(0, 160, mainPanel.getWidth(), mainPanel.getHeight() - 160);
            
                currentButtonGrid = settingsPanel;
                mainPanel.add(settingsPanel);

            }

            buttonsPanel.revalidate();
            buttonsPanel.repaint();

            sideMenu.setVisible(false);
            isMenuOpen = false;
        });

        sideMenu.add(btn);
        yPos += 45;
    }
    }
    

    private static JButton createMenuButton(String text, int y) {
    JButton btn = new JButton(text);
    btn.setBounds(5, y, 230, 40);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setForeground(Color.WHITE);
    btn.setBackground(MENU_BG);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);

    // Hover effect to match image 30a9a4
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(HOVER_COLOR); }
        public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(MENU_BG); }
    });

    btn.addActionListener(e -> {
        sideMenu.setVisible(false);
        isMenuOpen = false;
        // Add your logic here to switch panels/visibility of buttons
    });

    return btn;
    }

    public static void updateLivePreview(double x){
        previewLabel.setText("= "+x);
        previewLabel.setForeground(new Color(150, 150, 150));
        previewLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    private static void resizeCalculator(int targetWidth, int targetHeight) {

    Dimension startSize = frame.getSize();
    int startWidth = startSize.width;
    int startHeight = startSize.height;

    int steps = 20;          // More steps = smoother
    int delay = 10;          // Milliseconds between frames

    int deltaW = (targetWidth - startWidth) / steps;
    int deltaH = (targetHeight - startHeight) / steps;

    Timer timer = new Timer(delay, null);

    timer.addActionListener(new ActionListener() {
        int currentStep = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentStep < steps) {

                int newWidth = frame.getWidth() + deltaW;
                int newHeight = frame.getHeight() + deltaH;

                frame.setSize(newWidth, newHeight);

                Insets insets = frame.getInsets();
                int width = frame.getWidth() - insets.left - insets.right;
                int height = frame.getHeight() - insets.top - insets.bottom;

                layeredPane.setBounds(0, 0, width, height);
                mainPanel.setBounds(0, 0, width, height);

                displayPanel.setBounds(0, 0, width, 160);
                buttonsPanel.setBounds(0, 160, width, height - 160);

                sideMenu.setBounds(0, 0, 240, height);

                frame.revalidate();
                frame.repaint();

                currentStep++;
            } else {
                frame.setSize(targetWidth, targetHeight);
                timer.stop();
            }
        }
    });

    timer.start();
    }
    static JPanel createSettingsPanel() {

    JPanel panel = new JPanel(null);
    panel.setBounds(0, 0, 240, 600);
    panel.setBackground(currentTheme.background);

    JLabel title = new JLabel("Settings");
    title.setBounds(20, 20, 200, 30);
    title.setFont(new Font("Segoe UI", Font.BOLD, 18));
    title.setForeground(currentTheme.foreground);
    panel.add(title);

    JLabel themeLabel = new JLabel("Theme");
    themeLabel.setBounds(20, 80, 200, 25);
    themeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    themeLabel.setForeground(currentTheme.foreground);
    panel.add(themeLabel);

    String[] themes = {
        "Sunset", "Night", "Rainforest", "Suburbs", "Sunrise",
        "Velvet", "Sky", "Forest", "Sunlight", "Standard"
    };

    JComboBox<String> themeDropdown = new JComboBox<>(themes);
    themeDropdown.setBounds(20, 110, 200, 35);
    themeDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    themeDropdown.setFocusable(false);
    panel.add(themeDropdown);

    // Set current selection
    themeDropdown.setSelectedItem(currentTheme.name);

    themeDropdown.addActionListener(e -> {
        String selected = (String) themeDropdown.getSelectedItem();

        currentTheme = themeMap.get(selected);

        // Update settings panel colors immediately
        panel.setBackground(currentTheme.background);
        title.setForeground(currentTheme.foreground);
        themeLabel.setForeground(currentTheme.foreground);

        refreshAllPanels();
    });

    return panel;
    }
    static class Theme {
    String name;
    Color background;
    Color foreground;
    Color regularButton;
    Color functionButton;
    Color memoryDegColor;
    Color equalsColor;
    Color clearColor;

    Theme( Color bg, Color fg,
          Color regular, Color func, Color clear,
          Color memDeg, Color equals
          ) {

        this.name = name;
        this.background = bg;
        this.foreground = fg;
        this.regularButton = regular;
        this.functionButton = func;
        this.memoryDegColor = memDeg;
        this.equalsColor = equals;
        this.clearColor = clear;
    }
    }
    static void refreshAllPanels() {

    // Update main background
    mainPanel.setBackground(currentTheme.background);

    // Update display
    display.setBackground(currentTheme.background);
    display.setForeground(currentTheme.foreground);

    modeLabel.setForeground(currentTheme.memoryDegColor);

    // Recursively refresh buttons
    refreshComponentTree(mainPanel);

    mainPanel.revalidate();
    mainPanel.repaint();
    }
    static void refreshComponentTree(Container container) {

    for (Component c : container.getComponents()) {

        if (c instanceof JButton btn) {
            styleButtonByText(btn, btn.getText());
        }

        if (c instanceof Container child) {
            refreshComponentTree(child);
        }
    }
    }
    static void styleButtonByText(JButton button, String text) {

    button.setFont(new Font("Segoe UI", Font.BOLD, 20));
    button.setFocusPainted(false);
    button.setBorderPainted(false);

    // ===== DO NOT CHANGE NUMBERS =====
    if (text.matches("\\d") || text.equals(".")) {
        button.setBackground(currentTheme.regularButton);
        button.setForeground(currentTheme.foreground);
        return;
    }

    // ===== EQUALS =====
    if (text.equals("=")) {
        button.setBackground(currentTheme.equalsColor);
        button.setForeground(currentTheme.foreground);
        return;
    }

    // ===== CLEAR BUTTONS (foreground only special) =====
    if (text.equals("C") || text.equals("CE")) {
        button.setBackground(currentTheme.functionButton);
        button.setForeground(currentTheme.clearColor);
        return;
    }

    // ===== MEMORY + DEG (foreground only special) =====
    if (text.equals("M+") || text.equals("M-") ||
        text.equals("MR") || text.equals("MC") ||
        text.equals("DEG")) {

        button.setBackground(currentTheme.functionButton);
        button.setForeground(currentTheme.memoryDegColor);
        return;
    }

    // ===== ALL OTHER FUNCTION BUTTONS =====
    button.setBackground(currentTheme.functionButton);
    button.setForeground(currentTheme.foreground);
    }

    
}
