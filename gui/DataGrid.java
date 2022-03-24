package gui;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import normalization.Shared;
import normalization.Table;

public class DataGrid extends JScrollPane {
  private JTable jTable = new JTable() {
      public boolean isCellEditable(int row, int col) {
        return false;
      }
    };
  
  private DefaultTableModel model;
  
  public void init(Table table) {
    int[] columnWidths = getColumnWidths(table);
    this.model = new DefaultTableModel();
    this.model.addColumn("#");
    for (int f = 0; f < table.getFieldCount(); f++) {
      if (table.isFieldMarked(f)) {
        this.model.addColumn(String.valueOf(table.getField(f)) + " *");
      } else {
        this.model.addColumn(table.getField(f));
      } 
    } 
    this.jTable.setModel(this.model);
    for (int r = 0; r < table.getRowCount(); ) {
      this.model.addRow((Object[])table.rowToArray(r));
      r++;
    } 
    JTableHeader header = this.jTable.getTableHeader();
    header.setReorderingAllowed(false);
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    headerRenderer.setBackground(Shared.GRID_HEADER_BACKGROUND);
    headerRenderer.setForeground(Shared.GRID_HEADER_FOREGROUND);
    header.setDefaultRenderer(headerRenderer);
    DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
    rowRenderer.setBackground(Shared.GRID_BACKGROUND);
    rowRenderer.setForeground(Shared.GRID_FOREGROUND);
    rowRenderer.setHorizontalAlignment(2);
    DefaultTableCellRenderer firstColRenderer = new DefaultTableCellRenderer();
    firstColRenderer.setBackground(Shared.GRID_HEADER_BACKGROUND);
    firstColRenderer.setForeground(Shared.GRID_HEADER_FOREGROUND);
    firstColRenderer.setHorizontalAlignment(2);
    DefaultTableCellRenderer keyRenderer = new DefaultTableCellRenderer();
    keyRenderer.setBackground(Shared.GRID_KEY_BACKGROUND);
    keyRenderer.setForeground(Shared.GRID_KEY_FOREGROUND);
    firstColRenderer.setHorizontalAlignment(2);
    int scrollWidth = 0;
    for (int i = 0; i < this.model.getColumnCount(); i++) {
      String columnName = this.jTable.getColumnName(i);
      TableColumn tc = this.jTable.getColumn(columnName);
      if (i == 0) {
        tc.setCellRenderer(firstColRenderer);
        tc.setPreferredWidth(30);
        scrollWidth += 30;
      } else {
        if (table.isKey(i - 1)) {
          tc.setCellRenderer(keyRenderer);
        } else {
          tc.setCellRenderer(rowRenderer);
        } 
        tc.setPreferredWidth(20 + columnWidths[i - 1]);
        scrollWidth += 20 + columnWidths[i - 1];
      } 
    } 
    scrollWidth += table.getFieldCount();
    this.jTable.setShowVerticalLines(false);
    this.jTable.setShowHorizontalLines(false);
    this.jTable.setAutoResizeMode(0);
    this.jTable.setRowHeight(25);
    this.jTable.setSelectionBackground(Shared.GRID_SELECTED_BACKGROUND);
    this.jTable.setSelectionForeground(Shared.GRID_SELECTED_FOREGROUND);
    this.jTable.setCellSelectionEnabled(true);
    setViewportView(this.jTable);
    int width = Math.min(800, scrollWidth) + 5;
    int height = Math.min(220, (1 + table.getRowCount()) * 25) - 3;
    if (width < scrollWidth)
      height += 15; 
    setSize(new Dimension(width, height));
  }
  
  public void setFocus() {
    this.jTable.setColumnSelectionInterval(0, 0);
    if (this.model.getRowCount() >= 1)
      this.jTable.setRowSelectionInterval(0, 0); 
    this.jTable.requestFocus();
  }
  
  private int[] getColumnWidths(Table table) {
    int[] columnWidths = new int[table.getFieldCount()];
    for (int f = 0; f < table.getFieldCount(); f++) {
      String s = table.getField(f);
      JLabel label = new JLabel(s);
      int len = (label.getPreferredSize()).width;
      if (len > columnWidths[f])
        columnWidths[f] = len; 
    } 
    for (int r = 0; r < table.getRowCount(); r++) {
      for (int i = 0; i < table.getFieldCount(); i++) {
        String s = table.getValueAt(r, i);
        JLabel label = new JLabel(s);
        int len = (label.getPreferredSize()).width;
        if (len > columnWidths[i])
          columnWidths[i] = len; 
      } 
    } 
    return columnWidths;
  }
}
