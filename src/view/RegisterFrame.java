package view;

import javax.swing.*;

/**
 * 이제 탭 내부에서 등록이 처리되므로 이 프레임은 사용되지 않습니다.
 * 나중에 다른 목적으로 사용할 수 있도록 남겨두거나 삭제할 수 있습니다.
 */
public class RegisterFrame extends JFrame {
    public RegisterFrame(String target) {
        setTitle("신규 등록");
        setSize(300, 200);
        add(new JLabel("이 창은 더 이상 사용되지 않습니다.", SwingConstants.CENTER));
        setLocationRelativeTo(null);
    }
}