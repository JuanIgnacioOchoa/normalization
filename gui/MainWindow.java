package gui;

import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import normalization.Shared;

public class MainWindow extends JFrame {
  private JTabbedPane tabContainer;
  
  private JButton btnAcercade = new JButton("?");
  
  private static MainWindow instance = null;
  
  public MainWindow() {
    setTitle("Normalizaciv1.1");
    setSize(1008, 850);
    setDefaultCloseOperation(2);
    setLocationRelativeTo(null);
    setResizable(false);
    initComponents();
    instance = this;
    addWindowListener(new WindowAdapter() {
          public void windowClosed(WindowEvent e) {
            //Shared.registroSesion.setSalida();
            //Shared.bitacora.almacenarDatos();
            System.exit(0);
          }
        });
  }
  
  private void initComponents() {
    Container c = getContentPane();
    c.setLayout((LayoutManager)null);
    c.setBackground(Shared.WINDOW_COLOR);
    this.btnAcercade.setBounds(899, 13, 45, 40);
    this.btnAcercade.setFocusable(false);
    this.btnAcercade.setFont(Shared.BUTTON_FONT);
    this.btnAcercade.setToolTipText("Acerca de");
    this.btnAcercade.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            MainWindow.getInstance().setVisible(false);
            Shared.about.setVisible(true);
          }
        });
    c.add(this.btnAcercade);
    this.tabContainer = new JTabbedPane(1);
    this.tabContainer.setBounds(39, 33, 910, 740);
    this.tabContainer.addTab("Lectura", Shared.readPanel);
    this.tabContainer.setFont(Shared.TAB_FONT);
    getContentPane().add(this.tabContainer);
  }
  
  public void addNF1Tab() {
    this.tabContainer.addTab("Primera FN", Shared.nf1Panel);
    this.tabContainer.setSelectedIndex(1);
  }
  
  public void addNF2Tab() {
    this.tabContainer.addTab("Segunda FN", Shared.nf2Panel);
    this.tabContainer.setSelectedIndex(2);
  }
  
  public void addNF3Tab() {
    this.tabContainer.addTab("Tercera FN", Shared.nf3Panel);
    this.tabContainer.setSelectedIndex(3);
  }
  
  public void removeTabs() {
    int n = this.tabContainer.getTabCount();
    if (n >= 3)
      Shared.nf2Panel.init(); 
    if (n >= 4)
      Shared.nf3Panel.init(); 
    for (int t = 1; t < n; ) {
      this.tabContainer.removeTabAt(1);
      t++;
    } 
  }
  
  public static MainWindow getInstance() {
    return instance;
  }
}
