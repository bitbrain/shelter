package de.bitbrain.shelter.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;

public class Player {

   private final Vector2 direction = new Vector2(0f, 1f);
   private final Vector3 tmp = new Vector3();
   private final GameObject playerObject;
   private final GameCamera gameCamera;

   public Player(GameObject playerObject, GameCamera gameCamera) {
      this.playerObject = playerObject;
      this.gameCamera = gameCamera;
   }

   public Vector2 getDirection() {
      return direction;
   }

   public void lookAtScreen(float screenX, float screenY) {
      tmp.set(screenX, screenY, 0f);
      gameCamera.getInternalCamera().unproject(tmp);
      lookAtWorld(tmp.x, tmp.y);
   }

   public void lookAtWorld(float worldX, float worldY) {
      direction.x = playerObject.getLeft() - worldX;
      direction.y = playerObject.getTop() - worldY;
      direction.nor();
   }
}
