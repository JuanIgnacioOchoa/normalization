package gui;

import db.Registro;
import java.awt.Color;
import java.awt.Dimension;
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

public class NF2Panel extends JPanel {
  private Database database = null;
  
  private JPanel pnlTablasCreadas = new JPanel();
  
  private JScrollPane scrTablasCreadas = new JScrollPane(this.pnlTablasCreadas);
  
  private JTextArea txtComentarios = new JTextArea();
  
  private JScrollPane scrComentarios = new JScrollPane();
  
  private JButton btn3FN;
  
  public NF2Panel() {
    setLayout((LayoutManager)null);
    JLabel label1 = new JLabel("Tablas de datos en Segunda Forma Normal:");
    label1.setBounds(37, 36, 350, 25);
    label1.setFont(Shared.LABEL_FONT);
    add(label1);
    this.txtComentarios.setEditable(false);
    this.txtComentarios.setFont(Shared.LABEL_FONT);
    this.scrComentarios.setBounds(50, 460, 800, 145);
    this.scrComentarios.setViewportView(this.txtComentarios);
    add(this.scrComentarios);
    this.scrTablasCreadas.setBounds(35, 80, 840, 360);
    this.pnlTablasCreadas.setLayout((LayoutManager)null);
    this.pnlTablasCreadas.setBackground(new Color(200, 200, 210));
    this.pnlTablasCreadas.setPreferredSize(new Dimension(750, 350));
    add(this.scrTablasCreadas);
    this.btn3FN = new JButton("Ir a Tercera FN");
    this.btn3FN.setToolTipText("Ir a Tercera Forma Normal");
    this.btn3FN.setMnemonic('T');
    this.btn3FN.setFont(new Font("Calibri", 0, 16));
    this.btn3FN.setBounds(693, 620, 157, 30);
    this.btn3FN.setCursor(Shared.BUTTON_CURSOR);
    add(this.btn3FN);
    this.btn3FN.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Registro r = new Registro("3FN");
            r.setEntrada();
            Database db3fn = NormalForm.to3FN(NF2Panel.this.database);
            Shared.nf3Panel.setCurrentDB(db3fn);
            r.setDescripcion(db3fn.getComments());
            r.setSalida();
            Shared.bitacora.agregarRegistro(r);
            MainWindow.getInstance().addNF3Tab();
          }
        });
  }
  
  public void setCurrentDB(Database db) {
    this.database = db;
    int gridY = 20;
    for (int t = this.database.size() - 1; t >= 0; t--) {
      Table table = this.database.get(t);
      DataGrid dg = new DataGrid();
      dg.setBounds(30, gridY, 800, 170);
      dg.init(table);
      this.pnlTablasCreadas.add(dg);
      gridY += dg.getHeight() + 20;
    } 
    this.txtComentarios.setText(db.getComments());
    this.pnlTablasCreadas.setPreferredSize(new Dimension(750, gridY));
    this.scrTablasCreadas.updateUI();
  }
  
  public void init() {
    this.pnlTablasCreadas.removeAll();
    this.pnlTablasCreadas.setPreferredSize(new Dimension(750, 350));
  }
  
  public Database getDatabase() {
    return this.database;
  }
}
