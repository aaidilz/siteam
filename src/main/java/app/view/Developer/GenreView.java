package app.view.Developer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GenreView extends JFrame {

  public GenreView() {
    setTitle("Manajemen Genre");
    setSize(500, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTable table = new JTable(new DefaultTableModel(
        new Object[] { "ID", "Nama Genre" }, 0));

    add(new JScrollPane(table));
  }
}
