package de.bitbrain.shelter.screens;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.input.ingame.IngameKeyboardAdapter;

public class IngameScreen extends BrainGdxScreen2D<ShelterGame> {

   public IngameScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      setupInput(context);
   }

   private void setupInput(GameContext2D context2D) {
      context2D.getInputManager().register(new IngameKeyboardAdapter());
   }
}
