package ui;

import javax.swing.*;
import java.awt.*;

public class RewardPenaltyFrame extends JFrame {
    public RewardPenaltyFrame() {
        setTitle("상벌점 부여");
        setSize(800, 500);
        setLayout(new BorderLayout());

        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        // Instructor Column
        listsPanel.add(createListPanel("강사 리스트", "강사 이름 검색"));
        
        // Student Column
        listsPanel.add(createListPanel("학생 리스트", "학생 이름 검색"));
        
        // Behavior Column
        listsPanel.add(createListPanel("행동 리스트", "행동 이름 검색"));
        
        add(listsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnGrant = new JButton("부여");
        btnGrant.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "상벌점 부여 및 저장이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        bottomPanel.add(btnGrant);
        
        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }
    
    private JPanel createListPanel(String title, String searchPlaceholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        String[] dummyData = {"항목 1", "항목 2", "항목 3"};
        JList<String> list = new JList<>(dummyData);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        searchPanel.add(new JTextField(), BorderLayout.CENTER);
        
        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }
}