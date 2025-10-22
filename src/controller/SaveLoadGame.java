package controller;

import java.io.*;

public class SaveLoadGame {

    public static void saveGame(GameManager gameManager) {
        try {
            BufferedWriter writer = new BufferedWriter((new FileWriter("save.txt")));

        } catch (IOException e) {
            System.out.println("Can't write in the save.txt file!");
        }
    }

    public static void loadGame(GameManager gameManager) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String key = parts[0];

                switch (key) {
                    case "Paddle":

                }
            }
        } catch (IOException e) {
            System.out.println("Can't read the save.txt file!");
        }
    }
}
