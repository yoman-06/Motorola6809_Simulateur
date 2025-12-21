import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class clsROM extends JPanel{
	
    private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_PANEL = new Color(30, 41, 59);
    private static final Color BG_ROW_EVEN = new Color(15, 23, 42);
    private static final Color BG_ROW_ODD = new Color(30, 41, 59);
    private static final Color TEXT_ADDR = new Color(100, 116, 139);
    private static final Color TEXT_DATA = new Color(251, 146, 60);
    private static final Color HEADER_BG = new Color(51, 65, 85);
    private static final Color TEXT_HEADER = new Color(148, 163, 184);

	public static JTable table;
	public static DefaultTableModel model = new DefaultTableModel();
	
	clsROM() {
        this.setLayout(new BorderLayout());
        this.setBackground(BG_PANEL);
        
        model = new DefaultTableModel();
        model.addColumn("Address");
        model.addColumn("Data");
        
        Reset();
        
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table.setBackground(BG_DARK);
        table.setForeground(TEXT_DATA);
        table.setFont(new Font("Monospace", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(245, 158, 11, 60));
        table.setSelectionForeground(TEXT_DATA);
        
        // Header styling
        table.getTableHeader().setBackground(HEADER_BG);
        table.getTableHeader().setForeground(TEXT_HEADER);
        table.getTableHeader().setFont(new Font("Monospace", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        // Custom cell renderer
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD);
                    c.setForeground(column == 0 ? TEXT_ADDR : TEXT_DATA);
                }
                
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Monospace", Font.PLAIN, 12));
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_DARK);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public static void Reset(){
        model.setRowCount(0); 
        for (int i = 64512; i <= 65535; i++) {
            String address = clsMoto6809.intToHex(i);
            model.addRow(new Object[]{address, "FF"});
            
        }
    }
    public static void focusAddress(String hexAddress) {
    for (int row = 0; row < model.getRowCount(); row++) {
        String addr = (String) model.getValueAt(row, 0);
        if (addr.equalsIgnoreCase(hexAddress)) {
            table.setRowSelectionInterval(row, row);
            table.scrollRectToVisible(table.getCellRect(row, 0, true));
            return;
        }
    }
}

}



