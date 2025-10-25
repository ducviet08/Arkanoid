package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import model.*;
import controller.*;

import static Arkanoid.Main.*;

public class SaveLoadGame {

    /**
     * This method is used when player wants to save the game and play it next time.
     *
     * @param gameManager
     */
    public static void saveGame(controller.GameManager gameManager) {
        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter("src/data/save.txt")));

            writer.write("level " + currentLevel );
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

            writer.write("bricks");
            writer.newLine();
            List<Brick> saveBricks = gameManager.getBricks();
            for (Brick brick : saveBricks) {
                if (brick instanceof NormalBrick) {
                    writer.write("NormalBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                } else if (brick instanceof StrongBrick) {
                    writer.write("StrongBrick " + brick.getPath() + " " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                }
                writer.newLine(); // <- thêm dòng mới cho mỗi viên
            }

            writer.write("steels");
            List<Steel> steels = gameManager.getSteels();
            for (Steel steel : steels) {
                writer.write(" " + steel.getPath() + " "
                        + steel.getX() + " " + steel.getY());
            }
            writer.newLine();

            writer.write("powerUps");
            List<PowerUp> powerUps = gameManager.getPowerUps();
            for (PowerUp powerUp : powerUps) {
                String type = powerUp.getClass().getSimpleName();
                writer.write(" " + type + " " + powerUp.getX() + " " + powerUp.getY());
                /*if (powerUp instanceof ExpandPaddlePowerUp) {
                    writer.write(" ExpandPaddlePowerUp " + powerUp.getX() + " " + powerUp.getY());
                } else if (powerUp instanceof ShrinkPaddle) {
                    writer.write(" ShrinkPaddle " + powerUp.getX() + " " + powerUp.getY());
                } else if (powerUp instanceof StickyPaddle) {
                    writer.write(" StickyPaddle " + powerUp.getX() + " " + powerUp.getY());
                } else if (powerUp instanceof FastBallPowerUp) {
                    writer.write(" FastBallPowerUp " + powerUp.getX() + " " + powerUp.getY());
                } else if (powerUp instanceof SlowBall) {
                    writer.write(" SlowBall " + powerUp.getX() + " " + powerUp.getY());
                } else if (powerUp instanceof TinyBall) {
                    writer.write(" TinyBall " + powerUp.getX() + " " + powerUp.getY());
                }*/
            }
            writer.newLine();

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
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/data/save.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
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
                        gameManager.getBall().setActive(true);
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
                    case "bricks":
                        List<Brick> bricks = new ArrayList<>();
                        while ((line = reader.readLine()) != null && !line.startsWith("steels")) {
                            String[] parts2 = line.split("\\s+");
                            if (parts2[0].equals("NormalBrick")) {
                                bricks.add(new NormalBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 20, Integer.parseInt(parts2[4])));
                            } else if (parts2[0].equals("StrongBrick")) {
                                bricks.add(new StrongBrick(parts2[1], Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3]), 80, 20, Integer.parseInt(parts2[4])));
                            }
                        }
                        gameManager.setBricks(bricks);
                        break;
                    case "steels":
                        List<Steel> steels = new ArrayList<>();
                        int n1 = parts.length;
                        for (int i = 1; i < n1; i += 3) {
                            String path = parts[i];
                            double x = Double.parseDouble(parts[i + 1]);
                            double y = Double.parseDouble(parts[i + 2]);

                            steels.add(new Steel(path, x, y, 80, 20));
                        }
                        gameManager.setSteels(steels);
                        break;
//                    case "powerUps":
//                        gameManager.getPowerUps().clear();
//                        int n2 = parts.length;
//                        for (int i = 1; i < n2; i+=3) {
//                            double x = Double.parseDouble(parts[i + 1]);
//                            double y = Double.parseDouble(parts[i + 2]);
//
//                            if (parts[i].equals("ExpandPaddlePowerUp")) {
//                                gameManager.getPowerUps().add(new ExpandPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 5000));
//                            } else if (parts[i].equals("ShrinkPaddle")) {
//                                // gameManager.getPowerUps().add(new ShrinkPaddle(x, y, 20, 20, 5000));
//                            }  else if (parts[i].equals("FastBallPowerUp")) {
//                                gameManager.getPowerUps().add(new FastBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 5000, gameManager.getBall()));
//                            } else if (parts[i].equals("SlowBall")) {
//
//                            } else if (parts[i].equals("TinyBall")) {
//
//                            }
//                        }
//                        break;
                }
            }
            List<PowerUp> powerUps = new ArrayList<>();
            gameManager.setPowerUps(powerUps);
            gameManager.setGameState(GameManager.GameState.PLAYING);
            reader.close();
        } catch (Exception e) {
            System.out.println("Can't read the save.txt file!");
        }
    }

    public int getLives() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/save.txt"))) {
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
