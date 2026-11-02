import java.util.*;

public class GameManager {

    private Player player;

    private final List<Floor> floors = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();

    private int score = 0;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private boolean jumpRequested;

    private int climbingTargetFloor = -1;

    public GameManager() {
        restart();
    }

    public void restart() {

        player = new Player(35, 451);

        floors.clear();
        ladders.clear();

        score = 0;
        climbingTargetFloor = -1;

        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        jumpRequested = false;

        for (int n = 1; n <= 4; n++) {
            floors.add(
                    new Floor(n, 490 - (n - 1) * 120));
        }

        for (int n = 1; n < 4; n++) {
            ladders.add(
                    new Ladder(
                            n % 2 == 1 ? 480 : 90,
                            490 - n * 120,
                            120));
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public List<Floor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public List<Ladder> getLadders() {
        return Collections.unmodifiableList(ladders);
    }

    public boolean isGameOver() {
        return !player.isAlive();
    }

    public void setLeftPressed(boolean value) {
        leftPressed = value;
    }

    public void setRightPressed(boolean value) {
        rightPressed = value;
    }

    public void setUpPressed(boolean value) {
        upPressed = value;
    }

    public void setDownPressed(boolean value) {
        downPressed = value;
    }

    public void requestJump() {
        jumpRequested = true;
    }

    private int getFloorY(int number) {
        return floors.get(number - 1).getY();
    }

    public void update() {

        if (isGameOver()) {
            return;
        }

        player.updateInvincibility();

        // เดินซ้ายขวา
        if (!player.isClimbing()) {

            if (leftPressed && !rightPressed) {
                player.move(-1);
            }

            if (rightPressed && !leftPressed) {
                player.move(1);
            }
        }

        // กระโดด
        if (jumpRequested) {
            player.jump();
            jumpRequested = false;
        }

        // ปีนบันได
        if (!player.isClimbing()) {
            checkStartClimbing();
        } else {
            updateClimbing();
        }

        player.updateGravity(
                getFloorY(player.getCurrentFloor()));

        // อัปเดตงูและแอปเปิลทุกชั้น
        for (Floor floor : floors) {
            floor.getSnake().update();
            floor.getApple().update();
        }

        checkSnakeCollisions();
        checkAppleCollisions();
    }

    private void checkSnakeCollisions() {

        for (Floor floor : floors) {

            Snake snake = floor.getSnake();

            if (player.getBounds().intersects(
                    snake.getBounds())) {

                player.takeDamage(20);
            }
        }
    }

    private void checkAppleCollisions() {

        for (Floor floor : floors) {

            Apple apple = floor.getApple();

            if (!apple.isCollected()
                    && player.getBounds().intersects(
                            apple.getBounds())) {

                apple.collect();

                // เก็บแอปเปิลได้ 10 คะแนน
                score += 10;
            }
        }
    }

    private void checkStartClimbing() {

        if (!player.isOnGround()) {
            return;
        }

        int currentFloor = player.getCurrentFloor();

        // ปีนขึ้น
        if (upPressed && currentFloor < floors.size()) {

            Ladder ladder = ladders.get(currentFloor - 1);

            if (ladder.isPlayerNear(player)) {
                player.startClimbing();
                climbingTargetFloor = currentFloor + 1;
            }
        }

        // ปีนลง
        if (downPressed && currentFloor > 1) {

            Ladder ladder = ladders.get(currentFloor - 2);

            if (ladder.isPlayerNear(player)) {
                player.startClimbing();
                climbingTargetFloor = currentFloor - 1;
            }
        }
    }

    private void updateClimbing() {

        int targetY = getFloorY(climbingTargetFloor)
                - player.getHeight();

        if (climbingTargetFloor > player.getCurrentFloor()) {

            if (upPressed) {
                player.climb(-1);
            }

            if (player.getY() <= targetY) {
                player.stopClimbing(
                        climbingTargetFloor,
                        getFloorY(climbingTargetFloor));
                climbingTargetFloor = -1;
            }

        } else {

            if (downPressed) {
                player.climb(1);
            }

            if (player.getY() >= targetY) {
                player.stopClimbing(
                        climbingTargetFloor,
                        getFloorY(climbingTargetFloor));
                climbingTargetFloor = -1;
            }
        }
    }
}
