package gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import normalization.Shared;

public class About extends JFrame {
  public About() {
    super("Acerca de ...");
    JPanel panel = new DesiPanel();
    panel.setLayout(new FlowLayout());
    add(panel);
    setSize(911, 693);
    setLocationRelativeTo((Component)null);
    setDefaultCloseOperation(1);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            Shared.mainWindow.setVisible(true);
          }
        });
  }
}
