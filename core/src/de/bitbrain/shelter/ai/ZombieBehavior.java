package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.DamageBehavior;
import de.bitbrain.shelter.model.EntityFactory;
import de.bitbrain.shelter.model.EntityMover;

public class ZombieBehavior extends BehaviorAdapter {

   private static final float AGGRO_RANGE = 95;
   private final Vector2 tmp = new Vector2();

   private final ChasingBehavior chasingBehavior;
   private final RandomMovementBehavior randomMovementBehavior;
   private final GameObject playerObject;
   private DamageBehavior bitingBehavior;
   private final GameContext2D context;
   private final EntityFactory entityFactory;

   public ZombieBehavior(GameObject playerObject, GameContext2D context, EntityMover mover, EntityFactory entityFactory) {
      this.chasingBehavior = new ChasingBehavior(playerObject);
      this.randomMovementBehavior = new RandomMovementBehavior(mover);
      this.playerObject = playerObject;
      this.context = context;
      this.entityFactory = entityFactory;
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      if (bitingBehavior != null) {
         bitingBehavior.update(source, target, delta);
      }
      super.update(source, target, delta);
   }

   @Override
   public void update(GameObject source, float delta) {
      if (bitingBehavior == null) {
         this.bitingBehavior = new DamageBehavior(Vector2.Zero, source, context, entityFactory);
      }
      tmp.x = playerObject.getLeft() - source.getLeft();
      tmp.y = playerObject.getTop() - source.getTop();
      if (tmp.len() <= AGGRO_RANGE) {
         chasingBehavior.update(source, delta);
      } else {
         randomMovementBehavior.update(source, delta);
      }
      bitingBehavior.update(source, delta);
   }
}
