package app.view.Developer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GameView extends JFrame {

  public GameView() {
    setTitle("Manajemen Game");
    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTable table = new JTable(new DefaultTableModel(
        new Object[] { "ID", "Nama", "Genre", "Harga" }, 0));

    add(new JScrollPane(table), BorderLayout.CENTER);
  }
}
