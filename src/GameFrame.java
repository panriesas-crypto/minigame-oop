import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame() {
        super("Snakes and Ladders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
    }
}
