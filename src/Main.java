public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            view.MainFrame mainFrame = new view.MainFrame();
            mainFrame.setVisible(true);
        });
    }
}