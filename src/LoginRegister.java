/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macke
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginRegister extends JFrame {

    JTextField loginUsernameField, registerUsernameField, registerEmailField;
    JPasswordField loginPasswordField, registerPasswordField;
    JButton loginButton, registerButton;

    public LoginRegister() {
        setTitle("LingoMaster - Login/Register");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // --- Login Panel ---
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginUsernameField = new JTextField();
        loginPasswordField = new JPasswordField();
        loginButton = new JButton("Login");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPasswordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        // --- Register Panel ---
        JPanel registerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        registerUsernameField = new JTextField();
        registerEmailField = new JTextField();
        registerPasswordField = new JPasswordField();
        registerButton = new JButton("Register");

        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(registerUsernameField);
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(registerEmailField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(registerPasswordField);
        registerPanel.add(new JLabel());
        registerPanel.add(registerButton);

        tabs.addTab("Login", loginPanel);
        tabs.addTab("Register", registerPanel);

        add(tabs);

        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> registerUser());
    }

    private void loginUser() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lingomaster", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Ideally, hash passwords!

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Dashboard(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerUser() {
        String username = registerUsernameField.getText();
        String email = registerEmailField.getText();
        String password = new String(registerPasswordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lingomaster", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Hash for real apps
            stmt.setString(3, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration successful!");
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}
