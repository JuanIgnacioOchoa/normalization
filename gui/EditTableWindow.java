package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import normalization.Shared;
import normalization.Table;

public class EditTableWindow extends JFrame {
  private JTable datagrid = new JTable();
  
  private JScrollPane scroll = new JScrollPane(this.datagrid);
  
  private JPanel updownPanel = new JPanel();
  
  private JPanel buttonPanel = new JPanel();
  
  private ImageIcon upIcon;
  
  private ImageIcon downIcon;
  
  private JButton upBtn = new JButton();
  
  private JButton downBtn = new JButton();
  
  private JButton aceptarBtn = new JButton("Aceptar");
  
  private JButton cancelarBtn = new JButton("Cancelar");
  
  private Table currentTable = null;
  
  private DefaultTableModel model = null;
  
  public EditTableWindow() {
    setTitle("Editar campos");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(2);
    this.upIcon = new ImageIcon(getClass().getResource("/images/up.png"));
    this.downIcon = new ImageIcon(getClass().getResource("/images/down.png"));
    this.upBtn.setIcon(this.upIcon);
    this.downBtn.setIcon(this.downIcon);
    this.upBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            int row = EditTableWindow.this.datagrid.getSelectedRow();
            if (row <= 0)
              return; 
            String n0 = (String)EditTableWindow.this.model.getValueAt(row, 0);
            boolean m0 = ((Boolean)EditTableWindow.this.model.getValueAt(row, 1)).booleanValue();
            String n1 = (String)EditTableWindow.this.model.getValueAt(row - 1, 0);
            boolean m1 = ((Boolean)EditTableWindow.this.model.getValueAt(row - 1, 1)).booleanValue();
            EditTableWindow.this.model.setValueAt(n1, row, 0);
            EditTableWindow.this.model.setValueAt(Boolean.valueOf(m1), row, 1);
            EditTableWindow.this.model.setValueAt(n0, row - 1, 0);
            EditTableWindow.this.model.setValueAt(Boolean.valueOf(m0), row - 1, 1);
            EditTableWindow.this.datagrid.getSelectionModel().setSelectionInterval(row - 1, row - 1);
            EditTableWindow.this.datagrid.scrollRectToVisible(EditTableWindow.this.datagrid.getCellRect(row - 1, 0, true));
          }
        });
    this.downBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            int row = EditTableWindow.this.datagrid.getSelectedRow();
            if (row < 0 || row >= EditTableWindow.this.currentTable.getFieldCount() - 1)
              return; 
            String n0 = (String)EditTableWindow.this.model.getValueAt(row, 0);
            boolean m0 = ((Boolean)EditTableWindow.this.model.getValueAt(row, 1)).booleanValue();
            String n1 = (String)EditTableWindow.this.model.getValueAt(row + 1, 0);
            boolean m1 = ((Boolean)EditTableWindow.this.model.getValueAt(row + 1, 1)).booleanValue();
            EditTableWindow.this.model.setValueAt(n1, row, 0);
            EditTableWindow.this.model.setValueAt(Boolean.valueOf(m1), row, 1);
            EditTableWindow.this.model.setValueAt(n0, row + 1, 0);
            EditTableWindow.this.model.setValueAt(Boolean.valueOf(m0), row + 1, 1);
            EditTableWindow.this.datagrid.getSelectionModel().setSelectionInterval(row + 1, row + 1);
            EditTableWindow.this.datagrid.scrollRectToVisible(EditTableWindow.this.datagrid.getCellRect(row + 1, 0, true));
          }
        });
    this.aceptarBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            EditTableWindow.this.saveData();
            EditTableWindow.this.dispose();
          }
        });
    this.cancelarBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            EditTableWindow.this.dispose();
          }
        });
    this.datagrid.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if (code == 10 || code == 27) {
              if (code == 10)
                EditTableWindow.this.saveData(); 
              EditTableWindow.this.dispose();
            } 
          }
        });
    this.updownPanel.setLayout((LayoutManager)null);
    this.updownPanel.setPreferredSize(new Dimension(80, 200));
    this.upBtn.setBounds(15, 40, 50, 60);
    this.downBtn.setBounds(15, 130, 50, 60);
    this.upBtn.setCursor(Shared.BUTTON_CURSOR);
    this.downBtn.setCursor(Shared.BUTTON_CURSOR);
    this.upBtn.setFocusable(false);
    this.downBtn.setFocusable(false);
    this.updownPanel.add(this.upBtn);
    this.updownPanel.add(this.downBtn);
    this.buttonPanel.setLayout((LayoutManager)null);
    this.buttonPanel.setPreferredSize(new Dimension(400, 50));
    this.aceptarBtn.setBounds(80, 10, 140, 30);
    this.cancelarBtn.setBounds(235, 10, 140, 30);
    this.aceptarBtn.setMnemonic('A');
    this.cancelarBtn.setMnemonic('C');
    this.aceptarBtn.setFocusable(false);
    this.cancelarBtn.setFocusable(false);
    this.aceptarBtn.setFont(Shared.BUTTON_FONT);
    this.cancelarBtn.setFont(Shared.BUTTON_FONT);
    this.aceptarBtn.setCursor(Shared.BUTTON_CURSOR);
    this.cancelarBtn.setCursor(Shared.BUTTON_CURSOR);
    this.buttonPanel.add(this.aceptarBtn);
    this.buttonPanel.add(this.cancelarBtn);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.updownPanel, "West");
    getContentPane().add(this.scroll, "Center");
    getContentPane().add(this.buttonPanel, "South");
    setVisible(true);
  }
  
  static final Class<?> BOOLEAN_CLASS = (new Boolean(true)).getClass();
  
  static final Class<?> STRING_CLASS = "".getClass();
  
  public void initTable(Table table) {
    this.currentTable = table;
    this.model = new DefaultTableModel() {
        public Class<?> getColumnClass(int columnIndex) {
          if (columnIndex == 1)
            return EditTableWindow.BOOLEAN_CLASS; 
          return EditTableWindow.STRING_CLASS;
        }
        
        public boolean isCellEditable(int row, int column) {
          return (column == 1);
        }
      };
    this.model.addColumn("Atributo");
    this.model.addColumn("Marcado");
    for (int f = 0; f < table.getFieldCount(); f++) {
      String fieldName = table.getField(f);
      Boolean marked = Boolean.valueOf(table.isFieldMarked(f));
      this.model.addRow(new Object[] { fieldName, marked });
    } 
    this.datagrid.setShowVerticalLines(false);
    this.datagrid.setShowHorizontalLines(false);
    this.datagrid.setAutoResizeMode(0);
    this.datagrid.setRowHeight(25);
    this.datagrid.setSelectionBackground(Shared.GRID_SELECTED_BACKGROUND);
    this.datagrid.setSelectionForeground(Shared.GRID_SELECTED_FOREGROUND);
    this.datagrid.setRowSelectionAllowed(true);
    this.datagrid.setModel(this.model);
    TableColumn tc1 = this.datagrid.getColumn("Atributo");
    tc1.setResizable(false);
    tc1.setPreferredWidth(220);
    TableColumn tc2 = this.datagrid.getColumn("Marcado");
    tc2.setResizable(false);
    tc2.setPreferredWidth(70);
    if (this.model.getRowCount() > 0)
      this.datagrid.getSelectionModel().setSelectionInterval(0, 0); 
  }
  
  public void saveData() {
    this.currentTable.clearMarkedFields();
    for (int f = 0; f < this.currentTable.getFieldCount(); f++) {
      String fieldName = (String)this.model.getValueAt(f, 0);
      this.currentTable.moveFieldTo(fieldName, f);
      boolean isMarked = ((Boolean)this.model.getValueAt(f, 1)).booleanValue();
      if (isMarked)
        this.currentTable.addMarkedField(f); 
    } 
  }
}
