package de.bitbrain.shelter.physics;

import com.badlogic.gdx.physics.box2d.*;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.weapon.WeaponType;

public class BulletContactListener implements ContactListener {

   private final GameContext2D context;

   public BulletContactListener(GameContext2D context) {
      this.context = context;
   }

   @Override
   public void beginContact(Contact contact) {
      Body bodyA = contact.getFixtureA().getBody();
      Body bodyB = contact.getFixtureB().getBody();
      GameObject gameObjectA = (GameObject) bodyA.getUserData();
      GameObject gameObjectB = (GameObject) bodyB.getUserData();
      if (WeaponType.AK47.equals(gameObjectA.getType()) && !WeaponType.AK47.equals(gameObjectB.getType())) {
         context.getGameWorld().remove(gameObjectA);
         context.getGameWorld().remove(gameObjectB);
      }
      if (WeaponType.AK47.equals(gameObjectB.getType()) && !WeaponType.AK47.equals(gameObjectA.getType())) {
         context.getGameWorld().remove(gameObjectA);
         context.getGameWorld().remove(gameObjectB);
      }
   }

   @Override
   public void endContact(Contact contact) {

   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold) {

   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse) {

   }
}
