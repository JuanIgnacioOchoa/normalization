package normalization;

public class Dependency extends Pair<Key, String> {
  public Dependency(Key first, String second) {
    super(first, second);
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Dependency))
      return false; 
    Dependency d = (Dependency)o;
    return (getFirst().equals(d.getFirst()) && getSecond().equals(d.getSecond()));
  }
}
