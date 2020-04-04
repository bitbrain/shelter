package de.bitbrain.shelter.model;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.ai.RandomMovementBehavior;
import de.bitbrain.shelter.ai.ZombieBehavior;
import de.bitbrain.shelter.util.Supplier;

import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class EntityFactory {

   private final GameContext2D context;
   private final Supplier<GameObject> playerObjectSupplier;

   public EntityFactory(GameContext2D context, Supplier<GameObject> playerObjectSupplier) {
      this.context = context;
      this.playerObjectSupplier = playerObjectSupplier;
   }

   public GameObject addZombie(float x, float y) {
      GameObject zombie = context.getGameWorld().addObject();
      zombie.setAttribute(HealthData.class, new HealthData(20));
      zombie.setType("ZOMBIE");
      zombie.setPosition(x, y);
      zombie.setZIndex(99999f);
      zombie.setDimensions(8f, 8f);
      zombie.setScaleX(4f);
      zombie.setScaleY(4f);
      zombie.setOffset(-12f,-4f);
      EntityMover entityMover = new EntityMover(717f, context.getGameCamera());
      zombie.setAttribute(EntityMover.class, entityMover);
      zombie.setAttribute("tmx_layer_index", 0);
      context.getBehaviorManager().apply(entityMover, zombie);
      context.getBehaviorManager().apply(new ZombieBehavior(playerObjectSupplier.supply(), context, entityMover), zombie);
      Color color = new Color(1f, 0, 0, 0.15f);
      PointLight light = context.getLightingManager().createPointLight(10f, color);
      context.getLightingManager().attach(light, zombie, 16f, 17f);
      // add physics
      BodyDef bodyDef = createBodyDef(zombie);
      FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 4f);
      context.getPhysicsManager().attachBody(bodyDef, fixtureDef, zombie);
      return zombie;
   }
}
