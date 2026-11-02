import javax.swing.SwingUtilities;

public class GameLoop implements Runnable {

    private final GameManager manager;
    private final GamePanel panel;

    private volatile boolean running = false;
    private Thread gameThread;

    public GameLoop(GameManager manager, GamePanel panel) {
        this.manager = manager;
        this.panel = panel;
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;

        gameThread = new Thread(this, "Game-Loop-Thread");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void stop() {
        running = false;

        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void run() {
        final long frameTime = 16_000_000L;
        long nextFrame = System.nanoTime();

        while (running) {

            long now = System.nanoTime();

            if (now >= nextFrame) {

                SwingUtilities.invokeLater(() -> {
                    if (running && panel.isDisplayable()) {
                        manager.update();
                        panel.repaint();
                    }
                });

                nextFrame += frameTime;

                if (nextFrame < now) {
                    nextFrame = now + frameTime;
                }
            }

            long remaining = nextFrame - System.nanoTime();

            if (remaining > 0) {
                try {
                    Thread.sleep(
                        Math.max(1, remaining / 1_000_000L)
                    );
                } catch (InterruptedException e) {
                    if (!running) {
                        break;
                    }
                }
            }
        }
    }
}
