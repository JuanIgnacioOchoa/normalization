package normalization;

import java.util.HashSet;
import java.util.Set;

public class Key {
  private Set<Integer> values;
  
  private Set<String> names;
  
  public Key() {
    this.values = new HashSet<>();
    this.names = new HashSet<>();
  }
  
  public Key(Set<Integer> values, Set<String> names) {
    this.values = values;
    this.names = names;
  }
  
  public void addToKey(int value, String name) {
    this.values.add(Integer.valueOf(value));
    this.names.add(name);
  }
  
  public boolean contains(int value) {
    return this.values.contains(Integer.valueOf(value));
  }
  
  public boolean contains(String name) {
    return this.names.contains(name);
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Key))
      return false; 
    Key key = (Key)o;
    return this.values.equals(key.values);
  }
  
  public boolean equalsFieldNames(Key key) {
    if (size() != key.size())
      return false; 
    for (String n : this.names) {
      if (!key.names.contains(n))
        return false; 
    } 
    return true;
  }
  
  public Integer[] getValues() {
    Integer[] valuesArray = new Integer[this.values.size()];
    this.values.toArray(valuesArray);
    return valuesArray;
  }
  
  public String[] getNames() {
    String[] namesArray = new String[this.names.size()];
    this.names.toArray(namesArray);
    return namesArray;
  }
  
  public boolean isSubsetOf(Key key) {
    if (size() > key.size())
      return false; 
    for (String n : this.names) {
      if (!key.contains(n))
        return false; 
    } 
    return true;
  }
  
  public int size() {
    return this.values.size();
  }
  
  public String toString() {
    return this.names.toString();
  }
}
