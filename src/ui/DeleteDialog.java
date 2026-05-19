package ui;

import javax.swing.*;
import java.awt.*;

public class DeleteDialog extends JDialog {
    public DeleteDialog(JFrame parent) {
        super(parent, "삭제 확인", true);
        setSize(300, 150);
        setLayout(new BorderLayout());

        JLabel message = new JLabel("선택한 정보를 정말로 삭제하시겠습니까?", SwingConstants.CENTER);
        add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnYes = new JButton("Yes");
        JButton btnNo = new JButton("No");

        btnYes.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "삭제되었습니다.");
            dispose();
        });

        btnNo.addActionListener(e -> dispose());

        buttonPanel.add(btnYes);
        buttonPanel.add(btnNo);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }
}