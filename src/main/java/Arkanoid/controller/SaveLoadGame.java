// Arkanoid/controller/SaveLoadGame.java
package Arkanoid.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.String;

import Arkanoid.model.ball.Ball;
import Arkanoid.model.powerup.FastBallPowerUp;
import Arkanoid.model.powerup.FireBallPowerUp;
import Arkanoid.model.brick.*;
import Arkanoid.model.paddle.Paddle;
import Arkanoid.model.powerup.StickyPaddlePowerUp;
import Arkanoid.model.powerup.ExpandPaddlePowerUp;
import Arkanoid.model.powerup.ExtraLifePowerUp;
import Arkanoid.model.powerup.PowerUp;
import Arkanoid.model.powerup.ShrinkPaddlePowerUp;

import static Arkanoid.Main.*;

import static Arkanoid.Main.*;

public class SaveLoadGame {
    public static final String FILE_PATH = "src/main/resources/data/save.txt";

    public static void saveGame(GameManager gameManager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            writer.write("level " + currentLevel);
            writer.newLine();

            writer.write("paddle " + paddleImage + " " + gameManager.getPaddle().getX());
            writer.newLine();

            // --- THAY ĐỔI LOGIC LƯU BÓNG ---
            writer.write("balls"); // Mục mới
            writer.newLine();
            List<Ball> balls = gameManager.getBalls();
            for (Ball ball : balls) {
                writer.write(ballImage + " "
                        + ball.getX() + " "
                        + ball.getY() + " "
                        + ball.getDirectionX() + " "
                        + ball.getDirectionY() + " "
                        + ball.isActive()); // Lưu trạng thái active
                writer.newLine();
            }
            // --- KẾT THÚC THAY ĐỔI ---

            writer.write("score " + gameManager.getScore());
            writer.newLine();

            writer.write("lives " + gameManager.getLives());
            writer.newLine();

            // (Các dòng power-up giữ nguyên)
            writer.write("lastBallPowerUpTime " + (System.currentTimeMillis() - gameManager.getBallLastPowerUpTime()));
            writer.newLine();
            String activeBallPowerUpType = "null";
            if (gameManager.getBallActivePowerUp() != null) {
                activeBallPowerUpType = gameManager.getBallActivePowerUp().getClass().getSimpleName();
            }
            writer.write("activeBallPowerUp " + activeBallPowerUpType);
            writer.newLine();

            writer.write("lastPaddlePowerUpTime " + (System.currentTimeMillis() - gameManager.getPaddleLastPowerUpTime()));
            writer.newLine();
            String activePaddlePowerUpType = "null";
            if (gameManager.getPaddleActivePowerUp() != null) {
                activePaddlePowerUpType = gameManager.getPaddleActivePowerUp().getClass().getSimpleName();
            }
            writer.write("activePaddlePowerUp " + activePaddlePowerUpType);
            writer.newLine();

            writer.write("lastMixPowerUpTime " + (System.currentTimeMillis() - gameManager.getLastMixPowerUpTime()));
            writer.newLine();
            String activeMixPowerUpType = "null";
            if (gameManager.getMixActivePowerUp() != null) {
                activeMixPowerUpType = gameManager.getMixActivePowerUp().getClass().getSimpleName();
            }
            writer.write("activeMixPowerUp " + activeMixPowerUpType);
            writer.newLine();

            // (Các mục bricks, steels, powerUps giữ nguyên)
            writer.write("bricks");
            writer.newLine();
            List<Brick> saveBricks = gameManager.getBricks();
            for (Brick brick : saveBricks) {
                if (brick instanceof NormalBrick) {
                    writer.write("NormalBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                } else if (brick instanceof StrongBrick) {
                    writer.write("StrongBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                } else if (brick instanceof ExplosiveBrick) {
                    writer.write("ExplosiveBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                } else if (brick instanceof GlassBrick) {
                    writer.write("GlassBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                }
                writer.newLine();
            }

            writer.write("steels");
            writer.newLine();
            List<Steel> steels = gameManager.getSteels();
            for (Steel steel : steels) {
                writer.write(steel.getPath() + " "
                        + steel.getX() + " " + steel.getY());
                writer.newLine();
            }

            writer.write("powerUps");
            writer.newLine();
            List<PowerUp> powerUps = gameManager.getPowerUps();
            for (PowerUp powerUp : powerUps) {
                String type = powerUp.getClass().getSimpleName();
                writer.write(type + " " + powerUp.getX() + " " + powerUp.getY());
                writer.newLine();
            }

            // writer.close(); // Tự động đóng nhờ try-with-resources
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.out.println("Can't write in the save.txt file!");
            e.printStackTrace();
        }
    }

    public static void loadGame(GameManager gameManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Khai báo các list
            List<Ball> loadedBalls = new ArrayList<>(); // DANH SÁCH BÓNG MỚI
            List<Brick> bricks = new ArrayList<>();
            List<Steel> steels = new ArrayList<>();
            List<PowerUp> powerUps = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 0) continue;
                String key = parts[0];

                switch (key) {
                    case "level":
                        currentLevel = Integer.parseInt(parts[1]);
                        break;
                    case "paddle":
                        Paddle paddle = new Paddle(parts[1], Double.parseDouble(parts[2]), 550);
                        gameManager.setPaddle(paddle);
                        break;

                    // --- THAY ĐỔI LOGIC TẢI BÓNG ---
                    case "balls":
                        while ((line = reader.readLine()) != null && !line.startsWith("score")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length < 6) continue; // Cần 6 tham số (path, x, y, dx, dy, active)

                            String path = parts2[0];
                            double x = Double.parseDouble(parts2[1]);
                            double y = Double.parseDouble(parts2[2]);
                            double dirX = Double.parseDouble(parts2[3]);
                            double dirY = Double.parseDouble(parts2[4]);
                            boolean isActive = Boolean.parseBoolean(parts2[5]);

                            Ball ball = new Ball(path, x, y, dirX, dirY);
                            ball.setActive(isActive);
                            loadedBalls.add(ball);
                        }
                        gameManager.setBalls(loadedBalls); // Đặt danh sách bóng

                        // Xử lý dòng "score" đã bị đọc lố
                        if (line != null) parts = line.split("\\s+");
                        else continue;
                        key = parts[0];
                        // Dùng if-check thay vì fall-through
                        if (key.equals("score")) {
                            gameManager.setScore(Integer.parseInt(parts[1]));
                        }
                        break; // Quan trọng

                    // XÓA 'case "ball":' CŨ
                    // --- KẾT THÚC THAY ĐỔI ---

                    case "score":
                        // Case này chỉ chạy nếu file save không có mục 'balls' (file cũ)
                        gameManager.setScore(Integer.parseInt(parts[1]));
                        break;
                    case "lives":
                        gameManager.setLives(Integer.parseInt(parts[1]));
                        break;
                    case "lastBallPowerUpTime":
                        gameManager.setBallLastPowerUpTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activeBallPowerUp":
                        // Dòng này được gọi SAU KHI 'balls' đã được tải
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    case "lastPaddlePowerUpTime":
                        gameManager.setPaddleLastPowerUpTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activePaddlePowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    case "lastMixPowerUpTime":
                        gameManager.setMixLastPowerTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activeMixPowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;

                    case "bricks":
                        while ((line = reader.readLine()) != null && !line.startsWith("steels")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length == 0) continue;
                            if (parts2[0].equals("NormalBrick")) {
                                bricks.add(new NormalBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25, Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("StrongBrick")) {
                                bricks.add(new StrongBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25, Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("ExplosiveBrick")) {
                                bricks.add(new ExplosiveBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25, Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("GlassBrick")) {
                                bricks.add(new GlassBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25, Integer.parseInt(parts2[4])));
                            }
                        }
                        gameManager.setBricks(bricks);
                        if (line != null) parts = line.split("\\s+");
                        else continue;
                        key = parts[0];
                        if (key.equals("steels")) {
                            // Bắt đầu xử lý steels ngay, không cần chờ vòng lặp 'while' tiếp theo
                            // (Giữ logic fall-through cũ của bạn ở đây nếu muốn, nhưng 'break' và 'if' an toàn hơn)
                        } else {
                            break; // Nếu không phải 'steels', thoát
                        }

                    case "steels":
                        while ((line = reader.readLine()) != null && !line.startsWith("powerUps")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length < 3) continue;
                            steels.add(new Steel(parts2[0], Double.parseDouble(parts2[1]), Double.parseDouble(parts2[2]), 80, 25));
                        }
                        gameManager.setSteels(steels);
                        if (line != null) parts = line.split("\\s+");
                        else continue;
                        key = parts[0];
                        if (key.equals("powerUps")) {
                            // (Giữ logic fall-through cũ của bạn ở đây nếu muốn)
                        } else {
                            break;
                        }

                    case "powerUps":
                        while ((line = reader.readLine()) != null) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length < 3) continue;

                            String type = parts2[0];
                            double x = Double.parseDouble(parts2[1]);
                            double y = Double.parseDouble(parts2[2]);

                            // --- SỬA LỖI LOGIC POWER-UP ---
                            // Không truyền 'gameManager.getMainBall()' vào constructor
                            // (Truyền null, hoặc tốt hơn là xóa tham số đó khỏi constructor của PowerUp)

                            if (type.equals("ExpandPaddlePowerUp")) {
                                powerUps.add(new ExpandPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000));
                            } else if (type.equals("FastBallPowerUp")) {
                                powerUps.add(new FastBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, null));
                            } else if (type.equals("ExtraLifePowerUp")) {
                                powerUps.add(new ExtraLifePowerUp("/images/slow_ball.png", x, y, 20, 20));
                            } else if (type.equals("FireBallPowerUp")) {
                                powerUps.add(new FireBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, null));
                            } else if (type.equals("ShrinkPaddlePowerUp")) {
                                powerUps.add(new ShrinkPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000));
                            } else if (type.equals("StickyPaddlePowerUp")) {
                                powerUps.add(new StickyPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, null));
                            }
                        }
                        gameManager.setPowerUps(powerUps);
                        break;
                }
            }

            if (gameManager.getPowerUps() == null) {
                gameManager.setPowerUps(new ArrayList<>());
            }
            if (gameManager.getBalls() == null || gameManager.getBalls().isEmpty()) {
                // Xử lý nếu file save quá cũ không có 'balls'
                gameManager.setBalls(new ArrayList<>(Collections.singleton(new Ball("/images/ball1.png", 395, 530, 1, -1))));
            }


            gameManager.setGameState(GameManager.GameState.PLAYING);
        } catch (Exception e) {
            System.out.println("Can't read the save.txt file!");
            e.printStackTrace();
        }
    }

    public int getLives() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals("lives")) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read the save.txt file in getLives()");
        }
        return 0;
    }
}