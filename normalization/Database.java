package normalization;

import java.util.ArrayList;

public class Database {
  private ArrayList<Table> tables = new ArrayList<>();
  
  private StringBuffer comments = new StringBuffer();
  
  public void add(Table table) {
    this.tables.add(table);
  }
  
  public Table get(Key key) {
    for (Table t : this.tables) {
      Key k = t.getPrimaryKey();
      if (key.equalsFieldNames(k))
        return t; 
    } 
    return null;
  }
  
  public Table get(int index) {
    return this.tables.get(index);
  }
  
  public int size() {
    return this.tables.size();
  }
  
  public void addComment(String c) {
    this.comments.append(String.valueOf(c) + "\n");
  }
  
  public String getComments() {
    if (this.comments.length() == 0)
      return ""; 
    return this.comments.toString();
  }
}
