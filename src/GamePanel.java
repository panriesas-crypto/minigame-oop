
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class GamePanel extends JPanel {

    private final GameManager manager = new GameManager();

    public GamePanel() {
        setPreferredSize(new Dimension(600, 540));
        setBackground(new Color(23, 40, 60));
        setFocusable(true);

        setupControls();

        Timer gameTimer = new Timer(16, e -> {
            manager.update();
            repaint();
        });

        gameTimer.start();
    }

    private void setupControls() {
        bindKey("pressed A", "leftDown",
                () -> manager.setLeftPressed(true));
        bindKey("released A", "leftUp",
                () -> manager.setLeftPressed(false));

        bindKey("pressed D", "rightDown",
                () -> manager.setRightPressed(true));
        bindKey("released D", "rightUp",
                () -> manager.setRightPressed(false));

        bindKey("pressed W", "upDown",
                () -> manager.setUpPressed(true));
        bindKey("released W", "upUp",
                () -> manager.setUpPressed(false));

        bindKey("pressed S", "downDown",
                () -> manager.setDownPressed(true));
        bindKey("released S", "downUp",
                () -> manager.setDownPressed(false));

        bindKey("pressed SPACE", "jump",
                () -> manager.requestJump());
    }

    private void bindKey(
            String keyStroke,
            String actionName,
            Runnable action) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyStroke), actionName);

        getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (Floor floor : manager.getFloors()) {
            g.setColor(new Color(168, 124, 77));
            g.fillRect(0, floor.getY(), 600, 9);

            g.setColor(Color.WHITE);
            g.drawString(
                    "Floor " + floor.getNumber(),
                    12,
                    floor.getY() - 35);

            floor.getSnake().draw(g);
            floor.getApple().draw(g);
        }

        for (Ladder ladder : manager.getLadders()) {
            ladder.draw(g);
        }

        manager.getPlayer().draw(g);

        g.setColor(Color.WHITE);

        g.drawString(
                "HP: " + manager.getPlayer().getHp()
                        + "     Score: 0",
                420,
                22);

        if (manager.isGameOver()) {

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 600, 540);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 42));

            g.drawString("GAME OVER", 155, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));

            g.drawString("HP: 0", 270, 290);
        }

        g.drawString(
                "A/D: Move   W/S: Climb   SPACE: Jump",
                12,
                525);

        g.dispose();
    }
}
