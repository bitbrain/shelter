package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityMover;

public class ChasingBehavior extends BehaviorAdapter {

   private final GameObject playerObject;
   private final Vector2 direction = new Vector2();

   public ChasingBehavior(GameObject playerObject) {
      this.playerObject = playerObject;
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      EntityMover mover = source.getAttribute(EntityMover.class);
      direction.x = playerObject.getLeft() - source.getLeft();
      direction.y = playerObject.getTop() - source.getTop();
      mover.lookAtWorld(source.getLeft() + direction.x, source.getTop() + direction.y);
      mover.move(direction, 1240f);
   }
}
