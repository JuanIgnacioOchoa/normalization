package db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registro implements Serializable {
  private String fase;
  
  private String fechaEntrada;
  
  private String fechaSalida;
  
  private String descripcion;
  
  private int alumnoId;
  
  public Registro() {}
  
  public Registro(String fase) {
    this.fase = fase;
  }
  
  public void setFase(String fase) {
    this.fase = fase;
  }
  
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  
  public void setEntrada() {
    this.fechaEntrada = getHoraActual();
  }
  
  public void setSalida() {
    this.fechaSalida = getHoraActual();
  }
  
  public static String getHoraActual() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    String frmtdDate = dateFormat.format(date);
    return frmtdDate;
  }
  
  public void setAlumnoID(int id) {
    this.alumnoId = id;
  }
  
  public String toString() {
    return "'" + this.fase + "','" + 
      this.fechaEntrada + "','" + 
      this.fechaSalida + "','" + 
      this.descripcion + "'," + 
      this.alumnoId;
  }
}
