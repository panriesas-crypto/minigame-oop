import java.awt.*;

public class MainMenu {

    public enum Page {
        MAIN,
        HOW_TO_PLAY,
        EXIT_CONFIRM
    }

    private final String[] options = {
            "Start Game",
            "How to Play",
            "Exit Game"
    };

    private int selectedIndex = 0;
    private Page page = Page.MAIN;

    // ใช้เลือก Yes / No ในหน้า Exit
    private boolean exitConfirmed = false;

    public Page getPage() {
        return page;
    }

    public void moveSelection(int direction) {

        if (page == Page.MAIN) {
            selectedIndex = Math.floorMod(
                    selectedIndex + direction,
                    options.length);
        }

        else if (page == Page.EXIT_CONFIRM) {
            exitConfirmed = !exitConfirmed;
        }
    }

    public void select(GameManager manager) {

        if (page == Page.EXIT_CONFIRM) {

            if (exitConfirmed) {
                Window window = KeyboardFocusManager
                        .getCurrentKeyboardFocusManager()
                        .getActiveWindow();

                if (window != null) {
                    window.dispose();
                }
            } else {
                page = Page.MAIN;
            }

            return;
        }

        if (page != Page.MAIN) {
            return;
        }

        switch (selectedIndex) {

            case 0:
                manager.startGame();
                break;

            case 1:
                page = Page.HOW_TO_PLAY;
                break;

            case 2:
                page = Page.EXIT_CONFIRM;
                exitConfirmed = false;
                break;
        }
    }

    public void back() {
        page = Page.MAIN;
        exitConfirmed = false;
    }

    public void draw(Graphics2D g) {

        g.setColor(new Color(12, 20, 35, 245));
        g.fillRect(0, 0, 600, 540);

        switch (page) {

            case MAIN:
                drawMainMenu(g);
                break;

            case HOW_TO_PLAY:
                drawHowToPlay(g);
                break;

            case EXIT_CONFIRM:
                drawExitConfirm(g);
                break;
        }
    }

    private void drawCentered(
            Graphics2D g,
            String text,
            int y) {

        FontMetrics metrics = g.getFontMetrics();
        int x = (600 - metrics.stringWidth(text)) / 2;

        g.drawString(text, x, y);
    }

    private void drawMainMenu(Graphics2D g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 34));
        drawCentered(g, "APPLE HUNTER", 110);

        g.setFont(new Font("Arial", Font.BOLD, 23));
        drawCentered(g, "SNAKE TOWER", 145);

        for (int i = 0; i < options.length; i++) {

            int y = 210 + i * 75;

            if (i == selectedIndex) {
                g.setColor(new Color(75, 115, 170));
            } else {
                g.setColor(new Color(40, 55, 75));
            }

            g.fillRoundRect(
                    165, y, 270, 52, 14, 14);

            g.setColor(
                    i == selectedIndex
                            ? Color.YELLOW
                            : Color.WHITE);

            g.setFont(
                    new Font("Arial", Font.BOLD, 21));

            drawCentered(g, options[i], y + 34);
        }

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 15));

        drawCentered(
                g,
                "UP / DOWN: Select    ENTER: Confirm",
                495);
    }

    public void handleMouseMove(int mouseX, int mouseY) {

        if (page == Page.MAIN) {
            for (int i = 0; i < options.length; i++) {

                Rectangle button = new Rectangle(
                        165, 210 + i * 75, 270, 52);

                if (button.contains(mouseX, mouseY)) {
                    selectedIndex = i;
                    break;
                }
            }
        }

        else if (page == Page.EXIT_CONFIRM) {

            Rectangle yesButton = new Rectangle(125, 275, 150, 55);

            Rectangle noButton = new Rectangle(325, 275, 150, 55);

            if (yesButton.contains(mouseX, mouseY)) {
                exitConfirmed = true;
            }

            else if (noButton.contains(mouseX, mouseY)) {
                exitConfirmed = false;
            }
        }
    }

    public void handleMouseClick(
            int mouseX,
            int mouseY,
            GameManager manager) {

        if (page == Page.MAIN) {

            for (int i = 0; i < options.length; i++) {

                Rectangle button = new Rectangle(
                        165, 210 + i * 75, 270, 52);

                if (button.contains(mouseX, mouseY)) {
                    selectedIndex = i;
                    select(manager);
                    return;
                }
            }
        }

        else if (page == Page.HOW_TO_PLAY) {

            // พื้นที่ข้อความ Press ESC to return
            Rectangle backButton = new Rectangle(170, 460, 260, 45);

            if (backButton.contains(mouseX, mouseY)) {
                back();
            }
        }

        else if (page == Page.EXIT_CONFIRM) {

            Rectangle yesButton = new Rectangle(125, 275, 150, 55);

            Rectangle noButton = new Rectangle(325, 275, 150, 55);

            if (yesButton.contains(mouseX, mouseY)) {
                exitConfirmed = true;
                select(manager);
            }

            else if (noButton.contains(mouseX, mouseY)) {
                exitConfirmed = false;
                select(manager);
            }
        }
    }

    private void drawHowToPlay(Graphics2D g) {

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        drawCentered(g, "HOW TO PLAY", 70);

        g.setColor(new Color(40, 55, 75));
        g.fillRoundRect(45, 95, 510, 360, 18, 18);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("OBJECTIVE", 70, 130);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Climb the tower as high as possible.", 70, 160);
        g.drawString("Collect apples to increase your score.", 70, 185);
        g.drawString("Avoid snakes and the rising lava.", 70, 210);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("CONTROLS", 70, 250);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        g.drawString("A / D", 75, 280);
        g.drawString("Move Left / Right", 230, 280);

        g.drawString("W / S", 75, 310);
        g.drawString("Climb Ladder", 230, 310);

        g.drawString("SPACE", 75, 340);
        g.drawString("Jump", 230, 340);

        g.drawString("ESC", 75, 370);
        g.drawString("Pause / Resume", 230, 370);

        g.drawString("R", 75, 400);
        g.drawString("Restart Game", 230, 400);

        g.setColor(new Color(75, 115, 170));
        g.fillRoundRect(170, 460, 260, 45, 14, 14);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        drawCentered(g, "BACK (ESC)", 489);

    }

    private void drawExitConfirm(Graphics2D g) {

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        drawCentered(g, "EXIT GAME?", 165);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 19));
        drawCentered(
                g,
                "Are you sure you want to quit?",
                215);

        // YES
        g.setColor(
                exitConfirmed
                        ? new Color(180, 65, 65)
                        : new Color(50, 65, 85));
        g.fillRoundRect(125, 275, 150, 55, 14, 14);

        // NO
        g.setColor(
                !exitConfirmed
                        ? new Color(75, 115, 170)
                        : new Color(50, 65, 85));
        g.fillRoundRect(325, 275, 150, 55, 14, 14);

        g.setFont(new Font("Arial", Font.BOLD, 22));

        g.setColor(
                exitConfirmed ? Color.YELLOW : Color.WHITE);
        g.drawString("YES", 178, 310);

        g.setColor(
                !exitConfirmed ? Color.YELLOW : Color.WHITE);
        g.drawString("NO", 380, 310);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        drawCentered(
                g,
                "ARROWS: Select    ENTER: Confirm",
                405);

        drawCentered(
                g,
                "ESC: Cancel",
                440);
    }
}
