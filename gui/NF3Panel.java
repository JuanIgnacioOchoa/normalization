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
import normalization.Shared;
import normalization.Table;

public class NF3Panel extends JPanel {
  private Database database = null;
  
  private JPanel pnlTablasCreadas = new JPanel();
  
  private JScrollPane scrTablasCreadas = new JScrollPane(this.pnlTablasCreadas);
  
  private JLabel lblTitle = new JLabel("Tablas de datos en Tercera Forma Normal:");
  
  private JTextArea txtComentarios = new JTextArea();
  
  private JScrollPane scrComentarios = new JScrollPane();
  
  private JButton btnGuardar = new JButton("Guardar");
  
  public NF3Panel() {
    setLayout((LayoutManager)null);
    this.lblTitle.setBounds(37, 36, 350, 25);
    this.lblTitle.setFont(Shared.LABEL_FONT);
    add(this.lblTitle);
    this.scrTablasCreadas.setBounds(35, 80, 840, 360);
    this.pnlTablasCreadas.setLayout((LayoutManager)null);
    this.pnlTablasCreadas.setBackground(new Color(200, 200, 210));
    this.pnlTablasCreadas.setPreferredSize(new Dimension(750, 350));
    add(this.scrTablasCreadas);
    this.txtComentarios.setEditable(false);
    this.txtComentarios.setFont(Shared.LABEL_FONT);
    this.scrComentarios.setBounds(50, 460, 800, 145);
    this.scrComentarios.setViewportView(this.txtComentarios);
    add(this.scrComentarios);
    this.btnGuardar.setToolTipText("Guardar el proceso de normalizaciefectuado");
    this.btnGuardar.setMnemonic('G');
    this.btnGuardar.setFont(new Font("Calibri", 0, 16));
    this.btnGuardar.setBounds(693, 620, 157, 30);
    this.btnGuardar.setCursor(Shared.BUTTON_CURSOR);
    add(this.btnGuardar);
    
    this.btnGuardar.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Registro r = new Registro("Save");
            r.setEntrada();
            String filename = Utils.saveProcessToFile();
            if (filename.equals("")) {
              r.setDescripcion("No se pudo realizar el grabado de la normalizacion");
            } else {
              r.setDescripcion("Se grabaron los resultados de la normalizacien el archivo " + filename);
            } 
            r.setSalida();
            Shared.bitacora.agregarRegistro(r);
          }
        });
     
  }
  
  public void setTitle(String title) {
    this.lblTitle.setText(title);
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
