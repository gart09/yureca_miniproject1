public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ui.MainFrame mainFrame = new ui.MainFrame();
            mainFrame.setVisible(true);
        });
    }
}