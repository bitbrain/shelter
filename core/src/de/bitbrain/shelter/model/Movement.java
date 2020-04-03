package de.bitbrain.shelter.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;

public class Movement implements Behavior {

   private final Vector2 lookDirection = new Vector2(0f, 1f);
   private final Vector2 moveDirection = new Vector2(0f, 1f);
   private final Vector3 tmp = new Vector3();
   private final GameCamera gameCamera;
   private float speed = 0f;
   private GameObject gameObject;

   public Movement(GameCamera gameCamera) {
      this.gameCamera = gameCamera;
   }

   public void move(Vector2 direction) {
      moveDirection.x = direction.x;
      moveDirection.y = direction.y;
      moveDirection.nor();
      speed = 125f;
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
      if (gameObject != null) {
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
   public void onDetach(GameObject source) {

   }

   @Override
   public void update(GameObject source, float delta) {
      source.move(moveDirection.x * speed * delta, moveDirection.y * speed * delta);
      speed = 0f;
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {

   }
}
