
import java.awt.*;

public class GameOverlayMenu {

    public enum Type {
        PAUSE,
        GAME_OVER
    }

    private Type type = Type.PAUSE;
    private int selectedIndex = 0;

    private static final String[] PAUSE_OPTIONS = {
        "Resume",
        "Restart",
        "Main Menu"
    };

    private static final String[] GAME_OVER_OPTIONS = {
        "Try Again",
        "Main Menu"
    };

    private String[] getOptions() {
        return type == Type.PAUSE
                ? PAUSE_OPTIONS
                : GAME_OVER_OPTIONS;
    }

    public void show(Type newType) {
        if (type != newType) {
            type = newType;
            selectedIndex = 0;
        }
    }

    public void resetSelection() {
        selectedIndex = 0;
    }

    public void moveSelection(int direction) {
        selectedIndex = Math.floorMod(
                selectedIndex + direction,
                getOptions().length
        );
    }

    // ส่งกลับหมายเลขปุ่มที่เลือก
    public int getSelectedIndex() {
        return selectedIndex;
    }

    // เมื่อเลื่อนเมาส์ผ่านปุ่ม
    public void handleMouseMove(int x, int y) {
        String[] options = getOptions();

        for (int i = 0; i < options.length; i++) {
            if (getButtonBounds(i).contains(x, y)) {
                selectedIndex = i;
                return;
            }
        }
    }

    // คืนค่าปุ่มที่คลิก หรือ -1 ถ้าไม่ได้คลิกปุ่ม
    public int handleMouseClick(int x, int y) {
        String[] options = getOptions();

        for (int i = 0; i < options.length; i++) {
            if (getButtonBounds(i).contains(x, y)) {
                selectedIndex = i;
                return i;
            }
        }

        return -1;
    }

    private Rectangle getButtonBounds(int index) {
        int count = getOptions().length;
        int startY = count == 3 ? 235 : 275;

        return new Rectangle(
                170,
                startY + index * 70,
                260,
                52
        );
    }

    private void drawCentered(
            Graphics2D g,
            String text,
            int y) {

        FontMetrics metrics = g.getFontMetrics();
        int x = (600 - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    public void draw(Graphics2D g, GameManager manager) {

        // พื้นหลังโปร่งแสง
        g.setColor(new Color(0, 0, 0, 205));
        g.fillRect(0, 0, 600, 540);

        if (type == Type.PAUSE) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 42));
            drawCentered(g, "PAUSED", 140);
        } else {
            g.setColor(new Color(255, 100, 100));
            g.setFont(new Font("Arial", Font.BOLD, 42));
            drawCentered(g, "GAME OVER", 135);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 19));

            drawCentered(
                    g,
                    "Score: " + manager.getScore(),
                    185
            );

            drawCentered(
                    g,
                    "Floor: " +
                    manager.getPlayer().getCurrentFloor(),
                    215
            );
        }

        String[] options = getOptions();

        for (int i = 0; i < options.length; i++) {
            Rectangle button = getButtonBounds(i);

            if (i == selectedIndex) {
                g.setColor(new Color(75, 115, 170));
            } else {
                g.setColor(new Color(40, 55, 75));
            }

            g.fillRoundRect(
                    button.x,
                    button.y,
                    button.width,
                    button.height,
                    14,
                    14
            );

            g.setColor(
                    i == selectedIndex
                            ? Color.YELLOW
                            : Color.WHITE
            );

            g.setFont(new Font("Arial", Font.BOLD, 21));

            drawCentered(
                    g,
                    options[i],
                    button.y + 34
            );
        }

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 15));

        drawCentered(
                g,
                "UP / DOWN: Select    ENTER: Confirm",
                500
        );
    }
}
