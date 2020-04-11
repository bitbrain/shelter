package de.bitbrain.shelter.model;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import box2dLight.Light;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.PointLight2DTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.model.items.Item;
import de.bitbrain.shelter.model.items.LootTable;
import de.bitbrain.shelter.model.weapon.WeaponType;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 bulletDirection;
   private final GameObject bullet;
   private final GameContext2D context;
   private final EntityFactory entityFactory;
   private final JukeBox zombieHitSounds, zombieDeathSounds;

   public DamageBehavior(Vector2 bulletDirection, GameObject bullet, GameContext2D context, EntityFactory entityFactory) {
      this.bulletDirection = bulletDirection;
      this.bullet = bullet;
      this.context = context;
      this.entityFactory = entityFactory;
      this.zombieHitSounds = new JukeBox(context.getAudioManager(), 200,
            Assets.Sounds.ZOMBIE_HIT_01,
            Assets.Sounds.ZOMBIE_HIT_02,
            Assets.Sounds.ZOMBIE_HIT_03,
            Assets.Sounds.ZOMBIE_HIT_04);
      this.zombieDeathSounds = new JukeBox(context.getAudioManager(), 200,
            Assets.Sounds.ZOMBIE_DEATH_01,
            Assets.Sounds.ZOMBIE_DEATH_02);
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
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
      healthcheck(target);
   }

   private void healthcheck(GameObject target) {
      HealthData healthData = target.getAttribute(HealthData.class);
      EntityMover entityMover = target.getAttribute(EntityMover.class);
      if (healthData != null && healthData.isDead() && entityMover != null && target.isActive()) {
         if ("PLAYER".equals(target.getType())) {
            SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(0.8f, 1f, 0f);
            target.setActive(false);
            return;
         }
         if ("ZOMBIE".equals(target.getType())) {
            zombieDeathSounds.playSound(target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
         }
         target.setActive(false);
         Color targetColor = Color.BLUE.cpy();
         targetColor.a = 0f;
         TweenUtils.toColor(target.getColor(), targetColor, 0.7f);
         context.getBehaviorManager().remove(target);
         float randomX = (float) (target.getWidth() * 2f * Math.random());
         float randomY = (float) (target.getHeight() * 2f * Math.random());
         context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_EXPLOSION, target.getLeft() + randomX - target.getWidth() / 2f, target.getTop() + randomY + target.getHeight() / 2f);

         // Drop an item if we can
         if (target.hasAttribute(LootTable.class)) {
            LootTable lootTable = target.getAttribute(LootTable.class);
            Item item = lootTable.drop();
            if (item != null) {
               entityFactory.addItem(target.getLeft(), target.getTop(), item);
            }
         }
      }
   }

   private void dealDamage(GameObject damageDealer, final GameObject target) {
      if (target.getType() instanceof Item) {
         // To not deal damage on items but ignore them
         return;
      }
      if (!damageDealer.hasAttribute(HealthData.class)) {
         context.getGameWorld().remove(damageDealer);
      }
      if (damageDealer.getType() instanceof WeaponType) {
         WeaponType type = (WeaponType) damageDealer.getType();
         float impactX = damageDealer.getLeft() + damageDealer.getWidth() / 2f;
         float impactY = damageDealer.getTop() + damageDealer.getHeight() / 2f;
         context.getParticleManager().spawnEffect(type.getImpactParticleFx(), impactX, impactY);
         final Light light = context.getLightingManager().createPointLight(32f, Color.valueOf("ffaa8855"));
         light.setPosition(impactX, impactY);
         Tween.to(light, PointLight2DTween.COLOR_A, 0.1f)
               .target(0f)
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> source) {
                     context.getLightingManager().destroyLight(light);
                  }
               }).start(SharedTweenManager.getInstance());
      }
      if (target.hasAttribute(MaterialType.class)) {
         final JukeBox impactSound = target.getAttribute(MaterialType.class).getImpactSoundFx(context.getAudioManager());
         impactSound.playSound(target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
      }
      if (target.hasAttribute(HealthData.class)) {
         HealthData healthData = target.getAttribute(HealthData.class);
         if (healthData.isDead()) {
            // RIP
            return;
         }
         EntityMover mover = target.getAttribute(EntityMover.class);
         if (mover != null) {
            mover.move(bulletDirection, 1900f);
            context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_IMPACT, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
            target.setColor(Color.RED);
            TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 0.5f);
         }
         if (!("ZOMBIE".equals(damageDealer.getType()) && "BARREL".equals(target.getType())) && damageDealer.getType() instanceof WeaponType) {
            healthData.reduceHealth(((WeaponType) damageDealer.getType()).getDamage().get());
         }

         if ("PLAYER".equals(target.getType())) {
            context.getGameCamera().shake(0.2f, 0.2f);
         }
         if ("ZOMBIE".equals(target.getType())) {
            zombieHitSounds.playSound(target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
         }
      }
   }
}