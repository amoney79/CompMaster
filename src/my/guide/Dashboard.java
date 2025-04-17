/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.guide;

/**
 *
 * @author macke
 */
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    String username;

    public Dashboard(String username) {
        this.username = username;

        setTitle("LingoMaster - Dashboard");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));

        String[] languages = {"Spanish", "French", "German"};
        JComboBox<String> languageSelect = new JComboBox<>(languages);
        JButton startButton = new JButton("Start Lesson");

        startButton.addActionListener(e -> {
            String lang = (String) languageSelect.getSelectedItem();
            new LessonView(lang, username).setVisible(true);
            dispose();
        });

        JPanel center = new JPanel();
        center.add(new JLabel("Choose a Language:"));
        center.add(languageSelect);

        setLayout(new BorderLayout());
        add(welcome, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }
}
