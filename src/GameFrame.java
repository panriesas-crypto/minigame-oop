package com.applehunter;
import javax.swing.JFrame;
public class GameFrame extends JFrame {
    public GameFrame() {
        super("Apple Hunter: Snake Tower");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
    }
}
