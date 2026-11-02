
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class GamePanel extends JPanel {

    private final GameManager manager = new GameManager();
    private final GameLoop gameLoop = new GameLoop(manager, this);

    private final MainMenu mainMenu = new MainMenu();
    private final GameOverlayMenu overlayMenu =
            new GameOverlayMenu();

    public GamePanel() {
        setPreferredSize(new Dimension(600, 540));
        setBackground(new Color(23, 40, 60));
        setFocusable(true);

        setupControls();
        setupMouseControls();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameLoop.start();
    }

    @Override
    public void removeNotify() {
        gameLoop.stop();
        super.removeNotify();
    }

    // ตรวจสอบว่าอยู่หน้า Pause หรือ Game Over
    private boolean isOverlayActive() {
        return manager.isGameStarted()
                && (manager.isPaused() || manager.isGameOver());
    }

    // เลือกชนิดเมนูให้ตรงกับสถานะเกม
    private void updateOverlayType() {
        if (manager.isGameOver()) {
            overlayMenu.show(
                    GameOverlayMenu.Type.GAME_OVER
            );
        } else if (manager.isPaused()) {
            overlayMenu.show(
                    GameOverlayMenu.Type.PAUSE
            );
        }
    }

    // ทำงานตามปุ่มที่เลือก
    private void selectOverlayOption(int index) {

        if (manager.isGameOver()) {

            switch (index) {
                case 0:
                    // Try Again
                    manager.restart();
                    break;

                case 1:
                    // Main Menu
                    manager.returnToMenu();
                    mainMenu.back();
                    break;
            }

        } else if (manager.isPaused()) {

            switch (index) {
                case 0:
                    // Resume
                    manager.togglePause();
                    break;

                case 1:
                    // Restart
                    manager.restart();
                    break;

                case 2:
                    // Main Menu
                    manager.returnToMenu();
                    mainMenu.back();
                    break;
            }
        }

        overlayMenu.resetSelection();
        repaint();
    }

    private void setupControls() {

        // เดินซ้าย
        bindKey("pressed A", "leftDown",
                () -> manager.setLeftPressed(true));

        bindKey("released A", "leftUp",
                () -> manager.setLeftPressed(false));

        // เดินขวา
        bindKey("pressed D", "rightDown",
                () -> manager.setRightPressed(true));

        bindKey("released D", "rightUp",
                () -> manager.setRightPressed(false));

        // ปีนขึ้น
        bindKey("pressed W", "upDown",
                () -> manager.setUpPressed(true));

        bindKey("released W", "upUp",
                () -> manager.setUpPressed(false));

        // ปีนลง
        bindKey("pressed S", "downDown",
                () -> manager.setDownPressed(true));

        bindKey("released S", "downUp",
                () -> manager.setDownPressed(false));

        // กระโดด
        bindKey("pressed SPACE", "jump",
                () -> manager.requestJump());

        // R เริ่มเกมใหม่
        bindKey("pressed R", "restart", () -> {
            if (manager.isGameStarted()) {
                manager.restart();
                overlayMenu.resetSelection();
                repaint();
            }
        });

        // Enter
        bindKey("pressed ENTER", "select", () -> {

            if (!manager.isGameStarted()) {
                mainMenu.select(manager);
            } else if (isOverlayActive()) {
                updateOverlayType();
                selectOverlayOption(
                        overlayMenu.getSelectedIndex()
                );
            }

            repaint();
        });

        // Escape
        bindKey("pressed ESCAPE", "escape", () -> {

            if (!manager.isGameStarted()) {
                mainMenu.back();

            } else if (manager.isGameOver()) {
                // Game Over ใช้เมนูเลือกการกระทำ

            } else {
                manager.togglePause();
                overlayMenu.resetSelection();
            }

            repaint();
        });

        // ลูกศรขึ้น
        bindKey("pressed UP", "menuUp", () -> {

            if (!manager.isGameStarted()) {
                mainMenu.moveSelection(-1);

            } else if (isOverlayActive()) {
                updateOverlayType();
                overlayMenu.moveSelection(-1);
            }

            repaint();
        });

        // ลูกศรลง
        bindKey("pressed DOWN", "menuDown", () -> {

            if (!manager.isGameStarted()) {
                mainMenu.moveSelection(1);

            } else if (isOverlayActive()) {
                updateOverlayType();
                overlayMenu.moveSelection(1);
            }

            repaint();
        });
    }

    private void bindKey(
            String keyStroke,
            String actionName,
            Runnable action) {

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(
                        KeyStroke.getKeyStroke(keyStroke),
                        actionName
                );

        getActionMap().put(
                actionName,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }

    private void setupMouseControls() {

        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                if (!manager.isGameStarted()) {

                    mainMenu.handleMouseMove(
                            e.getX(),
                            e.getY()
                    );

                } else if (isOverlayActive()) {

                    updateOverlayType();

                    overlayMenu.handleMouseMove(
                            e.getX(),
                            e.getY()
                    );
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                if (!manager.isGameStarted()) {

                    mainMenu.handleMouseClick(
                            e.getX(),
                            e.getY(),
                            manager
                    );

                } else if (isOverlayActive()) {

                    updateOverlayType();

                    int clickedIndex =
                            overlayMenu.handleMouseClick(
                                    e.getX(),
                                    e.getY()
                            );

                    if (clickedIndex != -1) {
                        selectOverlayOption(clickedIndex);
                    }
                }

                repaint();
            }
        };

        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // ===================================
        // วาดโลกเกม
        // ===================================

        Graphics2D world = (Graphics2D) g.create();

        world.translate(0, manager.getCameraY());

        for (Floor floor : manager.getFloors()) {

            world.setColor(new Color(168, 124, 77));

            world.fillRect(
                    0,
                    floor.getY(),
                    600,
                    9
            );

            world.setColor(Color.WHITE);

            world.drawString(
                    "Floor " + floor.getNumber(),
                    12,
                    floor.getY() - 35
            );

            floor.getSnake().draw(world);
            floor.getApple().draw(world);
        }

        for (Ladder ladder : manager.getLadders()) {
            ladder.draw(world);
        }

        manager.getPlayer().draw(world);

        // ===================================
        // Lava
        // ===================================

        int lavaY = manager.getLavaY();

        world.setColor(new Color(235, 75, 30));

        world.fillRect(
                0,
                lavaY,
                600,
                100000
        );

        world.setColor(new Color(255, 180, 50));
        world.fillRect(0, lavaY, 600, 7);

        world.dispose();

        // ===================================
        // HUD
        // ===================================

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString(
                "HP: " + manager.getPlayer().getHp()
                        + "    Score: " + manager.getScore()
                        + "    Floor: "
                        + manager.getPlayer().getCurrentFloor(),
                320,
                22
        );

        g.setFont(new Font("Arial", Font.PLAIN, 13));

        g.drawString(
                "A/D: Move   W/S: Climb   SPACE: Jump   R: Restart",
                12,
                525
        );

        // ===================================
        // Main Menu
        // ===================================

        if (!manager.isGameStarted()) {

            mainMenu.draw(g);

        } else if (manager.isGameOver()) {

            // Game Over Menu
            updateOverlayType();
            overlayMenu.draw(g, manager);

        } else if (manager.isPaused()) {

            // Pause Menu
            updateOverlayType();
            overlayMenu.draw(g, manager);
        }

        g.dispose();
    }
}
