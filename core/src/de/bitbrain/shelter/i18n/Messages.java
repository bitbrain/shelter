package de.bitbrain.shelter.i18n;

public enum Messages {

   PLAY_GAME("play.game"),

   TIPS("mainmenu.tips"),

   BITBRAIN("credits.bitbrain"),

   STORY_INTRO_1("story.intro.1"),
   STORY_INTRO_2("story.intro.2"),
   STORY_INTRO_3("story.intro.3"),
   STORY_INTRO_4("story.intro.4"),

   STORY_OUTRO_1("story.outro.1"),
   STORY_OUTRO_2("story.outro.2"),
   STORY_OUTRO_3("story.outro.3");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
