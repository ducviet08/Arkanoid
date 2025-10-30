package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.Objects;

public class SoundManager {
    public static final String SOUND_CLICK = "/sounds/click.mp3";
    public static final String SOUND_PADDLE_HIT = "/sounds/BounceWithPaddle_sound.mp3";
    public static final String SOUND_NORMALBRICK_HIT = "/sounds/BounceWithNormalBrick_sound.mp3";
    public static final String SOUND_STEELBRICK_HIT = "/sounds/BounceWithSteelBrick.mp3";
    public static final String SOUND_POWERUP_GET = "/sounds/powerup_get.mp3";
    public static final String SOUND_LOSE_LIFE = "/sounds/fall_out.mp3";
    public static final String SOUND_LEVEL_COMPLETE = "/sounds/click.mp3";
    public static final String SOUND_GAME_OVER = "/sounds/game_over_sound.mp3";
    public static final String SOUND_WIN_GAME = "/sounds/win.mp3";
    public static final String SOUND_GAME_START = "/sounds/start_sound.mp3";


    // Lưu player hiện tại để có thể stop khi cần
    private static MediaPlayer currentPlayer;
    private static String typeCurrentPlayer;

    public static void playSound(String path) {
        if(Objects.equals(typeCurrentPlayer, SOUND_WIN_GAME) || Objects.equals(typeCurrentPlayer, SOUND_GAME_OVER)) {
            stopCurrentSound(); // dừng trước khi phát cái mới
        }

        URL resource = SoundManager.class.getResource(path);
        if (resource == null) {
            System.out.println("Sound file not found: " + path);
            return;
        }

        Media media = new Media(resource.toString());
        currentPlayer = new MediaPlayer(media);
        typeCurrentPlayer = path;
        currentPlayer.play();
    }

    /** Hàm dùng để ngắt âm thanh  */
    public static void stopCurrentSound() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
}
