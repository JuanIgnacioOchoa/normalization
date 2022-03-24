package gui;

import db.Bitacora;
//import db.DBConnection;
import db.Registro;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import normalization.Shared;

import java.io.*;

public class LoginWindow extends JFrame {
  private JLabel lblExpediente = new JLabel("Nde expediente:");
  
  private JLabel lblMensaje = new JLabel("(Seis d)");
  
  private JTextField txtExpediente = new JTextField("");
  
  private JButton btnAceptar = new JButton("Aceptar");
  
  private JButton btnCancelar = new JButton("Cancelar");
  
  public LoginWindow() {
    super("Normalizaciv1.1");
    setDefaultCloseOperation(3);
    setSize(420, 250);
    setResizable(false);
    setLocationRelativeTo(null);
    init();
    setVisible(true);
  }
  
  public void init() {
    Container c = getContentPane();
    c.setBackground(Shared.WINDOW_COLOR);
    c.setLayout((LayoutManager)null);
    this.lblExpediente.setFont(Shared.LABEL_FONT);
    this.lblExpediente.setHorizontalAlignment(4);
    this.lblExpediente.setBounds(30, 50, 200, 25);
    this.lblMensaje.setFont(new Font("Calibri", 0, 15));
    this.lblMensaje.setBounds(130, 70, 100, 25);
    this.txtExpediente.setFont(Shared.LABEL_FONT);
    this.txtExpediente.setHorizontalAlignment(0);
    this.txtExpediente.setBounds(250, 50, 100, 25);
    this.btnAceptar.setBounds(50, 130, 150, 30);
    this.btnAceptar.setCursor(Shared.BUTTON_CURSOR);
    this.btnAceptar.setFont(Shared.BUTTON_FONT);
    this.btnAceptar.setMnemonic('A');
    this.btnCancelar.setBounds(220, 130, 150, 30);
    this.btnCancelar.setCursor(Shared.BUTTON_CURSOR);
    this.btnCancelar.setFont(Shared.BUTTON_FONT);
    this.btnCancelar.setMnemonic('C');
    this.btnAceptar.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            LoginWindow.this.login();
          }
        });
    this.btnCancelar.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            System.exit(0);
          }
        });
    c.add(this.lblExpediente);
    c.add(this.lblMensaje);
    c.add(this.txtExpediente);
    c.add(this.btnAceptar);
    c.add(this.btnCancelar);
  }
  
  private void login() {
	  
    try {
      int expediente = Integer.parseInt(this.txtExpediente.getText().trim());
      /*
      DBConnection.State state = DBConnection.login(expediente);
      switch (state) {
        case null:
          JOptionPane.showMessageDialog(null, "Error en la conexicon la base de datos", "Imposible continuar", 0);
          return;
        case USER_NOT_FOUND:
          JOptionPane.showMessageDialog(this, "El expediente no existe", "Imposible continuar", 0);
          return;
      } 
      */
      //String nombre = DBConnection.getName(expediente);
      String nombre = "Juan";
      JOptionPane.showMessageDialog(this, "" + nombre + "!", "Ingreso exitoso", 1);
      Shared.bitacora = new Bitacora(expediente);
      Shared.registroSesion = new Registro("Sesion");
      Shared.registroSesion.setEntrada();
      Shared.registroSesion.setDescripcion("Sesion");
      Shared.bitacora.agregarRegistro(Shared.registroSesion);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "El expediente debe ser un nentero", "Imposible continuar", 0);
      return;
    } 
   	
    setVisible(false);
    Shared.mainWindow.setVisible(true);
  }
  
  public static void main(String[] args) {
	  LoginWindow lw = new LoginWindow();
  }
}
