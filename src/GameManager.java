
import java.util.*;

public class GameManager {

    private Player player;

    private final List<Floor> floors = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();

    private int score = 0;
    private int cameraY = 0;

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean jumpRequested;

    private int climbingTargetFloor = -1;
    private int climbingStartFloor = -1;

    public GameManager() {
        restart();
        climbingStartFloor = -1;
    }

    public void restart() {
        player = new Player(35, 451);

        floors.clear();
        ladders.clear();

        score = 0;
        cameraY = 0;
        climbingTargetFloor = -1;

        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        jumpRequested = false;

        // เริ่มต้นด้วย 4 ชั้น
        for (int n = 1; n <= 4; n++) {
            addFloor(n);
        }
    }

    private void addFloor(int number) {
        int floorY = 490 - (number - 1) * 120;

        floors.add(new Floor(number, floorY));

        if (number > 1) {
            int lowerFloor = number - 1;

            ladders.add(new Ladder(
                    lowerFloor % 2 == 1 ? 480 : 90,
                    floorY,
                    120));
        }
    }

    private void generateMoreFloors() {
        // เตรียมชั้นใหม่ไว้เหนือผู้เล่นเสมอ
        while (floors.size() < player.getCurrentFloor() + 4) {
            addFloor(floors.size() + 1);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public int getCameraY() {
        return cameraY;
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

    private void updateCamera() {

        // ต้องการให้ Player อยู่บริเวณ y = 240 ของหน้าจอ
        int targetCameraY = Math.max(0, 240 - player.getY());

        // กล้องเลื่อนขึ้นตาม Player และไม่เลื่อนกลับลง
        cameraY = Math.max(cameraY, targetCameraY);
    }

    public void update() {

        if (isGameOver()) {
            return;
        }

        player.updateInvincibility();

        if (!player.isClimbing()) {
            if (leftPressed && !rightPressed) {
                player.move(-1);
            }

            if (rightPressed && !leftPressed) {
                player.move(1);
            }
        }

        if (jumpRequested) {
            player.jump();
            jumpRequested = false;
        }

        if (!player.isClimbing()) {
            checkStartClimbing();
        } else {
            updateClimbing();
        }

        player.updateGravity(
                getFloorY(player.getCurrentFloor()));

        generateMoreFloors();
        updateCamera();

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
                score += 10;
            }
        }
    }

    private void checkStartClimbing() {

        if (!player.isOnGround()) {
            return;
        }

        int currentFloor = player.getCurrentFloor();

        // เริ่มปีนขึ้น
        if (upPressed) {
            generateMoreFloors();

            Ladder ladder = ladders.get(currentFloor - 1);

            if (ladder.isPlayerNear(player)) {
                climbingStartFloor = currentFloor;
                climbingTargetFloor = currentFloor + 1;

                player.startClimbing(ladder.getX());
                return;
            }
        }

        // เริ่มปีนลง
        if (downPressed && currentFloor > 1) {
            Ladder ladder = ladders.get(currentFloor - 2);

            if (ladder.isPlayerNear(player)) {
                climbingStartFloor = currentFloor - 1;
                climbingTargetFloor = currentFloor;

                player.startClimbing(ladder.getX());
            }
        }
    }

    private void updateClimbing() {

        int lowerFloorY = getFloorY(climbingStartFloor);
        int upperFloorY = getFloorY(climbingTargetFloor);

        int lowerPlayerY = lowerFloorY - player.getHeight();
        int upperPlayerY = upperFloorY - player.getHeight();

        // กด W เพื่อปีนขึ้น
        if (upPressed && !downPressed) {
            player.climb(-1);
        }

        // กด S เพื่อปีนลง
        else if (downPressed && !upPressed) {
            player.climb(1);
        }

        // ถึงชั้นบน
        if (player.getY() <= upperPlayerY) {

            player.stopClimbing(
                    climbingTargetFloor,
                    upperFloorY);

            climbingStartFloor = -1;
            climbingTargetFloor = -1;
        }

        // ถึงชั้นล่าง
        else if (player.getY() >= lowerPlayerY) {

            player.stopClimbing(
                    climbingStartFloor,
                    lowerFloorY);

            climbingStartFloor = -1;
            climbingTargetFloor = -1;
        }
    }

}
