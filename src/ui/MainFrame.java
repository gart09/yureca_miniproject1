package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTable resultTable;
    private JScrollPane scrollPane;

    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Right half: Result List and Delete Button
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] defaultColumnNames = {"결과 목록"};
        Object[][] defaultData = {{"조회/등록 버튼을 클릭하세요."}};
        resultTable = new JTable(defaultData, defaultColumnNames);
        scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(350, 400));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Delete button at the bottom of the result list
        JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDeleteSelected = new JButton("선택 항목 삭제");
        btnDeleteSelected.addActionListener(e -> {
            if (resultTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "리스트에서 삭제할 항목을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                new DeleteDialog(this).setVisible(true);
            }
        });
        rightBottomPanel.add(btnDeleteSelected);
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // Left half: Controls
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Tabbed Pane (Left - Vertical Tab Layout equivalent)
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
        tabbedPane.addTab("학생", createTabPanel("학생"));
        tabbedPane.addTab("강사", createTabPanel("강사"));
        tabbedPane.addTab("행동", createBehaviorTabPanel());
        tabbedPane.addTab("최근 이력", createHistorySearchPanel());

        leftPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom Action Panel (Only for Reward/Penalty)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel rewardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rewardPanel.setBorder(BorderFactory.createTitledBorder("상벌점"));
        
        JButton btnRewardPenalty = new JButton("상벌점 부여");
        btnRewardPenalty.addActionListener(e -> new RewardPenaltyFrame().setVisible(true));
        rewardPanel.add(btnRewardPenalty);
        bottomPanel.add(rewardPanel, BorderLayout.CENTER);

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(leftPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private JPanel createTabPanel(String type) {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        // Inner TabbedPane for Search vs Register
        JTabbedPane innerTabPane = new JTabbedPane();

        // --- 조회 탭 ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(10, 10, 10, 10);
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 0; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("이름:"), gbcSearch);
        gbcSearch.gridx = 1; gbcSearch.gridy = 0; gbcSearch.weightx = 0.7;
        searchPanel.add(new JTextField(10), gbcSearch);
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 1; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("나이:"), gbcSearch);
        gbcSearch.gridx = 1; gbcSearch.gridy = 1; gbcSearch.weightx = 0.7;
        searchPanel.add(new JTextField(10), gbcSearch);
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 2; gbcSearch.gridwidth = 2;
        JButton btnSearch = new JButton(type + " 조회");
        searchPanel.add(btnSearch, gbcSearch);
        
        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.add(searchPanel, BorderLayout.NORTH);
        innerTabPane.addTab("조회", searchWrapper);

        // --- 등록 탭 ---
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(10, 10, 10, 10);
        gbcReg.fill = GridBagConstraints.HORIZONTAL;
        
        gbcReg.gridx = 0; gbcReg.gridy = 0; gbcReg.weightx = 0.3;
        registerPanel.add(new JLabel("이름:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.gridy = 0; gbcReg.weightx = 0.7;
        registerPanel.add(new JTextField(10), gbcReg);
        
        gbcReg.gridx = 0; gbcReg.gridy = 1; gbcReg.weightx = 0.3;
        registerPanel.add(new JLabel("나이:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.gridy = 1; gbcReg.weightx = 0.7;
        registerPanel.add(new JTextField(10), gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 2; gbcReg.gridwidth = 2;
        JButton btnRegister = new JButton(type + " 등록");
        btnRegister.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, type + " 등록이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        });
        registerPanel.add(btnRegister, gbcReg);
        
        JPanel registerWrapper = new JPanel(new BorderLayout());
        registerWrapper.add(registerPanel, BorderLayout.NORTH);
        innerTabPane.addTab("등록", registerWrapper);

        wrapper.add(innerTabPane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createBehaviorTabPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        JTabbedPane behaviorTabs = new JTabbedPane();

        // --- 조회 하위 탭 (이름 / 점수) ---
        JTabbedPane behaviorSearchTabs = new JTabbedPane();

        // 이름으로 조회 탭
        JPanel namePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcName = new GridBagConstraints();
        gbcName.insets = new Insets(10, 10, 10, 10);
        gbcName.fill = GridBagConstraints.HORIZONTAL;
        gbcName.gridx = 0; gbcName.gridy = 0; gbcName.weightx = 0.3;
        namePanel.add(new JLabel("이름:"), gbcName);
        gbcName.gridx = 1; gbcName.gridy = 0; gbcName.weightx = 0.7;
        namePanel.add(new JTextField(10), gbcName);
        gbcName.gridx = 0; gbcName.gridy = 1; gbcName.gridwidth = 2;
        namePanel.add(new JButton("이름으로 조회"), gbcName);
        behaviorSearchTabs.addTab("이름으로 조회", namePanel);

        // 점수로 조회 탭
        JPanel scorePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.insets = new Insets(10, 5, 10, 5);
        gbcScore.fill = GridBagConstraints.HORIZONTAL;
        gbcScore.gridx = 0; gbcScore.gridy = 0; gbcScore.weightx = 0.2;
        scorePanel.add(new JLabel("점수:"), gbcScore);
        gbcScore.gridx = 1; gbcScore.gridy = 0; gbcScore.weightx = 0.3;
        scorePanel.add(new JTextField(3), gbcScore);
        gbcScore.gridx = 2; gbcScore.gridy = 0; gbcScore.weightx = 0.1;
        scorePanel.add(new JLabel("~", SwingConstants.CENTER), gbcScore);
        gbcScore.gridx = 3; gbcScore.gridy = 0; gbcScore.weightx = 0.3;
        scorePanel.add(new JTextField(3), gbcScore);
        gbcScore.gridx = 0; gbcScore.gridy = 1; gbcScore.gridwidth = 4;
        scorePanel.add(new JButton("점수로 조회"), gbcScore);
        behaviorSearchTabs.addTab("점수로 조회", scorePanel);
        
        behaviorTabs.addTab("조회", behaviorSearchTabs);

        // --- 행동 등록 탭 ---
        JPanel behaviorRegisterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBehReg = new GridBagConstraints();
        gbcBehReg.insets = new Insets(10, 10, 10, 10);
        gbcBehReg.fill = GridBagConstraints.HORIZONTAL;

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 0; gbcBehReg.weightx = 0.4;
        behaviorRegisterPanel.add(new JLabel("행동 이름:"), gbcBehReg);
        gbcBehReg.gridx = 1; gbcBehReg.gridy = 0; gbcBehReg.weightx = 0.6;
        behaviorRegisterPanel.add(new JTextField(10), gbcBehReg);

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 1; gbcBehReg.weightx = 0.4;
        behaviorRegisterPanel.add(new JLabel("점수:"), gbcBehReg);
        gbcBehReg.gridx = 1; gbcBehReg.gridy = 1; gbcBehReg.weightx = 0.6;
        behaviorRegisterPanel.add(new JTextField(10), gbcBehReg);

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 2; gbcBehReg.weightx = 0.4;
        behaviorRegisterPanel.add(new JLabel("상벌점 여부:"), gbcBehReg);
        gbcBehReg.gridx = 1; gbcBehReg.gridy = 2; gbcBehReg.weightx = 0.6;
        String[] options = {"상점 (True)", "벌점 (False)"};
        behaviorRegisterPanel.add(new JComboBox<>(options), gbcBehReg);

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 3; gbcBehReg.gridwidth = 2;
        JButton btnRegisterBehavior = new JButton("행동 등록");
        btnRegisterBehavior.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "행동 등록이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        });
        behaviorRegisterPanel.add(btnRegisterBehavior, gbcBehReg);

        JPanel behRegWrapper = new JPanel(new BorderLayout());
        behRegWrapper.add(behaviorRegisterPanel, BorderLayout.NORTH);
        behaviorTabs.addTab("등록", behRegWrapper);

        wrapper.add(behaviorTabs, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createHistorySearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        panel.add(new JLabel("최근 n개 조회:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.6;
        panel.add(new JTextField(10), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel hintLabel = new JLabel("(빈 칸일 시 모든 이력 조회)");
        hintLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        hintLabel.setForeground(Color.GRAY);
        panel.add(hintLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnSearch = new JButton("이력 조회");
        panel.add(btnSearch, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}