package de.bitbrain.shelter.model.spawn;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityFactory;

public class Spawner {

   private final EntityFactory entityFactory;
   private final Vector2 origin = new Vector2();
   private final Vector2 bounds = new Vector2();
   private final int capacity;

   public Spawner(float x, float y, float width, float height, EntityFactory entityFactory, int capacity) {
      this.entityFactory = entityFactory;
      this.origin.set(x, y);
      this.bounds.set(width, height);
      this.capacity = capacity;
   }

   public void spawn(GameContext2D context, GameObject player) {
      for (int i = 0; i < capacity; ++i) {
         float targetX = origin.x + (float)(bounds.x * Math.random());
         float targetY = origin.y + (float)(bounds.y * Math.random());
         entityFactory.addZombie(targetX, targetY);
      }
   }
}