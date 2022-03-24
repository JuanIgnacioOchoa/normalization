package normalization;

import db.Bitacora;
import db.Registro;
import gui.About;
import gui.MainWindow;
import gui.NF1Panel;
import gui.NF2Panel;
import gui.NF3Panel;
import gui.ReadPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class Shared {
	
  public static final Color WINDOW_COLOR = new Color(200, 210, 220);
  
  public static final Font TAB_FONT = new Font("Calibri", 2, 20);
  
  public static final Font LABEL_FONT = new Font("Calibri", 0, 18);
  
  public static final Font LABEL_FONT1 = new Font("Calibri", 2, 16);
  
  public static final Font BUTTON_FONT = new Font("Calibri", 0, 16);
  
  public static final Cursor BUTTON_CURSOR = new Cursor(12);
  
  public static final Color GRID_BACKGROUND = new Color(210, 220, 220);
  
  public static final Color GRID_FOREGROUND = new Color(0, 0, 64);
  
  public static final Color GRID_KEY_BACKGROUND = new Color(230, 210, 200);
  
  public static final Color GRID_KEY_FOREGROUND = new Color(64, 0, 0);
  
  public static final Color GRID_SELECTED_BACKGROUND = new Color(0, 0, 192);
  
  public static final Color GRID_SELECTED_FOREGROUND = Color.WHITE;
  
  public static final Color GRID_HEADER_BACKGROUND = new Color(0, 0, 32);
  
  public static final Color GRID_HEADER_FOREGROUND = Color.WHITE;
  
  public static final int GRID_ROW_HEIGHT = 25;
  
  public static final int GRID_MAX_WIDTH = 800;
  
  public static final int GRID_MAX_HEIGHT = 220;
  
  public static final int GRID_FIRST_COL_WIDTH = 30;
  
  public static final String FIRST_COL_TITLE = "#";
  
  public static ReadPanel readPanel = new ReadPanel();
  
  public static NF1Panel nf1Panel = new NF1Panel();
  
  public static NF2Panel nf2Panel = new NF2Panel();
  
  public static NF3Panel nf3Panel = new NF3Panel();
  
  public static MainWindow mainWindow = new MainWindow();
  
  public static About about = new About();
  
  public static Bitacora bitacora = null;
  
  public static Registro registroSesion = null;
}
