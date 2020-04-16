package de.bitbrain.shelter.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.core.model.HealthData;

public class EntityMover extends BehaviorAdapter {

   private final Vector2 lookDirection = new Vector2(0f, 1f);
   private final Vector2 moveDirection = new Vector2(0f, 1f);
   private final Vector3 tmp = new Vector3();
   private final GameCamera gameCamera;
   private final float maxSpeed;
   private Vector2 movement = new Vector2();
   private GameObject gameObject;
   private JukeBox walkJukeBox;

   public EntityMover(float maxSpeed, GameCamera gameCamera, AudioManager audioManager) {
      this.maxSpeed = maxSpeed;
      this.gameCamera = gameCamera;
      this.walkJukeBox = new JukeBox(audioManager, 200,
            Assets.Sounds.WALK_01,
            Assets.Sounds.WALK_02,
            Assets.Sounds.WALK_03,
            Assets.Sounds.WALK_04
      );
      walkJukeBox.setVolume(0.1f);

   }

   public void move(Vector2 direction, float amount) {
      moveDirection.x = direction.x;
      moveDirection.y = direction.y;
      moveDirection.nor();
      movement.x += moveDirection.x * amount;
      movement.y += moveDirection.y * amount;
   }

   public void move(Vector2 direction) {
      move(direction, maxSpeed);
   }

   public Vector2 getLookDirection() {
      return lookDirection;
   }

   public void lookAtScreen(float screenX, float screenY) {
      tmp.set(screenX, screenY, 0f);
      gameCamera.getInternalCamera().unproject(tmp);
      lookAtWorld(tmp.x, tmp.y);
   }

   public void lookAtWorld(float worldX, float worldY) {
      if (gameObject != null && (!gameObject.hasAttribute(HealthData.class) || !gameObject.getAttribute(HealthData.class).isDead())) {
         lookDirection.x = gameObject.getLeft() + gameObject.getWidth() / 2f - worldX;
         lookDirection.y = gameObject.getTop() + gameObject.getHeight() / 2f - worldY;
         lookDirection.nor();
      }
   }

   @Override
   public void onAttach(GameObject source) {
      this.gameObject = source;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (source.hasAttribute(HealthData.class) && source.getAttribute(HealthData.class).isDead()) {
         movement.set(0f, 0f);
         if (gameObject.hasAttribute(Body.class)) {
            Body body = gameObject.getAttribute(Body.class);
            body.setLinearVelocity(0f, 0f);
         }
         return;
      }
      float moveX = movement.x * delta;
      float moveY = movement.y * delta;
      if (gameObject.hasAttribute(Body.class)) {
         Body body = gameObject.getAttribute(Body.class);
         if (body.isActive()) {
            body.setLinearVelocity(moveX, moveY);
            if ((moveX != 0 || moveY != 0) && gameObject != null && "PLAYER".equals(gameObject.getType())) {
               walkJukeBox.playSound(gameObject.getLeft() + gameObject.getWidth() / 2f, gameObject.getTop() + gameObject.getHeight() / 2f);
            }
         } else {
            gameObject.move(moveX, moveY);
         }
      } else {
         gameObject.move(moveX, moveY);
      }
      movement.set(0f, 0f);
   }
}
