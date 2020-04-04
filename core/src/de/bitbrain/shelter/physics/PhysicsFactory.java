package de.bitbrain.shelter.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
      bodyFixtureDef.density = 0f;
      bodyFixtureDef.friction = 0f;
      bodyFixtureDef.restitution = 0f;
      return bodyFixtureDef;
   }

   public static FixtureDef createBodyFixtureDef(float offsetX, float offsetY, float width, float height) {
      FixtureDef bodyFixtureDef = new FixtureDef();
      PolygonShape box = new PolygonShape();
      box.setAsBox(width, height);
      box.setAsBox(width, height, new Vector2(offsetX, offsetY), 0f);
      bodyFixtureDef.shape = box;
      bodyFixtureDef.density = 0.00001f;
      bodyFixtureDef.friction = 0f;
      bodyFixtureDef.restitution = 0f;
      return bodyFixtureDef;
   }
}
