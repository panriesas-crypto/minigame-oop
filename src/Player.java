
import java.awt.*;

public class Player extends GameObject {

    private int hp = 100;

    private double velocityY = 0;
    private final double gravity = 0.6;
    private final double jumpPower = -11;

    private boolean onGround = true;
    private boolean climbing = false;

    private int currentFloor = 1;

    private int invincibleFrames = 0;

    private static final int WIDTH = 24;
    private static final int HEIGHT = 39;

    public Player(int x, int y) {
        super(x, y);
    }

    public int getHp() {
        return hp;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void takeDamage(int damage) {
        if (invincibleFrames > 0 || !isAlive()) {
            return;
        }

        hp = Math.max(0, hp - damage);

        // ประมาณ 1 วินาทีที่ 60 FPS
        invincibleFrames = 60;
    }

    public void updateInvincibility() {
        if (invincibleFrames > 0) {
            invincibleFrames--;
        }
    }

    public void move(int direction) {
        x += direction * 4;

        if (x < 0) {
            x = 0;
        }

        if (x > 600 - WIDTH) {
            x = 600 - WIDTH;
        }
    }

    public void jump() {
        if (onGround && !climbing) {
            velocityY = jumpPower;
            onGround = false;
        }
    }

    public void updateGravity(int floorY) {
        if (climbing) {
            return;
        }

        if (!onGround) {
            velocityY += gravity;
            y += (int) Math.round(velocityY);

            int groundY = floorY - HEIGHT;

            if (y >= groundY) {
                y = groundY;
                velocityY = 0;
                onGround = true;
            }
        }
    }

    public void startClimbing(int ladderX) {
        climbing = true;
        onGround = false;
        velocityY = 0;

        // จัดผู้เล่นให้อยู่ตรงกลางบันได
        x = ladderX + 14 - WIDTH / 2;
    }

    public void climb(int direction) {
        if (climbing) {
            y += direction * 3;
        }
    }

    public void stopClimbing(int floorNumber, int floorY) {
        climbing = false;
        currentFloor = floorNumber;
        y = floorY - HEIGHT;
        velocityY = 0;
        onGround = true;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void draw(Graphics2D g) {

        // กระพริบเมื่อได้รับความเสียหาย
        if (invincibleFrames > 0 &&
                invincibleFrames % 10 < 5) {
            return;
        }

        g.setColor(new Color(255, 207, 165));
        g.fillOval(x + 5, y, 14, 14);

        g.setColor(new Color(65, 155, 240));
        g.fillRect(x + 4, y + 14, 16, 17);

        g.setColor(new Color(80, 110, 190));
        g.fillRect(x + 4, y + 31, 6, 8);
        g.fillRect(x + 14, y + 31, 6, 8);
    }
}
