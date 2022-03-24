package gui;

import db.Registro;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import normalization.NormalForm;
import normalization.Shared;
import normalization.Table;

public class ReadPanel extends JPanel {
  private JLabel lblOpen = new JLabel("Tabla de datos a normalizar: ");
  
  private JLabel lblDir = new JLabel("No se ha seleccionado ninguna tabla");
  
  private JButton btnOpen = new JButton("Abrir ...");
  
  private DataGrid dataGrid = new DataGrid();
  
  private JScrollPane scrComentarios = new JScrollPane();
  
  private JTextArea txtComentarios = new JTextArea();
  
  private JButton btn1FN;
  
  private JButton btnEditTable = new JButton("Editar campos");
  
  private JButton btnSave = new JButton("Guardar ...");
  
  private JButton btnRefresh = new JButton("Refrescar");
  
  private Table currentTable = null;
  
  //private Registro editRegistro = null;
  
  public ReadPanel() {
    setLayout((LayoutManager)null);
    this.lblOpen.setFont(Shared.LABEL_FONT);
    this.lblOpen.setBounds(50, 40, 250, 25);
    this.lblDir.setFont(Shared.LABEL_FONT1);
    this.lblDir.setForeground(new Color(100, 0, 0));
    this.lblDir.setBounds(50, 80, 500, 25);
    this.btnOpen.setFont(Shared.BUTTON_FONT);
    this.btnOpen.setMnemonic('A');
    this.btnOpen.setCursor(Shared.BUTTON_CURSOR);
    this.btnOpen.setToolTipText("Abrir archivo CSV con la tabla de datos a normalizar");
    this.btnOpen.setBounds(320, 35, 100, 30);
    this.btnEditTable.setFont(Shared.BUTTON_FONT);
    this.btnEditTable.setMnemonic('E');
    this.btnEditTable.setCursor(Shared.BUTTON_CURSOR);
    this.btnEditTable.setToolTipText("Modificar metadatos de las columnas");
    this.btnEditTable.setBounds(430, 35, 130, 30);
    this.btnEditTable.setEnabled(false);
    this.btnEditTable.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            //ReadPanel.this.editRegistro = new Registro("Edit");
            //ReadPanel.this.editRegistro.setDescripcion("Modificar columnas de la tabla");
            //ReadPanel.this.editRegistro.setEntrada();
            EditTableWindow window = new EditTableWindow();
            /*
            window.addWindowListener(new WindowAdapter() {
                  public void windowClosed(WindowEvent e) {
                    (ReadPanel.null.access$0(ReadPanel.null.this)).dataGrid.init((ReadPanel.null.access$0(ReadPanel.null.this)).currentTable);
                    MainWindow.getInstance().removeTabs();
                    (ReadPanel.null.access$0(ReadPanel.null.this)).btn1FN.setEnabled(true);
                    (ReadPanel.null.access$0(ReadPanel.null.this)).editRegistro.setSalida();
                    Shared.bitacora.agregarRegistro((ReadPanel.null.access$0(ReadPanel.null.this)).editRegistro);
                    MainWindow.getInstance().setVisible(true);
                    MainWindow.getInstance().requestFocus();
                  }
                });
            */
            window.initTable(ReadPanel.this.currentTable);
            MainWindow.getInstance().setVisible(false);
          }
        });
    this.btnSave.setFont(Shared.BUTTON_FONT);
    this.btnSave.setMnemonic('G');
    this.btnSave.setCursor(Shared.BUTTON_CURSOR);
    this.btnSave.setToolTipText("Guardar tabla en un archivo CSV");
    this.btnSave.setEnabled(false);
    this.btnSave.setBounds(570, 35, 100, 30);
    this.btnRefresh.setFont(Shared.BUTTON_FONT);
    this.btnRefresh.setMnemonic('R');
    this.btnRefresh.setCursor(Shared.BUTTON_CURSOR);
    this.btnRefresh.setToolTipText("Vuelve a abrir el mismo archivo CSV");
    this.btnRefresh.setEnabled(false);
    this.btnRefresh.setBounds(680, 35, 100, 30);
    this.dataGrid.setBounds(50, 120, 800, 250);
    add(this.lblOpen);
    add(this.lblDir);
    add(this.btnOpen);
    add(this.btnEditTable);
    add(this.btnSave);
    add(this.btnRefresh);
    add(this.dataGrid);
    this.scrComentarios.setBounds(50, 395, 800, 145);
    add(this.scrComentarios);
    this.txtComentarios.setEditable(false);
    this.txtComentarios.setFont(Shared.LABEL_FONT);
    this.scrComentarios.setViewportView(this.txtComentarios);
    this.btn1FN = new JButton("Ir a Primera FN");
    this.btn1FN.setToolTipText("Ir a Primera Forma Normal");
    this.btn1FN.setMnemonic('P');
    this.btn1FN.setFont(new Font("Calibri", 0, 16));
    this.btn1FN.setBounds(693, 569, 157, 30);
    this.btn1FN.setCursor(Shared.BUTTON_CURSOR);
    this.btn1FN.setEnabled(false);
    add(this.btn1FN);
    this.btnOpen.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Registro r = new Registro("Read");
            r.setEntrada();
            File f = Utils.openFile(ReadPanel.this.lblDir);
            if (f == null) {
              r.setDescripcion("Lectura fallida de archivo");
              r.setSalida();
              Shared.bitacora.agregarRegistro(r);
              return;
            } 
            try {
              ReadPanel.this.currentTable = Table.loadFromFile(f);
              MainWindow.getInstance().removeTabs();
              ReadPanel.this.txtComentarios.setText(ReadPanel.this.currentTable.getComments());
              ReadPanel.this.dataGrid.init(ReadPanel.this.currentTable);
              ReadPanel.this.dataGrid.setFocus();
              ReadPanel.this.btn1FN.setEnabled(true);
              ReadPanel.this.btnEditTable.setEnabled(true);
              ReadPanel.this.btnSave.setEnabled(true);
              ReadPanel.this.btnRefresh.setEnabled(true);
              r.setDescripcion("Lectura exitosa del archivo " + f + ". " + ReadPanel.this.currentTable.getComments());
              r.setSalida();
              Shared.bitacora.agregarRegistro(r);
            } catch (Exception ex) {
              JOptionPane.showMessageDialog(Shared.readPanel, "El archivo no tiene el formato esperado " + e, "Error de lectura", 0);
              System.out.println(ex);
            } 
          }
        });
    this.btnSave.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (ReadPanel.this.currentTable == null)
              return; 
            try {
              Utils.saveFile(ReadPanel.this.currentTable, ReadPanel.this.lblDir);
            } catch (IOException ex) {
              System.out.println(ex);
            } 
          }
        });
    this.btnRefresh.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              ReadPanel.this.currentTable = Table.loadFromFile(Utils.fileOpened);
              MainWindow.getInstance().removeTabs();
              ReadPanel.this.txtComentarios.setText(ReadPanel.this.currentTable.getComments());
              ReadPanel.this.dataGrid.init(ReadPanel.this.currentTable);
              ReadPanel.this.dataGrid.setFocus();
              ReadPanel.this.btn1FN.setEnabled(true);
            } catch (Exception ex) {
              System.out.println(ex);
            } 
          }
        });
    this.btn1FN.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (ReadPanel.this.currentTable == null)
              return; 
            Registro r = new Registro("1FN");
            r.setEntrada();
            Table table1FN = NormalForm.to1FN(ReadPanel.this.currentTable);
            Shared.nf1Panel.setCurrentTable(table1FN);
            r.setDescripcion(table1FN.getComments());
            r.setSalida();
            Shared.bitacora.agregarRegistro(r);
            ReadPanel.this.btn1FN.setEnabled(false);
            MainWindow.getInstance().addNF1Tab();
          }
        });
  }
  
  public Table getCurrentTable() {
    return this.currentTable;
  }
}
