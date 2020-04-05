package de.bitbrain.shelter.input.logo;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import de.bitbrain.shelter.screens.LogoScreen;

public class LogoControllerInputAdapter extends ControllerAdapter {

   private final LogoScreen screen;

   public LogoControllerInputAdapter(LogoScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      screen.exit();
      return true;
   }
}
