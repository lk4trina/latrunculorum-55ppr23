package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioManager {

    private boolean muted = false;
    private double volume = 0.5; // 0.0 a 1.0

    public void playClick() {
        play("/res/audio/click.mp3");
    }

    public void playCapture() {
        play("/res/audio/capture.mp3");
    }

    public void playGameOver() {
        play("/res/audio/gameover.mp3");
    }

    private void play(String path) {
        if (muted) return;

        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Áudio não encontrado: " + path);
                return;
            }
            Media media = new Media(resource.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(volume);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setVolume(double volume) {
        this.volume = Math.max(0, Math.min(1.0, volume));
    }

    public double getVolume() {
        return volume;
    }
}
