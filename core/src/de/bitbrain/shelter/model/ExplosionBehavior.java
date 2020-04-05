package de.bitbrain.shelter.model;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.*;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;

import static de.bitbrain.shelter.ThemeColors.AMBIENT;

public class ExplosionBehavior extends BehaviorAdapter {

   private final Vector2 tmp = new Vector2();

   private final GameContext2D context;

   private final float explosionRadius;
   private final int explosionDamage;

   private boolean triggered = false;

   public ExplosionBehavior(float explosionRadius, int explosionDamage, GameContext2D context) {
      this.explosionRadius = explosionRadius;
      this.explosionDamage = explosionDamage;
      this.context = context;
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      if (source.getAttribute(HealthData.class).isDead()) {
         triggerExplosion(source);
      }
   }

   private void triggerExplosion(final GameObject barrel) {
      if (triggered) {
         return;
      }
      triggered = true;
      context.getGameCamera().shake(5f, 2f);
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            for (GameObject o : context.getGameWorld().getObjects()) {
               // check if object is in range
               final float range = tmp.set(o.getPosition()).sub(barrel.getPosition()).len();
               System.out.println(range);
               if (range <= explosionRadius && o.hasAttribute(HealthData.class)) {
                  o.getAttribute(HealthData.class).reduceHealth(explosionDamage);
               }
            }
         }
      }).delay(0.5f).start(SharedTweenManager.getInstance());
      Tween.to(barrel, GameObjectTween.ALPHA, 0.5f)
            .target(0f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  context.getGameWorld().remove(barrel);
               }
            }).start(SharedTweenManager.getInstance());
      final PointLight light = context.getLightingManager().createPointLight(explosionRadius * 3f, Color.valueOf("ff6f00"));
      light.setPosition(barrel.getLeft(), barrel.getTop());
      barrel.getAttribute(Body.class).setActive(false);
      Tween.to(light, PointLight2DTween.COLOR_A, 2f)
            .target(0f)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  context.getLightingManager().destroyLight(light);
               }
            })
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .start(SharedTweenManager.getInstance());
      context.getParticleManager().spawnEffect(Assets.Particles.EXPLOSION, barrel.getLeft() + barrel.getWidth() / 2f, barrel.getTop() + barrel.getHeight() / 2f);
   }
}
