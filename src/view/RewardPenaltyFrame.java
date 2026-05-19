package view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class RewardPenaltyFrame extends JFrame {

    private JList<String> instructorList;
    private JList<String> studentList;
    private JList<String> behaviorList;
    private JLabel previewLabel;

    public RewardPenaltyFrame() {
        setTitle("상벌점 부여");
        setSize(900, 500);
        setLayout(new BorderLayout());

        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Create lists with dummy data
        String[] instructors = {"1 김동근 (40)", "2 이몽룡 (35)", "3 성춘향 (28)"};
        String[] students = {"1 임진우 (20점)", "2 홍길동 (50점)", "3 아무개 (15점)"};
        String[] behaviors = {"1 인사잘하기 (+3)", "2 발표하기 (+10)", "3 쓰레기막버리기 (-5)"};

        instructorList = new JList<>(instructors);
        studentList = new JList<>(students);
        behaviorList = new JList<>(behaviors);

        // Add selection listeners
        ListSelectionListener selectionListener = e -> updatePreview();
        instructorList.addListSelectionListener(selectionListener);
        studentList.addListSelectionListener(selectionListener);
        behaviorList.addListSelectionListener(selectionListener);

        listsPanel.add(createListPanel("강사 리스트", "강사 이름 검색", instructorList));
        listsPanel.add(createListPanel("학생 리스트", "학생 이름 검색", studentList));
        listsPanel.add(createListPanel("행동 리스트", "행동 이름 검색", behaviorList));

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
    }

    private void updatePreview() {
        String studentVal = studentList.getSelectedValue();
        String behaviorVal = behaviorList.getSelectedValue();

        if (studentVal != null && behaviorVal != null) {
            // Very basic string parsing for dummy data display
            try {
                // Parse student: "1 임진우 (20점)"
                String[] sParts = studentVal.split(" ");
                String studentNo = sParts[0];
                String studentName = sParts[1];
                int currentScore = Integer.parseInt(sParts[2].replaceAll("[^0-9]", ""));

                // Parse behavior: "1 인사잘하기 (+3)" or "3 쓰레기막버리기 (-5)"
                int scoreChange = 0;
                if (behaviorVal.contains("(+")) {
                    String val = behaviorVal.substring(behaviorVal.indexOf("(+") + 2, behaviorVal.indexOf(")"));
                    scoreChange = Integer.parseInt(val);
                } else if (behaviorVal.contains("(-")) {
                    String val = behaviorVal.substring(behaviorVal.indexOf("(-") + 2, behaviorVal.indexOf(")"));
                    scoreChange = -Integer.parseInt(val);
                }

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

    private JPanel createListPanel(String title, String searchPlaceholder, JList<String> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        searchPanel.add(new JTextField(), BorderLayout.CENTER);

        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }
}