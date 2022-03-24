package normalization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalForm {
  private static boolean defines(int i1, int i2, Table table) {
    HashSet<String> value1Set = new HashSet<>();
    for (int i = 0; i < table.getRowCount(); i++) {
      String value1 = table.getValueAt(i, i1);
      if (!value1Set.contains(value1)) {
        value1Set.add(value1);
        String value2 = table.getValueAt(i, i2);
        for (int j = i + 1; j < table.getRowCount(); j++) {
          if (value1.equals(table.getValueAt(j, i1)) && !value2.equals(table.getValueAt(j, i2)))
            return false; 
        } 
      } 
    } 
    return true;
  }
  
  private static boolean defines(Set<Integer> iSet, int i2, Table table) {
    HashSet<String> valueSet = new HashSet<>();
    for (int i = 0; i < table.getRowCount(); i++) {
      String valuesi = "";
      for (Integer a : iSet)
        valuesi = String.valueOf(valuesi) + table.getValueAt(i, a.intValue()) + "\n"; 
      if (!valueSet.contains(valuesi)) {
        valueSet.add(valuesi);
        String value2 = table.getValueAt(i, i2);
        for (int j = i + 1; j < table.getRowCount(); j++) {
          String valuesj = "";
          for (Integer a : iSet)
            valuesj = String.valueOf(valuesj) + table.getValueAt(j, a.intValue()) + "\n"; 
          if (valuesi.equals(valuesj) && 
            !value2.equals(table.getValueAt(j, i2)))
            return false; 
        } 
      } 
    } 
    return true;
  }
  
  private static void buildSubset(Set<Integer> originalSet, Set<Integer> subset, Set<Set<Integer>> powerSet, int size, Integer lastAdded) {
    if (subset.size() >= size) {
      if (!powerSet.contains(subset))
        powerSet.add(subset); 
      return;
    } 
    for (Integer I : originalSet) {
      if (I.intValue() <= lastAdded.intValue() || subset.contains(I))
        continue; 
      Set<Integer> subsetClone = new HashSet<>();
      for (Integer J : subset)
        subsetClone.add(J); 
      subsetClone.add(I);
      buildSubset(originalSet, subsetClone, powerSet, size, I);
    } 
  }
  
  private static Set<Set<Integer>> findSubsets(Set<Integer> originalSet, int size) {
    Set<Set<Integer>> powerSet = new HashSet<>();
    if (size >= originalSet.size()) {
      powerSet.add(originalSet);
    } else if (size > 0) {
      for (Integer I : originalSet) {
        Set<Integer> subset = new HashSet<>();
        subset.add(I);
        buildSubset(originalSet, subset, powerSet, size, I);
      } 
    } 
    return powerSet;
  }
  
  public static void findPrimaryKey(Table table) {
    HashSet<Integer> Ap = new HashSet<>();
    HashSet<Integer> Adef = new HashSet<>();
    HashSet<Integer> Andf = new HashSet<>();
    for (int i = 0; i < table.getFieldCount(); i++) {
      if (!table.isFieldMarked(i))
        Ap.add(Integer.valueOf(i)); 
      Andf.add(Integer.valueOf(i));
    } 
    int iterations = Andf.size();
    for (int j = 1; j <= iterations; j++) {
      Set<Set<Integer>> powerSet = findSubsets(Ap, j);
      for (Set<Integer> phi : powerSet) {
        if (phi.size() < j)
          continue; 
        for (Integer I : phi) {
          if (Adef.contains(I))
            break; 
        } 
        for (Integer J : Andf) {
          if (phi.contains(J))
            continue; 
          if (defines(phi, J.intValue(), table)) {
            Adef.add(J);
            Key key = new Key();
            for (Integer I : phi)
              key.addToKey(I.intValue(), table.getField(I.intValue())); 
            table.addDependency(key, table.getField(J.intValue()));
          } 
        } 
        label49: for (Integer J : Adef) {
          Andf.remove(J);
          Ap.remove(J);
        } 
      } 
    } 
    for (Integer integer : Ap)
      table.addToPrimaryKey(integer.intValue()); 
    Key pk = table.getPrimaryKey();
    if (!table.existsDependency(pk))
      table.addDependency(table.getPrimaryKey(), null); 
  }
  
  public static Table to1FN(Table table) {
    Table table1FN = new Table(String.valueOf(table.getName()) + "_1FN");
    int first = -1;
    int end = -1;
    ArrayList<IntPair> multiValuedAtt = new ArrayList<>();
    for (int f = 0, f1 = 0; f < table.getFieldCount(); f++) {
      if (table.isFieldUntitled(f)) {
        if (first < 0) {
          first = f - 1;
          end = -1;
        } 
      } else {
        if (table.isFieldMarked(f))
          table1FN.addMarkedField(f1); 
        f1++;
        if (first >= 0) {
          end = f - 1;
          multiValuedAtt.add(new IntPair(first, end));
          first = -1;
        } 
        table1FN.addField(table.getField(f));
      } 
    } 
    if (first >= 0 && end < 0)
      multiValuedAtt.add(new IntPair(first, table.getFieldCount() - 1)); 
    for (int r = 0; r < table.getRowCount(); r++) {
      Row currentRow = table.getRow(r);
      if (multiValuedAtt.isEmpty()) {
        table1FN.addRow(currentRow.clone());
      } else {
        Row newRow = new Row();
        for (int i = 0; i < table.getFieldCount(); i++) {
          if (!table.isFieldUntitled(i))
            newRow.add(currentRow.get(i)); 
        } 
        table1FN.addRow(newRow);
        int firstIndex = table1FN.getRowCount() - 1;
        int targetCol = ((IntPair)multiValuedAtt.get(0)).getFirst().intValue();
        for (int mv = 0; mv < multiValuedAtt.size(); mv++) {
          int startMV = ((IntPair)multiValuedAtt.get(mv)).getFirst().intValue();
          int endMV = ((IntPair)multiValuedAtt.get(mv)).getSecond().intValue();
          if (mv > 0)
            targetCol += startMV - ((IntPair)multiValuedAtt.get(mv - 1)).getSecond().intValue(); 
          int lastIndex = table1FN.getRowCount() - 1;
          for (int j = startMV + 1; j <= endMV; j++) {
            String value = currentRow.get(j);
            if (value != null && !value.equals(""))
              for (int k = firstIndex; k <= lastIndex; k++) {
                Row veryCurrentRow = table1FN.getRow(k);
                Row veryNewRow = new Row();
                for (int m = 0; m < table1FN.getFieldCount(); m++) {
                  if (m == targetCol) {
                    veryNewRow.add(value);
                  } else {
                    veryNewRow.add(veryCurrentRow.get(m));
                  } 
                } 
                table1FN.addRow(veryNewRow);
              }  
          } 
        } 
      } 
    } 
    findPrimaryKey(table1FN);
    table1FN.addComment("Se encontrla siguiente llave principal: " + table1FN.getPrimaryKeyAsString());
    return table1FN;
  }
  
  private static void addDependency(Key rootKey, String field, List<Dependency> dependencies, List<Dependency> dependenciesToPK) {
    dependenciesToPK.add(new Dependency(rootKey, field));
    for (int i = 0; i < dependencies.size(); i++) {
      Dependency d = dependencies.get(i);
      Key key = d.getFirst();
      if (key.size() == 1 && key.contains(field))
        addDependency(rootKey, d.getSecond(), dependencies, dependenciesToPK); 
    } 
  }
  
  public static Database to2FN(Table table1FN) {
    List<Dependency> dependencies = table1FN.getDependencies();
    List<Key> keysFound = new ArrayList<>();
    Database database = new Database();
    Key primaryKey = table1FN.getPrimaryKey();
    List<Dependency> dependenciesToPK = new ArrayList<>();
    for (Dependency d : dependencies) {
      Key key = d.getFirst();
      if (key.isSubsetOf(primaryKey))
        addDependency(key, d.getSecond(), dependencies, dependenciesToPK); 
    } 
    for (Dependency d : dependenciesToPK) {
      Key key = d.getFirst();
      String field = d.getSecond();
      boolean marked = table1FN.isFieldMarked(field);
      if (keysFound.contains(key)) {
        Table table1 = database.get(key);
        table1.addField(field);
        if (marked)
          table1.addMarkedField(table1.getFieldCount() - 1); 
        continue;
      } 
      String title = "";
      byte b1;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = key.getNames()).length, b1 = 0; b1 < i; ) {
        String n = arrayOfString[b1];
        title = String.valueOf(title) + n + "_";
        b1++;
      } 
      if (title.length() > 1)
        title = title.substring(0, title.length() - 1); 
      Table table = new Table(title);
      int keyIndex = 0;
      byte b2;
      int j;
      Integer[] arrayOfInteger;
      for (j = (arrayOfInteger = key.getValues()).length, b2 = 0; b2 < j; ) {
        Integer k = arrayOfInteger[b2];
        table.addField(table1FN.getField(k.intValue()));
        table.addToPrimaryKey(keyIndex++);
        b2++;
      } 
      if (field != null) {
        table.addField(field);
        if (marked)
          table.addMarkedField(table.getFieldCount() - 1); 
      } 
      keysFound.add(key);
      database.add(table);
    } 
    for (int t = 0; t < database.size(); t++) {
      Table table = database.get(database.size() - t - 1);
      if (table.getPrimaryKey().size() == table1FN.getPrimaryKey().size()) {
        if (table.getFieldCount() == table.getPrimaryKey().size()) {
          database.addComment("Ningatributo no llave depende de toda la llave principal.");
          database.addComment("Se eliminaron todos los atributos no llave de la tabla original.\n");
        } else {
          String comment = "Los atributos {";
          for (int f = 0; f < table.getFieldCount(); f++) {
            if (!table.isKey(f)) {
              if (comment.length() > 15)
                comment = String.valueOf(comment) + ", "; 
              comment = String.valueOf(comment) + table.getField(f);
            } 
          } 
          database.addComment(String.valueOf(comment) + "} dependen de la llave principal.");
          database.addComment("Se eliminaron los atributos restantes de la tabla original.\n");
        } 
      } else {
        String comment = "Los atributos {";
        for (int f = 0; f < table.getFieldCount(); f++) {
          if (!table.isKey(f)) {
            if (comment.length() > 15)
              comment = String.valueOf(comment) + ", "; 
            comment = String.valueOf(comment) + table.getField(f);
          } 
        } 
        database.addComment(String.valueOf(comment) + "} dependen de una parte de la llave <" + table.getPrimaryKeyAsString() + ">.");
        database.addComment("Se creuna tabla con llave <" + table.getPrimaryKeyAsString() + "> para almacenar estos atributos.\n");
      } 
      for (int r = 0; r < table1FN.getRowCount(); r++) {
        Row row1 = table1FN.getRow(r);
        Row row2 = new Row();
        for (int f = 0; f < table.getFieldCount(); f++) {
          String fieldName = table.getField(f);
          int f1 = table1FN.getFieldIndex(fieldName);
          row2.add(row1.get(f1));
        } 
        if (!table.contains(row2))
          table.addRow(row2); 
      } 
    } 
    return database;
  }
  
  public static Database to3FN(Database db2FN) {
    Database db3FN = new Database();
    for (int t = 0; t < db2FN.size(); t++) {
      Table table = db2FN.get(t);
      Database db = to3FN(table, 1);
      if (!db.getComments().equals(""))
        db3FN.addComment(db.getComments()); 
      for (int u = 0; u < db.size(); u++)
        db3FN.add(db.get(u)); 
    } 
    return db3FN;
  }
  
  private static Database to3FN(Table table2FN, int iteration) {
    Database db3FN = new Database();
    Key pk = table2FN.getPrimaryKey();
    boolean[] removeField = new boolean[table2FN.getFieldCount()];
    boolean foundDependency = false;
    List<String> comments = new ArrayList<>();
    for (int f = 0; f < table2FN.getFieldCount(); f++) {
      if (!pk.contains(f))
        for (int g = f + 1; g < table2FN.getFieldCount(); g++) {
          if (!pk.contains(g)) {
            int key = -1, notKey = -1;
            if (!table2FN.isFieldMarked(f) && !removeField[f] && 
              defines(f, g, table2FN)) {
              key = f;
              notKey = g;
            } 
            if (key < 0 && !table2FN.isFieldMarked(g) && !removeField[g] && 
              defines(g, f, table2FN)) {
              key = g;
              notKey = f;
            } 
            if (key >= 0) {
              comments.add("El atributo " + table2FN.getField(notKey) + " depende del atributo no llave " + table2FN.getField(key) + ".");
              foundDependency = true;
              removeField[notKey] = true;
              String keyField = table2FN.getField(key);
              String notKeyField = table2FN.getField(notKey);
              Key newKey = new Key();
              newKey.addToKey(0, keyField);
              Table table3FN = db3FN.get(newKey);
              if (table3FN == null) {
                comments.add("Se creuna tabla con clave " + table2FN.getField(key) + " y se le ala columna " + table2FN.getField(notKey) + ".");
                table3FN = new Table(keyField);
                table3FN.addField(keyField);
                table3FN.addToPrimaryKey(0);
                for (int i = 0; i < table2FN.getRowCount(); i++) {
                  Row row = table2FN.getRow(i);
                  Row row1 = new Row();
                  row1.add(row.get(key));
                  table3FN.addRow(row1);
                } 
                db3FN.add(table3FN);
              } else {
                comments.add("Se ala columna " + table2FN.getField(notKey) + " a la tabla con clave " + table2FN.getField(key) + ".");
              } 
              for (int r = 0; r < table2FN.getRowCount(); r++) {
                Row row = table2FN.getRow(r);
                Row row1 = table3FN.getRow(r);
                row1.add(row.get(notKey));
              } 
              table3FN.addField(notKeyField);
              if (table2FN.isFieldMarked(notKey))
                table3FN.addMarkedField(table3FN.getFieldCount() - 1); 
            } 
          } 
        }  
    } 
    if (foundDependency) {
      Table table3FN = new Table(table2FN.getName());
      for (int i = 0, g = 0; i < table2FN.getFieldCount(); i++) {
        if (!removeField[i]) {
          String field = table2FN.getField(i);
          table3FN.addField(field);
          if (table2FN.isFieldMarked(i))
            table3FN.addMarkedField(g); 
          g++;
        } 
      } 
      for (int r = 0; r < table2FN.getRowCount(); r++) {
        Row row = table2FN.getRow(r);
        Row row1 = new Row();
        for (int j = 0; j < row.size(); j++) {
          if (!removeField[j])
            row1.add(row.get(j)); 
        } 
        table3FN.addRow(row1);
      } 
      db3FN.add(table3FN);
      table3FN.setPrimaryKey(table2FN.getPrimaryKey());
    } else {
      db3FN.add(table2FN);
    } 
    for (int t = 0; t < db3FN.size(); t++) {
      Table table = db3FN.get(t);
      table.removeRepeatingRows();
    } 
    Database finalDB = db3FN;
    if (!comments.isEmpty())
      comments.add(0, "Iteraci" + iteration + ":"); 
    if (db3FN.size() > 1) {
      finalDB = new Database();
      for (int i = 0; i < db3FN.size(); i++) {
        Table table = db3FN.get(i);
        Database db3FN1 = to3FN(table, iteration + 1);
        for (int u = 0; u < db3FN1.size(); u++) {
          Table table1 = db3FN1.get(u);
          finalDB.add(table1);
        } 
        if (db3FN1.getComments().length() > 0) {
          comments.add("");
          comments.add(db3FN1.getComments());
        } 
      } 
    } 
    for (String c : comments)
      finalDB.addComment(c); 
    return finalDB;
  }
}
