package view;

import model.dto.BehaviorDto;
import model.dto.CanNotFindException;
import model.service.BehaviorService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class RewardPenaltyFrame extends JFrame {

    private JList<String> instructorList;
    private JList<String> studentList;
    private JList<BehaviorDto> behaviorList;
    private JLabel previewLabel;
    
    private BehaviorService behaviorService;

    public RewardPenaltyFrame(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
        
        setTitle("상벌점 부여");
        setSize(900, 500);
        setLayout(new BorderLayout());

        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Create lists with dummy data for Student and Instructor
        String[] instructors = {"1 김동근 (40)", "2 이몽룡 (35)", "3 성춘향 (28)"};
        String[] students = {"1 임진우 (20점)", "2 홍길동 (50점)", "3 아무개 (15점)"};

        instructorList = new JList<>(instructors);
        studentList = new JList<>(students);
        behaviorList = new JList<>();

        // Custom renderer to display BehaviorDto properly in JList
        behaviorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BehaviorDto) {
                    BehaviorDto dto = (BehaviorDto) value;
                    String sign = dto.getScore() > 0 ? "+" : "";
                    setText(dto.getBehaviorId() + " " + dto.getName() + " (" + sign + dto.getScore() + ")");
                }
                return c;
            }
        });

        // Add selection listeners
        ListSelectionListener selectionListener = e -> updatePreview();
        instructorList.addListSelectionListener(selectionListener);
        studentList.addListSelectionListener(selectionListener);
        behaviorList.addListSelectionListener(selectionListener);

        listsPanel.add(createListPanel("강사 리스트", "강사 이름 검색", instructorList, null));
        listsPanel.add(createListPanel("학생 리스트", "학생 이름 검색", studentList, null));
        listsPanel.add(createBehaviorListPanel("행동 리스트", "행동 이름 검색"));

        add(listsPanel, BorderLayout.CENTER);

        // Bottom Panel (Preview + Grant Button)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        previewLabel = new JLabel("선택 대기중...");
        previewLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(previewLabel, BorderLayout.CENTER);

        JButton btnGrant = new JButton("부여");
        btnGrant.addActionListener(e -> {
            if (instructorList.getSelectedValue() == null ||
                studentList.getSelectedValue() == null ||
                behaviorList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(this, "강사, 학생, 행동을 모두 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "정말로 부여하시겠습니까?",
                    "부여 확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "상벌점 부여가 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.add(btnGrant);
        bottomPanel.add(buttonWrapper, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

        // Load initial behaviors
        loadAllBehaviors();
    }

    private void loadAllBehaviors() {
        try {
            List<BehaviorDto> allBehaviors = behaviorService.searchAll();
            updateBehaviorList(allBehaviors);
        } catch (Exception e) {
            System.err.println("초기 행동 리스트를 불러오는데 실패했습니다: " + e.getMessage());
        }
    }

    private void updateBehaviorList(List<BehaviorDto> behaviors) {
        Vector<BehaviorDto> vector = new Vector<>(behaviors);
        behaviorList.setListData(vector);
    }

    private void updatePreview() {
        String studentVal = studentList.getSelectedValue();
        BehaviorDto behaviorDto = behaviorList.getSelectedValue();

        if (studentVal != null && behaviorDto != null) {
            try {
                // Parse student dummy data: "1 임진우 (20점)"
                String[] sParts = studentVal.split(" ");
                String studentNo = sParts[0];
                String studentName = sParts[1];
                int currentScore = Integer.parseInt(sParts[2].replaceAll("[^0-9]", ""));

                int scoreChange = behaviorDto.getScore();
                int newScore = currentScore + scoreChange;
                
                String previewText = String.format("예상 결과: 학생번호 %s %s | 이전 점수: %d -> 부여 후 점수: %d", 
                                        studentNo, studentName, currentScore, newScore);
                previewLabel.setText(previewText);
                
            } catch (Exception ex) {
                previewLabel.setText("점수 변동을 계산할 수 없습니다.");
            }
        } else {
            previewLabel.setText("학생과 행동을 선택하면 점수 변동이 표시됩니다.");
        }
    }

    private JPanel createListPanel(String title, String searchPlaceholder, JList<String> list, DocumentListener searchListener) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        if (searchListener != null) {
            searchField.getDocument().addDocumentListener(searchListener);
        }
        searchPanel.add(searchField, BorderLayout.CENTER);

        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBehaviorListPanel(String title, String searchPlaceholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        behaviorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(behaviorList), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterBehaviors(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterBehaviors(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterBehaviors(); }

            private void filterBehaviors() {
                String text = searchField.getText();
                if (text.isEmpty()) {
                    loadAllBehaviors();
                } else {
                    try {
                        List<BehaviorDto> filtered = behaviorService.searchSimilarByName(text);
                        updateBehaviorList(filtered);
                    } catch (CanNotFindException ex) {
                        behaviorList.setListData(new Vector<>()); // Empty list if not found
                    }
                }
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);

        return panel;
    }
}