package Arkanoid.controller;

import java.io.*;
import java.util.ArrayList;
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

public class SaveLoadGame {
    public static final String FILE_PATH = "src/main/resources/data/save.txt";

    /**
     * This method is used when player wants to save the game and play it next time.
     *
     * @param gameManager
     */
    public static void saveGame(GameManager gameManager) {
        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter(FILE_PATH)));

            writer.write("level " + currentLevel);
            writer.newLine();

            writer.write("paddle " + paddleImage + " " + gameManager.getPaddle().getX());
            writer.newLine();

            writer.write("ball " + ballImage
                    + " " + gameManager.getBall().getX()
                    + " " + gameManager.getBall().getY()
                    + " " + gameManager.getBall().getDirectionX()
                    + " " + gameManager.getBall().getDirectionY());
            writer.newLine();

            writer.write("score " + gameManager.getScore());
            writer.newLine();

            writer.write("lives " + gameManager.getLives());
            writer.newLine();

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
                } else if (brick instanceof TeleportBrick) {
                    writer.write("TeleportBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                }
                writer.newLine(); // <- thêm dòng mới cho mỗi viên
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
                writer.write(type + " " + powerUp.getX() + " " + powerUp.getY()); // XÓA KHOẢNG TRẮNG ĐẦU
                writer.newLine();
            }

            writer.close();
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.out.println("Can't write in the save.txt file!");
        }
    }

    /**
     * The loadGame method is used when the player selects CONTINUE in the game menu.
     *
     * @param gameManager
     */
    public static void loadGame(GameManager gameManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Khai báo các list
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
                    case "ball":
                        Ball ball = new Ball(parts[1], Double.parseDouble(parts[2]),
                                Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]));
                        gameManager.setBall(ball);
                        gameManager.getBall().setActive(true); // Giả sử bóng active khi load
                        break;
                    case "score":
                        gameManager.setScore(Integer.parseInt(parts[1]));
                        break;
                    case "lives":
                        gameManager.setLives(Integer.parseInt(parts[1]));
                        break;
                    case "lastBallPowerUpTime":
                        gameManager.setBallLastPowerUpTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activeBallPowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    case "lastPaddlePowerUpTime":
                        gameManager.setPaddleLastPowerUpTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activePaddlePowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    // THÊM MỚI: Load MixPowerUp
                    case "lastMixPowerUpTime":
                        gameManager.setMixLastPowerTime(System.currentTimeMillis() - Long.parseLong(parts[1]));
                        break;
                    case "activeMixPowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    case "steels":
                        while ((line = reader.readLine()) != null && !line.startsWith("powerUps")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length < 3) continue;
                            steels.add(new Steel(parts2[0], Double.parseDouble(parts2[1]), Double.parseDouble(parts2[2]), 80, 25)); // Giả sử 80x25
                        }
                        gameManager.setSteels(steels);
                        if (line != null) parts = line.split("\\s+");
                        else continue;
                        key = parts[0];
                        break;
                    case "bricks":
                        while ((line = reader.readLine()) != null && !line.startsWith("steels")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length == 0) continue;
                            if (parts2[0].equals("NormalBrick")) {
                                bricks.add(new NormalBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25,
                                                    Integer.parseInt(parts2[4]))); // kích thước của brick là 80x25
                            } else if (parts2[0].equals("StrongBrick")) {
                                bricks.add(new StrongBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25,
                                                    Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("ExplosiveBrick")) {
                                bricks.add(new ExplosiveBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25,
                                                    Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("GlassBrick")) {
                                bricks.add(new GlassBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25,
                                                    Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("TeleportBrick")) {
                                bricks.add(new TeleportBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 25,
                                                    Integer.parseInt(parts2[4]), bricks, steels));
                            }
                        }
                        gameManager.setBricks(bricks);
                        if (line != null) parts = line.split("\\s+");
                        else continue;
                        key = parts[0];
                        break;
                    case "powerUps":
                        while ((line = reader.readLine()) != null) { // Đọc đến cuối file
                            String[] parts2 = line.split("\\s+");
                            if (parts2.length < 3) continue;

                            String type = parts2[0];
                            double x = Double.parseDouble(parts2[1]);
                            double y = Double.parseDouble(parts2[2]);

                            // (Giả sử kích thước và thời gian mặc định khi load)
                            if (type.equals("ExpandPaddlePowerUp")) {
                                powerUps.add(new ExpandPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000));
                            } else if (type.equals("FastBallPowerUp")) {
                                powerUps.add(new FastBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, gameManager.getBall()));
                            } else if (type.equals("ExtraLifePowerUp")) {
                                powerUps.add(new ExtraLifePowerUp("/images/slow_ball.png", x, y, 20, 20));
                            } else if (type.equals("FireBallPowerUp")) {
                                powerUps.add(new FireBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, gameManager.getBall()));
                            } else if (type.equals("ShrinkPaddlePowerUp")) {
                                powerUps.add(new ShrinkPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000));
                            } else if (type.equals("StickyPaddlePowerUp")) {
                                powerUps.add(new StickyPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 10000, gameManager.getBall()));
                            }
                        }
                        gameManager.setPowerUps(powerUps);
                        break;
                }
            }

            // Xóa danh sách power-up nếu file save cũ không có (tránh null)
            if (gameManager.getPowerUps() == null) {
                gameManager.setPowerUps(new ArrayList<>());
            }

            gameManager.setGameState(GameManager.GameState.PLAYING);
        } catch (Exception e) {
            System.out.println("Can't read the save.txt file!");
            e.printStackTrace(); // In ra lỗi để debug
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
