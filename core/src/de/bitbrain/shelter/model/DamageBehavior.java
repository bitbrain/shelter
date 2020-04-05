package de.bitbrain.shelter.model;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.items.Item;
import de.bitbrain.shelter.model.items.LootTable;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 bulletDirection;
   private final GameObject bullet;
   private final GameContext2D context;
   private final EntityFactory entityFactory;

   public DamageBehavior(Vector2 bulletDirection, GameObject bullet, GameContext2D context, EntityFactory entityFactory) {
      this.bulletDirection = bulletDirection;
      this.bullet = bullet;
      this.context = context;
      this.entityFactory = entityFactory;
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      super.update(source, target, delta);
      if (source.collidesWith(target) && !target.getType().equals(source.getType())) {
         if (source.getId().equals(bullet.getId())) {
            dealDamage(source, target);
         }
         if (target.getId().equals(bullet.getId())) {
            dealDamage(target, source);
         }
      }
   }

   private void dealDamage(GameObject damageDealer, final GameObject target) {
      if (!damageDealer.hasAttribute(HealthData.class)) {
         context.getGameWorld().remove(damageDealer);
      }
      if (target.hasAttribute(HealthData.class)) {
         HealthData healthData = target.getAttribute(HealthData.class);
         if (healthData.isDead()) {
            // RIP
            return;
         }
         EntityMover mover = target.getAttribute(EntityMover.class);
         mover.move(bulletDirection, 1900f);
         context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_IMPACT, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
         target.setColor(Color.RED);
         TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 0.5f);
         healthData.reduceHealth(5);
         if ("PLAYER".equals(target.getType())) {
            context.getGameCamera().shake(0.2f, 0.2f);
         }
         if (healthData.isDead()) {
            if ("PLAYER".equals(target.getType())) {
               SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(8f, 1f, 0f);
               return;
            }
            target.setActive(false);
            Color targetColor = Color.BLUE.cpy();
            targetColor.a = 0f;
            TweenUtils.toColor(target.getColor(), targetColor, 0.7f);
            Tween.to(target, GameObjectTween.SCALE_Y, 0.7f)
                  .target(10f)
                  .ease(TweenEquations.easeInOutCubic)
                  .start(SharedTweenManager.getInstance());
            Tween.to(target, GameObjectTween.OFFSET_Y, 0.7f)
                  .target(40f)
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .ease(TweenEquations.easeInOutCubic)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> source) {
                        context.getGameWorld().remove(target);
                     }
                  })
                  .ease(TweenEquations.easeInOutCubic)
                  .start(SharedTweenManager.getInstance());
            context.getBehaviorManager().remove(target);
            context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_EXPLOSION, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);

            // Drop an item if we can
            if (target.hasAttribute(LootTable.class)) {
               LootTable lootTable = target.getAttribute(LootTable.class);
               Item item = lootTable.drop();
               if (item != null) {
                  entityFactory.addItem(target.getLeft(), target.getTop(),item);
               }
            }
         }
      }
   }
}