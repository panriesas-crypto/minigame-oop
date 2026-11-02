
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

        bindKey("pressed R", "restart",
                () -> manager.restart());

        bindKey("pressed ENTER", "startGame",
                () -> manager.startGame());

        bindKey("pressed ESCAPE", "pauseGame",
                () -> manager.togglePause());

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

        // วาดโลกของเกมโดยเลื่อนตามกล้อง
        Graphics2D world = (Graphics2D) g.create();
        world.translate(0, manager.getCameraY());

        for (Floor floor : manager.getFloors()) {

            g.setColor(Color.WHITE);

            world.setColor(new Color(168, 124, 77));
            world.fillRect(0, floor.getY(), 600, 9);

            world.setColor(Color.WHITE);
            world.drawString(
                    "Floor " + floor.getNumber(),
                    12,
                    floor.getY() - 35);

            floor.getSnake().draw(world);
            floor.getApple().draw(world);
        }

        for (Ladder ladder : manager.getLadders()) {
            ladder.draw(world);
        }

        manager.getPlayer().draw(world);

        int lavaY = manager.getLavaY();

        world.setColor(new Color(235, 75, 30));
        world.fillRect(
                0,
                lavaY,
                600,
                100000);

        world.setColor(new Color(255, 180, 50));
        world.fillRect(0, lavaY, 600, 7);

        world.dispose();

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString(
                "HP: " + manager.getPlayer().getHp()
                        + "    Score: " + manager.getScore()
                        + "    Floor: " + manager.getPlayer().getCurrentFloor(),
                320,
                22);

        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString(
                "A/D: Move   W/S: Climb   SPACE: Jump   R: Restart",
                12,
                525);

        if (manager.isGameOver()) {

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 600, 540);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 42));
            g.drawString("GAME OVER", 155, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));

            g.drawString(
                    "Score: " + manager.getScore(),
                    255,
                    290);

            g.drawString(
                    "Floor: " + manager.getPlayer().getCurrentFloor(),
                    255,
                    320);

            g.drawString("Press R to Restart", 220, 350);
        }

        // หน้าจอ Start Menu
        if (!manager.isGameStarted()) {

            g.setColor(new Color(0, 0, 0, 210));
            g.fillRect(0, 0, 600, 540);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("APPLE HUNTER", 165, 170);

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("SNAKE TOWER", 190, 205);

            g.setFont(new Font("Arial", Font.PLAIN, 17));
            g.drawString("A / D  -  Move", 230, 270);
            g.drawString("W / S  -  Climb", 230, 300);
            g.drawString("SPACE  -  Jump", 230, 330);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press ENTER to Start", 185, 405);
        }

        // หน้าจอ Pause
        if (manager.isPaused()) {

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 600, 540);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 42));
            g.drawString("PAUSED", 205, 245);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press ESC to Continue", 205, 285);
        }

        g.dispose();
    }
}
