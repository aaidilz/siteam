package app.view.Developer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserView extends JFrame {

  public UserView() {
    setTitle("Manajemen User");
    setSize(500, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTable table = new JTable(new DefaultTableModel(
        new Object[] { "ID", "Username", "Role" }, 0));

    add(new JScrollPane(table));
  }
}
