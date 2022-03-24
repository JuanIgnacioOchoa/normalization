package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DesiPanel extends JPanel {
  ImageIcon imagen = new ImageIcon(getClass().getResource("/images/about.jpg"));
  
  public void paintComponent(Graphics g) {
    Dimension tam = getSize();
    g.drawImage(this.imagen.getImage(), 0, 0, tam.width, tam.height, null);
    setOpaque(false);
    super.paintComponent(g);
  }
}
