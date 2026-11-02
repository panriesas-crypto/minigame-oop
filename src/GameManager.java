
import java.util.*;

public class GameManager {

    private final Player player = new Player(35, 451);

    private final List<Floor> floors = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private boolean jumpRequested;

    private int climbingTargetFloor = -1;

    public GameManager() {
        for (int n = 1; n <= 4; n++) {
            floors.add(new Floor(n, 490 - (n - 1) * 120));
        }

        for (int n = 1; n < 4; n++) {
            ladders.add(new Ladder(
                    n % 2 == 1 ? 480 : 90,
                    490 - n * 120,
                    120));
        }
    }

    public Player getPlayer() {
        return player;
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

        // หยุดเกมเมื่อ HP หมด
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

        // อัปเดตงูทุกชั้น
        for (Floor floor : floors) {
            floor.getSnake().update();
        }

        checkSnakeCollisions();
    }

    private void checkSnakeCollisions() {

        for (Floor floor : floors) {

            Snake snake = floor.getSnake();

            // ใช้ Bounding Box ตรวจสอบการชน
            if (player.getBounds().intersects(
                    snake.getBounds())) {

                player.takeDamage(20);
            }
        }
    }

    private void checkStartClimbing() {

        if (!player.isOnGround()) {
            return;
        }

        int currentFloor = player.getCurrentFloor();

        if (upPressed && currentFloor < floors.size()) {

            Ladder ladder = ladders.get(currentFloor - 1);

            if (ladder.isPlayerNear(player)) {
                player.startClimbing();
                climbingTargetFloor = currentFloor + 1;
            }
        }

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
