
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class clsMoto6809 extends JFrame implements ActionListener{
	
	private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_PANEL = new Color(30, 41, 59);
    private static final Color BG_DARKER = new Color(51, 65, 85);
    private static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color ACCENT_BLUE = new Color(56, 189, 248);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    private static final Color ACCENT_ORANGE = new Color(245, 158, 11);
    private static final Color ACCENT_RED = new Color(239, 68, 68);


	static JTextArea txtEditeur;
	ImageIcon icon;
    public static JButton btnCompiler;
    public static JButton btnDebug;
    public static JButton btnExecuter;
    public static JButton btnReset;
    
	public clsMoto6809()
	{
		this.setTitle("MOTOROLA 6809 SIMULATOR");
        this.setSize(1400, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon = new ImageIcon("microprocpng.png");
        this.setIconImage(icon.getImage());
        this.setLayout(null);
        this.getContentPane().setBackground(BG_DARK);
        
      
        JPanel editorContainer = createEditorPanel();
        editorContainer.setBounds(720, 20, 660, 720);
        this.add(editorContainer);
        
        
        JPanel ramContainer = createMemoryContainer("RAM MEMORY", ACCENT_PURPLE);
        ramContainer.setBounds(420, 20, 280, 360);
        clsRAM ramPanel = new clsRAM();
        ramContainer.add(ramPanel, BorderLayout.CENTER);
        this.add(ramContainer);
        
        
        JPanel romContainer = createMemoryContainer("ROM MEMORY", ACCENT_ORANGE);
        romContainer.setBounds(420, 400, 280, 360);
        clsROM romPanel = new clsROM();
        romContainer.add(romPanel, BorderLayout.CENTER);
        this.add(romContainer);
        
        clsRegisters cpuPanel = new clsRegisters();
        cpuPanel.setBounds(20, 20, 380, 740);
        this.add(cpuPanel);
        
        /*JPanel statusBar = createStatusBar();
        statusBar.setBounds(0, 840, 1400, 60);
        this.add(statusBar);*/
        
        
        
        this.setVisible(true);
	}
    
    private JPanel createMemoryContainer(String title, Color accentColor) {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setBackground(BG_PANEL);
        container.setBorder(createRoundedBorder());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                           accentColor.getBlue(), 40));
        headerPanel.setPreferredSize(new Dimension(0, 40));
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        headerLabel.setForeground(accentColor);
        headerPanel.add(headerLabel);
        
        container.add(headerPanel, BorderLayout.NORTH);
        
        return container;
    }
    
    private JPanel createEditorPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setBackground(BG_PANEL);
        container.setBorder(createRoundedBorder());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(ACCENT_GREEN.getRed(), ACCENT_GREEN.getGreen(), 
                                           ACCENT_GREEN.getBlue(), 40));
        headerPanel.setPreferredSize(new Dimension(0, 40)); 
        
        JLabel headerLabel = new JLabel("ASSEMBLY EDITOR");
        headerLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        headerLabel.setForeground(ACCENT_GREEN);
        headerPanel.add(headerLabel);
        
        container.add(headerPanel, BorderLayout.NORTH);
        
        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBackground(BG_DARK);
        
        JTextArea lineNumbers = new JTextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39\n40\n41\n42\n43\n44\n45\n46\n47\n48\n49\n50\n51\n52\n53\n54\n55\n56\n57\n58\n59\n60\n61\n62\n63\n64\n65\n66\n67\n68\n69\n70\n71\n72\n73\n74\n75\n76\n77\n78\n79\n80\n81\n82\n83\n84\n85\n86\n87\n88\n89\n90\n91\n92\n93\n94\n95\n96\n97\n98\n99\n100");
        lineNumbers.setBackground(BG_DARK);
        lineNumbers.setForeground(TEXT_SECONDARY);
        lineNumbers.setFont(new Font("Monospace", Font.PLAIN, 14));
        lineNumbers.setEditable(false);
        lineNumbers.setPreferredSize(new Dimension(40, 0));
        lineNumbers.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        txtEditeur = new JTextArea();
        txtEditeur.setBackground(BG_DARK);
        txtEditeur.setForeground(TEXT_PRIMARY);
        txtEditeur.setCaretColor(ACCENT_BLUE);
        txtEditeur.setFont(new Font("Monospace", Font.PLAIN, 14));
        txtEditeur.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txtEditeur.setLineWrap(false);

        txtEditeur.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        Reset();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        Reset();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Reset();
    }
});
        
        editorPanel.add(lineNumbers, BorderLayout.WEST);
        editorPanel.add(txtEditeur, BorderLayout.CENTER);
        
        JScrollPane scroll = new JScrollPane(editorPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);
        
        container.add(scroll, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(BG_PANEL);
        
        btnDebug = createModernButton("PAS À PAS", new Color(59, 130, 246));
        btnExecuter = createModernButton("EXÉCUTER", ACCENT_GREEN);
        btnCompiler = createModernButton("COMPILER", ACCENT_PURPLE);
        btnReset = createModernButton("RESET", ACCENT_RED);
        
        btnDebug.addActionListener(this);
        btnExecuter.addActionListener(this);
        btnCompiler.addActionListener(this);
        btnReset.addActionListener(this);
        
        buttonPanel.add(btnDebug);
        buttonPanel.add(btnExecuter);
        buttonPanel.add(btnCompiler);
        buttonPanel.add(btnReset);
        
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        return container;
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BG_DARKER, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCompiler)
			clsCompiler.compiler();
		if(e.getSource() == btnDebug)
             clsPasàpas.pasapas();
		if(e.getSource() == btnExecuter)
                clsExecuter.executer();
		if(e.getSource() == btnReset){
            Reset();
            txtEditeur.setText("");
        }
	}

    public static void Reset(){
        clsROM.Reset();
        clsRAM.Reset();
        clsRegisters.Reset();
        clsCompiler.debug = false;
        clsPasàpas.c.clear();
        clsPasàpas.currentline = 0;
        clsCompiler.counter = 0;
        clsPasàpas.pas = false;
    }
	
	
	public static String intToHex(long value) {
        String hexValue = Long.toHexString(value).toUpperCase();

        while (hexValue.length() < 4) {
            hexValue = "0" + hexValue;
        }

        return hexValue;
    }
	
}
	
