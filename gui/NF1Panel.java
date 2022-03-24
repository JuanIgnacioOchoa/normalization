package gui;

import db.Registro;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import normalization.Database;
import normalization.NormalForm;
import normalization.Shared;
import normalization.Table;

public class NF1Panel extends JPanel {
  private Table currentTable = null;
  
  private DataGrid dataGrid = new DataGrid();
  
  private JScrollPane scrComentarios = new JScrollPane();
  
  private JTextArea txtComentarios = new JTextArea();
  
  private JButton btn2FN;
  
  public NF1Panel() {
    setLayout((LayoutManager)null);
    this.dataGrid.setBounds(41, 86, 800, 250);
    add(this.dataGrid);
    JLabel lblTablaDeDatos = new JLabel("Tabla de datos en Primera Forma Normal:");
    lblTablaDeDatos.setFont(new Font("Calibri", 0, 18));
    lblTablaDeDatos.setBounds(41, 36, 324, 25);
    add(lblTablaDeDatos);
    this.txtComentarios.setEditable(false);
    this.txtComentarios.setFont(Shared.LABEL_FONT);
    this.scrComentarios.setBounds(50, 395, 800, 145);
    this.scrComentarios.setViewportView(this.txtComentarios);
    add(this.scrComentarios);
    this.btn2FN = new JButton("Ir a Segunda FN");
    this.btn2FN.setToolTipText("Ir a Segunda Forma Normal");
    this.btn2FN.setMnemonic('S');
    this.btn2FN.setFont(new Font("Calibri", 0, 16));
    this.btn2FN.setBounds(693, 569, 157, 30);
    this.btn2FN.setCursor(Shared.BUTTON_CURSOR);
    add(this.btn2FN);
    this.btn2FN.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (NF1Panel.this.currentTable == null)
              return; 
            Registro r = new Registro("2FN");
            r.setEntrada();
            Database db2FN = NormalForm.to2FN(NF1Panel.this.currentTable);
            Shared.nf2Panel.setCurrentDB(db2FN);
            r.setDescripcion(db2FN.getComments());
            r.setSalida();
            Shared.bitacora.agregarRegistro(r);
            NF1Panel.this.btn2FN.setEnabled(false);
            MainWindow.getInstance().addNF2Tab();
          }
        });
  }
  
  public void setCurrentTable(Table table) {
    this.currentTable = table;
    this.dataGrid.init(this.currentTable);
    this.txtComentarios.setText(this.currentTable.getComments());
    this.btn2FN.setEnabled(true);
  }
  
  public Table getCurrentTable() {
    return this.currentTable;
  }
}
