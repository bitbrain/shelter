package de.bitbrain.shelter.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.world.GameObject;

public class PhysicsFactory {

   public static BodyDef createBodyDef(GameObject object) {
      BodyDef playerBodyDef = new BodyDef();
      playerBodyDef.type = BodyDef.BodyType.DynamicBody;
      playerBodyDef.position.set(object.getLeft() + object.getWidth() / 2f, object.getTop());
      playerBodyDef.fixedRotation = true;
      return playerBodyDef;
   }

   public static FixtureDef createBodyFixtureDef(float offsetX, float offsetY, float radius) {
      FixtureDef bodyFixtureDef = new FixtureDef();
      CircleShape playerBody = new CircleShape();
      playerBody.setPosition(new Vector2(offsetX, offsetY));
      playerBody.setRadius(radius);
      bodyFixtureDef.shape = playerBody;
      bodyFixtureDef.density = 0.00001f;
      bodyFixtureDef.friction = 0f;
      bodyFixtureDef.restitution = 0f;
      return bodyFixtureDef;
   }
}
