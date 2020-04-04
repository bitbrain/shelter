package de.bitbrain.shelter.model;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.ai.RandomMovementBehavior;

public class EntitySpawner {

   private final GameContext2D context;

   public EntitySpawner(GameContext2D context) {
      this.context = context;
   }

   public void spawnZombie(float x, float y) {
      GameObject zombie = context.getGameWorld().addObject();
      zombie.setType("ZOMBIE");
      zombie.setPosition(x, y);
      zombie.setZIndex(99999f);
      zombie.setDimensions(8f, 8f);
      zombie.setScaleX(4f);
      zombie.setScaleY(4f);
      zombie.setOffset(-12f,-4f);
      EntityMover entityMover = new EntityMover(17f, context.getGameCamera());
      zombie.setAttribute(EntityMover.class, entityMover);
      zombie.setAttribute("tmx_layer_index", 0);
      context.getBehaviorManager().apply(entityMover, zombie);
      context.getBehaviorManager().apply(new RandomMovementBehavior(entityMover), zombie);
      Color color = new Color(1f, 0, 0, 0.15f);
      PointLight light = context.getLightingManager().createPointLight(10f, color);
      context.getLightingManager().attach(light, zombie, 16f, 17f);
   }
}
