import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;  
import java.awt.geom.Path2D;      
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
import java.util.Arrays;
import java.util.TreeMap;

public class Arithmos {
    private static boolean isDeveloping = false;    //Turn true while developing the program
    private static Arithmos instance;
    static JFrame frame = new JFrame("Arithmos");
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
    static List<JButton> alphaButtons = new ArrayList<>();
    static List<JButton> greekButtons = new ArrayList<>();
    static JTabbedPane tabbedPane;
    static JTextField inputField = new JTextField();
    static GraphCanvas canvas;
    static Color[] graphColors = {
        new Color(0, 255, 170), // Mint
    new Color(255, 100, 100), // Red
    new Color(100, 200, 255), // Blue
    new Color(255, 200, 50),  // Yellow
    new Color(200, 100, 255)  // Purple
    };
    static int colorIndex = 0;
    static DefaultListModel<EquationEntry> model = new DefaultListModel<>();
    static JList<EquationEntry> list = new JList<>(model);
    // Class-level variables (Fields)
    private static JLabel hexLbl, decLbl, octLbl, binLbl;
    private static JComboBox<String> categoryBox, fromUnitBox, toUnitBox;
    private static JTextField convInput;
    private static JLabel convResult;
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
        new Color(230, 230, 230),     //bgColor
        Color.BLACK,            //foreground
        new Color(221,  221, 221),    //regularButton
        new Color(239, 239, 239),    //functionButton
        new Color(166, 73, 73),    //clearColor
        new Color(73, 79, 166),    //memoryDegColor
        new Color(166, 73, 73)    //equalsColor

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
    private static boolean isMenuOpen = false;
    private static JPanel currentButtonGrid = null;
    static JPanel buttonsPanel;
    private static JLabel panelTitleLabel; // The label next to the ham button
    // Add these at the top with your other private variables
    private static String topStatusText = ""; 
    private static boolean isTangMode = false;
    private static boolean isShadeMode = false;
    private static double tangM, tangC;
    private static JTextField activePhysicsField = null;
    private static JTextField activeChemField = null;
    private static JComboBox<String> physicsSelector;
    private static JTextField pf1, pf2, pf3;
    private static JComboBox<String> pu1, pu2, pu3;
    private static JLabel pl1, pl2, pl3;
    private static int activeFieldIdx = 0;
    private static boolean isInternalUpdate = false;
    private static ContourPanel contourPanel = new ContourPanel();
    private static Map<String, PhysData> physicsRegistry = new TreeMap<>();
    private static JComboBox<String> chemSelector;
    private static JTextField cf1, cf2, cf3;
    private static JComboBox<String> cu1, cu2, cu3;
    private static JLabel cl1, cl2, cl3;
    private static int activeChemFieldIdx = 0;
    private static boolean isChemInternalUpdate = false;
    private static Map<String, ChemData> chemRegistry = new TreeMap<>();
    private static final Map<String, FormulaData> chemistryFormulas = new TreeMap<>();
    public static boolean isTangent = false;
    public static double tangX = 0;
    
    public Arithmos(){
        instance = this;
    }
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
    initializeChemistryData(); // Make sure this runs!
    initializePhysicsData();
    setupSideMenu();
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(450, 600);
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
    displayPanel.setBackground(currentTheme.bgColor);

    display.setBackground(currentTheme.bgColor);
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
    modeLabel.setForeground(currentTheme.memoryDegColor);
    modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    modeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); 
    // slight left padding to align under hamburger

    previewLabel.setForeground(currentTheme.foreground);
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
    buttonsPanel.setBackground(currentTheme.bgColor);
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
    hamburgerBtn.setBackground(currentTheme.bgColor);

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
    panelTitleLabel = new JLabel("STANDARD"); // Default title
    panelTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    panelTitleLabel.setForeground(currentTheme.foreground);
    panelTitleLabel.setBounds(60, 5, 200, 40); 

    // Add it to the same layer as the ham button

    // ===== LAYER ASSEMBLY =====
    layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
    layeredPane.add(sideMenu, JLayeredPane.PALETTE_LAYER);
    layeredPane.add(hamburgerBtn, JLayeredPane.DRAG_LAYER);
    layeredPane.add(panelTitleLabel, JLayeredPane.PALETTE_LAYER);
    

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
            btn.setBackground(currentTheme.functionButton); // Use your mint/green focus color
            btn.setForeground(currentTheme.foreground);
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(currentTheme.regularButton);
            btn.setForeground(currentTheme.foreground);
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
        button.setForeground(currentTheme.bgColor);
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
                modeLabel.setForeground(isDegreeMode ? currentTheme.clearColor : currentTheme.memoryDegColor);
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
        if (text.equals("MR")) { updateDisplay(cur + memory); return; }
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
    "acoth", "assech", "acsch",
    "lim", "tang", "shade", "grf", "perp", "cont", "vect",
    "abs", "ceil", "floor", "round",
    "max", "min", "mod",
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
    
    if (!cur.contains("int(") && 
    !cur.contains("diff(") && 
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

        if (input.startsWith("tang(")) {
    try {
        // Extract content between tang( and )
        String content = raw.substring(raw.indexOf("(") + 1, raw.lastIndexOf(")")).trim();
        String[] parts = content.split(",");
        
        String math = parts[0].trim();
        double xTarget = Double.parseDouble(parts[1].trim());

        if (canvas != null && model != null) {
            EquationEntry en = new EquationEntry(math, graphColors[colorIndex++ % graphColors.length]);
            isTangent = true;
            tangX = xTarget;

            canvas.entries.add(en);
            model.addElement(en);
            canvas.repaint();
            
            display.setText("Tangent Applied at x=" + xTarget);
            tabbedPane.setSelectedIndex(5);
            return;
        }
    } catch (Exception ex) {
        display.setText("Error: Use tang(f(x), x)");
    }
    }
        
        if (input.startsWith("line(") || input.startsWith("dist(")) {
    // Regex to find all numbers (including decimals and negatives)
    Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?");
    Matcher m = p.matcher(input);
    double[] pts = new double[4];
    int count = 0;
    while (m.find() && count < 4) pts[count++] = Double.parseDouble(m.group());
    }
    
    // 1. Check for the limit command FIRST
    if (input.startsWith("lim(")) {
    try {
        // 1. Get everything between "lim(" and the "shade" keyword
        // Input: lim(0, 3.14)shade(sin(x))
        String parts[] = raw.split("shade"); 
        
        // 2. Process the Limit Part: "lim(0, 3.14)"
        String limitContent = parts[0].substring(parts[0].indexOf("(") + 1, parts[0].lastIndexOf(")"));
        String[] limits = limitContent.split(",");
        
        if(limits[0].contains("pi") || limits[0].contains("π")){
            limits[0] = "3.142";
        }
        if(limits[0].contains("e")){
            limits[0] = "2.718";
        }
        if(limits[1].contains("pi") || limits[1].contains("π")){
            limits[1] = "3.142";
        }
        if(limits[1].contains("e")){
            limits[1] = "2.718";
        }
        double start = Double.parseDouble(limits[0].trim());
        double end = Double.parseDouble(limits[1].trim());

        // 3. Process the Math Part: everything inside the second set of parens
        // parts[1] is "(sin(x))"
        String mathPart = parts[1].trim();
        String math = mathPart.substring(mathPart.indexOf("(") + 1, mathPart.lastIndexOf(")"));

        if (canvas != null && model != null) {
            EquationEntry en = new EquationEntry(math, graphColors[colorIndex++ % graphColors.length]);
            en.isShaded = true;
            en.isLimitShade = true;
            en.limStart = start;
            en.limEnd = end;

            canvas.entries.add(en);
            model.addElement(en);
            canvas.repaint();
            
            display.setText("Check Graph"); // Match your original style
            tabbedPane.setSelectedIndex(5); 
            return;
        }
    } catch (Exception ex) {
        display.setText("Check Graph");
        // Print the error to your IDE console so we can see the exact line it fails on
        ex.printStackTrace(); 
    }
    }
    

    // --- B. CALCULUS (LIMITS, DERIVATIVES, INTEGRALS) ---
    String calcInput = input.toLowerCase();
    // 1. THE CALCULUS MASTER CHECK (Checks for 'lim')
    if (input.toLowerCase().contains("lim(")) {
    try {
        String cleanInput = input.toLowerCase().replace(" ", "");
        
        // Extract the limit content: lim(0,1) or lim(5)
        int limStart = cleanInput.indexOf("lim(") + 4;
        int limEnd = cleanInput.indexOf(")", limStart);
        String limitStr = cleanInput.substring(limStart, limEnd);

        // --- SUB-ROUTING: Is it an Integral? ---
        if (cleanInput.contains("int") || cleanInput.contains("∫")) {
            String[] limits = limitStr.split(",");
            double a = evaluateExpression(limits[0].trim(), 0);
            double b = evaluateExpression(limits[1].trim(), 0);

            int intStart = cleanInput.contains("int") ? cleanInput.indexOf("int") + 3 : cleanInput.indexOf("∫") + 1;
            String math = cleanInput.substring(intStart);
            if (math.startsWith("(")) math = math.substring(1, math.lastIndexOf(")"));
            math = math.replace("dx", "");

            String symbolic = integrate(math).replace(" + C", "").trim();
            double resB = solveAtPoint(symbolic, b);
            double resA = solveAtPoint(symbolic, a);
            updateDisplay(formatCoeff(resB - resA));
        } 
        // --- SUB-ROUTING: Is it a Derivative? ---
        else if (cleanInput.contains("diff") || cleanInput.contains("d/dx")) {
            double c = evaluateExpression(limitStr.trim(), 0);
            
            int diffStart = cleanInput.contains("diff") ? cleanInput.indexOf("diff") + 4 : cleanInput.indexOf("d/dx") + 4;
            String math = cleanInput.substring(diffStart);
            if (math.startsWith("(")) math = math.substring(1, math.lastIndexOf(")"));

            String symbolic = derive(math).trim();
            updateDisplay(formatCoeff(solveAtPoint(symbolic, c)));
        }
    } catch (Exception e) {
        updateDisplay("Numeric Calc Error");
    }
    }

    // 2. SYMBOLIC DIFFERENTIATION (No 'lim')
    else if (input.toLowerCase().contains("diff") || input.toLowerCase().contains("d/dx")) {
    try {
        String math = input.substring(input.indexOf("(") + 1, input.lastIndexOf(")")).trim();
        updateDisplay(derive(math));
    } catch (Exception e) {
        updateDisplay("Symbolic Diff Error");
    }
    }

    // 3. SYMBOLIC INTEGRATION (No 'lim')
    else if (input.toLowerCase().contains("int") || input.toLowerCase().contains("∫")) {
    try {
        String math = "";
        
        // Handle cases with or without brackets: int(sin(x)) or int sin(x)
        if (input.contains("(")) {
            int start = input.indexOf("(") + 1;
            // Use lastIndexOf to find the end, but fallback to length if user forgot the closing bracket
            int end = input.contains(")") ? input.lastIndexOf(")") : input.length();
            math = input.substring(start, end).trim();
        } else {
            // If no bracket, take everything after "int" or "∫"
            int start = input.toLowerCase().contains("int") ? input.indexOf("int") + 3 : input.indexOf("∫") + 1;
            math = input.substring(start).trim();
        }

        // Standardize: remove 'dx' if the user typed it
        math = math.toLowerCase().replace("dx", "").trim();

        // Call your library
        String result = integrate(math);
        
        // If result is null or empty, provide a fallback
        if (result == null || result.isEmpty()) {
            updateDisplay("Unable to integrate");
        } else {
            updateDisplay(result);
        }

    } catch (Exception e) {
        // This is what you were seeing! Let's make it more descriptive for debugging
        e.printStackTrace();
        updateDisplay("Syntax Error");
    }
    }
        
        
        // --- C. STANDARD ARITHMETIC (THE FIX FOR 3+3) ---
        else {
            double result = evaluateExpression(input, 0.0);
            updateDisplay(formatCoeff(result));
            lastAnswer = result;
        }

        // Final Touch: Clear the live preview after solving
        previewLabel.setText(" ");

    
    }catch (Exception e) {
        updateDisplay("Check Graph");
    }
    }
    
    private static double solveAtPoint(String symbolic, double xVal) {
    // 1. CLEANING
    // Remove spaces and lowercase everything to prevent match errors
    String evalStr = symbolic.toLowerCase().replace(" ", "");

    // 2. TRIGONOMETRY LAYER
    // We solve these manually using Java's Math library before the evaluator sees them
    if (evalStr.contains("sin(x)")) evalStr = evalStr.replace("sin(x)", "(" + Math.sin(xVal) + ")");
    if (evalStr.contains("cos(x)")) evalStr = evalStr.replace("cos(x)", "(" + Math.cos(xVal) + ")");
    if (evalStr.contains("tan(x)")) evalStr = evalStr.replace("tan(x)", "(" + Math.tan(xVal) + ")");
    
    // Reciprocal Trig
    if (evalStr.contains("sec(x)")) evalStr = evalStr.replace("sec(x)", "(" + (1.0 / Math.cos(xVal)) + ")");
    if (evalStr.contains("csc(x)")) evalStr = evalStr.replace("csc(x)", "(" + (1.0 / Math.sin(xVal)) + ")");
    if (evalStr.contains("cot(x)")) evalStr = evalStr.replace("cot(x)", "(" + (1.0 / Math.tan(xVal)) + ")");

    // 3. HYPERBOLIC LAYER
    if (evalStr.contains("sinh(x)")) evalStr = evalStr.replace("sinh(x)", "(" + Math.sinh(xVal) + ")");
    if (evalStr.contains("cosh(x)")) evalStr = evalStr.replace("cosh(x)", "(" + Math.cosh(xVal) + ")");
    if (evalStr.contains("tanh(x)")) evalStr = evalStr.replace("tanh(x)", "(" + Math.tanh(xVal) + ")");

    // 4. LOGARITHMS & SPECIAL CASES
    // Handle ln|x| or log(x)
    if (evalStr.contains("ln|x|") || evalStr.contains("log(x)")) {
        evalStr = evalStr.replace("ln|x|", "(" + Math.log(Math.abs(xVal)) + ")")
                         .replace("log(x)", "(" + Math.log(xVal) + ")");
    }

    // 5. POWERS & CONSTANTS
    // Replace powers FIRST so we don't accidentally replace the 'x' in 'x^2' early
    evalStr = evalStr.replace("x^2", "(" + (xVal * xVal) + ")")
                     .replace("x^3", "(" + (xVal * xVal * xVal) + ")")
                     .replace("pi", String.valueOf(Math.PI))
                     .replace("e", String.valueOf(Math.E));

    // 6. VARIABLE INJECTION
    // Finally, replace all remaining standalone 'x' characters
    evalStr = evalStr.replace("x", "(" + xVal + ")");

    // 7. FINAL SYNTAX CLEANUP (The "Parser Protectors")
    // Fix Unary Minus: If it starts with -0.5, make it 0-0.5
    if (evalStr.startsWith("-")) {
        evalStr = "0" + evalStr;
    }
    
    // Fix Implicit Multiplication: e.g., "2(6.0)" becomes "2*(6.0)"
    evalStr = evalStr.replaceAll("(\\d)(\\()", "$1*$2");
    
    // Fix Double Negatives: e.g., "0--0.5" becomes "0+0.5"
    evalStr = evalStr.replace("--", "+");

    // 8. FINAL EVALUATION
    // Pass the "Number-Only" string to your existing expression evaluator
    return evaluateExpression(evalStr, xVal);
    }
    
    private static String extractNested(String s) {
    int start = s.indexOf("(") + 1;
    int end = s.lastIndexOf(")");
    if (start > 0 && end > start) {
        return s.substring(start, end);
    }
    return s; // Fallback
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
                    if (func.equals("L")){return 0.110001;}
                    if (func.equals("R")){return 262537412640768743.999;}
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
                        case "sum": 
                        case "Σ": {
                            double total = arg; // Start with the first number already parsed
                            while (eat(',')) {
                                total += parseExpression(); // Add every subsequent number found after a comma
                        }
                        v = total;
                        } break;
                        
                        case "prod":
                        case "Π": {
                            double product = arg; // Start with the first number
                                while (eat(',')) {
                                    product *= parseExpression(); // Multiply by every subsequent number
                                }
                            v = product;
                            } break;
                        
                        case "mean": {
                            List<Double> mData = new ArrayList<>();
                            mData.add(arg); 
                            while (eat(',')) {
                                mData.add(parseExpression());
                            }
                            double mSum = 0;
                            for (double d : mData) mSum += d;
                            v = mSum / mData.size();
                        } break;
                            
                            
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

    // --- HELPER TO CONVERT USER INPUT TO ARRAY ---
    public static double[] parseUserSet(String input) {
        // Removes any brackets if the user typed [1, 56, 32]
        String clean = input.replaceAll("[\\[\\](){}]", "");
        
        // Split by comma or space
        String[] parts = clean.split("[,\\s]+");
        
        return Arrays.stream(parts)
                     .mapToDouble(Double::parseDouble)
                     .toArray();
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

    public static String derive(String input) {
    // 1. Deep Normalization
    String expr = input.toLowerCase().trim()
                       .replaceAll("\\s+", "") // Remove spaces
                       .replaceAll("^d/dx\\(|^diff\\(", "").replaceAll("\\)$", ""); // Strip wrappers
    
    if (expr.isEmpty()) return "0";

    try {
        // --- 1. CONSTANTS ---
        if (expr.matches("-?\\d+(\\.\\d+)?") || expr.equals("pi") || expr.equals("e")) {
            return "0";
        }

        // --- 2. THE POWER RULE (ax^n -> (a*n)x^(n-1)) ---
        Pattern p = Pattern.compile("(-?\\d*\\.?\\d*)x(?:\\^?(-?\\d+\\.?\\d*))?");
        Matcher m = p.matcher(expr);
        if (m.matches()) {
            String coeffStr = m.group(1);
            String expStr = m.group(2);
            
            double a = (coeffStr.isEmpty() || coeffStr.equals("+")) ? 1 : 
                       (coeffStr.equals("-") ? -1 : Double.parseDouble(coeffStr));
            
            if (!expr.contains("x")) return "0"; // Safety check for constants
            
            double n = (expStr == null) ? 1 : Double.parseDouble(expStr);
            if (n == 0) return "0";
            if (n == 1) return formatCoeff(a);
            
            double newA = a * n;
            double newN = n - 1;
            
            String powerPart = (newN == 1) ? "x" : "x^" + formatCoeff(newN);
            return formatCoeff(newA) + powerPart;
        }

        // --- 3. EXPONENTIALS & LOGARITHMS ---
        if (expr.equals("e^x") || expr.equals("exp(x)")) return "e^x";
        if (expr.equals("ln(x)")) return "1/x";
        if (expr.matches("\\d+\\^x")) {
            String base = expr.substring(0, expr.indexOf("^"));
            return expr + "*ln(" + base + ")";
        }

        // --- 4. TRIGONOMETRY ---
        switch (expr) {
            case "sin(x)": return "cos(x)";
            case "cos(x)": return "-sin(x)";
            case "tan(x)": return "sec^2(x)";
            case "cot(x)": return "-csc^2(x)";
            case "sec(x)": return "sec(x)tan(x)";
            case "csc(x)": return "-csc(x)cot(x)";
        }

        // --- 5. HYPERBOLIC & INVERSE TRIG ---
        if (expr.equals("sinh(x)")) return "cosh(x)";
        if (expr.equals("cosh(x)")) return "sinh(x)";
        if (expr.equals("arcsin(x)")) return "1/sqrt(1-x^2)";
        if (expr.equals("arccos(x)")) return "-1/sqrt(1-x^2)";
        if (expr.equals("arctan(x)")) return "1/(1+x^2)";

        // --- 6. THE CHAIN RULE (Internal Multipliers) ---
        // Handles: sin(5x), e^(2x), cos(ax+b)
        Pattern comp = Pattern.compile("(sin|cos|tan|exp|e\\^)\\((-?\\d*\\.?\\d*)x([+-]\\d+)?\\)");
        Matcher cm = comp.matcher(expr);
        if (cm.find()) {
            String func = cm.group(1);
            double k = cm.group(2).isEmpty() ? 1 : 
                      (cm.group(2).equals("-") ? -1 : Double.parseDouble(cm.group(2)));
            String inner = cm.group(2) + "x" + (cm.group(3) == null ? "" : cm.group(3));
            
            if (func.equals("sin")) return formatCoeff(k) + "cos(" + inner + ")";
            if (func.equals("cos")) return "-" + formatCoeff(k) + "sin(" + inner + ")";
            if (func.equals("tan")) return formatCoeff(k) + "sec^2(" + inner + ")";
            if (func.contains("e") || func.equals("exp")) return formatCoeff(k) + "e^(" + inner + ")";
        }

        // --- 7. SUM/DIFFERENCE RULE (Recursive) ---
        if (expr.contains("+") || (expr.contains("-") && !expr.startsWith("-"))) {
            String[] terms = expr.split("(?=[+-])");
            StringBuilder sb = new StringBuilder();
            for (String term : terms) {
                String cleanTerm = term.startsWith("+") ? term.substring(1) : term;
                String deriv = derive(cleanTerm).trim();
                if (!deriv.equals("0")) {
                    sb.append(term.startsWith("-") ? " - " : " + ").append(deriv);
                }
            }
            String result = sb.toString().trim();
            if (result.startsWith("+")) result = result.substring(1).trim();
            return result.isEmpty() ? "0" : result;
        }

    } catch (Exception e) {
        return "diff(" + expr + ")"; 
    }

    return "diff(" + expr + ")"; 
    }

    public static String integrate(String input) {
    // 1. Deep Normalization
    String expr = input.toLowerCase().trim()
                       .replaceAll("\\s+", "") // Remove all spaces
                       .replaceAll("dx$", ""); // Remove trailing dx
    
    if (expr.isEmpty()) return "";

    try {
        // --- 1. SPECIAL FUNCTIONS & CONSTANTS ---
        switch (expr) {
            case "ln(x)": return "x*ln(x) - x + C";
            case "log(x)": return "(x*ln(x) - x)/ln(10) + C";
            case "e^x": case "exp(x)": return "e^x + C";
            case "1/x": case "x^-1": return "ln|x| + C";
            case "pi": case "π": return "πx + C";
            case "e": return "ex + C";
        }

        // --- 2. ADVANCED TRIGONOMETRY ---
        // Basic
        if (expr.equals("sin(x)") || expr.equals("sinx")) return "-cos(x) + C";
        if (expr.equals("cos(x)") || expr.equals("cosx")) return "sin(x) + C";
        if (expr.equals("tan(x)") || expr.equals("tanx")) return "ln|sec(x)| + C";
        if (expr.equals("cot(x)") || expr.equals("cotx")) return "ln|sin(x)| + C";
        if (expr.equals("sec(x)") || expr.equals("secx")) return "ln|sec(x)+tan(x)| + C";
        if (expr.equals("csc(x)") || expr.equals("cscx")) return "-ln|csc(x)+cot(x)| + C";
        
        // Squares (Identities)
        if (expr.matches("sec\\^?2\\(x\\)|sec\\(x\\)\\^2")) return "tan(x) + C";
        if (expr.matches("csc\\^?2\\(x\\)|csc\\(x\\)\\^2")) return "-cot(x) + C";
        if (expr.equals("sin^2(x)") || expr.equals("sin(x)^2")) return "x/2 - sin(2x)/4 + C";
        if (expr.equals("cos^2(x)") || expr.equals("cos(x)^2")) return "x/2 + sin(2x)/4 + C";

        // --- 3. HYPERBOLIC FUNCTIONS ---
        if (expr.equals("sinh(x)")) return "cosh(x) + C";
        if (expr.equals("cosh(x)")) return "sinh(x) + C";
        if (expr.equals("tanh(x)")) return "ln(cosh(x)) + C";

        // --- 4. INVERSE TRIG (Standard Integrands) ---
        if (expr.equals("1/sqrt(1-x^2)")) return "arcsin(x) + C";
        if (expr.equals("1/(1+x^2)"))     return "arctan(x) + C";
        if (expr.equals("1/(x*sqrt(x^2-1))")) return "arcsec|x| + C";

        // --- 5. THE ULTIMATE POWER RULE (ax^n) ---
        // Regex handles: -2.5x^3.1, x, 5, -x^2
        Pattern p = Pattern.compile("(-?\\d*\\.?\\d*)x(?:\\^?(-?\\d+\\.?\\d*))?");
        Matcher m = p.matcher(expr);
        if (m.matches()) {
            String coeffStr = m.group(1);
            String expStr = m.group(2);
            
            double a = (coeffStr.isEmpty() || coeffStr.equals("+")) ? 1 : 
                       (coeffStr.equals("-") ? -1 : Double.parseDouble(coeffStr));
            
            // If it's a constant (no x), integrate to ax
            if (!expr.contains("x")) return formatCoeff(a) + "x + C";
            
            double n = (expStr == null) ? 1 : Double.parseDouble(expStr);

            if (n == -1) return (a == 1 ? "" : formatCoeff(a)) + "ln|x| + C";
            
            double newN = n + 1;
            return formatCoeff(a / newN) + "x^" + formatCoeff(newN) + " + C";
        }

        // --- 6. LINEAR COMPOSITES (Internal Chain Rule) ---
        // Handles: sin(2x), e^(5x), cos(3x+1)
        Pattern comp = Pattern.compile("(sin|cos|exp|e\\^)\\((-?\\d*\\.?\\d*)x([+-]\\d+)?\\)");
        Matcher cm = comp.matcher(expr);
        if (cm.find()) {
            String func = cm.group(1);
            double k = cm.group(2).isEmpty() ? 1 : 
                      (cm.group(2).equals("-") ? -1 : Double.parseDouble(cm.group(2)));
            String inner = cm.group(2) + "x" + (cm.group(3) == null ? "" : cm.group(3));
            
            double invK = 1.0 / k;
            if (func.equals("sin")) return "-" + formatCoeff(invK) + "cos(" + inner + ") + C";
            if (func.equals("cos")) return formatCoeff(invK) + "sin(" + inner + ") + C";
            if (func.contains("e") || func.equals("exp")) return formatCoeff(invK) + "e^(" + inner + ") + C";
        }

        // --- 7. RECURSIVE SUM RULE ---
        // Splits by + or - but preserves the sign
        if (expr.contains("+") || (expr.contains("-") && !expr.startsWith("-"))) {
            String[] terms = expr.split("(?=[+-])");
            StringBuilder sb = new StringBuilder();
            for (String term : terms) {
                String cleanTerm = term.startsWith("+") ? term.substring(1) : term;
                String solved = integrate(cleanTerm).replace(" + C", "").trim();
                sb.append(term.startsWith("+") ? " + " : " ").append(solved);
            }
            return sb.toString().trim() + " + C";
        }

    } catch (Exception e) {
        return "int(" + expr + ")"; 
    }

    return "int(" + expr + ")"; 
    }

    // Helper to keep the output clean
    private static String formatCoeff(double d) {
    if (d == (long) d) return String.format("%d", (long) d);
    return String.format("%.3f", d).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    static double derivative(String e, double x) { double h = 1e-6; return (evaluateExpression(e, x+h)-evaluateExpression(e, x))/h; }
    static double secondDerivative(String e, double x) { double h = 1e-4; return (evaluateExpression(e, x+h)-2*evaluateExpression(e, x)+evaluateExpression(e, x-h))/(h*h); }
    static double numericIntegrate(String e, double a, double b) { double s=0, n=1000, h=(b-a)/n; for(int i=0;i<n;i++) s+=evaluateExpression(e, a+i*h); return s*h; }
    static double limit(String e, double t) { return evaluateExpression(e, t+1e-8); }

    static JPanel createGraphPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(currentTheme.bgColor);
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
    top.setBackground(currentTheme.bgColor);
    top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JTextField input = new JTextField("");
    input.setBackground(currentTheme.regularButton);
    input.setForeground(currentTheme.foreground);
    input.setCaretColor(currentTheme.foreground);
    input.setFont(new Font("Segoe UI", Font.PLAIN, 16));

    JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    rightGroup.setOpaque(false);
    JButton drawBtn = new JButton("DRAW");
    drawBtn.setBackground(currentTheme.functionButton);
    drawBtn.setForeground(currentTheme.memoryDegColor);
    JButton histBtn = new JButton("EQ");
    histBtn.setBackground(currentTheme.functionButton);
    histBtn.setForeground(currentTheme.foreground);
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
    bottomContainer.setBackground(currentTheme.bgColor);
    bottomContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

    // Zoom on the left
    JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    zoomPanel.setOpaque(false);
    JLabel zoomLabel = new JLabel("Zoom:");
    zoomLabel.setForeground(Color.GRAY);
    JSlider zoomSlider = new JSlider(10, 200, 40);
    zoomSlider.setBackground(currentTheme.bgColor);
    zoomSlider.setPreferredSize(new Dimension(150, 30));
    zoomPanel.add(zoomLabel);
    zoomPanel.add(zoomSlider);

    // Buttons on the right
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    btnPanel.setOpaque(false);
    JButton clearBtn = new JButton("CLEAR ALL");
    clearBtn.setBackground(currentTheme.functionButton);
    clearBtn.setForeground(currentTheme.clearColor);
    JButton saveBtn = new JButton("SAVE");
    saveBtn.setBackground(currentTheme.functionButton);
    saveBtn.setForeground(currentTheme.memoryDegColor);
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
    list.setBackground(currentTheme.bgColor); // Set the list background
    list.setOpaque(true);

    scroll.setPreferredSize(new Dimension(200, 0));
    scroll.getViewport().setBackground(currentTheme.bgColor); // THIS fixes the white background
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
    public double limStart = Double.NEGATIVE_INFINITY;
    public double limEnd = Double.POSITIVE_INFINITY;
    public boolean isLimitShade = false; // To distinguish from regular shade
    public EquationEntry(String text, Color color) {
        this.text = text;
        this.color = color;
        this.editor = new JTextField(text);
        this.editor.setBackground(currentTheme.regularButton);
        this.editor.setForeground(currentTheme.foreground);
        this.editor.setCaretColor(currentTheme.foreground);
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
        private Color activePointColor = currentTheme.foreground;
        private boolean isOverGraph = false;
        private double mouseWorldX = Double.NaN;
        private String activeEquation = "";
        private String currentEquation = "";
        private boolean isTangentMode = true;
        private String tangentExpression = null;
        public boolean isPerpActive = false;
        public boolean isTangentActive = true;
        public String activeMath = "";
        public double[] distData = null; // Stores {x1, y1, x2, y2, distanceValue}
        public double mouseX = 0; // The current math-x coordinate of the cursor
        
        // PLACE THESE INSIDE YOUR 2D PANEL CLASS
        private double toValueX(int pixelX) {
            // Mapping pixel back to math value (e.g., -10 to 10)
            return (pixelX - getWidth() / 2.0) / (zoomScale / 10.0);
        }

        private int toPixelY(double yVal) {
            // Mapping math value to pixel
            return (int) (getHeight() / 2.0 - yVal * (zoomScale / 10.0));
        }
        
 

        public void setPerpMode(boolean active, String math) {
        this.isPerpActive = active;
        this.activeMath = math;
        }

        public void setTangentMode(boolean active, String math) {
        this.isTangentActive = active;
        this.activeMath = math;
        }

        public void setDistanceTool(double x1, double y1, double x2, double y2, double d) {
        this.distData = new double[]{x1, y1, x2, y2, d};
        }
        public GraphCanvas() { 
        setBackground(currentTheme.regularButton); 
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
    g2.setColor(currentTheme.functionButton);
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
    g2.setColor(currentTheme.foreground); 
    g2.setStroke(new BasicStroke(2.5f));
    g2.drawLine(0, cy, getWidth(), cy);    // Horizontal X-Axis
    g2.drawLine(cx, 0, cx, getHeight());   // Vertical Y-Axis

    // 3. SHADING (Draw this BEFORE the lines so the lines stay on top)
    for (EquationEntry en : entries) {
    if (en.isShaded) {
        // Default to full screen
        int startPix = 0; 
        int endPix = getWidth();

        // If it's a limit shade, calculate exactly which pixels correspond to the math values
        if (en.isLimitShade) {
            // Formula: Pixel = (MathValue * scale) + Center
            startPix = (int) (en.limStart * scale + cx);
            endPix = (int) (en.limEnd * scale + cx);

            // Bounds safety: don't try to draw outside the component width
            startPix = Math.max(0, startPix);
            endPix = Math.min(getWidth(), endPix);
        }

        g2.setColor(new Color(en.color.getRed(), en.color.getGreen(), en.color.getBlue(), 50));
        
        // Loop ONLY from the start pixel to the end pixel
        for (int xP = startPix; xP < endPix; xP++) {
            double xM = (double)(xP - cx) / scale;
            try {
                double val = evaluateExpression(en.text, xM);
                int yP = cy - (int)(val * scale);
                g2.drawLine(xP, cy, xP, yP); // Shade from axis to curve
            } catch (Exception ex) {}
        }
    }
    }
    // Inside 2D Panel's paintComponent
    for (EquationLayer layer : layers) {
    if (layer.formula.startsWith("lim(") && layer.formula.endsWith(")shade")) {
        try {
            // Extracts "x^2, 0, 5" from "lim(x^2, 0, 5)shade"
            String content = layer.formula.substring(4, layer.formula.indexOf(")shade"));
            String[] parts = content.split(",");
            
            String expr = parts[0].trim();
            double startX = Double.parseDouble(parts[1].trim());
            double endX = Double.parseDouble(parts[2].trim());

            // Set a translucent version of the layer color
            g2.setColor(new Color(layer.color.getRed(), layer.color.getGreen(), layer.color.getBlue(), 100));

            // Draw the shaded area using vertical strips
            for (int xPix = 0; xPix <= getWidth(); xPix++) {
                double xVal = (xPix - getWidth() / 2.0) / (zoomScale / 10.0);
                
                // Only shade if within the specified math range
                if (xVal >= startX && xVal <= endX) {
                    double yVal = eval(expr, xVal, 0, 0); // Your existing math parser
                    int yPix = (int) (getHeight() / 2.0 - yVal * (zoomScale / 10.0));
                    int zeroPix = (int) (getHeight() / 2.0); // The x-axis
                    
                    g2.drawLine(xPix, zeroPix, xPix, yPix);
                }
            }
        } catch (Exception ex) {
            // Silently skip if the user is still typing the command
        }
    }
    }
    // Inside your 2D Panel's paintComponent method:
    for (EquationLayer layer : layers) {
    // Only process if it's a standard graph command
    if (layer.formula.startsWith("grf ")) {
        String mathExpression = layer.formula.substring(4); // Removes "grf "
        g2.setColor(layer.color);
        g2.setStroke(new BasicStroke(2.0f));

        Path2D.Double path = new Path2D.Double();
        boolean firstPoint = true;

        for (int xPix = 0; xPix <= getWidth(); xPix++) {
            // 1. Convert pixel to math X (e.g., -10 to 10)
            double xVal = (xPix - getWidth() / 2.0) / (zoomScale / 10.0);
            
            // 2. Evaluate the math (Passing 0 for y and time)
            double yVal = eval(mathExpression, xVal, 0, 0);

            // 3. Convert math Y back to pixel
            int yPix = (int) (getHeight() / 2.0 - yVal * (zoomScale / 10.0));

            if (firstPoint) {
                path.moveTo(xPix, yPix);
                firstPoint = false;
            } else {
                // Prevent lines from jumping across the screen on vertical asymptotes
                if (Math.abs(yPix) < 10000) { 
                    path.lineTo(xPix, yPix);
                }
            }
        }
        g2.draw(path);
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
                double val = Arithmos.evaluateExpression(en.text, xM);
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
    
    // Inside the 2D Panel's paintComponent loop:
    

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
        
        g2.setColor(currentTheme.foreground);
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
                    area += Math.abs(Arithmos.evaluateExpression(entry.text, i)) * step;
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
    private void drawTangentLine(Graphics2D g2, String expr) {
    // If mouse is off-screen, default to 0, otherwise use mouse position
    double x0 = Double.isNaN(mouseWorldX) ? 0 : mouseWorldX; 
    
    double y0 = Arithmos.evaluateExpression(expr, x0);
    double h = 0.0001;
    double slope = (Arithmos.evaluateExpression(expr, x0 + h) - y0) / h;

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
                
                double leftVal = Arithmos.evaluateExpression(leftSide, 0);
                double rightVal = Arithmos.evaluateExpression(rightSide, 0);
                
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
        tf.setForeground(currentTheme.foreground);
        tf.setCaretColor(currentTheme.foreground);
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
            setForeground(currentTheme.foreground);
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
    main.setBackground(currentTheme.bgColor); // Dark theme

    // --- TOP: Dimension Controls ---
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    topPanel.setOpaque(false);
    
    JLabel lblA = new JLabel("Matrix A:"); lblA.setForeground(currentTheme.foreground);
    JTextField rAField = new JTextField("2", 2); 
    JTextField cAField = new JTextField("2", 2);
    
    JLabel lblB = new JLabel("Matrix B:"); lblB.setForeground(currentTheme.foreground);
    JTextField rBField = new JTextField("2", 2); 
    JTextField cBField = new JTextField("2", 2);
    
    JButton setBtn = new JButton("Set Grid");
    setBtn.setBackground(currentTheme.functionButton);
    setBtn.setForeground(currentTheme.foreground);

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
    JButton addBtn = new JButton("A + B");
    JButton subBtn = new JButton("A - B");
    JButton mulBtn = new JButton("A × B");
    JButton clearBtn = new JButton("Clear");

    addBtn.setBackground(currentTheme.memoryDegColor);
    addBtn.setForeground(currentTheme.bgColor);
    subBtn.setBackground(currentTheme.memoryDegColor);
    subBtn.setForeground(currentTheme.bgColor);
    mulBtn.setBackground(currentTheme.memoryDegColor);
    mulBtn.setForeground(currentTheme.bgColor);
    clearBtn.setBackground(currentTheme.functionButton);
    clearBtn.setForeground(currentTheme.clearColor);

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
                f.setBackground(currentTheme.regularButton);
                f.setForeground(currentTheme.foreground);
                f.setCaretColor(currentTheme.foreground);
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
                f.setBackground(currentTheme.regularButton);
                f.setForeground(currentTheme.foreground);
                f.setCaretColor(currentTheme.foreground);
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
        JPanel p = new JPanel(new GridLayout(6,4,10,10)); p.setBackground(currentTheme.bgColor); 
        String[] b = {"M+","M-","MR","MC","7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
        for (String text : b) {
            JButton btn = createButton(text); // Use your createButton helper
            p.add(btn);
        } return p;
    }

    static JPanel createScientificPanel() {
        JPanel p = new JPanel(new GridLayout(8,4,10,10)); p.setBackground(currentTheme.bgColor);
        String[] b = {"M+","M-","MR","MC","sin","cos","tan","π","log", "ln", "e", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); return p;
    }

    static JPanel createAdvancedPanel() {
        JPanel p = new JPanel(new GridLayout(10,4,10,10)); p.setBackground(currentTheme.bgColor);
        String[] b = {"M+","M-","MR","MC","sinh","cosh","tanh","π","asinh","acosh","atanh","e","coth", "sech", "csch", "ln", "acoth", "asech", "acsch", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); return p;
    }
    
    static JPanel createTrigPanel(){
        JPanel p = new JPanel(new GridLayout(10, 4, 10, 10)); p.setBackground(currentTheme.bgColor);
        String[] b = {"M+", "M-", "MR", "MC", "sin", "cos", "tan", "π", "asin", "acos", "atan", "(", "cot", "sec", "csc", ")", "acot", "asec", "acsc", "^", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","DEG","√"};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createFuncPanel(){
        JPanel p = new JPanel(new GridLayout(9, 4, 10, 10)); p.setBackground(currentTheme.bgColor);
        String[] b = {"M+", "M-", "MR", "MC", "sgn", "Γ", "ζ", "erf", "W", "Σ", "Φ", "Π", "μ", "σ", "!", ",","7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createConstantPanel(){
        JPanel p = new JPanel(new GridLayout(8, 4, 20, 10)); p.setBackground(currentTheme.bgColor);
        String[] b = {"M+", "M-", "MR", "MC","π", "e", "γ", "φ", "ρ", "δ", "δs", "G", "K", "A", "E", "Ω", "i", "R", "L", "c", "Na", "h", "Ca", "M", "ω", "ζa", "D", "τ", "C", "CE", "DEG", "="};
        for(String s : b) p.add(createButton(s)); 
        return p;
    }
    
    static JPanel createCalculusPanel() {
    JPanel p = new JPanel(new GridLayout(10, 4, 10, 10)); 
    p.setBackground(currentTheme.bgColor);
    
    String[] b = {"M+", "M-", "MR", "MC", "x", "d/dx", "d²/dx²", "∫", "lim", "e", "π", ",", "!", "(", ")", "log", "sin", "cos", "tan", "ln", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b) p.add(createButton(s));
    return p;
    }
    
    static JPanel createFunctionPanel() {
    JPanel p = new JPanel(new GridLayout(11, 4, 10, 10)); 
    p.setBackground(currentTheme.bgColor);
    
    String[] b = {"M+", "M-", "MR", "MC", "x", "y", "grf", "3Dgrf", "lim", "tang", "shade", "perp", "line", "dist", "cont", "vect", "(", ")", "π", ",", "sin", "cos", "tan", "ln", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b) {
    JButton btn = createButton(s); 
    p.add(btn);

    if (s.equals("Tang") || s.equals("Shade")) {
        // Remove the default "append text" listener
        for (ActionListener al : btn.getActionListeners()) {
            btn.removeActionListener(al);
        }

        btn.addActionListener(e -> {
            String current = display.getText().trim();
            // IMPORTANT: Use lowercase to match your processEqualCommand checks!
            String funcName = s.toLowerCase(); 
            
            if (current.isEmpty()) {
                display.setText(funcName + "(");
            } else {
                display.setText(funcName + "(" + current + ")");
            }
            display.requestFocusInWindow();
        });
    }
    // Inside your createFunctionPanel for loop
    if (s.equals("line") || s.equals("dist")) {
    for (ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
    btn.addActionListener(e -> {
        display.setText(s.toLowerCase() + "(( , ),( , ))");
        // Set caret position between the first coordinates for convenience
        display.requestFocusInWindow();
    });
    } else if (s.equals("grf") || s.equals("3Dgrf")) {
    for (ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
    btn.addActionListener(e -> {
        display.setText(s.toLowerCase() + "(" + display.getText().trim() + ")");
        display.requestFocusInWindow();
    });
    }
    }
    return p;
    }

    
    static JPanel createStatPanel(){
    JPanel p = new JPanel(new GridLayout(10, 4, 10, 10));
    p.setBackground(currentTheme.bgColor);
    
    String[] b = {"M+", "M-", "MR", "MC", "abs", "ceil", "floor", "round", "max", "min", "mod", "rand", "nCr", "nPr", "stdev", "stdevp", "Σ", "mean", "%", ",", "7","8","9","÷","4","5","6","×","1","2","3","-",".","0","=","+","C","CE","√","^"};
    for(String s : b) p.add(createButton(s));
    return p;
    }
    // PLACE THIS IN YOUR MAIN CLASS
    private static void handleContourCommand(String cmd) {
    try {
        // Extracts "x^2+y^2" and "5" from "cont(x^2+y^2, 5)"
        String content = cmd.substring(cmd.indexOf("(") + 1, cmd.lastIndexOf(")"));
        String[] parts = content.split(",");
        
        String extractedExpr = parts[0].trim();
        double extractedZ = Double.parseDouble(parts[1].trim());

        // Now 'contourPanel' is recognized!
        contourPanel.updateData(extractedExpr, extractedZ);
        contourPanel.repaint();
        
        // Optional: If this panel is in a separate window, make it visible here
    } catch (Exception e) {
        System.out.println("Format error! Use: cont(formula, z)");
    }
    }
    private static double solveAtPoint3D(String math, double x, double y) {
    String evalStr = math.toLowerCase().replace(" ", "")
        .replace("x", "(" + x + ")")
        .replace("y", "(" + y + ")");
    // Standard replacements for sin, cos, etc. same as solveAtPoint
    return evaluateExpression(evalStr, 0); 
    }
    // PLACE THIS IN YOUR MAIN CLASS
    private static void add2DLayer(String input, Color color) {
    // This connects to your 2D layers list. 
    // Assuming your 2D list is just called 'layers' or similar:
    layers.add(new EquationLayer(input, color));
    // Trigger a repaint on your 2D panel here if it's a different variable
    }   
    private static double map(double value, double start1, double stop1, double start2, double stop2) {
    return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
    private static Color getContourColor(double z) {
    // Clamp z between -1 and 1
    float hue = (float) map(z, -1, 1, 0.6f, 0.0f); // Blue (0.6) to Red (0.0)
    return Color.getHSBColor(hue, 0.8f, 0.9f);
    }
    static JPanel createContourPanel(String equation) {
    return new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    double x = map(i, 0, getWidth(), -6, 6);
                    double y = map(j, 0, getHeight(), -1, 1);
                    
                    // Evaluate z = f(x, y)
                    double z = solveAtPoint3D(equation, x, y); 
                    
                    // Map Z (-1 to 1) to a Color Gradient (Blue -> Orange -> White)
                    g.setColor(getContourColor(z));
                    g.drawLine(i, j, i, j);
                }
            }
        }
    };
    }
    private static double[] parseArgs(String cmd) {
    try {
        // This regex finds everything between ( and )
        String content = cmd.substring(cmd.indexOf("(") + 1, cmd.indexOf(")"));
        String[] parts = content.split(",");
        double[] args = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            args[i] = Double.parseDouble(parts[i].trim());
        }
        return args;
    } catch (Exception e) {
        return new double[0]; // Return empty if format is wrong
    }
    }
    static JPanel createAlphaPanel() {
    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(currentTheme.bgColor);

    // --- Tab Styling ---
    UIManager.put("TabbedPane.selected", currentTheme.regularButton);
    UIManager.put("TabbedPane.contentAreaColor", currentTheme.regularButton);
    
    JTabbedPane alphaTabs = new JTabbedPane();
    alphaTabs.setForeground(currentTheme.foreground);
    alphaTabs.setBackground(currentTheme.bgColor);
    
    // --- Functional Control Buttons ---
    JButton clearAlphaBtn = createButton("C");
    clearAlphaBtn.setBackground(currentTheme.functionButton); 
    clearAlphaBtn.setForeground(currentTheme.clearColor);

    JButton backAlphaBtn = createButton("CE");
    backAlphaBtn.setBackground(currentTheme.functionButton);
    backAlphaBtn.setForeground(currentTheme.clearColor);

    JButton shiftBtn = createButton("UPPERCASE / lowercase");
    shiftBtn.setBackground(currentTheme.functionButton);
    shiftBtn.setForeground(currentTheme.memoryDegColor);
    
    for (ActionListener al : shiftBtn.getActionListeners()) shiftBtn.removeActionListener(al);
    for (ActionListener al : clearAlphaBtn.getActionListeners()) clearAlphaBtn.removeActionListener(al);
    for (ActionListener al : backAlphaBtn.getActionListeners()) backAlphaBtn.removeActionListener(al);
    
    // --- 1. Latin (English) Panel ---
    JPanel englishPanel = new JPanel(new GridLayout(0, 7, 2, 2));
    englishPanel.setBackground(currentTheme.regularButton);
    String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
    for (String s : alphabet) {
        JButton button = createButton(s); // Updated identifier
        for (ActionListener al : button.getActionListeners()) button.removeActionListener(al);
        
        // Add dynamic listener that reads the CURRENT label (A vs a)
        button.addActionListener(e -> {
            updateDisplay(display.getText() + button.getText());
            display.requestFocusInWindow();
        });
        alphaButtons.add(button);
        englishPanel.add(button);
    }

    // --- 2. Greek Panel ---
    JPanel greekPanel = new JPanel(new GridLayout(0, 6, 2, 2));
    greekPanel.setBackground(currentTheme.regularButton);
    String[] greekChars = "αβγδεζηθικλμνξοπρστυφχψω".split("");
    for (String g : greekChars) {
        JButton button = createButton(g); // Updated identifier
        for (ActionListener al : button.getActionListeners()) button.removeActionListener(al);
        
        // Add dynamic listener
        button.addActionListener(e -> {
            updateDisplay(display.getText() + button.getText());
            display.requestFocusInWindow();
        });
        greekButtons.add(button);
        greekPanel.add(button);
    }

    alphaTabs.addTab("Latin", englishPanel);
    alphaTabs.addTab("ελληνικά", greekPanel);

    // --- 3. Bottom Controls Layout ---
    JPanel bottomControls = new JPanel(new BorderLayout(2, 2));
    bottomControls.setBackground(currentTheme.bgColor);

    JPanel alphaGrid = new JPanel(new GridLayout(1, 2, 2, 2)); 
    alphaGrid.setBackground(currentTheme.bgColor);

    // Action Listeners for C and CE
    clearAlphaBtn.addActionListener(e -> {
        updateDisplay(""); 
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

    // Shift Logic (Case Toggling)
    shiftBtn.addActionListener(e -> {
        isUppercase = !isUppercase; 
        for (JButton b : alphaButtons) {
            String txt = b.getText();
            b.setText(isUppercase ? txt.toUpperCase() : txt.toLowerCase());
        }
        for (JButton b : greekButtons) {
            b.setText(toggleGreekCase(b.getText()));
        }
    });

    // Assemble the bottom section
    bottomControls.add(alphaGrid, BorderLayout.NORTH);
    bottomControls.add(shiftBtn, BorderLayout.SOUTH);

    // Final Assembly
    main.add(alphaTabs, BorderLayout.CENTER);
    main.add(bottomControls, BorderLayout.SOUTH); 
    
    return main;
    }

    private static String toggleGreekCase(String symbol) {
    String lower = "αβγδεζηθικλμνξοπρστυφχψω";
    String upper = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
    
    if (isUppercase) {
        int idx = lower.indexOf(symbol);
        return (idx != -1) ? String.valueOf(upper.charAt(idx)) : symbol;
    } else {
        int idx = upper.indexOf(symbol);
        return (idx != -1) ? String.valueOf(lower.charAt(idx)) : symbol;
    }
    }

    private static JPanel create3DGraphPanel() {
    JPanel container = new JPanel(new BorderLayout());
    container.setBackground(currentTheme.bgColor);

    // 1. THE MAIN CANVAS
    JPanel graphCanvas = new JPanel() {
        @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
    // Background
    g2d.setColor(currentTheme.bgColor);
    g2d.fillRect(0, 0, getWidth(), getHeight());

    int cX = getWidth() / 2;
    int cY = getHeight() / 2;

    // Configuration
    double axisLimit = 10.0; // Extended range for the lines
    
    // --- 1. DRAW GROUND GRID (Dashed Lines) ---
    g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{2}, 0));
    g2d.setColor(currentTheme.regularButton); // Very subtle white
    for (double i = -axisLimit; i <= axisLimit; i++) {
        drawLine3D(g2d, i, -axisLimit, 0, i, axisLimit, 0, cX, cY); 
        drawLine3D(g2d, -axisLimit, i, 0, axisLimit, i, 0, cX, cY); 
    }

    // --- 2. DRAW NUMBERS ON GRID ---
    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
    g2d.setColor(currentTheme.foreground);
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
    zoomSlider.setBackground(currentTheme.bgColor);
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
    formulaInput.setBackground(currentTheme.bgColor);
    formulaInput.setForeground(currentTheme.foreground);
    formulaInput.setCaretColor(currentTheme.foreground);
    formulaInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    formulaInput.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(70, 70, 70)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));

    formulaInput.addActionListener(e -> {
    String input = formulaInput.getText().toLowerCase().trim();
    if (input.isEmpty()) return;

    // 1. Define a cycling color for the new entry
    Color nextColor = LAYER_COLORS[layers.size() % LAYER_COLORS.length];

    if (input.startsWith("cont(")) {
        // This sends the command to your SEPARATE contour panel
        handleContourCommand(input); 
    } 
    else if (input.startsWith("3dgrf") || input.startsWith("vect") || input.startsWith("perp")) {
        // This adds it to the 3D layers list
        layers.add(new EquationLayer(input, nextColor));
        graphCanvas.repaint();
    } 
    else {
        // This handles your 2D features (grf, line, dist, lim)
        // Ensure you have a method or list for 2D layers
        add2DLayer(input, nextColor); 
    }
    formulaInput.setText(""); // Clear the box
    });

    JButton clearBtn = new JButton("CLEAR ALL");
    clearBtn.setBackground(currentTheme.functionButton);
    clearBtn.setForeground(currentTheme.clearColor);
    clearBtn.addActionListener(e -> {
        layers.clear();
        updateEQPanel(graphCanvas); // This refresh is what fixes the "dead" button
        graphCanvas.repaint();
    });

    southPanel.add(formulaInput, BorderLayout.CENTER);
    southPanel.add(clearBtn, BorderLayout.EAST);

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
    // Inside renderSolidScene or a specific draw3DGeometry method
    private static void draw3DVector(Graphics2D g2d, double x, double y, double z, Color color, int cX, int cY) {
    Point origin = project(0, 0, 0, cX, cY);
    Point tip = project(x, y, z, cX, cY);

    g2d.setColor(color);
    g2d.setStroke(new BasicStroke(2.5f));
    g2d.drawLine(origin.x, origin.y, tip.x, tip.y);

    // 3D Arrowhead: A small 3D dot or a cross-line at the tip
    g2d.fillOval(tip.x - 4, tip.y - 4, 8, 8);
    
    // Labeling coordinates at the tip
    g2d.setFont(new Font("Consolas", Font.PLAIN, 10));
    g2d.drawString(String.format("(%.1f, %.1f, %.1f)", x, y, z), tip.x + 5, tip.y - 5);
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
    panel.setBackground(currentTheme.functionButton);
    panel.setPreferredSize(new Dimension(190, 0));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
    panel.setVisible(false); //currently developing

    JLabel eqLabel = new JLabel("EQUATIONS");
    eqLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
    eqLabel.setForeground(currentTheme.foreground);
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
    styleSmallButton(clearAllBtn, currentTheme.bgColor);
    clearAllBtn.setForeground(currentTheme.clearColor);
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
        editBox.setBackground(currentTheme.regularButton);
        editBox.setForeground(currentTheme.foreground);
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
    btn.setForeground(currentTheme.foreground);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    btn.setMaximumSize(new Dimension(170, 28)); // Force small size
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void styleCButton(JButton btn) {
    btn.setFont(new Font("Arial", Font.BOLD, 9));
    btn.setBackground(currentTheme.functionButton);
    btn.setForeground(currentTheme.foreground);
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
    main.setBackground(currentTheme.bgColor);

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
    keypad.setBackground(currentTheme.regularButton);
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
        btn.setBorder(BorderFactory.createLineBorder(currentTheme.regularButton));
        btn.setBackground(new Color(55, 55, 55));
        btn.setForeground(currentTheme.foreground);
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
    styleDropdown(categoryBox, segoeFont, currentTheme.bgColor, currentTheme.foreground);
    styleDropdown(fromUnitBox, segoeFont, currentTheme.bgColor, currentTheme.foreground);
    styleDropdown(toUnitBox, segoeFont, currentTheme.bgColor, currentTheme.foreground);
    
    // Style the Input Box
    convInput.setFont(inputFont);
    convInput.setBackground(currentTheme.bgColor); // Match main background
    convInput.setForeground(currentTheme.foreground);
    convInput.setCaretColor(currentTheme.foreground);
    convInput.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // No visible border lines
    convInput.setOpaque(true);
    
    // Style the Result Label
    convResult.setFont(inputFont);
    convResult.setForeground(currentTheme.foreground);
    
    categoryBox.setForeground(currentTheme.foreground);
    fromUnitBox.setForeground(currentTheme.foreground);
    toUnitBox.setForeground(currentTheme.foreground);

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
    mainContainer.setBackground(currentTheme.bgColor);
    mainContainer.setBorder(null); // Ensure container is borderless

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1.0;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(currentTheme.bgColor);
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
    rightPanel.setBackground(currentTheme.bgColor);
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
    input.setForeground(currentTheme.foreground);
    input.setCaretColor(textColor);
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
    comp.setBackground(currentTheme.bgColor); // Use your dark background
    comp.setForeground(currentTheme.foreground);
    if (comp instanceof JPanel) {
        ((JPanel) comp).setOpaque(true);
        ((JPanel) comp).setBorder(null); // Kills the white frame
    }
    }
    private static JPanel createNumericKeypad() {
    JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
    keypad.setOpaque(false);

    String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "C", "0", "."};
    for (String key : keys) {
        JButton btn = new JButton(key);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        if (key.equals("C")) {
            btn.setBackground(currentTheme.functionButton); 
            btn.setForeground(currentTheme.clearColor);// Red for Clear Entry
        } else {
            btn.setBackground(currentTheme.regularButton);
            btn.setForeground(currentTheme.foreground);
        }
        
        
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
            if (key.equals("C")) {
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
    btn.setForeground(currentTheme.foreground);
    btn.setBackground(currentTheme.regularButton);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Simple hover effect
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(currentTheme.functionButton); }
        public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(currentTheme.regularButton); }
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
    sideMenu.setBackground(currentTheme.regularButton);
    sideMenu.setBounds(0, 0, 240, 600);
    sideMenu.setVisible(false);
    sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
    if(isDeveloping == true){
        String[] modes = {
        "Standard", "Functions", "Graphing",
        "Matrix", "Programmer", "Converter", "Other", "Settings", "Help"
        };
    }
    
    String[] modes = { 
        "Standard", "Functions", "Graphing", "Converter", "Help"
    };
    

    int yPos = 60;

    for (String mode : modes) {

        JButton btn = createMenuButton(mode, yPos);

        btn.addActionListener(e -> {

            buttonsPanel.removeAll();   // ✅ always clear central area

            if (mode.equals("Standard")) {
                resizeCalculator(450, 600);
                buttonsPanel.removeAll();
                buttonsPanel.add(createBasicPanel(), BorderLayout.CENTER);
                panelTitleLabel.setText("STANDARD");

            }

            else if (mode.equals("Functions")) {

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(currentTheme.regularButton);
                tabbedPane.setForeground(currentTheme.foreground);
                resizeCalculator(450, 600);
                
                tabbedPane.add("Sci", createScientificPanel());
                tabbedPane.add("aA", createAlphaPanel());
                tabbedPane.add("Func", createFuncPanel());
                tabbedPane.add("Const", createConstantPanel());
                tabbedPane.add("Trig", createTrigPanel());
                tabbedPane.add("Adv", createAdvancedPanel());
                tabbedPane.add("∫", createCalculusPanel());
                tabbedPane.add("Stat", createStatPanel());

                buttonsPanel.add(tabbedPane, BorderLayout.CENTER);
                panelTitleLabel.setText("FUNCTIONS");

            }

            else if (mode.equals("Graphing")) {

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(currentTheme.regularButton);
                tabbedPane.setForeground(currentTheme.foreground);
                resizeCalculator(450, 600);
                if(isDeveloping == true){
                tabbedPane.add("Func", createFunctionPanel());
                tabbedPane.add("aA", createAlphaPanel());
                tabbedPane.add("2D", createGraphPanel());
                tabbedPane.add("3D", create3DGraphPanel());
                tabbedPane.add("Cont", contourPanel);
                }
                else{
                    tabbedPane.add("2D", createGraphPanel());
                    tabbedPane.add("3D", create3DGraphPanel());
                }
                buttonsPanel.add(tabbedPane, BorderLayout.CENTER);
                panelTitleLabel.setText("GRAPHING");

            }

            else if (mode.equals("Programmer")) {
                buttonsPanel.add(createProgrammingPanel(), BorderLayout.CENTER);
                resizeCalculator(450, 600);
                panelTitleLabel.setText("PROGRAMMER");

            }

            else if (mode.equals("Converter")) {
                buttonsPanel.add(createConversionPanel(), BorderLayout.CENTER);
                resizeCalculator(450, 600);
                panelTitleLabel.setText("CONVERTER");

            }
            else if (mode.equals("Matrix")) {
                buttonsPanel.add(createMatrixPanel(), BorderLayout.CENTER);
                resizeCalculator(450, 600);
                panelTitleLabel.setText("MATRIX");

            }
            else if (mode.equals("Other")) {
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(currentTheme.regularButton);
                tabbedPane.setForeground(currentTheme.foreground);
                resizeCalculator(450, 600);
                
                tabbedPane.add("Physics", createPhysicsPanel());
                buttonsPanel.add(tabbedPane, BorderLayout.CENTER);
                panelTitleLabel.setText("OTHER");
                
            }
            else if (mode.equals("Settings")) {
                // 1. Generate the settings panel
                JPanel settingsPanel = createSettingsPanel();
    
                // 2. Treat it like all the other panels: add it to buttonsPanel!
                buttonsPanel.add(settingsPanel, BorderLayout.CENTER);
    
                // 3. Set your title and size matching your structural layout
                panelTitleLabel.setText("SETTINGS");
                resizeCalculator(450, 600);
            }
            else if(mode.equals("Help")){
                buttonsPanel.add(createHelpPanel(), BorderLayout.CENTER);
                resizeCalculator(450, 600);
                panelTitleLabel.setText("HELP");
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
    btn.setForeground(currentTheme.foreground);
    btn.setBackground(currentTheme.regularButton);
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);

    // Hover effect to match image 30a9a4
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(currentTheme.functionButton); }
        public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(currentTheme.regularButton); }
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
    public static JPanel createSettingsPanel() {
    // Initialize the panel with a background that MUST be visible
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(currentTheme.bgColor);
    panel.setOpaque(true); // Forces the background to paint

    // Creating the dropdown
    String[] themes = {"Light", "Dark", "Ocean", "System Settings"};
    JComboBox<String> themeBox = new JComboBox<>(themes);
    
    // SWING TRICK: JComboBox in BoxLayout needs a setMaximumSize or it stays invisible/tiny
    themeBox.setMaximumSize(new Dimension(250, 40));
    themeBox.setPreferredSize(new Dimension(250, 40));
    themeBox.setAlignmentX(Component.CENTER_ALIGNMENT);

    themeBox.addActionListener(e -> {
        applyTheme((String) themeBox.getSelectedItem());
    });

    // Add them to the panel
    panel.add(Box.createVerticalStrut(50)); // Add top spacing
    panel.add(themeBox);
    
    return panel;
    }
    private static void applyTheme(String mode) {
    switch (mode) {
        case "Light":
            currentTheme.bgColor = new Color(230, 230, 230);
            currentTheme.foreground = Color.BLACK;
            currentTheme.regularButton = new Color(221, 221, 221);
            currentTheme.functionButton = new Color(239, 239, 239);
            currentTheme.clearColor = new Color(166, 73, 73);
            currentTheme.memoryDegColor = new Color(73, 79, 166);
            currentTheme.equalsColor = new Color(166, 73, 73);
            break;

        case "Ocean":
            currentTheme.bgColor = new Color(11, 39, 116);
            currentTheme.foreground = Color.WHITE;
            currentTheme.regularButton = new Color(23, 33, 198);
            currentTheme.functionButton = new Color(12, 5, 96);
            currentTheme.clearColor = new Color(230, 136, 136);
            currentTheme.memoryDegColor = new Color(51, 147, 255);
            currentTheme.equalsColor = new Color(0, 141, 223);
            break;

        case "Dark":
        case "System Settings":
        default:
            currentTheme.bgColor = new Color(25, 25, 25);
            currentTheme.foreground = Color.WHITE;
            currentTheme.regularButton = new Color(34, 34, 34);
            currentTheme.functionButton = new Color(17, 17, 17);
            currentTheme.clearColor = new Color(230, 136, 136);
            currentTheme.memoryDegColor = new Color(238, 255, 122);
            currentTheme.equalsColor = new Color(238, 255, 122);
            break;
    }

    // After updating the object, trigger the visual refresh
    refreshAppAppearance();
    }

    private static void refreshAppAppearance() {
    if (instance == null) return; // Safety check

    // Now we use 'instance' instead of 'this' or 'getContentPane'
    frame.getContentPane().setBackground(currentTheme.bgColor);
    
    if (canvas != null) {
        canvas.setBackground(currentTheme.bgColor);
        canvas.repaint();
    }
    
    // This will refresh every panel, label, and button in the frame
    SwingUtilities.updateComponentTreeUI(frame);
    }

    private static void styleSettingComponent(JComponent comp) {
    comp.setBackground(currentTheme.regularButton);
    comp.setForeground(currentTheme.foreground);
    comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    comp.setAlignmentX(Component.LEFT_ALIGNMENT);
    comp.setFocusable(false);
    }
    static class Theme {
    String name;
    Color bgColor;
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
        this.bgColor = bg;
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
    mainPanel.setBackground(currentTheme.bgColor);

    // Update display
    display.setBackground(currentTheme.bgColor);
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
    // Add these fields to the top of your Arithmos class

    static class PhysData {
        String[] labels;
        String[][] units;
        double[][] factors;
        java.util.function.BiFunction<double[], Integer, Double> solver;

        PhysData(String[] l, String[][] u, double[][] f, java.util.function.BiFunction<double[], Integer, Double> s) {
            this.labels = l; this.units = u; this.factors = f; this.solver = s;
        }
    }

    // --- 2. THE MASTER LIST (Categories 1-7) ---
    private static void initializePhysicsData(){
    addFormula("Velocity / Speed", new String[]{"Distance", "Time", "Velocity"},
        new String[][]{
            {"m", "km", "cm", "mm", "μm", "nm", "in", "ft", "yd", "mi", "nmi", "fathom", "AU", "ly", "pc", "Å"}, 
            {"s", "min", "hr", "ms", "μs", "ns", "ps", "day", "wk", "yr", "decade"}, 
            {"m/s", "km/h", "mph", "knot", "ft/s", "mach", "cm/s", "mm/s", "km/s", "c (light)", "in/s", "yd/min"}
        },
        new double[][]{
            {1, 1000, 0.01, 0.001, 1E-6, 1E-9, 0.0254, 0.3048, 0.9144, 1609.34, 1852, 1.8288, 1.496E11, 9.461E15, 3.086E16, 1E-10}, 
            {1, 60, 3600, 0.001, 1E-6, 1E-9, 1E-12, 86400, 604800, 3.1536E7, 3.1536E8}, 
            {1, 0.277778, 0.44704, 0.514444, 0.3048, 340.3, 0.01, 0.001, 1000, 299792458, 0.0254, 0.01524}
        },
        (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

    addFormula("Acceleration", new String[]{"Δ Velocity", "Time", "Acceleration"},
        new String[][]{
            {"m/s", "km/h", "mph", "knot", "ft/s", "mach", "cm/s", "mm/s", "km/s", "c (light)", "in/s"}, 
            {"s", "min", "hr", "ms", "μs", "ns", "day", "wk", "yr"}, 
            {"m/s²", "g-unit", "ft/s²", "km/h²", "in/s²", "cm/s²", "Gal (cm/s²)", "km/s²", "μm/s²", "mph/s"}
        },
        new double[][]{
            {1, 0.277778, 0.44704, 0.514444, 0.3048, 340.3, 0.01, 0.001, 1000, 299792458, 0.0254}, 
            {1, 60, 3600, 0.001, 1E-6, 1E-9, 86400, 604800, 3.1536E7}, 
            {1, 9.80665, 0.3048, 7.716E-5, 0.0254, 0.01, 0.01, 1000, 1E-6, 0.44704}
        },
        (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

    addFormula("Momentum", new String[]{"Mass", "Velocity", "Momentum"},
        new String[][]{
            {"kg", "g", "mg", "μg", "lb", "oz", "slug", "ton (m)", "ton (US)", "st", "ct", "Solar Mass", "u (amu)"}, 
            {"m/s", "km/h", "mph", "knot", "ft/s", "mach", "cm/s", "km/s", "c (light)"}, 
            {"kg·m/s", "g·cm/s", "lb·ft/s", "slug·ft/s", "lb·in/s", "N·s", "dyn·s"}
        },
        new double[][]{
            {1, 0.001, 1E-6, 1E-9, 0.453592, 0.028349, 14.5939, 1000, 907.185, 6.35029, 0.0002, 1.988E30, 1.6605E-27}, 
            {1, 0.277778, 0.44704, 0.514444, 0.3048, 340.3, 0.01, 1000, 299792458}, 
            {1, 1E-5, 0.138255, 4.44822, 0.01152, 1, 1E-5}
        },
        (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));
        addFormula("Density", new String[]{"Mass", "Volume", "Density"},
    new String[][]{
        {"kg", "g", "mg", "μg", "ton (m)", "ton (US)", "lb", "oz", "slug", "ct", "st", "Solar Mass", "Earth Mass"}, 
        {"m³", "L", "mL", "cm³", "mm³", "μL", "ft³", "in³", "gal", "qt", "pt", "cup", "fl oz", "bbl", "tsp", "tbsp"}, 
        {"kg/m³", "g/cm³", "lb/ft³", "lb/in³", "kg/L", "g/mL", "oz/in³", "slug/ft³", "mg/L", "μg/m³"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1000, 907.185, 0.453592, 0.0283495, 14.5939, 0.0002, 6.35029, 1.988E30, 5.972E24}, 
        {1, 0.001, 1E-6, 1E-6, 1E-9, 1E-9, 0.0283168, 1.6387E-5, 0.00378541, 0.000946, 0.000473, 0.000236, 2.957E-5, 0.15898, 4.928E-6, 1.478E-5}, 
        {1, 1000, 16.0185, 27679.9, 1000, 1000, 1729.99, 515.379, 0.001, 1E-9}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Force / Weight", new String[]{"Mass", "Accel", "Force"},
    new String[][]{
        {"kg", "g", "mg", "lb", "oz", "slug", "ton", "st", "ct", "u (amu)"}, 
        {"m/s²", "g-unit", "ft/s²", "cm/s²", "in/s²", "km/h²", "Gal", "km/s²"}, 
        {"N", "kN", "mN", "μN", "lbf", "dyn", "kgf", "pdl", "ozf", "kip", "sn (sthene)"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 0.453592, 0.0283495, 14.5939, 907.185, 6.35, 0.0002, 1.66E-27}, 
        {1, 9.80665, 0.3048, 0.01, 0.0254, 7.716E-5, 0.01, 1000}, 
        {1, 1000, 0.001, 1E-6, 4.44822, 1E-5, 9.80665, 0.138255, 0.27801, 4448.22, 1000}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Impulse", new String[]{"Force", "Time", "Impulse"},
    new String[][]{
        {"N", "kN", "mN", "μN", "lbf", "dyn", "kgf", "pdl", "ozf", "kip"}, 
        {"s", "min", "hr", "ms", "μs", "ns", "ps", "day", "wk"}, 
        {"N·s", "lb-s", "dyn-s", "kgf-s", "pdl-s", "ozf-s", "kip-s"}
    },
    new double[][]{
        {1, 1000, 0.001, 1E-6, 4.44822, 1E-5, 9.80665, 0.138255, 0.27801, 4448.22}, 
        {1, 60, 3600, 0.001, 1E-6, 1E-9, 1E-12, 86400, 604800}, 
        {1, 4.44822, 1E-5, 9.80665, 0.138255, 0.27801, 4448.22}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Pressure", new String[]{"Force", "Area", "Pressure"},
    new String[][]{
        {"N", "kN", "mN", "lbf", "kgf", "dyn", "ozf", "kip"}, 
        {"m²", "cm²", "mm²", "in²", "ft²", "yd²", "acre", "ha", "barn"}, 
        {"Pa", "kPa", "MPa", "GPa", "bar", "mbar", "psi", "atm", "torr", "mmHg", "inHg", "psf", "kgf/cm²", "dyn/cm²", "ba", "cmH2O", "inH2O", "ksi"}
    },
    new double[][]{
        {1, 1000, 0.001, 4.44822, 9.80665, 1E-5, 0.278, 4448.22}, 
        {1, 1E-4, 1E-6, 0.00064516, 0.092903, 0.8361, 4046.86, 10000, 1E-28}, 
        {1, 1000, 1E6, 1E9, 100000, 100, 6894.76, 101325, 133.322, 133.322, 3386.39, 47.88, 98066.5, 0.1, 0.1, 98.0665, 249.08, 6894757}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Work / Energy", new String[]{"Force", "Distance", "Energy"},
    new String[][]{
        {"N", "kN", "mN", "lbf", "kgf", "dyn", "ozf", "kip"}, 
        {"m", "km", "cm", "mm", "μm", "nm", "in", "ft", "yd", "mi", "nmi", "AU", "ly", "pc", "Å", "fathom"}, 
        {"J", "kJ", "MJ", "GJ", "cal", "kcal", "Wh", "kWh", "MWh", "eV", "keV", "MeV", "GeV", "TeV", "BTU", "ft-lb", "erg", "latm", "toe", "tnt", "quad"}
    },
    new double[][]{
        {1, 1000, 0.001, 4.44822, 9.80665, 1E-5, 0.278, 4448.22}, 
        {1, 1000, 0.01, 0.001, 1E-6, 1E-9, 0.0254, 0.3048, 0.9144, 1609.34, 1852, 1.496E11, 9.461E15, 3.086E16, 1E-10, 1.8288}, 
        {1, 1000, 1E6, 1E9, 4.184, 4184, 3600, 3.6E6, 3.6E9, 1.602E-19, 1.602E-16, 1.602E-13, 1.602E-10, 1.602E-7, 1055.06, 1.35582, 1E-7, 101.325, 4.187E10, 4.184E9, 1.055E18}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Power", new String[]{"Work/Energy", "Time", "Power"},
    new String[][]{
        {"J", "kJ", "MJ", "GJ", "cal", "kcal", "Wh", "kWh", "MWh", "eV", "BTU", "ft-lb", "erg"}, 
        {"s", "min", "hr", "ms", "μs", "ns", "day", "wk", "yr"}, 
        {"W", "kW", "MW", "GW", "hp (mech)", "hp (metric)", "hp (elec)", "cal/s", "kcal/h", "BTU/h", "ft-lb/s", "erg/s", "VA"}
    },
    new double[][]{
        {1, 1000, 1E6, 1E9, 4.184, 4184, 3600, 3.6E6, 3.6E9, 1.602E-19, 1055.06, 1.35582, 1E-7}, 
        {1, 60, 3600, 0.001, 1E-6, 1E-9, 86400, 604800, 3.1536E7}, 
        {1, 1000, 1E6, 1E9, 745.7, 735.5, 746, 4.184, 1.163, 0.29307, 1.35582, 1E-7, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

    addFormula("Torque", new String[]{"Force", "Radius", "Torque"},
    new String[][]{
        {"N", "kN", "mN", "lbf", "ozf", "kgf", "dyn", "kip"}, 
        {"m", "cm", "mm", "μm", "nm", "in", "ft", "yd", "mi", "Å"}, 
        {"N·m", "kN·m", "mN·m", "lb-ft", "lb-in", "oz-in", "kg-m", "dyn-cm", "kgf-cm", "kip-ft", "kip-in"}
    },
    new double[][]{
        {1, 1000, 0.001, 4.44822, 0.278014, 9.80665, 1E-5, 4448.22}, 
        {1, 0.01, 0.001, 1E-6, 1E-9, 0.0254, 0.3048, 0.9144, 1609.34, 1E-10}, 
        {1, 1000, 0.001, 1.35582, 0.112985, 0.007062, 9.80665, 1E-7, 0.0980665, 1355.82, 112.98}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

    addFormula("Surface Tension", new String[]{"Force", "Length", "Surface Tension"},
    new String[][]{
        {"N", "kN", "mN", "μN", "lbf", "ozf", "kgf", "dyn"}, 
        {"m", "cm", "mm", "μm", "nm", "in", "ft", "yd", "Å"}, 
        {"N/m", "mN/m", "dyn/cm", "lb/ft", "lb/in", "kgf/m", "mN/cm", "gf/cm"}
    },
    new double[][]{
        {1, 1000, 0.001, 1E-6, 4.44822, 0.278014, 9.80665, 1E-5}, 
        {1, 0.01, 0.001, 1E-6, 1E-9, 0.0254, 0.3048, 0.9144, 1E-10}, 
        {1, 0.001, 0.001, 14.5939, 175.127, 9.80665, 0.1, 0.980665}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));
    addFormula("Electric Charge", new String[]{"Current", "Time", "Charge"},
    new String[][]{
        {"A", "mA", "μA", "nA", "pA", "kA", "MA", "abA", "statA"}, 
        {"s", "min", "hr", "ms", "μs", "ns", "day", "wk", "yr"}, 
        {"C", "mC", "μC", "nC", "pC", "kC", "MC", "Ah", "mAh", "Fr", "e", "Faraday", "abC", "statC"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1000, 1E6, 10, 3.3356E-10}, 
        {1, 60, 3600, 0.001, 1E-6, 1E-9, 86400, 604800, 3.1536E7}, 
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1000, 1E6, 3600, 3.6, 3.3356E-10, 1.602E-19, 96485, 10, 3.3356E-10}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Voltage / Pot. Difference", new String[]{"Energy", "Charge", "Voltage"},
    new String[][]{
        {"J", "kJ", "cal", "kcal", "Wh", "kWh", "eV", "erg", "BTU"}, 
        {"C", "mC", "μC", "nC", "Ah", "mAh", "statC", "abC", "Fr"}, 
        {"V", "mV", "μV", "kV", "MV", "GV", "nV", "pV", "abV", "statV", "W/A"}
    },
    new double[][]{
        {1, 1000, 4.184, 4184, 3600, 3.6E6, 1.602E-19, 1E-7, 1055.06}, 
        {1, 0.001, 1E-6, 1E-9, 3600, 3.6, 3.3356E-10, 10, 3.3356E-10}, 
        {1, 0.001, 1E-6, 1000, 1E6, 1E9, 1E-9, 1E-12, 1E-8, 299.79, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Conductance", new String[]{"Current", "Voltage", "Conductance"},
    new String[][]{
        {"A", "mA", "μA", "kA", "nA"}, 
        {"V", "mV", "μV", "kV", "MV"}, 
        {"S (Siemens)", "mS", "μS", "nS", "pS", "kS", "MS", "mho", "abmho", "statmho"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000, 1E-9}, 
        {1, 0.001, 1E-6, 1000, 1E6}, 
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1000, 1E6, 1, 1E9, 1.112E-12}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Electric Field Strength", new String[]{"Voltage", "Distance", "Field Strength"},
    new String[][]{
        {"V", "mV", "kV", "MV", "abV", "statV"}, 
        {"m", "cm", "mm", "μm", "in", "ft", "mil"}, 
        {"V/m", "kV/m", "V/cm", "V/mm", "mV/m", "MV/m", "V/mil", "V/in", "N/C"}
    },
    new double[][]{
        {1, 0.001, 1000, 1E6, 1E-8, 299.79}, 
        {1, 0.01, 0.001, 1E-6, 0.0254, 0.3048, 2.54E-5}, 
        {1, 1000, 100, 1000, 0.001, 1E6, 39370, 39.37, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Current Density", new String[]{"Current", "Area", "Density (J)"},
    new String[][]{
        {"A", "mA", "μA", "kA", "nA", "pA", "MA"}, 
        {"m²", "cm²", "mm²", "in²", "ft²", "circular mil"}, 
        {"A/m²", "A/cm²", "A/mm²", "mA/cm²", "A/ft²", "A/in²", "kA/m²"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000, 1E-9, 1E-12, 1E6}, 
        {1, 1E-4, 1E-6, 0.00064516, 0.0929, 5.067E-10}, 
        {1, 10000, 1E6, 10, 10.76, 1550, 1000}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Resistance", new String[]{"Voltage", "Current", "Resistance"},
    new String[][]{
        {"V", "mV", "μV", "kV", "MV", "GV", "abV", "statV"}, 
        {"A", "mA", "μA", "kA", "nA", "pA", "abA", "statA"}, 
        {"Ω", "mΩ", "kΩ", "MΩ", "GΩ", "TΩ", "μΩ", "nΩ", "abΩ", "statΩ"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000, 1E6, 1E9, 1E-8, 299.79}, 
        {1, 0.001, 1E-6, 1000, 1E-9, 1E-12, 10, 3.3356E-10}, 
        {1, 0.001, 1000, 1E6, 1E9, 1E12, 1E-6, 1E-9, 1E-9, 8.987E11}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Permeability", new String[]{"Inductance", "Length", "Permeability"},
    new String[][]{
        {"H", "mH", "μH", "nH", "pH", "abH", "statH"}, 
        {"m", "cm", "mm", "in", "ft"}, 
        {"H/m", "μH/m", "nH/m", "H/cm", "μH/cm"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E-9, 8.987E11}, 
        {1, 0.01, 0.001, 0.0254, 0.3048}, 
        {1, 1E-6, 1E-9, 100, 0.0001}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Capacitance", new String[]{"Charge", "Voltage", "Capacitance"},
    new String[][]{
        {"C", "mC", "μC", "nC", "pC", "Ah", "statC", "abC"}, 
        {"V", "mV", "μV", "kV", "MV", "abV", "statV"}, 
        {"F", "mF", "μF", "nF", "pF", "fF", "kF", "abF", "statF", "cm (cap)"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1E-12, 3600, 3.3356E-10, 10}, 
        {1, 0.001, 1E-6, 1000, 1E6, 1E-8, 299.79}, 
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E-15, 1000, 1E9, 1.112E-12, 1.112E-12}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Permittivity", new String[]{"Capacitance", "Length", "Permittivity"},
    new String[][]{
        {"F", "mF", "μF", "nF", "pF", "abF", "statF"}, 
        {"m", "cm", "mm", "in", "ft"}, 
        {"F/m", "μF/m", "nF/m", "pF/m", "F/cm", "statF/cm"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E9, 1.112E-12}, 
        {1, 0.01, 0.001, 0.0254, 0.3048}, 
        {1, 1E-6, 1E-9, 1E-12, 100, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Magnetic Flux", new String[]{"Voltage", "Time", "Flux (Φ)"},
    new String[][]{
        {"V", "mV", "μV", "kV", "MV", "abV", "statV"}, 
        {"s", "min", "hr", "ms", "μs", "ns"}, 
        {"Wb", "mWb", "μWb", "nWb", "Mx", "kMx", "uMx", "abWb", "statWb"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000, 1E6, 1E-8, 299.79}, 
        {1, 60, 3600, 0.001, 1E-6, 1E-9}, 
        {1, 0.001, 1E-6, 1E-9, 1E-8, 1E-5, 1E-14, 1, 299.79}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Mag. Flux/Field Density", new String[]{"Flux", "Area", "Density (B)"},
    new String[][]{
        {"Wb", "mWb", "μWb", "Mx", "kMx", "abWb", "statWb"}, 
        {"m²", "cm²", "mm²", "in²", "ft²"}, 
        {"T", "mT", "μT", "nT", "pT", "G", "kG", "mG", "μG", "abT", "statT"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-8, 1E-5, 1, 299.79}, 
        {1, 1E-4, 1E-6, 0.00064516, 0.0929}, 
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E-4, 0.1, 1E-7, 1E-10, 1, 3.335E-14}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Inductance", new String[]{"Flux", "Current", "Inductance"},
    new String[][]{
        {"Wb", "mWb", "μWb", "Mx", "kMx", "abWb", "statWb"}, 
        {"A", "mA", "μA", "kA", "nA", "abA", "statA"}, 
        {"H", "mH", "μH", "nH", "pH", "fH", "kH", "abH", "statH"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-8, 1E-5, 1, 299.79}, 
        {1, 0.001, 1E-6, 1000, 1E-9, 10, 3.335E-10}, 
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E-15, 1000, 1E-9, 8.987E11}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));
    addFormula("Angular Resolution", new String[]{"Wavelength (λ)", "Aperture Diameter (D)", "Resolution (θ)"},
    new String[][]{
        {"m", "cm", "mm", "μm", "nm", "Å", "in", "ft"}, 
        {"m", "cm", "mm", "μm", "in", "ft", "yd"}, 
        {"rad", "deg", "arcmin", "arcsec", "mas (milliarcsec)", "μas", "grad"}
    },
    new double[][]{
        {1, 0.01, 0.001, 1E-6, 1E-9, 1E-10, 0.0254, 0.3048}, 
        {1, 0.01, 0.001, 1E-6, 0.0254, 0.3048, 0.9144}, 
        {1, 0.017453, 0.000291, 4.8481E-6, 4.8481E-9, 4.8481E-12, 0.015708}
    },
    (v, t) -> t == 2 ? 1.22 * (v[0] / v[1]) : (t == 1 ? 1.22 * (v[0] / v[2]) : (v[2] * v[1]) / 1.22));

addFormula("Angular Velocity", new String[]{"Angle (θ)", "Time (t)", "Ang. Vel (ω)"},
    new String[][]{
        {"rad", "deg", "rev", "grad", "arcmin", "arcsec", "quadrant", "sextant", "sign"}, 
        {"s", "min", "hr", "ms", "μs", "day", "wk", "yr", "decade"}, 
        {"rad/s", "deg/s", "rpm", "rev/s", "deg/hr", "rad/min", "grad/s", "deg/min", "rad/hr"}
    },
    new double[][]{
        {1, 0.01745329, 6.283185, 0.0157079, 0.0002908, 4.8481E-6, 1.570796, 1.047198, 0.523599}, 
        {1, 60, 3600, 0.001, 1E-6, 86400, 604800, 3.1536E7, 3.1536E8}, 
        {1, 0.01745329, 0.1047198, 6.283185, 4.8481E-6, 0.0166667, 0.0157079, 0.0002908, 0.0002777}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Angular Acceleration", new String[]{"Δ Ang. Velocity", "Time (t)", "Ang. Accel (α)"},
    new String[][]{
        {"rad/s", "deg/s", "rpm", "rev/s", "grad/s", "rev/min", "deg/min", "rad/min", "deg/hr"}, 
        {"s", "min", "hr", "ms", "μs", "ns", "day", "wk"}, 
        {"rad/s²", "deg/s²", "rpm/s", "rev/s²", "rpm/min", "deg/hr²", "rad/min²", "rev/min²", "rad/hr²"}
    },
    new double[][]{
        {1, 0.01745329, 0.1047198, 6.283185, 0.0157079, 0.1047198, 0.0002908, 0.0166667, 4.8481E-6}, 
        {1, 60, 3600, 0.001, 1E-6, 1E-9, 86400, 604800}, 
        {1, 0.01745329, 0.1047198, 6.283185, 0.0017453, 1.3466E-9, 0.0002777, 0.0017453, 7.716E-8}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Moment of Inertia", new String[]{"Mass (m)", "Radius (r)", "Inertia (I)"},
    new String[][]{
        {"kg", "g", "mg", "μg", "lb", "oz", "slug", "ton (m)", "ton (US)", "st", "ct", "u", "Solar Mass"}, 
        {"m", "cm", "mm", "μm", "nm", "in", "ft", "yd", "mi", "nmi", "Å", "fathom", "rod"}, 
        {"kg·m²", "g·cm²", "lb·ft²", "lb·in²", "slug·ft²", "oz·in²", "kg·cm²", "mg·mm²", "ton·m²", "g·m²"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 0.453592, 0.028349, 14.5939, 1000, 907.185, 6.35029, 0.0002, 1.6605E-27, 1.988E30}, 
        {1, 0.01, 0.001, 1E-6, 1E-9, 0.0254, 0.3048, 0.9144, 1609.34, 1852, 1E-10, 1.8288, 5.0292}, 
        {1, 1E-7, 1.355818, 0.0002926, 1.355818, 1.829E-5, 0.0001, 1E-12, 1000, 0.001}
    },
    (v, t) -> t == 2 ? v[0] * Math.pow(v[1], 2) : (t == 1 ? Math.sqrt(v[2] / v[0]) : v[2] / Math.pow(v[1], 2)));
    addFormula("Frequency", new String[]{"Period (T)", "Const (1)", "Frequency (f)"},
    new String[][]{
        {"s", "ms", "μs", "ns", "ps", "fs", "min", "hr", "day", "wk", "yr", "decade"}, 
        {"-"}, 
        {"Hz", "kHz", "MHz", "GHz", "THz", "PHz", "EHz", "rpm", "deg/s", "rad/s", "grad/s"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-9, 1E-12, 1E-15, 60, 3600, 86400, 604800, 3.1536E7, 3.1536E8}, 
        {1}, 
        {1, 1000, 1E6, 1E9, 1E12, 1E15, 1E18, 0.016667, 0.002778, 0.159155, 0.0025}
    },
    (v, t) -> t == 2 ? 1 / v[0] : 1 / v[2]);

addFormula("Viscosity (Dynamic)", new String[]{"Shear Stress", "Shear Rate", "Viscosity"},
    new String[][]{
        {"Pa", "kPa", "mPa", "psi", "bar", "atm", "torr", "dyn/cm²", "kgf/m²"}, 
        {"1/s", "1/min", "1/hr", "1/ms"}, 
        {"Pa·s", "P (Poise)", "cP", "lb·s/ft²", "kg/(m·s)", "mPa·s", "lb·s/in²", "reyn"}
    },
    new double[][]{
        {1, 1000, 0.001, 6894.76, 100000, 101325, 133.32, 0.1, 9.806}, 
        {1, 0.0166, 0.000277, 1000}, 
        {1, 0.1, 0.001, 47.8803, 1, 0.001, 6894.7, 6894.7}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Specific Heat Capacity", new String[]{"Energy", "Mass × ΔTemp", "Spec. Heat"},
    new String[][]{
        {"J", "kJ", "MJ", "cal", "kcal", "BTU", "Wh", "kWh", "erg"}, 
        {"kg·K", "g·K", "kg·°C", "lb·°F", "g·°C", "lb·°C"}, 
        {"J/(kg·K)", "kJ/(kg·K)", "cal/(g·°C)", "kcal/(kg·°C)", "BTU/(lb·°F)", "J/(g·K)"}
    },
    new double[][]{
        {1, 1000, 1E6, 4.184, 4184, 1055.06, 3600, 3.6E6, 1E-7}, 
        {1, 0.001, 1, 0.252, 0.001, 0.4536}, 
        {1, 1000, 4184, 4184, 4186.8, 1000}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Thermal Conductivity", new String[]{"Power", "Length × ΔTemp", "Conductivity"},
    new String[][]{
        {"W", "kW", "MW", "cal/s", "kcal/h", "BTU/h", "ft·lb/s"}, 
        {"m·K", "cm·K", "mm·K", "in·K", "ft·°F"}, 
        {"W/(m·K)", "W/(cm·K)", "cal/(s·cm·°C)", "kcal/(h·m·°C)", "BTU/(h·ft·°F)", "BTU·in/(h·ft²·°F)"}
    },
    new double[][]{
        {1, 1000, 1E6, 4.184, 1.163, 0.29307, 1.3558}, 
        {1, 0.01, 0.001, 0.0254, 0.1693}, 
        {1, 100, 418.4, 1.163, 1.7307, 0.1442}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Volumetric Flow Rate", new String[]{"Volume", "Time", "Flow Rate"},
    new String[][]{
        {"m³", "L", "mL", "cm³", "ft³", "in³", "gal", "qt", "pt", "fl oz", "bbl"}, 
        {"s", "min", "hr", "ms", "day", "yr"}, 
        {"m³/s", "m³/h", "L/s", "L/min", "L/h", "ft³/s", "ft³/min", "gpm (US)", "gph (US)", "cm³/s", "mL/min"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1E-6, 0.0283168, 1.6387E-5, 0.0037854, 0.000946, 0.000473, 2.957E-5, 0.15898}, 
        {1, 60, 3600, 0.001, 86400, 3.1536E7}, 
        {1, 0.0002778, 0.001, 1.666E-5, 2.777E-7, 0.0283168, 0.0004719, 6.309E-5, 1.051E-6, 1E-6, 1.666E-8}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));
    addFormula("Radioactivity", new String[]{"Decays", "Time", "Activity"},
    new String[][]{
        {"counts", "decays", "particles", "events"}, 
        {"s", "min", "hr", "day", "yr", "ms", "μs", "ns", "wk"}, 
        {"Bq", "kBq", "MBq", "GBq", "TBq", "Ci", "mCi", "μCi", "nCi", "pCi", "Rd", "kRd", "MRd", "dps", "dpm"}
    },
    new double[][]{
        {1, 1, 1, 1}, 
        {1, 60, 3600, 86400, 3.1536E7, 0.001, 1E-6, 1E-9, 604800}, 
        {1, 1000, 1E6, 1E9, 1E12, 3.7E10, 3.7E7, 3.7E4, 37, 0.037, 1E6, 1E9, 1E12, 1, 0.016667}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Absorbed Dose", new String[]{"Energy", "Mass", "Dose"},
    new String[][]{
        {"J", "kJ", "mJ", "μJ", "cal", "kcal", "erg", "Wh", "eV", "MeV"}, 
        {"kg", "g", "mg", "μg", "lb", "oz", "ton (m)", "st"}, 
        {"Gy", "mGy", "μGy", "nGy", "rad", "mrad", "μrad", "erg/g", "J/kg"}
    },
    new double[][]{
        {1, 1000, 0.001, 1E-6, 4.184, 4184, 1E-7, 3600, 1.602E-19, 1.602E-13}, 
        {1, 0.001, 1E-6, 1E-9, 0.453592, 0.028349, 1000, 6.35029}, 
        {1, 0.001, 1E-6, 1E-9, 0.01, 0.0001, 1E-5, 0.0001, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Dose Equivalent", new String[]{"Absorbed Dose", "Quality Factor (Q)", "Dose Equiv."},
    new String[][]{
        {"Gy", "mGy", "μGy", "rad", "mrad"}, 
        {"-"}, 
        {"Sv", "mSv", "μSv", "nSv", "rem", "mrem", "μrem"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 0.01, 0.0001}, 
        {1}, 
        {1, 0.001, 1E-6, 1E-9, 0.01, 0.0001, 1E-5}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Illuminance", new String[]{"Luminous Flux", "Area", "Illuminance"},
    new String[][]{
        {"lm (lumen)", "mlm", "klm"}, 
        {"m²", "cm²", "mm²", "ft²", "in²", "yd²"}, 
        {"lx (lux)", "fc (foot-candle)", "ph (phot)", "nox", "lm/m²", "lm/ft²"}
    },
    new double[][]{
        {1, 0.001, 1000}, 
        {1, 1E-4, 1E-6, 0.0929, 0.000645, 0.8361}, 
        {1, 10.7639, 10000, 0.001, 1, 10.7639}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Optical Power", new String[]{"Const (1)", "Focal Length", "Optical Power"},
    new String[][]{
        {"-"}, 
        {"m", "cm", "mm", "in", "ft"}, 
        {"D (dioptre)", "1/m", "1/cm", "1/in"}
    },
    new double[][]{
        {1}, 
        {1, 0.01, 0.001, 0.0254, 0.3048}, 
        {1, 1, 100, 39.3701}
    },
    (v, t) -> t == 2 ? 1 / v[1] : 1 / v[2]);
    addFormula("Bernoulli's Equation", new String[]{"Pressure + 0.5ρv²", "ρgh", "Total Constant"},
    new String[][]{
        {"Pa", "kPa", "psi", "bar", "atm"}, 
        {"Pa", "kPa", "psi", "bar", "atm"}, 
        {"Pa", "kPa", "psi", "bar", "atm"}
    },
    new double[][]{
        {1, 1000, 6894.76, 100000, 101325}, 
        {1, 1000, 6894.76, 100000, 101325}, 
        {1, 1000, 6894.76, 100000, 101325}
    },
    (v, t) -> t == 2 ? v[0] + v[1] : (t == 1 ? v[2] - v[0] : v[2] - v[1]));

addFormula("Brewster's Angle", new String[]{"n2 (Refractive Index)", "n1 (Refractive Index)", "Angle (θb)"},
    new String[][]{
        {"index"}, {"index"}, {"rad", "deg", "grad"}
    },
    new double[][]{
        {1}, {1}, {1, 0.017453, 0.015708}
    },
    (v, t) -> t == 2 ? Math.atan(v[0] / v[1]) : (t == 0 ? v[1] * Math.tan(v[2]) : v[0] / Math.tan(v[2])));

addFormula("Buoyancy", new String[]{"Fluid Density", "Displaced Vol", "Buoyant Force"},
    new String[][]{
        {"kg/m³", "g/cm³", "lb/ft³"}, {"m³", "L", "ft³", "cm³"}, {"N", "lbf", "kgf", "dyn"}
    },
    new double[][]{
        {1, 1000, 16.018}, {1, 0.001, 0.0283, 1E-6}, {1, 4.448, 9.806, 1E-5}
    },
    (v, t) -> t == 2 ? v[0] * v[1] * 9.80665 : (t == 1 ? v[2] / (v[0] * 9.80665) : v[2] / (v[1] * 9.80665)));

addFormula("Drag Equation", new String[]{"Drag Coeff × Area", "0.5 × Density × v²", "Drag Force"},
    new String[][]{
        {"m²", "cm²", "ft²", "in²"}, {"Pa", "kPa", "psi", "bar"}, {"N", "lbf", "kN", "dyn"}
    },
    new double[][]{
        {1, 0.0001, 0.0929, 0.000645}, {1, 1000, 6894.76, 100000}, {1, 4.448, 1000, 1E-5}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Freefall Distance", new String[]{"Gravity (g)", "Time (t)", "Distance (h)"},
    new String[][]{
        {"m/s²", "ft/s²", "g-unit"}, {"s", "min", "ms"}, {"m", "ft", "km", "mi"}
    },
    new double[][]{
        {1, 0.3048, 9.806}, {1, 60, 0.001}, {1, 0.3048, 1000, 1609.34}
    },
    (v, t) -> t == 2 ? 0.5 * v[0] * Math.pow(v[1], 2) : (t == 1 ? Math.sqrt((2 * v[2]) / v[0]) : (2 * v[2]) / Math.pow(v[1], 2)));

addFormula("Friction Force", new String[]{"Coeff (μ)", "Normal Force", "Friction Force"},
    new String[][]{
        {"coeff"}, {"N", "lbf", "kgf", "kN"}, {"N", "lbf", "kgf", "kN"}
    },
    new double[][]{
        {1}, {1, 4.448, 9.806, 1000}, {1, 4.448, 9.806, 1000}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Froude Number", new String[]{"Velocity", "sqrt(g × Length)", "Fr Number"},
    new String[][]{
        {"m/s", "km/h", "mph", "ft/s"}, {"m/s", "km/h", "mph", "ft/s"}, {"ratio"}
    },
    new double[][]{
        {1, 0.277, 0.447, 0.3048}, {1, 0.277, 0.447, 0.3048}, {1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 0 ? v[2] * v[1] : v[0] / v[2]));

addFormula("Hydraulic Radius", new String[]{"Area", "Wetted Perimeter", "Radius (Rh)"},
    new String[][]{
        {"m²", "cm²", "ft²", "in²"}, {"m", "cm", "ft", "in"}, {"m", "cm", "ft", "in"}
    },
    new double[][]{
        {1, 1E-4, 0.0929, 0.000645}, {1, 0.01, 0.3048, 0.0254}, {1, 0.01, 0.3048, 0.0254}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Hydrostatic Pressure", new String[]{"Density × Gravity", "Depth (h)", "Pressure"},
    new String[][]{
        {"N/m³", "lb/ft³", "kgf/m³"}, {"m", "ft", "cm", "in"}, {"Pa", "kPa", "psi", "bar", "atm"}
    },
    new double[][]{
        {1, 157.08, 9.806}, {1, 0.3048, 0.01, 0.0254}, {1, 1000, 6894.76, 100000, 101325}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Refractive Index", new String[]{"Speed in Vacuum (c)", "Speed in Medium (v)", "Index (n)"},
    new String[][]{
        {"m/s", "km/s", "c"}, {"m/s", "km/s", "c"}, {"index"}
    },
    new double[][]{
        {1, 1000, 299792458}, {1, 1000, 299792458}, {1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Kinetic Energy", new String[]{"Mass", "Velocity", "Energy (K)"},
    new String[][]{
        {"kg", "g", "lb", "slug"}, {"m/s", "km/h", "mph", "ft/s"}, {"J", "kJ", "cal", "eV", "BTU", "ft-lb"}
    },
    new double[][]{
        {1, 0.001, 0.453, 14.59}, {1, 0.277, 0.447, 0.3048}, {1, 1000, 4.184, 1.6E-19, 1055, 1.355}
    },
    (v, t) -> t == 2 ? 0.5 * v[0] * Math.pow(v[1], 2) : (t == 1 ? Math.sqrt((2 * v[2]) / v[0]) : (2 * v[2]) / Math.pow(v[1], 2)));

addFormula("Potential Energy", new String[]{"Mass", "Gravity × Height", "Energy (U)"},
    new String[][]{
        {"kg", "g", "lb"}, {"J/kg", "ft-lb/lb"}, {"J", "kJ", "cal", "BTU", "ft-lb"}
    },
    new double[][]{
        {1, 0.001, 0.453}, {1, 2.989}, {1, 1000, 4.184, 1055, 1.355}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Knudsen Number", new String[]{"Mean Free Path", "Length Scale (L)", "Kn Number"},
    new String[][]{
        {"m", "μm", "nm", "Å"}, {"m", "cm", "mm", "μm"}, {"ratio"}
    },
    new double[][]{
        {1, 1E-6, 1E-9, 1E-10}, {1, 0.01, 0.001, 1E-6}, {1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Length Contraction", new String[]{"Proper Length (L0)", "Lorentz Factor (γ)", "Observed (L)"},
    new String[][]{
        {"m", "km", "ly", "AU"}, {"gamma"}, {"m", "km", "ly", "AU"}
    },
    new double[][]{
        {1, 1000, 9.46E15, 1.49E11}, {1}, {1, 1000, 9.46E15, 1.49E11}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Lightning Distance", new String[]{"Time (Thunder)", "Speed of Sound", "Distance"},
    new String[][]{
        {"s", "ms"}, {"m/s", "km/h", "mph", "ft/s"}, {"m", "km", "ft", "mi"}
    },
    new double[][]{
        {1, 0.001}, {1, 0.277, 0.447, 0.3048}, {1, 1000, 0.3048, 1609}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Malus' Law", new String[]{"Initial Intensity", "cos²(θ)", "Final Intensity"},
    new String[][]{
        {"W/m²", "lux"}, {"ratio"}, {"W/m²", "lux"}
    },
    new double[][]{
        {1, 1}, {1}, {1, 1}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Period of Pendulum", new String[]{"Length (L)", "Gravity (g)", "Period (T)"},
    new String[][]{
        {"m", "cm", "ft", "in"}, {"m/s²", "ft/s²", "g-unit"}, {"s", "min", "ms"}
    },
    new double[][]{
        {1, 0.01, 0.3048, 0.0254}, {1, 0.3048, 9.806}, {1, 60, 0.001}
    },
    (v, t) -> t == 2 ? 2 * Math.PI * Math.sqrt(v[0] / v[1]) : (t == 0 ? (Math.pow(v[2] / (2 * Math.PI), 2)) * v[1] : v[0] / Math.pow(v[2] / (2 * Math.PI), 2)));

addFormula("Poisson's Ratio", new String[]{"Lateral Strain", "Axial Strain", "Poisson's (ν)"},
    new String[][]{
        {"strain"}, {"strain"}, {"ratio"}
    },
    new double[][]{
        {1}, {1}, {1}
    },
    (v, t) -> t == 2 ? -v[0] / v[1] : (t == 1 ? -v[0] / v[2] : -v[1] * v[2]));

addFormula("Reduced Mass", new String[]{"m1 × m2", "m1 + m2", "Reduced Mass (μ)"},
    new String[][]{
        {"kg²", "g²", "lb²"}, {"kg", "g", "lb"}, {"kg", "g", "lb", "u"}
    },
    new double[][]{
        {1, 1E-6, 0.205}, {1, 0.001, 0.453}, {1, 0.001, 0.453, 1.66E-27}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 0 ? v[2] * v[1] : v[0] / v[2]));

addFormula("Rotational K.E.", new String[]{"Inertia (I)", "Ang. Vel (ω)", "Energy (K_rot)"},
    new String[][]{
        {"kg·m²", "lb·ft²"}, {"rad/s", "rpm", "deg/s"}, {"J", "kJ", "cal", "ft-lb"}
    },
    new double[][]{
        {1, 0.042}, {1, 0.1047, 0.0174}, {1, 1000, 4.184, 1.355}
    },
    (v, t) -> t == 2 ? 0.5 * v[0] * Math.pow(v[1], 2) : (t == 1 ? Math.sqrt((2 * v[2]) / v[0]) : (2 * v[2]) / Math.pow(v[1], 2)));

addFormula("Stress", new String[]{"Force", "Area", "Stress (σ)"},
    new String[][]{
        {"N", "kN", "lbf", "kgf"}, {"m²", "mm²", "in²", "ft²"}, {"Pa", "MPa", "psi", "ksi", "bar"}
    },
    new double[][]{
        {1, 1000, 4.448, 9.806}, {1, 1E-6, 0.000645, 0.0929}, {1, 1E6, 6894.76, 6894757, 100000}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Strain", new String[]{"Δ Length", "Original Length", "Strain (ε)"},
    new String[][]{
        {"m", "cm", "mm", "in"}, {"m", "cm", "mm", "in"}, {"unitless", "percent", "microstrain"}
    },
    new double[][]{
        {1, 0.01, 0.001, 0.0254}, {1, 0.01, 0.001, 0.0254}, {1, 0.01, 1E-6}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));
    }

    private static void addFormula(String name, String[] l, String[][] u, double[][] f, java.util.function.BiFunction<double[], Integer, Double> s) {
        physicsRegistry.put(name, new PhysData(l, u, f, s));
    }

    // --- 3. THE UI BUILDER ---
    public static JPanel createPhysicsPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(currentTheme.bgColor);

        JPanel leftSide = new JPanel(new BorderLayout(0, 20));
        leftSide.setBackground(currentTheme.bgColor);
        leftSide.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 15));

        physicsSelector = new JComboBox<>(physicsRegistry.keySet().toArray(new String[0]));
        styleScientificSelector(physicsSelector);

        JPanel inputContainer = new JPanel(new GridLayout(3, 1, 0, 25));
        inputContainer.setBackground(currentTheme.bgColor);

        // Create fields with "Converter" style
        pf1 = createField(0, false); pf2 = createField(1, false); pf3 = createField(2, true);
        pu1 = createUnitCombo(); pu2 = createUnitCombo(); pu3 = createUnitCombo();
        pl1 = createLabel(); pl2 = createLabel(); pl3 = createLabel();

        inputContainer.add(assemblePhysicsRow(pl1, pf1, pu1));
        inputContainer.add(assemblePhysicsRow(pl2, pf2, pu2));
        inputContainer.add(assemblePhysicsRow(pl3, pf3, pu3));

        physicsSelector.addActionListener(e -> updatePhysicsSelection());
        updatePhysicsSelection(); 

        leftSide.add(physicsSelector, BorderLayout.NORTH);
        leftSide.add(inputContainer, BorderLayout.CENTER);

        // Keypad on the Right
        JPanel keypad = createKeypad(val -> handleScientificInput(val, true));
        keypad.setPreferredSize(new Dimension(280, 0));

        main.add(leftSide, BorderLayout.CENTER);
        main.add(keypad, BorderLayout.EAST);
        return main;
    }

    // --- 4. HELPER & LOGIC METHODS ---
    private static void updatePhysicsSelection() {
        isInternalUpdate = true;
        PhysData data = physicsRegistry.get(physicsSelector.getSelectedItem());
        pl1.setText(data.labels[0]); pl2.setText(data.labels[1]); pl3.setText(data.labels[2]);
        
        setupCombo(pu1, data.units[0]);
        setupCombo(pu2, data.units[1]);
        setupCombo(pu3, data.units[2]);
        
        pf1.setText(""); pf2.setText(""); pf3.setText("");
        isInternalUpdate = false;
    }

    private static void runCalculation() {
        if (isInternalUpdate) return;
        PhysData data = physicsRegistry.get(physicsSelector.getSelectedItem());
        try {
            double v1 = parse(pf1.getText()) * data.factors[0][pu1.getSelectedIndex()];
            double v2 = parse(pf2.getText()) * data.factors[1][pu2.getSelectedIndex()];
            double v3 = parse(pf3.getText()) * data.factors[2][pu3.getSelectedIndex()];

            int target = (activeFieldIdx == 2) ? 0 : 2; 
            double result = data.solver.apply(new double[]{v1, v2, v3}, target);

            isInternalUpdate = true;
            String output = format(result / data.factors[target][(target == 0 ? pu1 : pu3).getSelectedIndex()]);
            if (target == 0) pf1.setText(output); else pf3.setText(output);
            isInternalUpdate = false;
        } catch (Exception e) {}
    }

    private static JPanel assemblePhysicsRow(JLabel l, JTextField f, JComboBox<String> u) {
        JPanel p = new JPanel(new BorderLayout(10, 5));
        p.setBackground(currentTheme.bgColor);
        p.add(l, BorderLayout.NORTH);
        p.add(f, BorderLayout.CENTER);
        p.add(u, BorderLayout.EAST);
        return p;
    }

    private static JTextField createField(int idx, boolean isDerived) {
        JTextField f = new JTextField();
        f.setBackground(currentTheme.bgColor);
        f.setForeground(isDerived ? new Color(0, 255, 255) : currentTheme.foreground);
        f.setCaretColor(new Color(0, 150, 255));
        f.setFont(new Font("SansSerif", Font.PLAIN, 22));
        // No box border, only bottom line
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
        
        f.addFocusListener(new FocusAdapter() { 
            @Override public void focusGained(FocusEvent e) { 
                activeFieldIdx = idx; 
                f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 255)));
            }
            @Override public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
            }
        });
        f.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { runCalculation(); }
            public void removeUpdate(DocumentEvent e) { runCalculation(); }
            public void changedUpdate(DocumentEvent e) { runCalculation(); }
        });
        return f;
    }

    private static JComboBox<String> createUnitCombo() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(currentTheme.bgColor);
        cb.setForeground(currentTheme.foreground);
        cb.setBorder(BorderFactory.createEmptyBorder());
        cb.addActionListener(e -> runCalculation());
        return cb;
    }

    private static void setupCombo(JComboBox<String> cb, String[] items) {
        cb.removeAllItems();
        for (String s : items) cb.addItem(s);
    }

    private static void styleScientificSelector(JComboBox<String> cb) {
        cb.setBackground(currentTheme.functionButton);
        cb.setForeground(currentTheme.foreground);
        cb.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private static JLabel createLabel() {
        JLabel l = new JLabel();
        l.setForeground(new Color(120, 120, 120));
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        return l;
    }

    private static double parse(String s) {
        try { return s.isEmpty() ? 0 : Double.parseDouble(s); } catch (Exception e) { return 0; }
    }

    private static String format(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) return "0";
        return String.format("%.4f", d).replaceAll("0*$", "").replaceAll("\\.$", "");
    }
    

    static class ChemData {
        String[] labels;
        String[][] units;
        double[][] factors;
        double[] offsets; 
        java.util.function.BiFunction<double[], Integer, Double> solver;

        ChemData(String[] l, String[][] u, double[][] f, double[] o, java.util.function.BiFunction<double[], Integer, Double> s) {
            this.labels = l; this.units = u; this.factors = f; this.offsets = o; this.solver = s;
        }
    }

    private static void initializeChemistryData() {
        // --- CHEMISTRY REGISTRY ---

        addFormula("Molar Energy", new String[]{"Total Energy", "Amount of Substance", "Molar Energy"},
    new String[][]{
        {"J", "kJ", "MJ", "cal", "kcal", "eV", "BTU"}, 
        {"mol", "mmol", "μmol", "kmol"}, 
        {"J/mol", "kJ/mol", "cal/mol", "kcal/mol", "kJ/kmol"}
    },
    new double[][]{
        {1, 1000, 1E6, 4.184, 4184, 1.602E-19, 1055.06}, 
        {1, 0.001, 1E-6, 1000}, 
        {1, 1000, 4.184, 4184, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Atom Calculator", new String[]{"Moles", "Avogadro's Constant", "Number of Atoms"},
    new String[][]{
        {"mol", "mmol", "μmol", "kmol"}, 
        {"6.022E23"}, 
        {"atoms", "molecules", "particles"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000}, 
        {6.02214076E23}, 
        {1}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Reaction Rate", new String[]{"Δ Concentration", "Δ Time", "Reaction Rate"},
    new String[][]{
        {"M", "mM", "μM", "mol/L", "mol/m³"}, 
        {"s", "min", "hr", "ms"}, 
        {"M/s", "M/min", "M/hr", "mol/(L·s)", "mol/(m³·s)"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1, 0.001}, 
        {1, 60, 3600, 0.001}, 
        {1, 0.016667, 0.0002778, 1, 0.001}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Boyle's Law", new String[]{"Initial P × V", "Final Pressure", "Final Volume"},
    new String[][]{
        {"unit-constant"}, 
        {"Pa", "kPa", "bar", "atm", "psi", "torr", "mmHg"}, 
        {"L", "mL", "m³", "cm³", "ft³", "gal"}
    },
    new double[][]{
        {1}, 
        {1, 1000, 100000, 101325, 6894.76, 133.322, 133.322}, 
        {1, 0.001, 1000, 0.001, 28.3168, 3.7854}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Charles's Law", new String[]{"Initial V / T", "Final Temperature", "Final Volume"},
    new String[][]{
        {"unit-constant"}, 
        {"K", "°C (abs)", "°F (abs)", "°R"}, 
        {"L", "mL", "m³", "cm³", "ft³", "in³"}
    },
    new double[][]{
        {1}, 
        {1, 1, 0.5555, 0.5555}, 
        {1, 0.001, 1000, 0.001, 28.3168, 0.01638}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Combined Gas Law", new String[]{"P1·V1 / T1", "Final P × V", "Final Temp"},
    new String[][]{
        {"unit-constant"}, 
        {"J", "L·atm", "cal", "ft·lb"}, 
        {"K", "°C (abs)", "°F (abs)", "°R"}
    },
    new double[][]{
        {1}, 
        {1, 101.325, 4.184, 1.3558}, 
        {1, 1, 0.5555, 0.5555}
    },
    (v, t) -> t == 2 ? v[1] / v[0] : (t == 1 ? v[0] * v[2] : v[1] / v[2]));
            addFormula("Density (Chemistry)", new String[]{"Mass", "Volume", "Density"},
    new String[][]{
        {"g", "kg", "mg", "μg", "lb", "oz"}, 
        {"cm³", "mL", "L", "m³", "dm³", "μL", "fl oz"}, 
        {"g/cm³", "g/mL", "kg/m³", "g/L", "mg/mL", "lb/ft³"}
    },
    new double[][]{
        {1, 1000, 0.001, 1E-6, 453.59, 28.35}, 
        {1, 1, 1000, 1E6, 1000, 0.001, 29.57}, 
        {1, 1, 0.001, 0.001, 1, 0.01602}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Molarity", new String[]{"Moles of Solute", "Volume of Solution", "Molarity (M)"},
    new String[][]{
        {"mol", "mmol", "μmol", "kmol"}, 
        {"L", "mL", "cm³", "dm³", "m³", "μL"}, 
        {"mol/L (M)", "mmol/L", "mol/m³", "M (molar)"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 1000}, 
        {1, 0.001, 0.001, 1, 1000, 1E-6}, 
        {1, 0.001, 0.001, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Molality", new String[]{"Moles of Solute", "Mass of Solvent", "Molality (m)"},
    new String[][]{
        {"mol", "mmol", "μmol"}, 
        {"kg", "g", "mg", "lb"}, 
        {"mol/kg (m)", "mmol/g", "mol/lb"}
    },
    new double[][]{
        {1, 0.001, 1E-6}, 
        {1, 0.001, 1E-6, 0.4536}, 
        {1, 1, 2.2046}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Mole Fraction", new String[]{"Moles of Component", "Total Moles", "Mole Fraction (χ)"},
    new String[][]{
        {"mol", "mmol"}, {"mol", "mmol"}, {"ratio", "percent"}
    },
    new double[][]{
        {1, 0.001}, {1, 0.001}, {1, 0.01}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Ideal Gas Law", new String[]{"Pressure × Volume", "Moles × Temp", "Gas Constant (R)"},
    new String[][]{
        {"J", "L·atm", "cm³·atm", "m³·Pa"}, 
        {"mol·K", "mol·°C (abs)"}, 
        {"J/(mol·K)", "L·atm/(mol·K)", "cal/(mol·K)", "m³·Pa/(mol·K)"}
    },
    new double[][]{
        {1, 101.325, 0.1013, 1}, 
        {1, 1}, 
        {1, 0.08206, 1.987, 1}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));

addFormula("Mass Percent", new String[]{"Mass of Solute", "Mass of Solution", "Mass %"},
    new String[][]{
        {"g", "kg", "mg"}, {"g", "kg", "mg"}, {"%", "fraction", "ppm", "ppb"}
    },
    new double[][]{
        {1, 1000, 0.001}, {1, 1000, 0.001}, {1, 100, 0.0001, 1E-7}
    },
    (v, t) -> t == 2 ? (v[0] / v[1]) * 100 : (t == 1 ? (v[0] * 100) / v[2] : (v[2] * v[1]) / 100));

addFormula("Beer-Lambert Law", new String[]{"Molar Absorptivity × Path", "Concentration", "Absorbance (A)"},
    new String[][]{
        {"L/(mol·cm)·cm"}, {"mol/L (M)", "mM"}, {"absorbance"}
    },
    new double[][]{
        {1}, {1, 0.001}, {1}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("pH / pOH", new String[]{"-log10", "Ion Concentration", "pH or pOH"},
    new String[][]{
        {"-"}, {"mol/L", "M", "mM", "μM"}, {"scale"}
    },
    new double[][]{
        {1}, {1, 1, 0.001, 1E-6}, {1}
    },
    (v, t) -> t == 2 ? -Math.log10(v[1]) : (t == 1 ? Math.pow(10, -v[2]) : 0));

addFormula("Rate Law (General)", new String[]{"Rate Constant (k)", "Concentration Product", "Reaction Rate"},
    new String[][]{
        {"units vary"}, {"M^n"}, {"M/s", "M/min", "mol/(L·s)"}
    },
    new double[][]{
        {1}, {1}, {1, 0.0166, 1}
    },
    (v, t) -> t == 2 ? v[0] * v[1] : (t == 1 ? v[2] / v[0] : v[2] / v[1]));

addFormula("Specific Volume", new String[]{"Volume", "Mass", "Spec. Vol (v)"},
    new String[][]{
        {"m³", "L", "cm³", "ft³"}, {"kg", "g", "lb", "mg"}, {"m³/kg", "L/kg", "cm³/g", "ft³/lb"}
    },
    new double[][]{
        {1, 0.001, 1E-6, 0.0283}, {1, 0.001, 0.4536, 1E-6}, {1, 0.001, 1, 0.0624}
    },
    (v, t) -> t == 2 ? v[0] / v[1] : (t == 1 ? v[0] / v[2] : v[1] * v[2]));
    }

    private static void addChem(String name, String[] l, String[][] u, double[][] f, java.util.function.BiFunction<double[], Integer, Double> s) {
        chemRegistry.put(name, new ChemData(l, u, f, new double[]{0,0,0}, s));
    }

    public static JPanel createChemistryPanel() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(currentTheme.bgColor);

        JPanel leftSide = new JPanel(new BorderLayout(0, 20));
        leftSide.setBackground(currentTheme.bgColor);
        leftSide.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 15));

        chemSelector = new JComboBox<>(chemRegistry.keySet().toArray(new String[0]));
        styleScientificSelector(chemSelector);

        JPanel inputContainer = new JPanel(new GridLayout(3, 1, 0, 25));
        inputContainer.setBackground(currentTheme.bgColor);

        cf1 = createChemField(0, false); cf2 = createChemField(1, false); cf3 = createChemField(2, true);
        cu1 = createChemUnitCombo(); cu2 = createChemUnitCombo(); cu3 = createChemUnitCombo();
        cl1 = createLabel(); cl2 = createLabel(); cl3 = createLabel();

        inputContainer.add(assembleRow(cl1, cf1, cu1));
        inputContainer.add(assembleRow(cl2, cf2, cu2));
        inputContainer.add(assembleRow(cl3, cf3, cu3));

        chemSelector.addActionListener(e -> updateChemSelection());
        updateChemSelection();

        leftSide.add(chemSelector, BorderLayout.NORTH);
        leftSide.add(inputContainer, BorderLayout.CENTER);

        // Keypad on the Right
        JPanel keypad = createKeypad(val -> handleScientificInput(val, false));
        keypad.setPreferredSize(new Dimension(280, 0));

        main.add(leftSide, BorderLayout.CENTER);
        main.add(keypad, BorderLayout.EAST);
       
        main.revalidate(); 
        main.repaint();
        updateChemSelection();
        return main;
    }
    class FormulaData {
    String name;
    String[] variables;
    String[][] units;
    double[][] conversions;
    java.util.function.BiFunction<double[], Integer, Double> calculation;

    FormulaData(String name, String[] vars, String[][] u, double[][] c, java.util.function.BiFunction<double[], Integer, Double> calc) {
        this.name = name;
        this.variables = vars;
        this.units = u;
        this.conversions = c;
        this.calculation = calc;
    }
    }

    private static void updateChemSelection() {
    Object selected = chemSelector.getSelectedItem();
    
    // 1. If nothing is selected, we still want to see the UI, just empty.
    if (selected == null) {
        // Clear labels so it doesn't look 'stuck'
        cl1.setText(""); cl2.setText(""); cl3.setText("");
        return; 
    }

    isChemInternalUpdate = true;
    
    try {
        ChemData data = chemRegistry.get(selected);
        if (data != null) {
            // 2. Set the Labels (This makes it look like the Physics panel)
            cl1.setText(data.labels[0]); 
            cl2.setText(data.labels[1]); 
            cl3.setText(data.labels[2]);

            // 3. Set the Units
            setupCombo(cu1, data.units[0]); 
            setupCombo(cu2, data.units[1]); 
            setupCombo(cu3, data.units[2]);

            // 4. Clear the fields for new input
            cf1.setText(""); cf2.setText(""); cf3.setText("");
        }
    } finally {
        isChemInternalUpdate = false;
    }

    // 5. THE MISSING PIECE: Force the panel to refresh its look
    if (createChemistryPanel() != null) {
        createChemistryPanel().revalidate();
        createChemistryPanel().repaint();
    }
    }

    private static void runChemCalc() {
    if (isChemInternalUpdate) return;

    // 1. Get the selected items first
    Object selectedChem = chemSelector.getSelectedItem();
    Object unit1 = cu1.getSelectedItem();
    Object unit2 = cu2.getSelectedItem();
    Object unit3 = cu3.getSelectedItem();

    // 2. GUARD: If the selector or units aren't ready, stop immediately
    if (selectedChem == null || unit1 == null || unit2 == null || unit3 == null) {
        return;
    }

    // 3. SECURE ACCESS: Now it's safe to touch the TreeMap
    ChemData data = chemRegistry.get(selectedChem);
    if (data == null) return;

    try {
        double v1 = parse(cf1.getText());
        
        // Use the 'unit1' variable we already null-checked
        if (unit1.equals("°C")) v1 += 273.15;
        else if (unit1.equals("°F")) v1 = (v1 - 32) * 5/9 + 273.15;
        
        v1 *= data.factors[0][cu1.getSelectedIndex()];

        double v2 = parse(cf2.getText()) * data.factors[1][cu2.getSelectedIndex()];
        double v3 = parse(cf3.getText()) * data.factors[2][cu3.getSelectedIndex()];

        int target = (activeChemFieldIdx == 2) ? 0 : 2;
        double result = data.solver.apply(new double[]{v1, v2, v3}, target);

        isChemInternalUpdate = true;
        // Divide result back by the target factor before displaying
        cf3.setText(format(result / data.factors[2][cu3.getSelectedIndex()]));
        isChemInternalUpdate = false;
    } catch (Exception e) {
        // This will now actually catch math/parsing errors instead of crashing
        isChemInternalUpdate = false; 
    }
    }
    
    private static JPanel createKeypad(java.util.function.Consumer<String> callback) {
        JPanel p = new JPanel(new GridLayout(4, 3, 5, 5));
        p.setBackground(currentTheme.bgColor);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", ".", "0", "C"};
        for (String k : keys) {
            JButton b = new JButton(k);
            b.setFont(new Font("Segoe UI", Font.BOLD, 18));
            b.setBackground(currentTheme.regularButton);
            if(b.equals("C")) b.setForeground(currentTheme.clearColor);
            else b.setForeground(currentTheme.foreground);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
            b.addActionListener(e -> callback.accept(k));
            p.add(b);
        }
        return p;
    }

    private static JTextField createChemField(int idx, boolean isDerived) {
        JTextField f = new JTextField();
        f.setBackground(currentTheme.bgColor); 
        f.setForeground(isDerived ? new Color(0, 255, 255) : currentTheme.foreground);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
        
        f.addFocusListener(new java.awt.event.FocusAdapter() { 
            public void focusGained(java.awt.event.FocusEvent e) { 
                activeChemFieldIdx = idx; 
                f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 255)));
            } 
            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));
            }
        });
        f.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { runChemCalc(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { runChemCalc(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { runChemCalc(); }
        });
        return f;
    }

    private static JComboBox<String> createChemUnitCombo() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setBackground(currentTheme.bgColor);
        cb.setForeground(new Color(150, 150, 150));
        cb.setBorder(BorderFactory.createEmptyBorder());
        cb.addActionListener(e -> runChemCalc());
        return cb;
    }

    private static JPanel assembleRow(JLabel l, JTextField f, JComboBox<String> u) {
        JPanel p = new JPanel(new BorderLayout(10, 5));
        p.setBackground(currentTheme.bgColor);
        p.add(l, BorderLayout.NORTH); 
        p.add(f, BorderLayout.CENTER); 
        p.add(u, BorderLayout.EAST);
        return p;
    }

    private static void handleScientificInput(String val, boolean isPhys) {
        JTextField active = isPhys ? (activeFieldIdx == 0 ? pf1 : pf2) : (activeChemFieldIdx == 0 ? cf1 : cf2);
        if (val.equals("C")) active.setText("");
        else if (val.equals("CE")) {
            String s = active.getText();
            if (s.length() > 0) active.setText(s.substring(0, s.length() - 1));
        } else {
            active.setText(active.getText() + val);
        }
    }
    private static class ContourPanel extends JPanel {
    private String formula = "";
    private double zLevel = 0;

    public void updateData(String formula, double zLevel) {
        this.formula = formula;
        this.zLevel = zLevel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (formula.isEmpty()) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(currentTheme.foreground);
        
        // Simple 2D scanline to find where f(x,y) approx equals zLevel
        double step = 0.1;
        for (double x = -10; x <= 10; x += step) {
            for (double y = -10; y <= 10; y += step) {
                if (Math.abs(eval(formula, x, y, 0) - zLevel) < 0.1) {
                    int px = (int) (getWidth() / 2 + x * 20);
                    int py = (int) (getHeight() / 2 - y * 20);
                    g2d.fillRect(px, py, 2, 2);
                }
            }
        }
    }
    }
    public static JPanel createHelpPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(currentTheme.bgColor);
    panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    // --- 1. HEADER TITLE ---
    JLabel titleLabel = new JLabel("Application Guide & Help");
    titleLabel.setForeground(currentTheme.foreground);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(titleLabel);
    panel.add(Box.createVerticalStrut(10));

    // --- 2. SUBTITLE / INTRO ---
    JLabel subLabel = new JLabel("Below you'll find quick tips to navigate the app.");
    subLabel.setForeground(currentTheme.foreground);
    subLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(subLabel);
    panel.add(Box.createVerticalStrut(25));

    // --- 3. HELP CONTENT ---
    // Section: Graphing
    panel.add(createHelpSection("Exponentation(^) and Rooting(√)", 
        "Use ^ to carry out exponentation. " +
        "Use √x to find out the square root. n√x finds the 'n'th root of x."));
    panel.add(Box.createVerticalStrut(15));

    // Section: Themes
    panel.add(createHelpSection("2D Graphing", 
        "Graphing x^n graphs should be replaced with x*x*x...n times as the software currently does not support '^'." +
        "Arithmos is currently under progress and does not support linear, quadratic or cubic equation graphing in 2D"));
    panel.add(Box.createVerticalStrut(15));

    // Section: Formulas & Other Panels
    panel.add(createHelpSection("3D Graphing", 
        "Add, Subtract or Multiply any equation with 't' to get a moving graph" +
        "Arithmos is currently under progress and does not show the equations typed on the equation bar."));
    

    panel.add(Box.createVerticalGlue()); // Keeps everything cleanly pushed to the top

    // Force rendering update
    panel.revalidate();
    panel.repaint();

    return panel;
    }
    private static JPanel createHelpSection(String title, String bodyText) {
    JPanel section = new JPanel();
    section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    section.setBackground(currentTheme.bgColor);
    section.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Section Subheader
    JLabel sectionTitle = new JLabel(title);
    sectionTitle.setForeground(currentTheme.memoryDegColor); // Uses your accent accent color!
    sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
    sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Body text using JTextArea for reliable line wrapping
    JTextArea sectionBody = new JTextArea(bodyText);
    sectionBody.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    sectionBody.setForeground(currentTheme.foreground);
    sectionBody.setBackground(currentTheme.bgColor);
    sectionBody.setLineWrap(true);
    sectionBody.setWrapStyleWord(true);
    sectionBody.setEditable(false);
    sectionBody.setFocusable(false);
    sectionBody.setAlignmentX(Component.LEFT_ALIGNMENT);
    // Give it a tiny indent from the header
    sectionBody.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0)); 

    section.add(sectionTitle);
    section.add(sectionBody);
    
    return section;
    }
}