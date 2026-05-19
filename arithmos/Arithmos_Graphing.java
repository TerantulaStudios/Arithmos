import javax.swing.*;
import java.awt.*;
public class Arithmos_Graphing{
    static JFrame frame = new JFrame("Arithmos");
    static Color bgColor = new Color(0, 150, 255);
    static Color funcColor = new Color(64, 64, 64);
    static JPanel displayPanel = new JPanel(); 
    public static void main(String[]args){
        frame.setSize(800, 694);
        frame.setVisible(true);
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBounds(0, 0, 800, 694);
        displayPanel.setBackground(bgColor);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        topPanel.setBackground(funcColor);
        displayPanel.add(topPanel, BorderLayout.NORTH);        
        frame.add(displayPanel);
        
    }
}