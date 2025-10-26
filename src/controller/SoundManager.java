package controller;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class SoundManager {
    private static AudioClip loadSound(String path) {
        URL resource = SoundManager.class.getResource(path);
        if(resource == null) {
            System.out.println("Sound file not found: " + path);
            return null;
        }

        return new AudioClip(resource.toString());
    }

    public static final AudioClip CLICK = loadSound("sounds/click.mp3");
    public static final AudioClip PADDLE_HIT = loadSound("sounds/click.mp3");
    public static final AudioClip BRICK_HIT = loadSound("sounds/click.mp3");
    public static final AudioClip POWERUP_GET = loadSound("sounds/click.mp3");
    public static final AudioClip LOSE_LIFE = loadSound("sounds/click.mp3");
    public static final AudioClip LEVEL_COMPELTE = loadSound("sounds/click.mp3");
    public static final AudioClip GAME_OVER = loadSound("sounds/click.mp3");

    public static void play(AudioClip clip) {
        if(clip != null) clip.play();
    }
}
