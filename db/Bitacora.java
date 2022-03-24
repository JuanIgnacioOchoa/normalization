package db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Vector;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class Bitacora {
  private static final String FILE_NAME = "nrmConfig.nrm";
  
  private int alumnoId;
  
  private Vector<Registro> registros = new Vector<>();
  
  private static final byte[] key = "0123456789111155".getBytes();
  
  private static final String transformation = "AES/ECB/PKCS5Padding";
  
  public Bitacora(int alumnoId) {
    this.alumnoId = alumnoId;
    leerArchivoEncriptado();
  }
  
  public Bitacora(int alumnoID, Registro r) {
    this(alumnoID);
    agregarRegistro(r);
  }
  
  public void agregarRegistro(Registro r) {
    r.setAlumnoID(this.alumnoId);
    this.registros.add(r);
  }
  /*
  public void almacenarDatos() {
    if (!almacenarEnBD())
      almacenarEnArchivoEncriptado(); 
  }
  */
  private void leerArchivoEncriptado() {
    try {
      File file = new File("nrmConfig.nrm");
      if (!file.exists())
        return; 
      ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
            new FileInputStream(file)));
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] bArray = (byte[])in.readObject();
      bos.write(bArray);
      ByteArrayInputStream bais = new ByteArrayInputStream(
          bos.toByteArray());
      Object ob = decrypt(bais);
      this.registros = (Vector<Registro>)ob;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error al desencriptar");
    } 
  }
  
  private void leerArchivo() {
    try {
      File file = new File("nrmConfig.nrm");
      if (!file.exists())
        return; 
      ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
            new FileInputStream(file)));
      try {
        this.registros = (Vector<Registro>)in.readObject();
      } catch (OptionalDataException e) {
        System.out.println("Error al leer archivo");
        file.delete();
      } finally {
        in.close();
      } 
    } catch (IOException e) {
      System.out.println("Error no se encontrarchivo");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("Error con el archivo");
    } 
  }
  
  private void almacenarEnArchivo(Object o) {
    ObjectOutputStream out = null;
    try {
      File file = new File("nrmConfig.nrm");
      file.delete();
      if (!file.exists())
        file.createNewFile(); 
      FileOutputStream outFile = new FileOutputStream(file, false);
      out = new ObjectOutputStream(new BufferedOutputStream(outFile));
      try {
        out.writeObject(o);
        out.reset();
        out.flush();
      } finally {
        out.close();
      } 
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  private void almacenarEnArchivoEncriptado() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      encrypt(this.registros, baos);
      almacenarEnArchivo(baos.toByteArray());
    } catch (Exception e) {
      System.out.println("Error al encriptar");
    } 
  }
  /*
  private boolean almacenarEnBD() {
    boolean res = true;
    Connection miConexion = DBConnection.getConnection();
    if (miConexion != null) {
      for (Registro r : this.registros) {
        try {
          int maxId = 0;
          Statement st = miConexion.createStatement();
          st.execute("SELECT MAX(idBitacora) FROM Bitacora");
          ResultSet rs = st.getResultSet();
          if (rs.next())
            maxId = rs.getInt(1); 
          String sql = "INSERT INTO Bitacora  VALUES (" + (
            maxId + 1) + "," + r + ")";
          st.executeUpdate(sql);
        } catch (SQLException e) {
          System.out.println("Error en el sql");
          res = false;
        } 
      } 
      File file = new File("nrmConfig.nrm");
      file.delete();
    } else {
      return false;
    } 
    try {
      miConexion.close();
    } catch (Exception e) {
      res = false;
      System.out.println("Error al querer  guardar en la base de datos");
    } 
    return res;
  }
  */
  public static void encrypt(Serializable object, OutputStream ostream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    try {
      SecretKeySpec sks = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(1, sks);
      SealedObject sealedObject = new SealedObject(object, cipher);
      CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
      ObjectOutputStream outputStream = new ObjectOutputStream(cos);
      outputStream.writeObject(sealedObject);
      outputStream.close();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } 
  }
  
  public static Object decrypt(InputStream istream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    SecretKeySpec sks = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(2, sks);
    CipherInputStream cipherInputStream = new CipherInputStream(istream, 
        cipher);
    ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
    try {
      SealedObject sealedObject = (SealedObject)inputStream.readObject();
      return sealedObject.getObject(cipher);
    } catch (ClassNotFoundException|IllegalBlockSizeException|javax.crypto.BadPaddingException e) {
      e.printStackTrace();
      return null;
    } 
  }
}
