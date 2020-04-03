package de.bitbrain.shelter.i18n;

public enum Messages {

   PLAY_GAME("play.game");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
