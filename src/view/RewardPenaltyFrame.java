package view;

import model.dto.BehaviorDto;
import model.dto.InstructorDto;
import model.dto.StudentDto;
import model.dto.CanNotFindException;
import model.service.BehaviorService;
import model.service.EvaluationService;
import model.service.InstructorService;
import model.service.StudentService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class RewardPenaltyFrame extends JFrame {

    private JList<InstructorDto> instructorList;
    private JList<StudentDto> studentList;
    private JList<BehaviorDto> behaviorList;
    private JLabel previewLabel;
    
    private StudentService studentService;
    private InstructorService instructorService;
    private BehaviorService behaviorService;
    private EvaluationService evaluationService;

    public RewardPenaltyFrame(StudentService studentService, InstructorService instructorService, 
                              BehaviorService behaviorService, EvaluationService evaluationService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
        this.behaviorService = behaviorService;
        this.evaluationService = evaluationService;
        
        setTitle("상벌점 부여");
        setSize(900, 500);
        setLayout(new BorderLayout());

        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        instructorList = new JList<>();
        studentList = new JList<>();
        behaviorList = new JList<>();

        // Custom renderers
        instructorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof InstructorDto) {
                    InstructorDto dto = (InstructorDto) value;
                    setText(dto.getInstructorId() + " " + dto.getName() + " (" + dto.getAge() + "세)");
                }
                return c;
            }
        });

        studentList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof StudentDto) {
                    StudentDto dto = (StudentDto) value;
                    setText(dto.getStudentId() + " " + dto.getName() + " (" + dto.getScore() + "점)");
                }
                return c;
            }
        });

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

        listsPanel.add(createInstructorListPanel("강사 리스트", "강사 이름 검색"));
        listsPanel.add(createStudentListPanel("학생 리스트", "학생 이름 검색"));
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
        btnGrant.addActionListener(e -> handleGrant());

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.add(btnGrant);
        bottomPanel.add(buttonWrapper, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

        // Load initial data
        loadAllData();
    }

    private void loadAllData() {
        try {
            instructorList.setListData(new Vector<>(instructorService.searchAll()));
            studentList.setListData(new Vector<>(studentService.searchAll()));
            behaviorList.setListData(new Vector<>(behaviorService.searchAll()));
        } catch (Exception e) {
            System.err.println("초기 데이터를 불러오는데 실패했습니다: " + e.getMessage());
        }
    }

    private void updatePreview() {
        StudentDto student = studentList.getSelectedValue();
        BehaviorDto behavior = behaviorList.getSelectedValue();

        if (student != null && behavior != null) {
            int currentScore = student.getScore();
            int scoreChange = behavior.getScore();
            int newScore = currentScore + scoreChange;
            
            String previewText = String.format("예상 결과: 학생번호 %d %s | 이전 점수: %d -> 부여 후 점수: %d", 
                                    student.getStudentId(), student.getName(), currentScore, newScore);
            previewLabel.setText(previewText);
        } else {
            previewLabel.setText("학생과 행동을 선택하면 점수 변동이 표시됩니다.");
        }
    }

    private void handleGrant() {
        InstructorDto instructor = instructorList.getSelectedValue();
        StudentDto student = studentList.getSelectedValue();
        BehaviorDto behavior = behaviorList.getSelectedValue();

        if (instructor == null || student == null || behavior == null) {
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
            try {
                // 1. Evaluation 테이블에 이력 추가
                evaluationService.add(instructor.getInstructorId(), student.getStudentId(), behavior.getBehaviorId());

                // 2. Student 테이블 점수 업데이트 (이 로직은 EvaluationService.add 내부에서 처리될 수 있음)
                // 만약 서비스에서 처리하지 않는다면 아래 코드가 필요합니다.
                // int newStudentScore = student.getScore() + behavior.getScore();
                // student.setScore(newStudentScore);
                // studentService.update(student);

                JOptionPane.showMessageDialog(this, "상벌점 부여가 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "부여 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createInstructorListPanel(String title, String searchPlaceholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        instructorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(instructorList), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.isEmpty()) {
                    instructorList.setListData(new Vector<>(instructorService.searchAll()));
                } else {
                    try {
                        instructorList.setListData(new Vector<>(instructorService.searchSimilarByName(text)));
                    } catch (CanNotFindException ex) {
                        instructorList.setListData(new Vector<>());
                    }
                }
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStudentListPanel(String title, String searchPlaceholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(studentList), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel(searchPlaceholder + ": "), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.isEmpty()) {
                    studentList.setListData(new Vector<>(studentService.searchAll()));
                } else {
                    try {
                        studentList.setListData(new Vector<>(studentService.searchSimilarByName(text)));
                    } catch (CanNotFindException ex) {
                        studentList.setListData(new Vector<>());
                    }
                }
            }
        });
        
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
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.isEmpty()) {
                    behaviorList.setListData(new Vector<>(behaviorService.searchAll()));
                } else {
                    try {
                        behaviorList.setListData(new Vector<>(behaviorService.searchSimilarByName(text)));
                    } catch (CanNotFindException ex) {
                        behaviorList.setListData(new Vector<>());
                    }
                }
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);
        return panel;
    }
}