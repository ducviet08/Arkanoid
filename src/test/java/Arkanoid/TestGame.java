package Arkanoid;

import Arkanoid.model.ball.Ball;
import Arkanoid.model.paddle.Paddle;
import Arkanoid.model.brick.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {

    private Ball ball;
    private Paddle paddle;

    // KHỞI TẠO JAVAFX TRƯỚC KHI CHẠY TẤT CẢ TEST
    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // Start JavaFX toolkit
        Platform.startup(() -> {
            latch.countDown();
        });

        latch.await();
    }

    @BeforeEach
    public void setup() {
        // Test trực tiếp Ball, Paddle, Brick - không cần GameManager
        ball = new Ball("/images/ball1.png", 395, 530, 1, -1);
        paddle = new Paddle("/images/paddle2.png", 350, 550);
    }

    // ========== BALL TESTS ==========

    @Test
    public void testInitialBallPosition() {
        assertEquals(395, ball.getX(), 0.1);
        assertEquals(530, ball.getY(), 0.1);
    }

    @Test
    public void testBallNotActiveInitially() {
        assertFalse(ball.isActive());
    }

    @Test
    public void testBallDirectionInitial() {
        assertEquals(1, ball.getDirectionX(), 0.1);
        assertEquals(-1, ball.getDirectionY(), 0.1);
    }

    @Test
    public void testBallSetPosition() {
        ball.setX(200);
        ball.setY(300);
        assertEquals(200, ball.getX(), 0.1);
        assertEquals(300, ball.getY(), 0.1);
    }

    @Test
    public void testBallSetActive() {
        ball.setActive(true);
        assertTrue(ball.isActive());

        ball.setActive(false);
        assertFalse(ball.isActive());
    }

    @Test
    public void testBallSetDirection() {
        ball.setDirectionX(2);
        ball.setDirectionY(-2);
        assertEquals(2, ball.getDirectionX(), 0.1);
        assertEquals(-2, ball.getDirectionY(), 0.1);
    }

    // ========== PADDLE TESTS ==========

    @Test
    public void testInitialPaddlePosition() {
        assertEquals(350, paddle.getX(), 0.1);
        assertEquals(550, paddle.getY(), 0.1);
    }

    @Test
    public void testPaddleMoveLeft() {
        double initialX = paddle.getX();
        paddle.moveLeft();
        paddle.update();
        assertTrue(paddle.getX() < initialX, "Paddle should move left");
    }

    @Test
    public void testPaddleMoveRight() {
        double initialX = paddle.getX();
        paddle.moveRight();
        paddle.update();
        assertTrue(paddle.getX() > initialX, "Paddle should move right");
    }

    @Test
    public void testPaddleStop() {
        paddle.moveRight();
        paddle.stop();
        double x1 = paddle.getX();
        paddle.update();
        double x2 = paddle.getX();
        assertEquals(x1, x2, 0.1, "Paddle should not move after stop");
    }

    @Test
    public void testPaddleSetPosition() {
        paddle.setX(200);
        assertEquals(200, paddle.getX(), 0.1);
    }

    // ========== COLLISION TESTS ==========

    @Test
    public void testBallPaddleCollision() {
        ball.setX(paddle.getX() + 35);
        ball.setY(paddle.getY() - ball.getHeight() + 0.01);

        assertTrue(ball.checkCollision(paddle), "Ball should collide with paddle");
    }

    @Test
    public void testBallPaddleNoCollision() {
        ball.setX(100);
        ball.setY(100);

        assertFalse(ball.checkCollision(paddle), "Ball should not collide when far away");
    }

    @Test
    public void testBallBallCollision() {
        Ball newBall = new Ball("/images/ball2.png", 100, 100, 1, -1);

        ball.setY(86);
        ball.setX(100);

        assertTrue(ball.checkCollision(newBall), "Ball should collide with other ball");
    }

    @Test
    public void testBallBrickCollision() {
        NormalBrick brick = new NormalBrick("/images/brick1.png", 100, 100, 80, 25);
        ball.setX(brick.getX() + 30);
        ball.setY(brick.getY() - ball.getHeight() + 0.01);

        assertTrue(ball.checkCollision(brick), "Ball should collide with brick");
    }

    // ========== BRICK TESTS ==========

    @Test
    public void testNormalBrickNotDestroyedInitially() {
        NormalBrick brick = new NormalBrick("/images/brick1.png", 50, 50, 80, 25);
        assertFalse(brick.isDestroyed());
    }

    @Test
    public void testNormalBrickDestroyedAfterHit() {
        NormalBrick brick = new NormalBrick("/images/brick1.png", 50, 50, 80, 25);
        brick.takeHit();
        assertTrue(brick.isDestroyed(), "Normal brick should be destroyed after 1 hit");
    }

    @Test
    public void testStrongBrickMultipleHits() {
        StrongBrick brick = new StrongBrick("/images/brick10.png", 50, 50, 80, 25);

        assertFalse(brick.isDestroyed());

        brick.takeHit();
        assertFalse(brick.isDestroyed(), "Strong brick should not break after 2 hit");

        brick.takeHit();
        assertFalse(brick.isDestroyed(), "Strong brick should not break after 2 hits");

        brick.takeHit();
        assertTrue(brick.isDestroyed(), "Strong brick should break after 3 hits");
    }

    @Test
    public void testGlassBrickOneHit() {
        GlassBrick brick = new GlassBrick("/images/brick7.png", 50, 50, 80, 25);
        brick.takeHit();
        assertTrue(brick.getHealth() == 1, "Glass brick should break after 2 hit");
    }

    @Test
    public void testBrickPosition() {
        NormalBrick brick = new NormalBrick("/images/brick1.png", 100, 200, 80, 25);
        assertEquals(100, brick.getX(), 0.1);
        assertEquals(200, brick.getY(), 0.1);
        assertEquals(80, brick.getWidth(), 0.1);
        assertEquals(25, brick.getHeight(), 0.1);
    }

    @Test
    public void testBrickTakeDestroy() {
        NormalBrick brick1 = new NormalBrick("/images/brick1.png", 50, 50, 80, 25);
        StrongBrick brick2 = new StrongBrick("/images/brick1.png", 50, 50, 80, 25);
        brick1.takeDestroy();
        brick2.takeDestroy();
        assertTrue(brick1.isDestroyed(), "Brick should be destroyed immediately");
        assertTrue(brick2.isDestroyed(), "Brick should be destroyed immediately");
    }

    // ========== STEEL TESTS ==========

    @Test
    public void testSteelPosition() {
        Steel steel = new Steel("/images/steel.png", 100, 150, 80, 25);
        assertEquals(100, steel.getX(), 0.1);
        assertEquals(150, steel.getY(), 0.1);
    }

    @Test
    public void testBallSteelCollision() {
        Steel steel = new Steel("/images/steel.png", 100, 100, 80, 25);
        ball.setX(steel.getX() + 30);
        ball.setY(steel.getY() + ball.getHeight());

        assertTrue(ball.checkCollision(steel), "Ball should collide with steel");
    }

    // ========== BALL PROPERTIES TESTS ==========

    @Test
    public void testBallHasSize() {
        assertTrue(ball.getWidth() > 0, "Ball width should be positive");
        assertTrue(ball.getHeight() > 0, "Ball height should be positive");
    }

    @Test
    public void testPaddleHasSize() {
        assertTrue(paddle.getWidth() > 0, "Paddle width should be positive");
        assertTrue(paddle.getHeight() > 0, "Paddle height should be positive");
    }

    // ========== MOVEMENT TESTS ==========

    @Test
    public void testBallMovement() {
        ball.setActive(true);
        ball.setX(200);
        ball.setY(200);
        ball.setDirectionX(1);
        ball.setDirectionY(1);

        double initialX = ball.getX();
        double initialY = ball.getY();

        // Ball should move when active
        assertNotNull(ball);
    }

    @Test
    public void testPaddleMovementSpeed() {
        paddle.setX(300);
        paddle.moveRight();
        paddle.update();
        double x1 = paddle.getX();

        paddle.update();
        double x2 = paddle.getX();

        assertTrue(x2 > x1, "Paddle should continue moving right");
    }
}