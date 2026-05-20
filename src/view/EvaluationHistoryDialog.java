package view;

import model.dto.EvaluationDetailDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EvaluationHistoryDialog extends JDialog {

    public EvaluationHistoryDialog(JFrame parent, String targetName, List<EvaluationDetailDto> history, boolean isStudent) {
        super(parent, targetName + "의 상벌점 기록", true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // 상단 정보 패널
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel(targetName + "의 " + (isStudent ? "상벌점 기록" : "부여 기록"));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        infoPanel.add(titleLabel);

        int totalPositive = 0;
        int totalNegative = 0;
        for (EvaluationDetailDto dto : history) {
            if (dto.getBehaviorScore() > 0) {
                totalPositive += dto.getBehaviorScore();
            } else {
                totalNegative += dto.getBehaviorScore();
            }
        }
        
        JLabel summaryLabel = new JLabel("총 상점: " + totalPositive + "점 | 총 벌점: " + totalNegative + "점");
        infoPanel.add(summaryLabel);
        
        add(infoPanel, BorderLayout.NORTH);

        // 중앙 테이블
        String[] columnNames;
        if (isStudent) {
            columnNames = new String[]{"강사", "행동", "부여 점수", "일시"};
        } else {
            columnNames = new String[]{"학생", "행동", "부여 점수", "일시"};
        }
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (EvaluationDetailDto dto : history) {
            String dateStr = dto.getEvaluatedAt() != null ? dto.getEvaluatedAt().format(formatter) : "";
            if (isStudent) {
                model.addRow(new Object[]{dto.getInstructorName(), dto.getBehaviorName(), dto.getBehaviorScore(), dateStr});
            } else {
                model.addRow(new Object[]{dto.getStudentName(), dto.getBehaviorName(), dto.getBehaviorScore(), dateStr});
            }
        }
        
        JTable historyTable = new JTable(model);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        setLocationRelativeTo(parent);
    }
}