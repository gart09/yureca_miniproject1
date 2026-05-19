package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 350); // 창 크기를 확 줄임
        setLayout(new BorderLayout());

        // Right half: Result List
        String[] columnNames = {"ID", "Name", "Info1", "Info2"};
        Object[][] data = {
                {"1", "임진우", "32", "20"},
                {"1", "김동근", "40", "-"}
        };
        JTable resultTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(300, 350)); // 리스트 패널의 너비와 높이도 줄임
        add(scrollPane, BorderLayout.EAST);

        // Left half: Controls
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("조회 (이름/나이 조건)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 이름 입력
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        searchPanel.add(new JLabel("이름:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
        searchPanel.add(new JTextField(10), gbc);
        
        // 나이 입력
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        searchPanel.add(new JLabel("나이:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
        searchPanel.add(new JTextField(10), gbc);

        // 버튼들을 담을 서브 패널
        JPanel searchButtonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        searchButtonPanel.add(new JButton("학생 조회"));
        searchButtonPanel.add(new JButton("강사 조회"));
        searchButtonPanel.add(new JButton("행동 조회"));
        searchButtonPanel.add(new JButton("최근 기록 조회"));
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        searchPanel.add(searchButtonPanel, gbc);
        
        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        
        controlPanel.add(searchPanel);
        controlPanel.add(actionPanel);
        
        // 위쪽으로 컴포넌트들을 밀어올리기 위해 북쪽에 추가
        leftPanel.add(controlPanel, BorderLayout.NORTH);
        
        add(leftPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
}