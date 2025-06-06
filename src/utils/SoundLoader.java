package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundLoader {
    private static final Map<Clip, Long> pausedPositions = new HashMap<>();

    // For playing a short sound without looping
    public static void playWAV(String path) {
        URL soundURL = SoundLoader.class.getResource(path);
        if (soundURL == null) {
            System.err.println("Sound file not found: " + path);
            return;
        }

        // Attempt to load and play the sound file
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            applyVolumeReduction(clip);
            clip.start();
        } catch (UnsupportedAudioFileException e) { System.err.println("Unsupported audio file: " + path);
        } catch (IOException e) { System.err.println("I/O error while playing sound: " + path);
        } catch (LineUnavailableException e) { System.err.println("Audio line unavailable: " + path);
        }
    }

    // For background music
    public static Clip loadLoopClip(String path) {
        URL soundURL = SoundLoader.class.getResource(path);
        if (soundURL == null) {
            System.err.println("Sound file not found: " + path);
            return null;
        }

        // Attempt to load and loop the sound file
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            applyVolumeReduction(clip);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return clip;
        } catch (UnsupportedAudioFileException e) { System.err.println("Unsupported audio file: " + path);
        } catch (IOException e) { System.err.println("I/O error while loading sound: " + path);
        } catch (LineUnavailableException e) { System.err.println("Audio line unavailable: " + path);
        }
        return null;
    }

    // For stopping and closing clips
    public static void stopClip(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) { clip.stop(); }
            clip.close();
        }
        pausedPositions.remove(clip);
    }

    // For temporary pausing clips
    public static void pauseClip(Clip clip) {
        if (clip != null && clip.isRunning()) {
            pausedPositions.put(clip, clip.getMicrosecondPosition());
            clip.stop();
        }
    }

    // For resuming paused clips
    public static void resumeClip(Clip clip) {
        if (clip != null && !clip.isRunning()) {
            Long position = pausedPositions.get(clip);
            if (position != null) { clip.setMicrosecondPosition(position); }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // For adjusting volume of a clip
    private static void applyVolumeReduction(Clip clip) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue((float) -15.0);
        }
    }
}