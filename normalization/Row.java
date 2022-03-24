package normalization;

import java.util.ArrayList;

public class Row {
  private ArrayList<String> values;
  
  public Row() {
    this.values = new ArrayList<>();
  }
  
  public Row(String[] valuesArray) {
    this.values = new ArrayList<>(valuesArray.length);
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = valuesArray).length, b = 0; b < i; ) {
      String v = arrayOfString[b];
      this.values.add(v);
      b++;
    } 
  }
  
  public void add(String v) {
	if(v == null) {
		this.values.add("");
	} else {
		this.values.add(v);
	}
    
  }
  
  public void addFirst(int index, String v) {
    this.values.add(0, v);
  }
  
  public Row clone() {
    Row r = new Row();
    for (String v : this.values)
      r.add(v); 
    return r;
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Row))
      return false; 
    Row r = (Row)o;
    return this.values.equals(r.values);
  }
  
  public String get(int index) {
    if (index < 0 || index >= this.values.size())
      return null; 
    return this.values.get(index);
  }
  
  public void set(int index, String v) {
    if (index < 0 || index >= this.values.size())
      return; 
    this.values.set(index, v);
  }
  
  public int size() {
    return this.values.size();
  }
  
  public String toString() {
    return String.valueOf(this.values.toString()) + "\n";
  }
  
  public String[] toArray(int rowIndex) {
    String[] stringArray = new String[this.values.size() + 1];
    stringArray[0] = (new StringBuilder(String.valueOf(rowIndex))).toString();
    for (int i = 0; i < this.values.size(); ) {
      stringArray[i + 1] = this.values.get(i);
      i++;
    } 
    return stringArray;
  }
}
