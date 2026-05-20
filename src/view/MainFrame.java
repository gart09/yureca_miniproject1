package view;

import model.dto.*;
import model.service.BehaviorService;
import model.service.BehaviorServiceImp;
import model.service.EvaluationService;
import model.service.EvaluationServiceImp;
import model.service.InstructorService;
import model.service.InstructorServiceImp;
import model.service.StudentService;
import model.service.StudentServiceImp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable resultTable;
    private JScrollPane scrollPane;
    private BehaviorService behaviorService;
    private StudentService studentService;
    private InstructorService instructorService;
    private EvaluationService evaluationService;
    private JTabbedPane tabbedPane;
    private TableCellRenderer defaultHeaderRenderer;
    private boolean behaviorHeaderSortingEnabled;
    private String currentResultType;
    private String behaviorSortColumn;
    private String behaviorSortDirection;
    private BehaviorSearchMode studentSearchMode = BehaviorSearchMode.ALL;
    private String studentNameKeyword = "";
    private BehaviorSearchMode instructorSearchMode = BehaviorSearchMode.ALL;
    private String instructorNameKeyword = "";
    private BehaviorSearchMode behaviorSearchMode = BehaviorSearchMode.ALL;
    private String behaviorNameKeyword = "";
    private int behaviorMinScore = Integer.MIN_VALUE;
    private int behaviorMaxScore = Integer.MAX_VALUE;
    private final String[] studentSortColumns = {null, "name", "age", "score"};
    private final String[] instructorSortColumns = {null, "name", "age"};
    private final String[] behaviorSortColumns = {null, "name", "score"};

    public MainFrame() {
        setTitle("Yureca Miniproject 1 - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Service Initialization
        behaviorService = new BehaviorServiceImp();
        studentService = new StudentServiceImp();
        instructorService = new InstructorServiceImp();
        evaluationService = new EvaluationServiceImp();

        // Right half: Result List and Action Buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] defaultColumnNames = {"결과 목록"};
        Object[][] defaultData = {{"조회/등록 버튼을 클릭하세요."}};
        resultTable = new JTable(new DefaultTableModel(defaultData, defaultColumnNames));
        defaultHeaderRenderer = resultTable.getTableHeader().getDefaultRenderer();
        installResultTableHeaderClickHandler();
        scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(350, 400));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons at the bottom of the result list
        JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnHistory = new JButton("상벌점 기록");
        JButton btnModify = new JButton("수정");
        JButton btnDeleteSelected = new JButton("선택 항목 삭제");

        btnHistory.addActionListener(e -> handleHistory());
        btnModify.addActionListener(e -> handleModify());
        btnDeleteSelected.addActionListener(e -> handleDelete());

        rightBottomPanel.add(btnHistory);
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
        btnRewardPenalty.addActionListener(e -> new RewardPenaltyFrame(studentService, instructorService, behaviorService, evaluationService).setVisible(true));
        rewardPanel.add(btnRewardPenalty);
        bottomPanel.add(rewardPanel, BorderLayout.CENTER);

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void handleHistory() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "기록을 조회할 항목을 리스트에서 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        try {
            if (currentTab.equals("학생")) {
                int studentId = (int) resultTable.getValueAt(selectedRow, 0);
                String studentName = (String) resultTable.getValueAt(selectedRow, 1);
                List<EvaluationDetailDto> history = evaluationService.getStudentAllEvaluation(studentId);
                new EvaluationHistoryDialog(this, studentName, history, true).setVisible(true);
            } else if (currentTab.equals("강사")) {
                int instructorId = (int) resultTable.getValueAt(selectedRow, 0);
                String instructorName = (String) resultTable.getValueAt(selectedRow, 1);
                List<EvaluationDetailDto> history = evaluationService.getInstructorAllEvaluation(instructorId);
                new EvaluationHistoryDialog(this, instructorName, history, false).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "이 탭에서는 상벌점 기록 조회를 지원하지 않습니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "기록 조회 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
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
                setStudentAllMode();
                updateStudentTable(studentService.searchAll());
            } else if (title.equals("강사")) {
                int instructorId = (int) resultTable.getValueAt(selectedRow, 0);
                instructorService.remove(instructorId);
                setInstructorAllMode();
                updateInstructorTable(instructorService.searchAll());
            } else if (title.equals("행동")) {
                int behaviorId = (int) resultTable.getValueAt(selectedRow, 0);
                behaviorService.remove(behaviorId);
                setBehaviorAllMode();
                updateBehaviorTable(behaviorService.searchAll());
            }
            JOptionPane.showMessageDialog(this, "삭제되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "삭제 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void installResultTableHeaderClickHandler() {
        resultTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleResultTableHeaderClick(e);
            }
        });
    }

    private void handleResultTableHeaderClick(MouseEvent e) {
        if (!isSortableResultTable()) {
            return;
        }

        int viewColumn = resultTable.columnAtPoint(e.getPoint());
        if (viewColumn < 0) {
            return;
        }

        int modelColumn = resultTable.convertColumnIndexToModel(viewColumn);
        String clickedColumn = getSortableColumn(modelColumn);
        if (clickedColumn == null) {
            return;
        }

        if (!clickedColumn.equals(behaviorSortColumn)) {
            behaviorSortColumn = clickedColumn;
            behaviorSortDirection = "ASC";
        } else if ("ASC".equals(behaviorSortDirection)) {
            behaviorSortDirection = "DESC";
        } else {
            behaviorSortColumn = null;
            behaviorSortDirection = null;
        }

        try {
            String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            if ("학생".equals(title)) {
                updateStudentTable(searchStudentByCurrentMode(), false);
            } else if ("강사".equals(title)) {
                updateInstructorTable(searchInstructorByCurrentMode(), false);
            } else if ("행동".equals(title)) {
                updateBehaviorTable(searchBehaviorByCurrentMode(), false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "정렬 중 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isSortableResultTable() {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex < 0 || !behaviorHeaderSortingEnabled) {
            return false;
        }

        String title = tabbedPane.getTitleAt(selectedTabIndex);
        if (!title.equals(currentResultType)) {
            return false;
        }

        return ("학생".equals(title) && resultTable.getColumnCount() == studentSortColumns.length)
                || ("강사".equals(title) && resultTable.getColumnCount() == instructorSortColumns.length)
                || ("행동".equals(title) && resultTable.getColumnCount() == behaviorSortColumns.length);
    }

    private String getSortableColumn(int modelColumn) {
        String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        String[] sortColumns = null;
        if ("학생".equals(title)) {
            sortColumns = studentSortColumns;
        } else if ("강사".equals(title)) {
            sortColumns = instructorSortColumns;
        } else if ("행동".equals(title)) {
            sortColumns = behaviorSortColumns;
        }

        if (sortColumns == null || modelColumn < 0 || modelColumn >= sortColumns.length) {
            return null;
        }

        return sortColumns[modelColumn];
    }

    private void setBehaviorHeaderSortingEnabled(boolean enabled) {
        JTableHeader header = resultTable.getTableHeader();
        behaviorHeaderSortingEnabled = enabled;
        if (enabled) {
            header.setDefaultRenderer(new BehaviorHeaderRenderer(defaultHeaderRenderer));
            header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            header.setDefaultRenderer(defaultHeaderRenderer);
            header.setCursor(Cursor.getDefaultCursor());
            currentResultType = null;
            behaviorSortColumn = null;
            behaviorSortDirection = null;
            studentSearchMode = BehaviorSearchMode.ALL;
            studentNameKeyword = "";
            instructorSearchMode = BehaviorSearchMode.ALL;
            instructorNameKeyword = "";
            behaviorSearchMode = BehaviorSearchMode.ALL;
            behaviorNameKeyword = "";
            behaviorMinScore = Integer.MIN_VALUE;
            behaviorMaxScore = Integer.MAX_VALUE;
        }
        header.repaint();
    }

    private List<StudentDto> searchStudentByCurrentMode() {
        boolean sorted = behaviorSortColumn != null;

        if (studentSearchMode == BehaviorSearchMode.NAME) {
            if (sorted) {
                return studentService.searchSimilarByName(studentNameKeyword, behaviorSortColumn, behaviorSortDirection);
            }
            return studentService.searchSimilarByName(studentNameKeyword);
        }

        if (sorted) {
            return studentService.searchAll(behaviorSortColumn, behaviorSortDirection);
        }
        return studentService.searchAll();
    }

    private List<InstructorDto> searchInstructorByCurrentMode() {
        boolean sorted = behaviorSortColumn != null;

        if (instructorSearchMode == BehaviorSearchMode.NAME) {
            if (sorted) {
                return instructorService.searchSimilarByName(instructorNameKeyword, behaviorSortColumn, behaviorSortDirection);
            }
            return instructorService.searchSimilarByName(instructorNameKeyword);
        }

        if (sorted) {
            return instructorService.searchAll(behaviorSortColumn, behaviorSortDirection);
        }
        return instructorService.searchAll();
    }

    private List<BehaviorDto> searchBehaviorByCurrentMode() {
        boolean sorted = behaviorSortColumn != null;

        if (behaviorSearchMode == BehaviorSearchMode.NAME) {
            if (sorted) {
                return behaviorService.searchSimilarByName(behaviorNameKeyword, behaviorSortColumn, behaviorSortDirection);
            }
            return behaviorService.searchSimilarByName(behaviorNameKeyword);
        }

        if (behaviorSearchMode == BehaviorSearchMode.SCORE) {
            List<BehaviorDto> behaviors = behaviorService.searchByScore(behaviorMinScore, behaviorMaxScore);
            if (sorted) {
                sortBehaviorResults(behaviors);
            }
            return behaviors;
        }

        if (sorted) {
            return behaviorService.searchAll(behaviorSortColumn, behaviorSortDirection);
        }
        return behaviorService.searchAll();
    }

    private void sortBehaviorResults(List<BehaviorDto> behaviors) {
        java.util.Comparator<BehaviorDto> comparator;

        if ("name".equals(behaviorSortColumn)) {
            comparator = java.util.Comparator.comparing(BehaviorDto::getName);
        } else if ("score".equals(behaviorSortColumn)) {
            comparator = java.util.Comparator.comparingInt(BehaviorDto::getScore);
        } else {
            comparator = java.util.Comparator.comparingInt(BehaviorDto::getBehaviorId);
        }

        if ("DESC".equals(behaviorSortDirection)) {
            comparator = comparator.reversed();
        }

        Collections.sort(behaviors, comparator);
    }

    private void setBehaviorAllMode() {
        behaviorSearchMode = BehaviorSearchMode.ALL;
        behaviorNameKeyword = "";
        behaviorMinScore = Integer.MIN_VALUE;
        behaviorMaxScore = Integer.MAX_VALUE;
    }

    private void setStudentAllMode() {
        studentSearchMode = BehaviorSearchMode.ALL;
        studentNameKeyword = "";
    }

    private void setStudentNameMode(String keyword) {
        studentSearchMode = BehaviorSearchMode.NAME;
        studentNameKeyword = keyword;
    }

    private void setInstructorAllMode() {
        instructorSearchMode = BehaviorSearchMode.ALL;
        instructorNameKeyword = "";
    }

    private void setInstructorNameMode(String keyword) {
        instructorSearchMode = BehaviorSearchMode.NAME;
        instructorNameKeyword = keyword;
    }

    private void setBehaviorNameMode(String keyword) {
        behaviorSearchMode = BehaviorSearchMode.NAME;
        behaviorNameKeyword = keyword;
        behaviorMinScore = Integer.MIN_VALUE;
        behaviorMaxScore = Integer.MAX_VALUE;
    }

    private void setBehaviorScoreMode(int minScore, int maxScore) {
        behaviorSearchMode = BehaviorSearchMode.SCORE;
        behaviorNameKeyword = "";
        behaviorMinScore = minScore;
        behaviorMaxScore = maxScore;
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
                if (title.equals("학생")) {
                    setStudentAllMode();
                    updateStudentTable(studentService.searchAll());
                }
                else if (title.equals("강사")) {
                    setInstructorAllMode();
                    updateInstructorTable(instructorService.searchAll());
                }
                else if (title.equals("행동")) {
                    setBehaviorAllMode();
                    updateBehaviorTable(behaviorService.searchAll());
                }
            } catch (Exception ex) {
                System.err.println("테이블 갱신 실패: " + ex.getMessage());
            }
        }
    }

    // --- Table Update Methods ---
    private void updateStudentTable(List<StudentDto> students) {
        updateStudentTable(students, true);
    }

    private void updateStudentTable(List<StudentDto> students, boolean resetSort) {
        if (resetSort) {
            behaviorSortColumn = null;
            behaviorSortDirection = null;
        }

        String[] columnNames = {"ID", "이름", "나이", "점수"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (StudentDto dto : students) {
            model.addRow(new Object[]{dto.getStudentId(), dto.getName(), dto.getAge(), dto.getScore()});
        }
        resultTable.setModel(model);
        currentResultType = "학생";
        setBehaviorHeaderSortingEnabled(true);
    }

    private void updateInstructorTable(List<InstructorDto> instructors) {
        updateInstructorTable(instructors, true);
    }

    private void updateInstructorTable(List<InstructorDto> instructors, boolean resetSort) {
        if (resetSort) {
            behaviorSortColumn = null;
            behaviorSortDirection = null;
        }

        String[] columnNames = {"ID", "이름", "나이"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (InstructorDto dto : instructors) {
            model.addRow(new Object[]{dto.getInstructorId(), dto.getName(), dto.getAge()});
        }
        resultTable.setModel(model);
        currentResultType = "강사";
        setBehaviorHeaderSortingEnabled(true);
    }

    private void updateBehaviorTable(List<BehaviorDto> behaviors) {
        updateBehaviorTable(behaviors, true);
    }

    private void updateBehaviorTable(List<BehaviorDto> behaviors, boolean resetSort) {
        if (resetSort) {
            behaviorSortColumn = null;
            behaviorSortDirection = null;
        }

        // 행동 테이블에서 '상점여부' 제거
        String[] columnNames = {"ID", "이름", "점수"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (BehaviorDto dto : behaviors) {
            model.addRow(new Object[]{dto.getBehaviorId(), dto.getName(), dto.getScore()});
        }
        resultTable.setModel(model);
        currentResultType = "행동";

        if (resultTable.getColumnModel().getColumnCount() > 1) {
            javax.swing.table.TableColumn nameColumn = resultTable.getColumnModel().getColumn(1);
            nameColumn.setPreferredWidth(200); // 이름 열 너비 증가
        }

        setBehaviorHeaderSortingEnabled(true);
    }

    private void updateEvaluationTable(List<EvaluationDetailDto> evaluations) {
        String[] columnNames = {"이력ID", "강사", "학생", "행동", "부여점수", "일시"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (EvaluationDetailDto dto : evaluations) {
            String dateStr = "";
            if (dto.getEvaluatedAt() != null) {
                dateStr = dto.getEvaluatedAt().format(formatter);
            }

            model.addRow(new Object[]{
                    dto.getEvaluationId(),
                    dto.getInstructorName(),
                    dto.getStudentName(),
                    dto.getBehaviorName(),
                    dto.getBehaviorScore(),
                    dateStr
            });
        }

        resultTable.setModel(model);
        currentResultType = "평가이력";
        setBehaviorHeaderSortingEnabled(false); // 평가 이력은 헤더 정렬 비활성화
    }

    private SortState getBehaviorSortState(int modelColumn) {
        String column = getSortableColumn(modelColumn);
        if (column == null) {
            return SortState.NOT_SORTABLE;
        }

        if (behaviorSortColumn == null) {
            return SortState.NONE;
        }

        if (!column.equals(behaviorSortColumn)) {
            return SortState.NONE;
        }

        return "DESC".equals(behaviorSortDirection) ? SortState.DESC : SortState.ASC;
    }

    private enum SortState {
        NOT_SORTABLE, NONE, ASC, DESC
    }

    private enum BehaviorSearchMode {
        ALL, NAME, SCORE
    }

    private class BehaviorHeaderRenderer extends JPanel implements TableCellRenderer {
        private final TableCellRenderer delegate;
        private final JLabel textLabel = new JLabel();
        private final SortIcon sortIcon = new SortIcon();

        BehaviorHeaderRenderer(TableCellRenderer delegate) {
            this.delegate = delegate;
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
            setOpaque(true);
            add(textLabel);
            add(new JLabel(sortIcon));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setFont(component.getFont());
            setForeground(component.getForeground());
            setBackground(component.getBackground());

            if (component instanceof JComponent) {
                setBorder(((JComponent) component).getBorder());
            }

            textLabel.setFont(component.getFont());
            textLabel.setForeground(component.getForeground());
            textLabel.setText(value == null ? "" : value.toString());
            sortIcon.setState(getBehaviorSortState(table.convertColumnIndexToModel(column)));

            return this;
        }
    }

    private static class SortIcon implements Icon {
        private static final int WIDTH = 9;
        private static final int HEIGHT = 15;
        private SortState state = SortState.NONE;

        void setState(SortState state) {
            this.state = state;
        }

        @Override
        public int getIconWidth() {
            return state == SortState.NOT_SORTABLE ? 0 : WIDTH;
        }

        @Override
        public int getIconHeight() {
            return state == SortState.NOT_SORTABLE ? 0 : HEIGHT;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (state == SortState.NOT_SORTABLE) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            Color active = new Color(60, 60, 60);
            Color inactive = new Color(170, 170, 170);

            g2.setColor(state == SortState.ASC ? active : inactive);
            int centerX = x + WIDTH / 2;
            int upTop = y + 2;
            g2.fillPolygon(
                    new int[]{centerX, x + 1, x + WIDTH - 2},
                    new int[]{upTop, upTop + 5, upTop + 5},
                    3
            );

            g2.setColor(state == SortState.DESC ? active : inactive);
            int downTop = y + 9;
            g2.fillPolygon(
                    new int[]{x + 1, x + WIDTH - 2, centerX},
                    new int[]{downTop, downTop, downTop + 5},
                    3
            );

            g2.dispose();
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

        gbcSearch.gridx = 0; gbcSearch.gridy = 0; gbcSearch.gridwidth = 2;
        JLabel hintLabel = new JLabel("빈 칸으로 검색하시면 전체 조회가 됩니다.");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(Color.GRAY);
        searchPanel.add(hintLabel, gbcSearch);

        gbcSearch.gridwidth = 1;
        gbcSearch.gridx = 0; gbcSearch.gridy = 1; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("이름:"), gbcSearch);
        JTextField nameField = new JTextField(10);
        gbcSearch.gridx = 1; gbcSearch.gridy = 1; gbcSearch.weightx = 0.7;
        searchPanel.add(nameField, gbcSearch);

        JButton btnSearch = new JButton("학생 조회");
        gbcSearch.gridx = 0; gbcSearch.gridy = 2; gbcSearch.gridwidth = 2;
        searchPanel.add(btnSearch, gbcSearch);

        btnSearch.addActionListener(e -> {
            try {
                String keyword = nameField.getText().trim();
                if (!keyword.isEmpty()) {
                    setStudentNameMode(keyword);
                    updateStudentTable(studentService.searchSimilarByName(keyword));
                } else {
                    setStudentAllMode();
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
                setStudentAllMode();
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

        gbcSearch.gridx = 0; gbcSearch.gridy = 0; gbcSearch.gridwidth = 2;
        JLabel hintLabel = new JLabel("빈 칸으로 검색하시면 전체 조회가 됩니다.");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(Color.GRAY);
        searchPanel.add(hintLabel, gbcSearch);

        gbcSearch.gridwidth = 1;
        gbcSearch.gridx = 0; gbcSearch.gridy = 1; gbcSearch.weightx = 0.3;
        searchPanel.add(new JLabel("이름:"), gbcSearch);
        JTextField nameField = new JTextField(10);
        gbcSearch.gridx = 1; gbcSearch.gridy = 1; gbcSearch.weightx = 0.7;
        searchPanel.add(nameField, gbcSearch);

        JButton btnSearch = new JButton("강사 조회");
        gbcSearch.gridx = 0; gbcSearch.gridy = 2; gbcSearch.gridwidth = 2;
        searchPanel.add(btnSearch, gbcSearch);

        btnSearch.addActionListener(e -> {
            try {
                String keyword = nameField.getText().trim();
                if (!keyword.isEmpty()) {
                    setInstructorNameMode(keyword);
                    updateInstructorTable(instructorService.searchSimilarByName(keyword));
                } else {
                    setInstructorAllMode();
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
                setInstructorAllMode();
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

        gbcName.gridx = 0; gbcName.gridy = 0; gbcName.gridwidth = 2;
        JLabel hintLabel1 = new JLabel("빈 칸으로 검색하시면 전체 조회가 됩니다.");
        hintLabel1.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel1.setForeground(Color.GRAY);
        namePanel.add(hintLabel1, gbcName);

        gbcName.gridwidth = 1;
        gbcName.gridx = 0; gbcName.gridy = 1; gbcName.weightx = 0.3;
        namePanel.add(new JLabel("이름:"), gbcName);
        JTextField nameField = new JTextField(10);
        gbcName.gridx = 1; gbcName.gridy = 1; gbcName.weightx = 0.7;
        namePanel.add(nameField, gbcName);
        JButton searchByNameBtn = new JButton("이름으로 조회");
        gbcName.gridx = 0; gbcName.gridy = 2; gbcName.gridwidth = 2;
        namePanel.add(searchByNameBtn, gbcName);
        behaviorSearchTabs.addTab("이름으로 조회", namePanel);

        searchByNameBtn.addActionListener(e -> {
            try {
                String keyword = nameField.getText().trim();
                if (keyword.isEmpty()) {
                    setBehaviorAllMode();
                    updateBehaviorTable(behaviorService.searchAll());
                } else {
                    setBehaviorNameMode(keyword);
                    updateBehaviorTable(behaviorService.searchSimilarByName(keyword));
                }
            } catch (CanNotFindException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "조회 실패", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 점수로 조회 탭
        JPanel scorePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.insets = new Insets(10, 5, 10, 5);
        gbcScore.fill = GridBagConstraints.HORIZONTAL;

        gbcScore.gridx = 0; gbcScore.gridy = 0; gbcScore.gridwidth = 4;
        JLabel hintLabel2 = new JLabel("입력으로 넣은 값 사이의 행동들이 출력됩니다.");
        hintLabel2.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel2.setForeground(Color.GRAY);
        scorePanel.add(hintLabel2, gbcScore);

        gbcScore.gridwidth = 1;
        gbcScore.gridx = 0; gbcScore.gridy = 1; gbcScore.weightx = 0.2;
        scorePanel.add(new JLabel("점수:"), gbcScore);
        JTextField minScoreField = new JTextField(3);
        gbcScore.gridx = 1; gbcScore.gridy = 1; gbcScore.weightx = 0.3;
        scorePanel.add(minScoreField, gbcScore);
        gbcScore.gridx = 2; gbcScore.gridy = 1; gbcScore.weightx = 0.1;
        scorePanel.add(new JLabel("~", SwingConstants.CENTER), gbcScore);
        JTextField maxScoreField = new JTextField(3);
        gbcScore.gridx = 3; gbcScore.gridy = 1; gbcScore.weightx = 0.3;
        scorePanel.add(maxScoreField, gbcScore);
        JButton searchByScoreBtn = new JButton("점수로 조회");
        gbcScore.gridx = 0; gbcScore.gridy = 2; gbcScore.gridwidth = 4;
        scorePanel.add(searchByScoreBtn, gbcScore);
        behaviorSearchTabs.addTab("점수로 조회", scorePanel);

        searchByScoreBtn.addActionListener(e -> {
            try {
                String minStr = minScoreField.getText();
                String maxStr = maxScoreField.getText();
                int min = minStr.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minStr);
                int max = maxStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxStr);
                setBehaviorScoreMode(min, max);
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
                setBehaviorAllMode();
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

        JButton loadHistoryBtn = new JButton("최근 이력 전체 조회");
        loadHistoryBtn.addActionListener(e -> {
            try {
                updateEvaluationTable(evaluationService.searchAll());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "이력 조회 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loadHistoryBtn, BorderLayout.NORTH);

        return panel;

    }
}