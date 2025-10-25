// Arkanoid/model/FastBallPowerUp.java
package model;

public class StickyPaddle extends PowerUp {
    private Ball gameBall; // Tham chiếu đến quả bóng chính của game
    private boolean stuck = false;  // bóng đang dính trên paddle
    private Paddle currentPaddle;   // Paddle hiện tại
    private double offSetX = 0; // Khoảng cách giữa ball và paddle khi dính

    public StickyPaddle(String imagePath, double x, double y, double width, double height, long duration, Ball ball) {
        super(imagePath, x, y, width, height, FALL_SPEED, duration);
        this.gameBall = ball;
    }

    // Setter để GameManager có thể thiết lập tham chiếu đến ball
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    @Override
    public void applyEffect(Paddle paddle) {
        System.out.println("StickyPaddle PowerUp activated!");
        paddle.setSticky(true);
        this.currentPaddle = paddle;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        System.out.println("ExpandPaddle PowerUp deactivated!");
    }

    // Xử lý bóng dính khi va chạm
    public void onBallHitPaddle(Ball ball, Paddle paddle) {
        if (paddle.isSticky() && !stuck) {
            stuck = true;

            // Tham chiếu đến ball và paddle hiện tại
            this.currentPaddle = paddle;
            this.gameBall = ball;

            gameBall.setActive(false);  // Ball ngừng di chuyển

            // Vị trí của bóng và Paddle
            this.offSetX = gameBall.getX() - paddle.getX();
            this.setOffSetX(offSetX);

            // Gắn bóng lên trên paddle, xử lý các trường hợp cạnh
            if (gameBall.x < currentPaddle.x) {
                currentPaddle.setLeftBoder(currentPaddle.getLeftBoder() + currentPaddle.x - gameBall.x);
            }

            if (gameBall.x + gameBall.width > currentPaddle.x + currentPaddle.width) {
                currentPaddle.setRightBoder(currentPaddle.getRightBoder()
                        - ((gameBall.x + gameBall.width) - (currentPaddle.x + currentPaddle.width)));
            }
        }
    }

    // Ném bóng ra
    public void releaseBall(Ball gameBall, Paddle paddle) {
        if(stuck && gameBall != null) {
            // Tham chiếu đến đến bóng và paddle hiện tại
            this.gameBall = gameBall;
            this.currentPaddle = paddle;

            // cho bóng di chuyển
            stuck = false;
            this.setStuck(stuck);
            gameBall.setActive(true);

            // Điều chỉnh hướng bóng
            double ballCenterX = gameBall.x + gameBall.width / 2;
            double paddleCenterX = currentPaddle.getX() + currentPaddle.getWidth() / 2.0;
            double relativeIntersect = (ballCenterX - paddleCenterX) / (currentPaddle.getWidth() / 2.0);

            // Giới hạn lại giá trị [-1, 1]
            relativeIntersect = Math.max(-1, Math.min(1, relativeIntersect));

            // Góc bật (0 = giữa, ±MAX_ANGLE = hai mép)
            double bounceAngle = relativeIntersect * Ball.MAX_ANGLE;

            // Tính lại hướng bóng
            gameBall.setDirectionX(Math.sin(bounceAngle));
            gameBall.setDirectionY(-Math.cos(bounceAngle));

            currentPaddle.setRightBoder(800);
            currentPaddle.setLeftBoder(0);
        }
    }

    public boolean isStuck() {
        return stuck;
    }

    public double getOffSetX() {
        return offSetX;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    public void setOffSetX(double offSetX) {
        this.offSetX = offSetX;
    }
}