
import java.awt.*;

public class Snake extends GameObject {

    private final int floorNumber;
    private final double speed;

    private double positionX;
    private int direction = 1;

    private static final int WIDTH = 86;
    private static final int HEIGHT = 15;

    public Snake(int x, int y, int floorNumber) {
        super(x, y);

        this.positionX = x;
        this.floorNumber = floorNumber;

        // ชั้นสูง งูจะเคลื่อนที่เร็วขึ้น
        this.speed = Math.min(6.0, 1.0 + (floorNumber - 1) * 0.5);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public double getSpeed() {
        return speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        positionX += speed * direction;

        // ชนขอบซ้าย
        if (positionX < 0) {
            positionX = 0;
            direction = 1;
        }

        // ชนขอบขวา
        if (positionX > 600 - WIDTH) {
            positionX = 600 - WIDTH;
            direction = -1;
        }

        x = (int) Math.round(positionX);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(69, 165, 92));

        for (int i = 0; i < 5; i++) {
            g.fillRoundRect(
                x + i * 17,
                y,
                18,
                HEIGHT,
                7,
                7
            );
        }

        // วาดตาไว้ด้านที่งูกำลังเดิน
        g.setColor(Color.BLACK);

        if (direction == 1) {
            g.fillOval(x + 76, y + 3, 3, 3);
        } else {
            g.fillOval(x + 7, y + 3, 3, 3);
        }
    }
}
