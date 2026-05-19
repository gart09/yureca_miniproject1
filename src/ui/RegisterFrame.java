package ui;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private JLabel statusLabel;

    public RegisterFrame() {
        setTitle("신규 등록");
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton btnStudent = new JButton("학생 등록");
        JButton btnInstructor = new JButton("강사 등록");
        JButton btnBehavior = new JButton("행동 등록");

        buttonPanel.add(btnStudent);
        buttonPanel.add(btnInstructor);
        buttonPanel.add(btnBehavior);
        add(buttonPanel, BorderLayout.NORTH);
        
        // Status Label to show which registration is active
        statusLabel = new JLabel("--- 학생 등록 ---", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Center Panel to hold Status Label + Forms
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(statusLabel, BorderLayout.NORTH);

        // Card Panel for dynamic forms
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createStudentForm(), "Student");
        cards.add(createInstructorForm(), "Instructor");
        cards.add(createBehaviorForm(), "Behavior");

        centerPanel.add(cards, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners for buttons
        btnStudent.addActionListener(e -> {
            cardLayout.show(cards, "Student");
            statusLabel.setText("--- 학생 등록 ---");
        });
        
        btnInstructor.addActionListener(e -> {
            cardLayout.show(cards, "Instructor");
            statusLabel.setText("--- 강사 등록 ---");
        });
        
        btnBehavior.addActionListener(e -> {
            cardLayout.show(cards, "Behavior");
            statusLabel.setText("--- 행동 등록 ---");
        });

        setLocationRelativeTo(null);
    }

    private JPanel createStudentForm() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
        panel.add(new JLabel("이름:"));
        panel.add(new JTextField());
        panel.add(new JLabel("나이:"));
        panel.add(new JTextField());
        JButton btnSave = new JButton("저장");
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "학생이 등록되었습니다.");
            dispose();
        });
        panel.add(new JLabel("")); 
        panel.add(btnSave);
        return wrapInTopAlignPanel(panel);
    }

    private JPanel createInstructorForm() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
        panel.add(new JLabel("이름:"));
        panel.add(new JTextField());
        panel.add(new JLabel("나이:"));
        panel.add(new JTextField());
        JButton btnSave = new JButton("저장");
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "강사가 등록되었습니다.");
            dispose();
        });
        panel.add(new JLabel(""));
        panel.add(btnSave);
        return wrapInTopAlignPanel(panel);
    }

    private JPanel createBehaviorForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));
        panel.add(new JLabel("행동 이름:"));
        panel.add(new JTextField());
        panel.add(new JLabel("점수:"));
        panel.add(new JTextField());
        panel.add(new JLabel("상벌점 여부:"));
        
        String[] options = {"상점 (True)", "벌점 (False)"};
        panel.add(new JComboBox<>(options));
        
        JButton btnSave = new JButton("저장");
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "행동이 등록되었습니다.");
            dispose();
        });
        panel.add(new JLabel(""));
        panel.add(btnSave);
        return wrapInTopAlignPanel(panel);
    }
    
    private JPanel wrapInTopAlignPanel(JPanel formPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(formPanel, BorderLayout.NORTH);
        return wrapper;
    }
}