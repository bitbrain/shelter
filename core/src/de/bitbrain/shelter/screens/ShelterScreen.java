package de.bitbrain.shelter.screens;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.shelter.ShelterGame;

public class ShelterScreen extends BrainGdxScreen2D<ShelterGame> {

   private final String nextLevelPath;

   public ShelterScreen(ShelterGame game, String nextLevelPath) {
      super(game);
      this.nextLevelPath = nextLevelPath;
   }

   @Override
   protected void onCreate(GameContext2D context) {
      if (nextLevelPath != null) {
         // This bunker doesn't seem save. It's time to move to the next exit to continue
         // the search
         context.getScreenTransitions().out(new IngameScreen(getGame(), nextLevelPath), 0.5f);
      } else {
         // GAME OVER! WELCOME HOME!
      }
   }
}
