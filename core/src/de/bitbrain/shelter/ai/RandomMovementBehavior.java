package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.core.entities.EntityMover;

public class RandomMovementBehavior extends BehaviorAdapter {

   private final Vector2 direction = new Vector2(1f, 0f);
   private float angle;

   private final EntityMover mover;

   public RandomMovementBehavior(EntityMover mover) {
      this.mover = mover;
      this.angle = (float) (Math.random() * 360f);
   }

   @Override
   public void update(GameObject source, float delta) {
      float multiplicator = Math.random() < 0.5f ? 1f : -1f;
      if (Math.random() < 0.5f) {
         angle += multiplicator * 500f * delta * Math.random();
      } else {
         angle -= multiplicator * 500f * delta * Math.random();
      }
      direction.setAngle(angle);
      direction.nor();
      final float lookAtX = source.getLeft() + source.getWidth() / 2f + direction.x;
      final float lookAtY = source.getTop() + source.getHeight() / 2f + direction.y;
      mover.lookAtWorld(lookAtX, lookAtY);
      mover.move(direction);
   }
}
