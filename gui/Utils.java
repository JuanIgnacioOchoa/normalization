package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import normalization.Database;
import normalization.Key;
import normalization.Row;
import normalization.Shared;
import normalization.Table;

public class Utils {
  public static File fileOpened = null;
  
  private static FileFilter csvFileFilter = new FileFilter() {
      public boolean accept(File f) {
        return !(!f.isDirectory() && !f.getName().toUpperCase().endsWith("CSV"));
      }
      
      public String getDescription() {
        return "Archivos separados por comas (CSV)";
      }
    };
  
  public static File openFile(JLabel lblDir) {
    String currentDir = (fileOpened == null) ? (new File("")).getAbsolutePath() : fileOpened.getParent();
    JFileChooser chooser = new JFileChooser(currentDir);
    chooser.setFileFilter(csvFileFilter);
    int result = chooser.showOpenDialog(Shared.nf1Panel);
    if (result != 0)
      return null; 
    fileOpened = chooser.getSelectedFile();
    if (!fileOpened.getName().toUpperCase().endsWith("CSV")) {
      JOptionPane.showMessageDialog(Shared.nf1Panel, "La tabla debe encontrarse en un archivo CSV");
      return null;
    } 
    File parent = fileOpened.getParentFile();
    String strParent = (parent.getName().length() <= 3) ? parent.toString() : ("...\\" + parent.getName() + "\\");
    lblDir.setText("Tabla seleccionada: " + strParent + fileOpened.getName());
    return fileOpened;
  }
  
  public static void saveFile(Table currentTable, JLabel lblDir) throws IOException {
    String currentDir = (fileOpened == null) ? (new File("")).getAbsolutePath() : fileOpened.getParent();
    JFileChooser chooser = new JFileChooser(currentDir);
    chooser.setFileFilter(csvFileFilter);
    int result = chooser.showSaveDialog(Shared.nf1Panel);
    if (result != 0)
      return; 
    fileOpened = chooser.getSelectedFile();
    if (!fileOpened.getName().toUpperCase().endsWith("CSV"))
      fileOpened = new File(String.valueOf(fileOpened.getAbsolutePath()) + ".csv"); 
    File parent = fileOpened.getParentFile();
    String strParent = (parent.getName().length() <= 3) ? parent.toString() : ("...\\" + parent.getName() + "\\");
    lblDir.setText("Tabla seleccionada: " + strParent + fileOpened.getName());
    if (fileOpened.exists()) {
      int answer = JOptionPane.showConfirmDialog(Shared.nf1Panel, "El archivo ya existe. sobreescribirlo?");
      if (answer != 0)
        return; 
    } 
    BufferedWriter bw = new BufferedWriter(new FileWriter(fileOpened));
    String line1 = "";
    for (int f = 0; f < currentTable.getFieldCount(); f++) {
      if (f > 0)
        line1 = String.valueOf(line1) + ","; 
      line1 = String.valueOf(line1) + currentTable.getField(f);
    } 
    bw.append(line1);
    bw.newLine();
    String line2 = "";
    for (int i = 0; i < currentTable.getFieldCount(); i++) {
      if (i > 0)
        line2 = String.valueOf(line2) + ","; 
      if (currentTable.isFieldMarked(i))
        line2 = String.valueOf(line2) + "x"; 
    } 
    bw.append(line2);
    bw.newLine();
    for (int r = 0; r < currentTable.getRowCount(); r++) {
      Row row = currentTable.getRow(r);
      String line = "";
      for (int j = 0; j < currentTable.getFieldCount(); j++) {
        if (j > 0)
          line = String.valueOf(line) + ","; 
        line = String.valueOf(line) + row.get(j);
      } 
      bw.append(line);
      if (r < currentTable.getRowCount() - 1)
        bw.newLine(); 
    } 
    bw.close();
    JOptionPane.showMessageDialog(null, "El archivo " + fileOpened.getName() + " fue generado con exito");
  }
  
  public static String saveProcessToFile() {
    String currentDir = (fileOpened == null) ? (new File("")).getAbsolutePath() : fileOpened.getParent();
    JFileChooser chooser = new JFileChooser(currentDir);
    chooser.setFileFilter(csvFileFilter);
    int result = chooser.showSaveDialog(Shared.nf3Panel);
    if (result != 0)
      return ""; 
    fileOpened = chooser.getSelectedFile();
    if (!fileOpened.getName().toUpperCase().endsWith("CSV"))
      fileOpened = new File(String.valueOf(fileOpened.getAbsolutePath()) + ".csv"); 
    File parent = fileOpened.getParentFile();
    String strParent = (parent.getName().length() <= 3) ? parent.toString() : ("...\\" + parent.getName() + "\\");
    String fileSelected = String.valueOf(strParent) + fileOpened.getName();
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fileOpened));
      Table table = Shared.readPanel.getCurrentTable();
      bw.append("========================");
      bw.newLine();
      bw.append("Tabla original");
      bw.newLine();
      bw.append("========================");
      bw.newLine();
      for (int f = 0; f < table.getFieldCount(); ) {
        bw.append(String.valueOf(table.getField(f)) + ",");
        f++;
      } 
      bw.newLine();
      bw.append("------------------------");
      bw.newLine();
      for (int r = 0; r < table.getRowCount(); r++) {
        for (int c = 0; c < table.getFieldCount(); ) {
          bw.append(String.valueOf(table.getValueAt(r, c)) + ",");
          c++;
        } 
        bw.newLine();
      } 
      bw.newLine();
      Table table1FN = Shared.nf1Panel.getCurrentTable();
      bw.append("========================");
      bw.newLine();
      bw.append("Tabla en Primera Forma Normal");
      bw.newLine();
      bw.append("========================");
      bw.newLine();
      Key pk = table1FN.getPrimaryKey();
      for (int j = 0; j < table1FN.getFieldCount(); j++) {
        if (pk.contains(table1FN.getField(j)))
          bw.append("[PK] "); 
        bw.append(String.valueOf(table1FN.getField(j)) + ",");
      } 
      bw.newLine();
      bw.append("------------------------");
      bw.newLine();
      for (int i = 0; i < table1FN.getRowCount(); i++) {
        for (int c = 0; c < table1FN.getFieldCount(); ) {
          bw.append(String.valueOf(table1FN.getValueAt(i, c)) + ",");
          c++;
        } 
        bw.newLine();
      } 
      bw.newLine();
      Database db2FN = Shared.nf2Panel.getDatabase();
      bw.append("========================");
      bw.newLine();
      bw.append("Tablas resultantes en Segunda Forma Normal");
      bw.newLine();
      bw.append("========================");
      bw.newLine();
      for (int d = 0; d < db2FN.size(); d++) {
        Table t = db2FN.get(d);
        pk = t.getPrimaryKey();
        for (int n = 0; n < t.getFieldCount(); n++) {
          if (pk.contains(t.getField(n)))
            bw.append("[PK] "); 
          bw.append(String.valueOf(t.getField(n)) + ",");
        } 
        bw.newLine();
        bw.append("------------------------");
        bw.newLine();
        for (int m = 0; m < t.getRowCount(); m++) {
          for (int c = 0; c < t.getFieldCount(); ) {
            bw.append(String.valueOf(t.getValueAt(m, c)) + ",");
            c++;
          } 
          bw.newLine();
        } 
        bw.newLine();
      } 
      bw.newLine();
      Database db3FN = Shared.nf3Panel.getDatabase();
      bw.append("========================");
      bw.newLine();
      bw.append("Tablas resultantes en Tercera Forma Normal");
      bw.newLine();
      bw.append("========================");
      bw.newLine();
      for (int k = 0; k < db3FN.size(); k++) {
        Table t = db3FN.get(k);
        pk = t.getPrimaryKey();
        for (int n = 0; n < t.getFieldCount(); n++) {
          if (pk.contains(t.getField(n)))
            bw.append("[PK] "); 
          bw.append(String.valueOf(t.getField(n)) + ",");
        } 
        bw.newLine();
        bw.append("------------------------");
        bw.newLine();
        for (int m = 0; m < t.getRowCount(); m++) {
          for (int c = 0; c < t.getFieldCount(); ) {
            bw.append(String.valueOf(t.getValueAt(m, c)) + ",");
            c++;
          } 
          bw.newLine();
        } 
        bw.newLine();
      } 
      bw.newLine();
      bw.close();
      JOptionPane.showMessageDialog(Shared.nf3Panel, "Se guardel proceso de la normalizacicon en " + fileSelected);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(Shared.nf3Panel, "Hubo un error al guardar el archivo. Posiblemente estabierto", 
          "No se pudo guardar", 0);
      System.out.println(ex);
      return "";
    } 
    return fileOpened.getName();
  }
}
