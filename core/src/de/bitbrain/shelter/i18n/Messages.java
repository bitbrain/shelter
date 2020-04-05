package de.bitbrain.shelter.i18n;

public enum Messages {

   PLAY_GAME("play.game"),

   STORY_INTRO_1("story.intro.1"),

   STORY_OUTRO_1("story.outro.1");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
