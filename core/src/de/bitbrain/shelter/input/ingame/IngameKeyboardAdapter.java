package de.bitbrain.shelter.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.braingdx.util.Updateable;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   @Override
   public void update(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
   }
}
