package de.bitbrain.shelter.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.model.items.Item;
import de.bitbrain.shelter.model.items.LootTable;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 bulletDirection;
   private final GameObject bullet;
   private final GameContext2D context;
   private final EntityFactory entityFactory;
   private final JukeBox zombieHitSounds, impactSounds, zombieDeathSounds;

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
      this.impactSounds = new JukeBox(context.getAudioManager(), 200,
            Assets.Sounds.IMPACT_01,
            Assets.Sounds.IMPACT_02,
            Assets.Sounds.IMPACT_03);
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
      if (healthData != null && healthData.isDead() && entityMover != null) {
         if ("PLAYER".equals(target.getType())) {
            SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(8f, 1f, 0f);
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
               entityFactory.addItem(target.getLeft(), target.getTop(),item);
            }
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
         impactSounds.playSound(target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
         EntityMover mover = target.getAttribute(EntityMover.class);
         if (mover != null) {
            mover.move(bulletDirection, 1900f);
            context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_IMPACT, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
            target.setColor(Color.RED);
            TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 0.5f);
         }
         if (!("ZOMBIE".equals(damageDealer.getType()) && "BARREL".equals(target.getType()))) {
            healthData.reduceHealth(5);
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