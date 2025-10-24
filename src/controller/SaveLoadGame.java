package controller;

import java.io.*;
import java.util.List;
import java.lang.String;
import model.*;
import controller.*;

public class SaveLoadGame {

    /**
     * This method is used when player wants to save the game and play it next time.
     * @param gameManager
     */
    public static void saveGame(controller.GameManager gameManager) {
        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter("src/data/save.txt")));
            writer.write("paddle " + gameManager.getPaddle().getX());
            writer.newLine();

            writer.write("ball " + gameManager.getBall().getX()
                    + " " + gameManager.getBall().getY()
                    + " " + gameManager.getBall().getDirectionX()
                    + " " + gameManager.getBall().getDirectionY());
            writer.newLine();

            writer.write("score " + gameManager.getScore());
            writer.newLine();

            writer.write("lives " + gameManager.getLives());
            writer.newLine();

            writer.write("lastPowerUpTime " + (System.currentTimeMillis() - gameManager.getLastPowerUpTime()));
            writer.newLine();

            String activePowerUpType = "null";
            if (gameManager.getActivePowerUp() != null) {
                activePowerUpType = gameManager.getActivePowerUp().getClass().getSimpleName();
            }
            writer.write("activePowerUp " + activePowerUpType);
            writer.newLine();

            writer.write("bricks");
            List<Brick> saveBricks = gameManager.getBricks();
            for (Brick brick : saveBricks) {
                if (brick instanceof NormalBrick) {
                    writer.write(" NormalBrick " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                } else if (brick instanceof StrongBrick) {
                    writer.write(" StrongBrick " + brick.getX() + " " + brick.getY() + " " + brick.getHealth());
                }
            }
            writer.newLine();

            writer.write("steels");
            List<Steel> steels = gameManager.getSteels();
            for (Steel steel : steels) {
                writer.write(" " + steel.getX() + " " + steel.getY());
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
                    case "paddle":
                        gameManager.getPaddle().setX(Double.parseDouble(parts[1]));
                        gameManager.getPaddle().setY(550);
                        break;
                    case "ball":
                        gameManager.getBall().setX(Double.parseDouble(parts[1]));
                        gameManager.getBall().setY(Double.parseDouble(parts[2]));
                        gameManager.getBall().setDirectionX(Double.parseDouble(parts[3]));
                        gameManager.getBall().setDirectionY(Double.parseDouble(parts[4]));
                        break;
                    case "score":
                        gameManager.setScore(Integer.parseInt(parts[1]));
                        break;
                    case "lives":
                        gameManager.setLives(Integer.parseInt(parts[1]));
                        break;
                    case "lastPowerUpTime":
                        gameManager.setLastPowerUpTime(System.currentTimeMillis() + Long.parseLong(parts[1]));
                        break;
                    case "activePowerUp":
                        gameManager.setActivePowerUpByName(parts[1]);
                        break;
                    case "bricks":
                        gameManager.getBricks().clear();
                        int n = parts.length;
                        for (int i = 1; i < n; i += 4) {
                            double x = Double.parseDouble(parts[i + 1]);
                            double y = Double.parseDouble(parts[i + 2]);
                            int health = Integer.parseInt(parts[i + 3]);

                            if (parts[i].equals("NormalBrick")) {
                                gameManager.getBricks().add(new NormalBrick("/images/brick 1.png", x, y, 80, 25, health));
                            } else if (parts[i].equals("StrongBrick")) {
                                gameManager.getBricks().add(new StrongBrick("/images/brick 6.png", x, y, 80, 25, health));
                            }
                        }
                        break;
                    case "steels":
                        int n1 = parts.length;
                        for (int i = 1; i < n1; i += 2) {
                            double x = Double.parseDouble(parts[i]);
                            double y = Double.parseDouble(parts[i + 1]);

                            gameManager.getSteels().add(new Steel("", x, y, 80, 25));
                        }
                        break;
                    case "powerUps":
                        gameManager.getPowerUps().clear();
                        int n2 = parts.length;
                        for (int i = 1; i < n2; i+=3) {
                            double x = Double.parseDouble(parts[i + 1]);
                            double y = Double.parseDouble(parts[i + 2]);

                            if (parts[i].equals("ExpandPaddlePowerUp")) {
                                gameManager.getPowerUps().add(new ExpandPaddlePowerUp("/images/slow_ball.png", x, y, 20, 20, 5000));
                            } else if (parts[i].equals("ShrinkPaddle")) {
                                // gameManager.getPowerUps().add(new ShrinkPaddle(x, y, 20, 20, 5000));
                            }  else if (parts[i].equals("FastBallPowerUp")) {
                                gameManager.getPowerUps().add(new FastBallPowerUp("/images/slow_ball.png", x, y, 20, 20, 5000, gameManager.getBall()));
                            } else if (parts[i].equals("SlowBall")) {

                            } else if (parts[i].equals("TinyBall")) {

                            }
                        }
                        break;
                }
            }
            gameManager.setGameState(GameManager.GameState.PLAYING);
            reader.close();
        } catch (Exception e) {
            System.out.println("Can't read the save.txt file!");
        }
    }
}
