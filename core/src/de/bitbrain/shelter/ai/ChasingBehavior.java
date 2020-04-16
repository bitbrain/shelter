package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.core.EntityFactory;
import de.bitbrain.shelter.core.EntityMover;
import de.bitbrain.shelter.core.weapon.AttackHandler;

public class ChasingBehavior extends BehaviorAdapter {

   private final GameObject playerObject;
   private final Vector2 direction = new Vector2();
   private final EntityFactory entityFactory;
   private final GameContext2D context;

   private AttackHandler attackHandler;

   public ChasingBehavior(GameObject playerObject, EntityFactory entityFactory, GameContext2D context) {
      this.playerObject = playerObject;
      this.entityFactory = entityFactory;
      this.context = context;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (attackHandler == null) {
         attackHandler = new AttackHandler(source, entityFactory);
      }
      super.update(source, delta);
      attackHandler.update(delta);
      EntityMover mover = source.getAttribute(EntityMover.class);
      direction.x = playerObject.getLeft() - source.getLeft();
      direction.y = playerObject.getTop() - source.getTop();
      mover.lookAtWorld(source.getLeft() + direction.x, source.getTop() + direction.y);
      mover.move(direction, 2040f);
      attackHandler.attack(context);

   }
}
