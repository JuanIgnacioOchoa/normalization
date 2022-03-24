package normalization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Table {
  private String name;
  
  private List<String> header;
  
  private List<Boolean> untitledFields;
  
  private List<Row> data;
  
  private List<Integer> markedFields;
  
  private StringBuffer comments;
  
  private Key primaryKey;
  
  private List<Dependency> dependencies;
  
  public Table(String name) {
    this.name = name;
    this.header = new ArrayList<>();
    this.data = new ArrayList<>();
    this.markedFields = new ArrayList<>();
    this.untitledFields = new ArrayList<>();
    this.primaryKey = new Key();
    this.comments = new StringBuffer();
    this.dependencies = new ArrayList<>();
  }
  
  public String toString() {
    String s = String.valueOf(this.name) + "\n------------------\n";
    for (String f : this.header) {
      s = String.valueOf(s) + f;
      if (this.primaryKey.contains(f))
        s = String.valueOf(s) + " PK"; 
      s = String.valueOf(s) + "\n";
    } 
    return s;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void addDependency(Key A, String B) {
    this.dependencies.add(new Dependency(A, B));
  }
  
  public boolean existsDependency(Key K) {
    for (Dependency d : this.dependencies) {
      if (d.getFirst().equals(K))
        return true; 
    } 
    return false;
  }
  
  public void addField(String field) {
    addField(field, true);
  }
  
  public void addField(String field, boolean isUntitled) {
    this.header.add(field);
    this.untitledFields.add(Boolean.valueOf(isUntitled));
  }
  
  public int getFieldIndex(String field) {
    return this.header.indexOf(field);
  }
  
  public void addRow(Row row) {
	 
    this.data.add(row);
  }
  
  public void moveFieldTo(String field, int newIndex) {
    int currentIndex = getFieldIndex(field);
    if (newIndex == currentIndex)
      return; 
    exchangeColumns(currentIndex, newIndex);
  }
  
  public void exchangeColumns(int f1, int f2) {
    String field = this.header.get(f1);
    this.header.set(f1, this.header.get(f2));
    this.header.set(f2, field);
    for (int r = 0; r < getRowCount(); r++) {
      Row row = this.data.get(r);
      String tmp = row.get(f1);
      row.set(f1, row.get(f2));
      row.set(f2, tmp);
    } 
  }
  
  public List<Dependency> getDependencies() {
    return this.dependencies;
  }
  
  public boolean contains(Row r) {
    return this.data.contains(r);
  }
  
  public Row getRow(int rowIndex) {
    return this.data.get(rowIndex);
  }
  
  public int getRowCount() {
    return this.data.size();
  }
  
  public String[] rowToArray(int row) {
    if (row < 0 || row >= this.data.size())
      return new String[] { "" }; 
    return ((Row)this.data.get(row)).toArray(row);
  }
  
  public void addToPrimaryKey(int field) {
    this.primaryKey.addToKey(field, this.header.get(field));
  }
  
  public boolean isKey(int field) {
    return this.primaryKey.contains(field);
  }
  
  public Key getPrimaryKey() {
    return this.primaryKey;
  }
  
  public void setPrimaryKey(Key key) {
    this.primaryKey = key;
  }
  
  public String getPrimaryKeyAsString() {
    String s = "";
    for (int i = 0; i < getFieldCount(); i++) {
      if (isKey(i)) {
        if (!s.equals(""))
          s = String.valueOf(s) + ", "; 
        s = String.valueOf(s) + getField(i);
      } 
    } 
    return s;
  }
  
  public String getValueAt(int row, int col) {
    if (row < 0 || row >= this.data.size() || col < 0 || col >= this.header.size())
      return "INVALID INDEX"; 
    return ((Row)this.data.get(row)).get(col);
  }
  
  public void addMarkedField(int index) {
    this.markedFields.add(Integer.valueOf(index));
  }
  
  public void removeMarkedField(int index) {
    this.markedFields.remove(new Integer(index));
  }
  
  public void clearMarkedFields() {
    this.markedFields.clear();
  }
  
  public int getFieldCount() {
    return this.header.size();
  }
  
  public String getField(int index) {
    return this.header.get(index);
  }
  
  public boolean isFieldMarked(int index) {
    return this.markedFields.contains(Integer.valueOf(index));
  }
  
  public boolean isFieldMarked(String field) {
    int fieldIndex = this.header.indexOf(field);
    return isFieldMarked(fieldIndex);
  }
  
  public boolean isFieldUntitled(int index) {
    return ((Boolean)this.untitledFields.get(index)).booleanValue();
  }
  
  public void addComment(String c) {
    this.comments.append(String.valueOf(c) + "\n");
  }
  
  public String getComments() {
    return this.comments.toString();
  }
  
  public List<Integer> removeRepeatingRows() {
    for (int r1 = 0; r1 < getRowCount() - 1; r1++) {
      Row row1 = getRow(r1);
      for (int r2 = r1 + 1; r2 < getRowCount(); r2++) {
        Row row2 = getRow(r2);
        if (row1.equals(row2))
          row2.set(0, ""); 
      } 
    } 
    List<Integer> rowsRemoved = new ArrayList<>(getRowCount() / 2);
    int rowCount = getRowCount();
    for (int c = 0, r = 0; c < rowCount; c++, r++) {
      Row row = getRow(r);
      if (row.get(0) == null ) {
    	  this.data.remove(r--);
          rowsRemoved.add(Integer.valueOf(c + 1));
      }
      else if (row.get(0).equals("")) {
        this.data.remove(r--);
        rowsRemoved.add(Integer.valueOf(c + 1));
      } 
    } 
    return rowsRemoved;
  }
  
  public static Table loadFromFile(File f) throws Exception {
    int extensionIndex = f.getName().toLowerCase().indexOf(".csv", 0);
    String tableName = f.getName().substring(0, extensionIndex);
    Table table = new Table(tableName);
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line = br.readLine().trim();
    for (; line.equals(""); line = br.readLine().trim());
    String[] cellsArray = line.trim().split("\\s*,\\s*");
    ArrayList<Integer> untitledColumns = new ArrayList<>(cellsArray.length);
    for (int i = 0, a = 1; i < cellsArray.length; i++) {
      if (cellsArray[i].equals("")) {
        cellsArray[i] = "SIN_NOMBRE_" + a++;
        table.addField(cellsArray[i], true);
        untitledColumns.add(Integer.valueOf(i));
      } else {
        table.addField(cellsArray[i], false);
      } 
    } 
    String repeatedTitles = "";
    for (int j = 0; j < cellsArray.length; j++) {
      if (!cellsArray[j].equals("?"))
        for (int m = j + 1; m < cellsArray.length; m++) {
          if (cellsArray[j].equals(cellsArray[m])) {
            if (!repeatedTitles.equals(""))
              repeatedTitles = String.valueOf(repeatedTitles) + ", "; 
            repeatedTitles = String.valueOf(repeatedTitles) + cellsArray[j];
            break;
          } 
        }  
    } 
    if (!repeatedTitles.equals(""))
      table.addComment("Existen columnas con nombres repetidos: " + repeatedTitles); 
    int maxColumns = cellsArray.length;
    line = br.readLine().trim();
    for (; line.equals(""); line = br.readLine().trim());
    String[] marksArray = line.split(",");
    boolean marksRowFound = true;
    int k;
    for (k = 0; k < marksArray.length; k++) {
      String s = marksArray[k].trim();
      if (!s.equals("") && !s.equals("x") && !s.equals("X")) {
        marksRowFound = false;
        break;
      } 
    } 
    if (marksRowFound) {
      for (k = 0; k < marksArray.length; k++) {
        String s = marksArray[k].trim();
        if (!s.equals(""))
          table.addMarkedField(k); 
      } 
      line = br.readLine().trim();
      for (; line.equals(""); line = br.readLine().trim());
    } 
    while (line != null) {
      if (line.trim().equals("")) {
        line = br.readLine();
        continue;
      } 
      cellsArray = line.trim().split("\\s*,\\s*");
      Row row = new Row(cellsArray);
      if (row.size() > maxColumns)
        maxColumns = row.size(); 
      table.addRow(row);
      line = br.readLine();
    } 
    int colsToAdd = maxColumns - table.getFieldCount();
    int c;
    for (c = 0; c < colsToAdd; c++) {
      untitledColumns.add(Integer.valueOf(table.getFieldCount()));
      table.addField("SIN_NOMBRE_" + untitledColumns.size(), true);
    } 
    br.close();
    for (c = maxColumns; c < marksArray.length; ) {
      table.removeMarkedField(c);
      c++;
    } 
    List<Integer> rowsRemoved = table.removeRepeatingRows();
    if (!rowsRemoved.isEmpty()) {
      String str = "Se eliminaron las siguientes filas repetidas: ";
      for (int m = 0; m < rowsRemoved.size(); m++) {
        if (m > 0)
          str = String.valueOf(str) + ", "; 
        str = String.valueOf(str) + rowsRemoved.get(m);
      } 
      table.addComment(str);
    } 
    if (!untitledColumns.isEmpty()) {
      table.addComment("Las siguientes columnas (comenzando en 0) no tienen ty se eliminaren la 1FN: " + untitledColumns);
      String comment1 = "Estos atributos se considerarmultivaluados: ";
      String comment = "";
      for (int m = 0; m < untitledColumns.size(); m++) {
        int column = ((Integer)untitledColumns.get(m)).intValue();
        if (m == 0) {
          comment = String.valueOf(comment) + table.getField(column - 1);
        } else if (((Integer)untitledColumns.get(m - 1)).intValue() != column - 1) {
          if (!comment.equals(""))
            comment = String.valueOf(comment) + ", "; 
          comment = String.valueOf(comment) + table.getField(column - 1);
        } 
      } 
      table.addComment(String.valueOf(comment1) + comment);
      table.addComment("La 1FN generaruna nueva fila por cada combinacide valores de los atributos multivaluados");
    } else {
      table.addComment("La tabla tiene el formato adecuado");
    } 
    return table;
  }
}
