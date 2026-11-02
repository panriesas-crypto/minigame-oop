import java.awt.*;

public class Ladder extends GameObject {

    private final int height;

    public Ladder(int x, int y, int height) {
        super(x, y);
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPlayerNear(Player player) {
        int playerCenter = player.getX() + player.getWidth() / 2;
        int ladderCenter = x + 14;

        return Math.abs(playerCenter - ladderCenter) <= 22;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(196, 138, 70));

        g.fillRect(x, y, 5, height);
        g.fillRect(x + 24, y, 5, height);

        for (int yy = y + 7; yy < y + height; yy += 13) {
            g.fillRect(x + 4, yy, 21, 4);
        }
    }
}
