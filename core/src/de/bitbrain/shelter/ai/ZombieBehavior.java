package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityMover;

public class ZombieBehavior extends BehaviorAdapter {

   private static final float AGGRO_RANGE = 100;
   private final Vector2 tmp = new Vector2();

   private final ChasingBehavior chasingBehavior;
   private final RandomMovementBehavior randomMovementBehavior;
   private final GameObject playerObject;

   public ZombieBehavior(GameObject playerObject, EntityMover mover) {
      this.chasingBehavior = new ChasingBehavior(playerObject);
      this.randomMovementBehavior = new RandomMovementBehavior(mover);
      this.playerObject = playerObject;
   }

   @Override
   public void update(GameObject source, float delta) {
      tmp.x = playerObject.getLeft() - source.getLeft();
      tmp.y = playerObject.getTop() - source.getTop();
      if (tmp.len() <= AGGRO_RANGE) {
         chasingBehavior.update(source, delta);
      } else {
         randomMovementBehavior.update(source, delta);
      }
   }
}
