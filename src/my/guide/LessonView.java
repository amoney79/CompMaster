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
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class LessonView extends JFrame {

    private String language;
    private String username;
    private ArrayList<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;

    public LessonView(String language, String username) {
        this.language = language;
        this.username = username;

        setTitle("LingoMaster - " + language + " Lesson");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        questions = loadQuestionsFromDB(language);
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for " + language);
            dispose();
            return;
        }

        questionLabel = new JLabel("", SwingConstants.CENTER);
        options = new JRadioButton[4];
        group = new ButtonGroup();
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> checkAndLoadNext());

        setLayout(new BorderLayout());
        add(questionLabel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        loadQuestion(currentIndex);
    }

    private void loadQuestion(int index) {
        group.clearSelection();
        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.text);
        options[0].setText("A. " + q.optionA);
        options[1].setText("B. " + q.optionB);
        options[2].setText("C. " + q.optionC);
        options[3].setText("D. " + q.optionD);
    }

    private void checkAndLoadNext() {
        String selected = null;
        if (options[0].isSelected()) selected = "A";
        else if (options[1].isSelected()) selected = "B";
        else if (options[2].isSelected()) selected = "C";
        else if (options[3].isSelected()) selected = "D";

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }

        if (selected.equals(questions.get(currentIndex).correctOption)) {
            score++;
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect! Correct answer is " + questions.get(currentIndex).correctOption);
        }

        currentIndex++;
        if (currentIndex < questions.size()) {
            loadQuestion(currentIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Lesson complete!\nScore: " + score + "/" + questions.size());
            new Dashboard(username).setVisible(true);
            dispose();
        }
    }

    private ArrayList<Question> loadQuestionsFromDB(String lang) {
        ArrayList<Question> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/lingomaster", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE language=?")) {
            stmt.setString(1, lang);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Question(
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                ));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Inner class for question object
    class Question {
        String text, optionA, optionB, optionC, optionD, correctOption;

        Question(String text, String a, String b, String c, String d, String correct) {
            this.text = text;
            this.optionA = a;
            this.optionB = b;
            this.optionC = c;
            this.optionD = d;
            this.correctOption = correct;
        }
    }
}
