package de.bitbrain.shelter.audio;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.audio.AudioManager;

public class JukeBox {

   public static float MINIMUM_INTERVAL_MILLIS = 400f;

   private final AudioManager audioManager;
   private final String[] audioFiles;
   private float pitchVariation = 0.1f;
   private final float range;
   private long interval = -1;
   private float volume = 0.3f;

   public JukeBox(AudioManager audioManager, float range, String... audioFiles) {
      this.audioManager = audioManager;
      this.range = range;
      this.audioFiles = audioFiles;
   }

   public void setVolume(float volume) {
      this.volume = volume;
   }

   public void setPitchVariation(float pitchVariation) {
      this.pitchVariation = pitchVariation;
   }

   public void playSound(float x, float y) {
      if (audioFiles.length == 0) {
         Gdx.app.error("SOUND", "Missing audio files in JukeBox!");
         return;
      }
      if (interval >= 0 && System.currentTimeMillis() - interval < MINIMUM_INTERVAL_MILLIS) {
         return;
      }
      String audioFile = audioFiles[(int) (audioFiles.length * Math.random())];
      float pitch = (float) ((1f - pitchVariation / 2f) + (pitchVariation * Math.random()));
      audioManager.spawnSound(audioFile, x, y, pitch, volume, range);
      interval = System.currentTimeMillis();
   }
}
