package view;

import model.dto.BehaviorDto;
import model.dto.CanNotFindException;
import model.dto.DuplicateBehaviorException;
import model.dto.InstructorDto;
import model.dto.StudentDto;
import model.service.BehaviorService;
import model.service.BehaviorServiceImp;
import model.service.InstructorService;
import model.service.InstructorServiceImp;
import model.service.StudentService;
import model.service.StudentServiceImp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable resultTable;
    private JScrollPane scrollPane;
    private BehaviorService behaviorService;
    private StudentService studentService;
    private InstructorService instructorService;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Service Initialization
        behaviorService = new BehaviorServiceImp();
        studentService = new StudentServiceImp();
        instructorService = new InstructorServiceImp();

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
        
        tabbedPane.addTab("학생", createStudentTabPanel());
        tabbedPane.addTab("강사", createInstructorTabPanel());
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
        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            if (title.equals("학생")) {
                int studentId = (int) resultTable.getValueAt(selectedRow, 0);
                studentService.remove(studentId);
                updateStudentTable(studentService.searchAll());
            } else if (title.equals("강사")) {
                int instructorId = (int) resultTable.getValueAt(selectedRow, 0);
                instructorService.remove(instructorId);
                updateInstructorTable(instructorService.searchAll());
            } else if (title.equals("행동")) {
                int behaviorId = (int) resultTable.getValueAt(selectedRow, 0);
                behaviorService.remove(behaviorId);
                updateBehaviorTable(behaviorService.searchAll());
            }
            JOptionPane.showMessageDialog(this, "삭제되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "삭제 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSort() {
        // ... (unchanged)
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

        ModifyDialog dialog = new ModifyDialog(this, title, resultTable, selectedRow, studentService, instructorService, behaviorService);
        dialog.setVisible(true);
        
        if (dialog.isModified()) {
             try {
                 if (title.equals("학생")) updateStudentTable(studentService.searchAll());
                 else if (title.equals("강사")) updateInstructorTable(instructorService.searchAll());
                 else if (title.equals("행동")) updateBehaviorTable(behaviorService.searchAll());
             } catch (Exception ex) {
                 System.err.println("테이블 갱신 실패: " + ex.getMessage());
             }
        }
    }

    // --- Table Update Methods ---
    private void updateStudentTable(List<StudentDto> students) {
        String[] columnNames = {"ID", "이름", "나이", "점수"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (StudentDto dto : students) {
            model.addRow(new Object[]{dto.getStudentId(), dto.getName(), dto.getAge(), dto.getScore()});
        }
        resultTable.setModel(model);
    }

    private void updateInstructorTable(List<InstructorDto> instructors) {
        String[] columnNames = {"ID", "이름", "나이"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (InstructorDto dto : instructors) {
            model.addRow(new Object[]{dto.getInstructorId(), dto.getName(), dto.getAge()});
        }
        resultTable.setModel(model);
    }

    private void updateBehaviorTable(List<BehaviorDto> behaviors) {
        String[] columnNames = {"ID", "이름", "점수"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (BehaviorDto dto : behaviors) {
            model.addRow(new Object[]{dto.getBehaviorId(), dto.getName(), dto.getScore()});
        }
        resultTable.setModel(model);

        if (resultTable.getColumnModel().getColumnCount() > 1) {
            // 인덱스 1번 ("이름" 열)을 가져옵니다.
            javax.swing.table.TableColumn nameColumn = resultTable.getColumnModel().getColumn(1);

            // 원하는 기본 너비를 지정합니다. (예: 200 픽셀)
            nameColumn.setPreferredWidth(200);

            // 필요하다면 최소/최대 너비도 제한할 수 있습니다.
            // nameColumn.setMinWidth(150);
        }
    }

    // --- Panel Creation Methods ---
    private JPanel createStudentTabPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JTabbedPane innerTabPane = new JTabbedPane();

        // --- 조회 탭 ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(10, 10, 10, 10);
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 0; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("이름:"), gbcSearch);
        JTextField nameField = new JTextField(10);
        gbcSearch.gridx = 1; gbcSearch.gridy = 0; gbcSearch.weightx = 0.7;
        searchPanel.add(nameField, gbcSearch);
        
        JButton btnSearch = new JButton("학생 조회");
        gbcSearch.gridx = 0; gbcSearch.gridy = 2; gbcSearch.gridwidth = 2;
        searchPanel.add(btnSearch, gbcSearch);
        
        btnSearch.addActionListener(e -> {
            try {
                if (!nameField.getText().trim().isEmpty()) {
                    updateStudentTable(studentService.searchSimilarByName(nameField.getText()));
                } else {
                    updateStudentTable(studentService.searchAll());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "조회 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        JTextField regNameField = new JTextField(10);
        gbcReg.gridx = 1; gbcReg.gridy = 0; gbcReg.weightx = 0.7;
        registerPanel.add(regNameField, gbcReg);
        
        gbcReg.gridx = 0; gbcReg.gridy = 1; gbcReg.weightx = 0.3;
        registerPanel.add(new JLabel("나이:"), gbcReg);
        JTextField regAgeField = new JTextField(10);
        gbcReg.gridx = 1; gbcReg.gridy = 1; gbcReg.weightx = 0.7;
        registerPanel.add(regAgeField, gbcReg);

        JButton btnRegister = new JButton("학생 등록");
        gbcReg.gridx = 0; gbcReg.gridy = 2; gbcReg.gridwidth = 2;
        registerPanel.add(btnRegister, gbcReg);
        
        btnRegister.addActionListener(e -> {
            try {
                studentService.add(new StudentDto(0, regNameField.getText(), Integer.parseInt(regAgeField.getText()), 0));
                JOptionPane.showMessageDialog(this, "학생 등록 완료");
                updateStudentTable(studentService.searchAll());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "등록 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel registerWrapper = new JPanel(new BorderLayout());
        registerWrapper.add(registerPanel, BorderLayout.NORTH);
        innerTabPane.addTab("등록", registerWrapper);

        wrapper.add(innerTabPane, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel createInstructorTabPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JTabbedPane innerTabPane = new JTabbedPane();

        // --- 조회 탭 ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(10, 10, 10, 10);
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 0; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("이름:"), gbcSearch);
        JTextField nameField = new JTextField(10);
        gbcSearch.gridx = 1; gbcSearch.gridy = 0; gbcSearch.weightx = 0.7;
        searchPanel.add(nameField, gbcSearch);
        
        JButton btnSearch = new JButton("강사 조회");
        gbcSearch.gridx = 0; gbcSearch.gridy = 2; gbcSearch.gridwidth = 2;
        searchPanel.add(btnSearch, gbcSearch);
        
        btnSearch.addActionListener(e -> {
            try {
                if (!nameField.getText().trim().isEmpty()) {
                    updateInstructorTable(instructorService.searchSimilarByName(nameField.getText()));
                } else {
                    updateInstructorTable(instructorService.searchAll());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "조회 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        JTextField regNameField = new JTextField(10);
        gbcReg.gridx = 1; gbcReg.gridy = 0; gbcReg.weightx = 0.7;
        registerPanel.add(regNameField, gbcReg);
        
        gbcReg.gridx = 0; gbcReg.gridy = 1; gbcReg.weightx = 0.3;
        registerPanel.add(new JLabel("나이:"), gbcReg);
        JTextField regAgeField = new JTextField(10);
        gbcReg.gridx = 1; gbcReg.gridy = 1; gbcReg.weightx = 0.7;
        registerPanel.add(regAgeField, gbcReg);

        JButton btnRegister = new JButton("강사 등록");
        gbcReg.gridx = 0; gbcReg.gridy = 2; gbcReg.gridwidth = 2;
        registerPanel.add(btnRegister, gbcReg);
        
        btnRegister.addActionListener(e -> {
            try {
                instructorService.add(new InstructorDto(0, regNameField.getText(), Integer.parseInt(regAgeField.getText())));
                JOptionPane.showMessageDialog(this, "강사 등록 완료");
                updateInstructorTable(instructorService.searchAll());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "등록 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });
        
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
        JTextField nameField = new JTextField(10);
        gbcName.gridx = 1; gbcName.gridy = 0; gbcName.weightx = 0.7;
        namePanel.add(nameField, gbcName);
        JButton searchByNameBtn = new JButton("이름으로 조회");
        gbcName.gridx = 0; gbcName.gridy = 1; gbcName.gridwidth = 2;
        namePanel.add(searchByNameBtn, gbcName);
        behaviorSearchTabs.addTab("이름으로 조회", namePanel);

        searchByNameBtn.addActionListener(e -> {
            try {
                updateBehaviorTable(behaviorService.searchSimilarByName(nameField.getText()));
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
                updateBehaviorTable(behaviorService.searchByScore(min, max));
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

        JButton btnRegisterBehavior = new JButton("행동 등록");
        gbcBehReg.gridx = 0; gbcBehReg.gridy = 3; gbcBehReg.gridwidth = 2;
        behaviorRegisterPanel.add(btnRegisterBehavior, gbcBehReg);

        btnRegisterBehavior.addActionListener(e -> {
            try {
                behaviorService.add(new BehaviorDto(0, regNameField.getText(), Integer.parseInt(regScoreField.getText())));
                JOptionPane.showMessageDialog(this, "행동 등록 완료");
                updateBehaviorTable(behaviorService.searchAll());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "등록 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
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