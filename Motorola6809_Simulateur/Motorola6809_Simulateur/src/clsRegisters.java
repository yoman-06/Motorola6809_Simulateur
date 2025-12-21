import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class clsRegisters extends JPanel{

	//private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_PANEL = new Color(30, 41, 59);
    private static final Color BG_FIELD = new Color(51, 65, 85);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);
    private static final Color TEXT_LABEL = new Color(148, 163, 184);
    private static final Color TEXT_VALUE = new Color(56, 189, 248);
    private static final Color ACCENT_BLUE = new Color(14, 165, 233);
    private static final Color ACCENT_ORANGE = new Color(251, 146, 60);


	static JTextField txtPC;
	static JTextField txtinstruction;
	static JTextField txtS;
	static JTextField txtU;
	static JTextField txtA;
	static JTextField txtB;
	static JTextField txtDP;
	static JTextField txtX;
	static JTextField txtY;
	static JTextField txtFLAG;
	
	static public int PC = 64512;
	static public int S; 
	static public int U; 
	static public int X, Y; 
	static public int A, B, D; 
	static public int DP = 0; 
	static public int CC;
	
	public static final int CC_C = 1 << 0; 
	public static final int CC_V = 1 << 1; 
	public static final int CC_Z = 1 << 2;
	public static final int CC_N = 1 << 3; 
	public static final int CC_I = 1 << 4; 
	public static final int CC_H = 1 << 5;
	public static final int CC_F = 1 << 6;
	public static final int CC_E = 1 << 7;
	
	
	clsRegisters() {
        setLayout(null);
        setBackground(BG_PANEL);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel header = new JPanel();
        header.setBounds(20, 15, 340, 40);
        header.setBackground(new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(), 
                                       ACCENT_BLUE.getBlue(), 40));
        JLabel headerLabel = new JLabel("CPU REGISTERS");
        headerLabel.setFont(new Font("Monospace", Font.BOLD, 16));
        headerLabel.setForeground(ACCENT_BLUE);
        header.add(headerLabel);
        add(header);
        
        addLabel("PC", 40, 80);
        txtPC = createRegisterField(100, 75, 240, 35);
        add(txtPC);
        
        addLabel("INS", 40, 130);
        txtinstruction = createRegisterField(100, 125, 240, 35);
        add(txtinstruction);
        
        addLabel("S", 40, 180);
        txtS = createRegisterField(100, 175, 110, 35);
        add(txtS);
        
        addLabel("U", 230, 180);
        txtU = createRegisterField(260, 175, 110, 35);
        add(txtU);
        
        addLabel("A", 40, 240);
        txtA = createRegisterField(100, 235, 100, 35);
        add(txtA);
        
        JLabel aluLabel = new JLabel("ALU");
        aluLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        aluLabel.setForeground(ACCENT_BLUE);
        aluLabel.setBounds(210, 285, 60, 30);
        add(aluLabel);
        
        addLabel("B", 40, 350);
        txtB = createRegisterField(100, 345, 100, 35);
        add(txtB);
        
        addLabel("DP", 40, 410);
        txtDP = createRegisterField(100, 405, 100, 35);
        add(txtDP);
        
        JLabel flagLabel = new JLabel("           E F H I N Z V C");
        flagLabel.setFont(new Font("Monospace", Font.BOLD, 11));
        flagLabel.setForeground(TEXT_LABEL);
        flagLabel.setBounds(220, 387, 150, 15);
        add(flagLabel);
        
        txtFLAG = createRegisterField(220, 405, 150, 35);
        txtFLAG.setFont(new Font("Monospace", Font.PLAIN, 17));
        txtFLAG.setForeground(ACCENT_ORANGE);
        add(txtFLAG);
        
        addLabel("X", 40, 457);
        txtX = createRegisterField(100, 450, 110, 35);
        add(txtX);
        
        addLabel("Y", 230, 457);
        txtY = createRegisterField(260, 450, 110, 35);
        add(txtY);
        
        Reset();
    }

	private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospace", Font.BOLD, 16));
        label.setForeground(TEXT_LABEL);
        label.setBounds(x, y, 50, 20);
        add(label);
    }
    
    private JTextField createRegisterField(int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setFont(new Font("Monospace", Font.BOLD, 18));
        field.setForeground(TEXT_VALUE);
        field.setBackground(BG_FIELD);
        field.setCaretColor(TEXT_VALUE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(ACCENT_BLUE);
        
        // A to ALU
        g2D.drawLine(150, 252, 150, 275);
        g2D.drawLine(150, 275, 230, 285);
        
        // ALU to B
        g2D.drawLine(230, 315, 150, 325);
        g2D.drawLine(150, 325, 150, 345);
        
        // ALU shape (simplified)
        int[] xPoints = {210, 250, 260, 250, 210, 200};
        int[] yPoints = {285, 285, 300, 315, 315, 300};
        g2D.setColor(new Color(ACCENT_BLUE.getRed(), ACCENT_BLUE.getGreen(), 
                               ACCENT_BLUE.getBlue(), 60));
        g2D.fillPolygon(xPoints, yPoints, 6);
        g2D.setColor(ACCENT_BLUE);
        g2D.drawPolygon(xPoints, yPoints, 6);
    }

	
	static public void Reset() {
        PC = 0xFC00;
        S = 0x0000;
        U = 0x0000;
        X = Y = 0x0000;
        A = B = 0x00;
        D = 0x00;
        DP = 0x00;
        CC = 0;
        
        txtY.setText(formatTo4Digits(Y));
        txtX.setText(formatTo4Digits(X));
        txtFLAG.setText(formatTo8Digits(CC));
        txtDP.setText(formatTo2Digits(DP));
        txtB.setText(formatTo2Digits(B));
        txtA.setText(formatTo2Digits(A));
        txtU.setText(formatTo4Digits(U));
        txtPC.setText("FC00");
        txtS.setText(formatTo4Digits(S));
        txtinstruction.setText("--");
    }

	public static String formatTo8Digits(int number) {
	    return String.format("%08X", number);
	}
	public static String formatTo4Digits(String number) {
	    return String.format("%04X", number);
	}
    public static String formatTo4Digits(int number) {
	    return String.format("%04X", number);
	}
	public static String formatTo2Digits(int number) {
	    return String.format("%02X", number);
	}
// seraj
	public static void assignFlag() {
	    clsRegisters.txtFLAG.setText(
	        String.format("%8s", Integer.toBinaryString(clsRegisters.CC)).replace(' ', '0')
	    );
    }
	
	
	
	
}
