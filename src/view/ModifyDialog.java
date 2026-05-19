package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ModifyDialog extends JDialog {
    public ModifyDialog(JFrame parent, String type, JTable resultTable, int selectedRow) {
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
            // 학생, 강사의 경우 나이 정보를 가져옴 (임시로 2번째 컬럼이라 가정, 실제 데이터 구조에 맞게 수정 필요)
            ageField = new JTextField(model.getColumnCount() > 2 ? model.getValueAt(selectedRow, 2).toString() : "");
            gbc.gridx = 1;
            formPanel.add(ageField, gbc);
        }

        if (type.equals("학생") || type.equals("행동")) {
            int gridy = type.equals("학생") ? 3 : 2;
            gbc.gridx = 0; gbc.gridy = gridy;
            formPanel.add(new JLabel("점수:"), gbc);
            // 행동의 경우 점수 정보 가져옴
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

        btnConfirm.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "해당 내용으로 변경하시겠습니까?",
                    "수정 확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // To-do: 실제 DB 업데이트 로직 추가
                JOptionPane.showMessageDialog(this, "수정되었습니다.");
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }
}