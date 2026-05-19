package view;

import model.dto.BehaviorDto;
import model.dto.CanNotFindException;
import model.dto.DuplicateBehaviorException;
import model.service.BehaviorService;
import model.service.BehaviorServiceImp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable resultTable;
    private JScrollPane scrollPane;
    private BehaviorService behaviorService;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Service Initialization
        behaviorService = new BehaviorServiceImp();

        // Right half: Result List and Action Buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] defaultColumnNames = {"결과 목록"};
        Object[][] defaultData = {{"조회/등록 버튼을 클릭하세요."}};
        resultTable = new JTable(new DefaultTableModel(defaultData, defaultColumnNames));
        scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(350, 400));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons at the bottom of the result list
        JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnSort = new JButton("정렬");
        JButton btnModify = new JButton("수정");
        JButton btnDeleteSelected = new JButton("선택 항목 삭제");
        
        btnSort.addActionListener(e -> handleSort());
        btnModify.addActionListener(e -> handleModify());
        btnDeleteSelected.addActionListener(e -> handleDelete());

        rightBottomPanel.add(btnSort);
        rightBottomPanel.add(btnModify);
        rightBottomPanel.add(btnDeleteSelected);
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // Left half: Controls
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Tabbed Pane (Left - Vertical Tab Layout equivalent)
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
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
        btnRewardPenalty.addActionListener(e -> new RewardPenaltyFrame(behaviorService).setVisible(true));
        rewardPanel.add(btnRewardPenalty);
        bottomPanel.add(rewardPanel, BorderLayout.CENTER);

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(leftPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void handleDelete() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "리스트에서 삭제할 항목을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedTabIndex = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(selectedTabIndex);

        if (title.equals("행동")) {
            int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int behaviorId = (int) resultTable.getValueAt(selectedRow, 0);
                    String name = (String) resultTable.getValueAt(selectedRow, 1);
                    int score = (int) resultTable.getValueAt(selectedRow, 2);
                    BehaviorDto dtoToDelete = new BehaviorDto(behaviorId, name, score);
                    
                    behaviorService.remove(dtoToDelete);
                    JOptionPane.showMessageDialog(this, "삭제되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    updateBehaviorTable(behaviorService.searchAll());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "삭제 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
             new DeleteDialog(this).setVisible(true);
        }
    }

    private void handleSort() {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(selectedTabIndex);
        JPopupMenu sortMenu = new JPopupMenu();

        if (title.equals("학생")) {
            sortMenu.add(new JMenuItem("이름순 정렬"));
            sortMenu.add(new JMenuItem("나이순 정렬"));
            sortMenu.add(new JMenuItem("점수순 정렬"));
        } else if (title.equals("강사")) {
            sortMenu.add(new JMenuItem("이름순 정렬"));
            sortMenu.add(new JMenuItem("나이순 정렬"));
        } else if (title.equals("행동")) {
            sortMenu.add(new JMenuItem("이름순 정렬"));
            sortMenu.add(new JMenuItem("점수순 정렬"));
        } else {
            JOptionPane.showMessageDialog(this, "이 탭에서는 정렬을 지원하지 않습니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for(Component comp : sortMenu.getComponents()) {
            if(comp instanceof JMenuItem) {
                ((JMenuItem)comp).addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, ((JMenuItem)e.getSource()).getText() + " 기능은 준비중입니다.");
                });
            }
        }
        
        sortMenu.show(this, 350, 400); 
    }

    private void handleModify() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "리스트에서 수정할 항목을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedTabIndex = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(selectedTabIndex);
        
        if(title.equals("최근 이력")) {
            JOptionPane.showMessageDialog(this, "이력은 수정할 수 없습니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ModifyDialog dialog = new ModifyDialog(this, title, resultTable, selectedRow, behaviorService);
        dialog.setVisible(true);
        
        if (dialog.isModified() && title.equals("행동")) {
             try {
                 updateBehaviorTable(behaviorService.searchAll());
             } catch (Exception ex) {
                 System.err.println("테이블 갱신 실패: " + ex.getMessage());
             }
        }
    }

    private void updateBehaviorTable(List<BehaviorDto> behaviors) {
        String[] columnNames = {"ID", "이름", "점수", "상점여부"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (BehaviorDto dto : behaviors) {
            Object[] row = {
                    dto.getBehaviorId(),
                    dto.getName(),
                    dto.getScore(),
                    dto.getScore() >= 0 ? "상점" : "벌점"
            };
            model.addRow(row);
        }
        resultTable.setModel(model);
    }

    private JPanel createTabPanel(String type) {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        JTabbedPane innerTabPane = new JTabbedPane();

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.add(new JLabel(type + " 조회 기능은 준비중입니다."), BorderLayout.CENTER);
        innerTabPane.addTab("조회", searchWrapper);

        JPanel registerWrapper = new JPanel(new BorderLayout());
        registerWrapper.add(new JLabel(type + " 등록 기능은 준비중입니다."), BorderLayout.CENTER);
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
        JTextField nameField = new JTextField(10);
        gbcName.gridx = 1; gbcName.gridy = 0; gbcName.weightx = 0.7;
        namePanel.add(nameField, gbcName);
        JButton searchByNameBtn = new JButton("이름으로 조회");
        gbcName.gridx = 0; gbcName.gridy = 1; gbcName.gridwidth = 2;
        namePanel.add(searchByNameBtn, gbcName);
        behaviorSearchTabs.addTab("이름으로 조회", namePanel);

        searchByNameBtn.addActionListener(e -> {
            try {
                List<BehaviorDto> behaviors = behaviorService.searchSimilarByName(nameField.getText());
                updateBehaviorTable(behaviors);
            } catch (CanNotFindException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "조회 실패", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 점수로 조회 탭
        JPanel scorePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.insets = new Insets(10, 5, 10, 5);
        gbcScore.fill = GridBagConstraints.HORIZONTAL;
        gbcScore.gridx = 0; gbcScore.gridy = 0; gbcScore.weightx = 0.2;
        scorePanel.add(new JLabel("점수:"), gbcScore);
        JTextField minScoreField = new JTextField(3);
        gbcScore.gridx = 1; gbcScore.gridy = 0; gbcScore.weightx = 0.3;
        scorePanel.add(minScoreField, gbcScore);
        gbcScore.gridx = 2; gbcScore.gridy = 0; gbcScore.weightx = 0.1;
        scorePanel.add(new JLabel("~", SwingConstants.CENTER), gbcScore);
        JTextField maxScoreField = new JTextField(3);
        gbcScore.gridx = 3; gbcScore.gridy = 0; gbcScore.weightx = 0.3;
        scorePanel.add(maxScoreField, gbcScore);
        JButton searchByScoreBtn = new JButton("점수로 조회");
        gbcScore.gridx = 0; gbcScore.gridy = 1; gbcScore.gridwidth = 4;
        scorePanel.add(searchByScoreBtn, gbcScore);
        behaviorSearchTabs.addTab("점수로 조회", scorePanel);
        
        searchByScoreBtn.addActionListener(e -> {
            try {
                String minStr = minScoreField.getText();
                String maxStr = maxScoreField.getText();
                int min = minStr.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minStr);
                int max = maxStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxStr);
                List<BehaviorDto> behaviors = behaviorService.searchByScore(min, max);
                updateBehaviorTable(behaviors);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "점수는 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } catch (CanNotFindException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "조회 실패", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        behaviorTabs.addTab("조회", behaviorSearchTabs);

        // --- 행동 등록 탭 ---
        JPanel behaviorRegisterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBehReg = new GridBagConstraints();
        gbcBehReg.insets = new Insets(10, 10, 10, 10);
        gbcBehReg.fill = GridBagConstraints.HORIZONTAL;

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 0; gbcBehReg.weightx = 0.4;
        behaviorRegisterPanel.add(new JLabel("행동 이름:"), gbcBehReg);
        JTextField regNameField = new JTextField(10);
        gbcBehReg.gridx = 1; gbcBehReg.gridy = 0; gbcBehReg.weightx = 0.6;
        behaviorRegisterPanel.add(regNameField, gbcBehReg);

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 1; gbcBehReg.weightx = 0.4;
        behaviorRegisterPanel.add(new JLabel("점수:"), gbcBehReg);
        JTextField regScoreField = new JTextField(10);
        gbcBehReg.gridx = 1; gbcBehReg.gridy = 1; gbcBehReg.weightx = 0.6;
        behaviorRegisterPanel.add(regScoreField, gbcBehReg);

        gbcBehReg.gridx = 0; gbcBehReg.gridy = 3; gbcBehReg.gridwidth = 2;
        JButton btnRegisterBehavior = new JButton("행동 등록");
        behaviorRegisterPanel.add(btnRegisterBehavior, gbcBehReg);

        btnRegisterBehavior.addActionListener(e -> {
            try {
                String name = regNameField.getText();
                int score = Integer.parseInt(regScoreField.getText());
                
                behaviorService.add(new BehaviorDto(0, name, score)); // 수정됨: isPositive 제거
                JOptionPane.showMessageDialog(this, "행동이 성공적으로 등록되었습니다.", "등록 성공", JOptionPane.INFORMATION_MESSAGE);
                
                regNameField.setText("");
                regScoreField.setText("");
                updateBehaviorTable(behaviorService.searchAll());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "점수는 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } catch (DuplicateBehaviorException | CanNotFindException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "등록 실패", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel behRegWrapper = new JPanel(new BorderLayout());
        behRegWrapper.add(behaviorRegisterPanel, BorderLayout.NORTH);
        behaviorTabs.addTab("등록", behRegWrapper);

        wrapper.add(behaviorTabs, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createHistorySearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("최근 이력 조회 기능은 준비중입니다."), BorderLayout.CENTER);
        return panel;
    }
}