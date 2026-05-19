package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Right half: Result List
        String[] columnNames = {"ID", "Name", "Info1", "Info2"};
        Object[][] data = {
                {"1", "임진우", "32", "20"},
                {"1", "김동근", "40", "-"}
        };
        JTable resultTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(400, 600));
        add(scrollPane, BorderLayout.EAST);

        // Left half: Controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("조회 (이름/나이 조건)"));
        searchPanel.add(new JLabel("이름:"));
        searchPanel.add(new JTextField());
        searchPanel.add(new JLabel("나이:"));
        searchPanel.add(new JTextField());
        
        JButton btnSearchStudent = new JButton("학생 조회");
        JButton btnSearchInstructor = new JButton("강사 조회");
        JButton btnSearchBehavior = new JButton("행동 조회");
        JButton btnSearchHistory = new JButton("최근 기록 조회");
        
        searchPanel.add(btnSearchStudent);
        searchPanel.add(btnSearchInstructor);
        searchPanel.add(btnSearchBehavior);
        searchPanel.add(btnSearchHistory);
        
        controlPanel.add(searchPanel);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder("작업"));
        
        JButton btnRegister = new JButton("신규 등록");
        JButton btnDelete = new JButton("삭제");
        JButton btnRewardPenalty = new JButton("상벌점 부여");
        
        btnRegister.addActionListener(e -> new RegisterFrame().setVisible(true));
        
        btnDelete.addActionListener(e -> {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "조회 결과 리스트에서 삭제할 항목을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                new DeleteDialog(this).setVisible(true);
            }
        });
        
        btnRewardPenalty.addActionListener(e -> new RewardPenaltyFrame().setVisible(true));
        
        actionPanel.add(btnRegister);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRewardPenalty);
        
        controlPanel.add(actionPanel);
        
        add(controlPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}