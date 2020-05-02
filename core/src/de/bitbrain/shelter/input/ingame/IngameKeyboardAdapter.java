package de.bitbrain.shelter.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.shelter.core.entities.EntityMover;
import de.bitbrain.shelter.core.items.Inventory;
import de.bitbrain.shelter.core.weapon.AttackHandler;
import de.bitbrain.shelter.core.weapon.RangeType;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   private Vector2 moveDirection = new Vector2();
   private final EntityMover playerEntityMover;
   private final AttackHandler playerAttackHandler;
   private final Inventory inventory;
   private final GameContext2D context;

   public IngameKeyboardAdapter(EntityMover playerEntityMover, Inventory inventory, AttackHandler playerAttackHandler, GameContext2D context) {
      this.playerEntityMover = playerEntityMover;
      this.playerAttackHandler = playerAttackHandler;
      this.inventory = inventory;
      this.context = context;
   }

   @Override
   public void update(float delta) {
      playerEntityMover.lookAtScreen(Gdx.input.getX(), Gdx.input.getY());
      if (input.isKeyPressed(ESCAPE)) {
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
         playerAttackHandler.attack(context);
      }
      playerEntityMover.move(moveDirection);
      moveDirection.x = 0;
      moveDirection.y = 0;
   }

   @Override
   public boolean scrolled(int amount) {
      RangeType rangeType = inventory.getEquippedRangeType();
      if (rangeType == null) {
         return false;
      }
      RangeType nextType = rangeType == RangeType.CLOSE_RANGE ? RangeType.LONG_RANGE : RangeType.CLOSE_RANGE;
      inventory.equipSelected(nextType);
      return true;
   }
}
