package app.view;

import app.Controller.UserController;
import app.Util.Session;
import app.Model.User;

import javax.swing.*;
import java.awt.*;

public class ProfileView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JLabel lblRole;
    private UserController controller;
    private User currentUser;

    // Dark theme colors
    private static final Color BG_DARK = new Color(27, 40, 56);
    private static final Color BG_DARKER = new Color(23, 26, 33);
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_INPUT = new Color(45, 45, 45);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_BLUE = new Color(102, 192, 244);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color BTN_BLUE = new Color(66, 133, 244);
    private static final Color BTN_GRAY = new Color(100, 100, 100);

    public ProfileView() {
        this.controller = new UserController();
        this.currentUser = Session.getInstance().getUser();

        setTitle("My Profile");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_DARKER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("My Profile");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(ACCENT_BLUE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        add(createFormPanel(), BorderLayout.CENTER);

        // Button Panel
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Role & Saldo Info
        lblRole = new JLabel("Role: " + (currentUser != null ? currentUser.getRole() : "-"));
        lblRole.setFont(new Font("Arial", Font.BOLD, 14));
        lblRole.setForeground(ACCENT_GREEN);
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblRole);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (currentUser != null && "USER".equals(currentUser.getRole())) {
            // We need to fetch saldo from DB to be accurate
            // But for now, Session user might not have latest saldo if not updated.
            // Ideally UserController should have getSaldo.
            // We can skip saldo here or fetch it. Let's skip for simplicity or use label
            // placeholder.
        }

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username
        addLabel(panel, "Username");
        txtUsername = createTextField();
        if (currentUser != null)
            txtUsername.setText(currentUser.getUsername());
        panel.add(txtUsername);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        addLabel(panel, "New Password (leave blank to keep current)");
        txtPassword = createPasswordField();
        panel.add(txtPassword);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirm Password
        addLabel(panel, "Confirm New Password");
        txtConfirmPassword = createPasswordField();
        panel.add(txtConfirmPassword);

        return panel;
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(BG_INPUT);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(BG_INPUT);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

        JButton btnSave = createStyledButton("Save Changes", BTN_BLUE);
        JButton btnClose = createStyledButton("Close", BTN_GRAY);

        btnSave.addActionListener(e -> saveChanges());
        btnClose.addActionListener(e -> dispose());

        panel.add(Box.createHorizontalGlue());
        panel.add(btnClose);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnSave);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void saveChanges() {
        String newUsername = txtUsername.getText().trim();
        String newPass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.isEmpty() && !newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update profile
        if (currentUser != null) {
            boolean success = controller.updateProfile(currentUser.getId(), newUsername);
            if (success) {
                // Update password if provided
                if (!newPass.isEmpty()) {
                    controller.updateUserPassword(currentUser.getId(), newPass);
                }

                // Update session
                currentUser.setUsername(newUsername);
                // Ideally refresh other views if they show username

                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
    }
}
