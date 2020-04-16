package de.bitbrain.shelter.ai;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.core.EntityFactory;
import de.bitbrain.shelter.core.EntityMover;

public class ZombieBehavior extends BehaviorAdapter {

   private static final float AGGRO_RANGE = 95;
   private final Vector2 tmp = new Vector2();

   private final ChasingBehavior chasingBehavior;
   private final RandomMovementBehavior randomMovementBehavior;
   private final GameObject playerObject;
   private final JukeBox zombieScreamSound;

   public ZombieBehavior(GameObject playerObject, GameContext2D context, EntityMover mover, EntityFactory entityFactory) {
      this.chasingBehavior = new ChasingBehavior(playerObject, entityFactory, context);
      this.randomMovementBehavior = new RandomMovementBehavior(mover);
      this.playerObject = playerObject;
      zombieScreamSound = new JukeBox(context.getAudioManager(), 1000,
            Assets.Sounds.ZOMBIE_NOISE_1,
            Assets.Sounds.ZOMBIE_NOISE_2,
            Assets.Sounds.ZOMBIE_NOISE_3);
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
      if (Math.random() < 0.000065f) {
         zombieScreamSound.playSound(source.getLeft() + source.getWidth() / 2f, source.getTop() + source.getTop() / 2f);
      }
   }
}
