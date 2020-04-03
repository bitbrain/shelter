package de.bitbrain.shelter.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.shelter.model.EntityMover;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   private Vector2 moveDirection = new Vector2();
   private final EntityMover playerEntityMover;

   public IngameKeyboardAdapter(EntityMover playerEntityMover) {
      this.playerEntityMover = playerEntityMover;
   }

   @Override
   public void update(float delta) {
      if (input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (input.isKeyPressed(W)) {
         moveDirection.y = 1f;
      }
      if (input.isKeyPressed(A)) {
         moveDirection.x = -1;
      }
      if (input.isKeyPressed(S)) {
         moveDirection.y = -1;
      }
      if (input.isKeyPressed(D)) {
         moveDirection.x = 1;
      }
      playerEntityMover.move(moveDirection);
      moveDirection.x = 0;
      moveDirection.y = 0;
   }
}
