package de.bitbrain.shelter.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.shelter.model.EntityMover;
import de.bitbrain.shelter.model.weapon.WeaponHandler;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   private Vector2 moveDirection = new Vector2();
   private final EntityMover playerEntityMover;
   private final WeaponHandler playerWeaponHandler;
   private final GameContext2D context;

   public IngameKeyboardAdapter(EntityMover playerEntityMover, WeaponHandler playerWeaponHandler, GameContext2D context) {
      this.playerEntityMover = playerEntityMover;
      this.playerWeaponHandler = playerWeaponHandler;
      this.context = context;
   }

   @Override
   public void update(float delta) {
      playerEntityMover.lookAtScreen(Gdx.input.getX(), Gdx.input.getY());
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
      if (input.isTouched()) {
         playerWeaponHandler.fire(context);
      }
      playerEntityMover.move(moveDirection);
      moveDirection.x = 0;
      moveDirection.y = 0;
   }
}
