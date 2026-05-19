package view;

import model.dto.BehaviorDto;
import model.dto.InstructorDto;
import model.dto.StudentDto;
import model.service.BehaviorService;
import model.service.InstructorService;
import model.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ModifyDialog extends JDialog {
    private boolean isModified = false;

    public ModifyDialog(JFrame parent, String type, JTable resultTable, int selectedRow, 
                        StudentService studentService, InstructorService instructorService, BehaviorService behaviorService) {
        super(parent, type + " 수정", true);
        setSize(300, 250);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        
        // ID 필드 (공통, 수정 불가)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        JTextField idField = new JTextField(model.getValueAt(selectedRow, 0).toString());
        idField.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        // 이름 필드 (공통)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("이름:"), gbc);
        JTextField nameField = new JTextField(model.getValueAt(selectedRow, 1).toString());
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        JTextField ageField = null;
        JTextField scoreField = null;

        // 타입별 추가 필드
        if (type.equals("학생") || type.equals("강사")) {
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("나이:"), gbc);
            ageField = new JTextField(model.getColumnCount() > 2 ? model.getValueAt(selectedRow, 2).toString() : "");
            gbc.gridx = 1;
            formPanel.add(ageField, gbc);
        }

        if (type.equals("학생") || type.equals("행동")) {
            int gridy = type.equals("학생") ? 3 : 2;
            gbc.gridx = 0; gbc.gridy = gridy;
            formPanel.add(new JLabel("점수:"), gbc);
            int scoreColIdx = type.equals("학생") ? 3 : 2;
            scoreField = new JTextField(model.getColumnCount() > scoreColIdx ? model.getValueAt(selectedRow, scoreColIdx).toString() : "");
            gbc.gridx = 1;
            formPanel.add(scoreField, gbc);
        }

        add(formPanel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton btnConfirm = new JButton("확인");
        JButton btnCancel = new JButton("취소");

        final JTextField finalAgeField = ageField;
        final JTextField finalScoreField = scoreField;

        btnConfirm.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "해당 내용으로 변경하시겠습니까?",
                    "수정 확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();

                    if (type.equals("학생") && studentService != null) {
                        int age = Integer.parseInt(finalAgeField.getText());
                        int score = Integer.parseInt(finalScoreField.getText());
                        studentService.update(new StudentDto(id, name, age, score));
                        isModified = true;
                    } else if (type.equals("강사") && instructorService != null) {
                        int age = Integer.parseInt(finalAgeField.getText());
                        instructorService.update(new InstructorDto(id, name, age));
                        isModified = true;
                    } else if (type.equals("행동") && behaviorService != null) {
                        int score = Integer.parseInt(finalScoreField.getText());
                        behaviorService.update(new BehaviorDto(id, name, score));
                        isModified = true;
                    }
                    
                    if (isModified) {
                        JOptionPane.showMessageDialog(this, "수정되었습니다.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "서비스가 초기화되지 않았습니다.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "점수나 나이는 올바른 숫자 형식이어야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "수정 중 오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    public boolean isModified() {
        return isModified;
    }
}