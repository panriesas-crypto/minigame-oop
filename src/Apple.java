
import java.awt.*;

public class Apple extends GameObject implements Updatable {

    private boolean collected = false;
    private int respawnFrames = 0;

    private static final int WIDTH = 18;
    private static final int HEIGHT = 18;

    public Apple(int x, int y) {
        super(x, y);
    }

    public boolean isCollected() {
        return collected;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void collect() {
        if (!collected) {
            collected = true;

            respawnFrames = 3600;
        }
    }

    @Override
    public void update() {
        if (collected) {
            respawnFrames--;

            if (respawnFrames <= 0) {
                collected = false;
                respawnFrames = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {

        if (collected) {
            return;
        }

        g.setColor(new Color(238, 75, 70));
        g.fillOval(x, y, WIDTH, HEIGHT);

        g.setColor(new Color(90, 190, 80));
        g.fillOval(x + 9, y - 5, 9, 5);
    }
}
