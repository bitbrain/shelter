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
import de.bitbrain.shelter.model.weapon.WeaponType;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 impactDirection;
   private final GameObject damageDealer;
   private final GameContext2D context;
   private final EntityFactory entityFactory;
   private final JukeBox zombieHitSounds, zombieDeathSounds;

   public DamageBehavior(Vector2 impactDirection, GameObject damageDealer, GameContext2D context, EntityFactory entityFactory) {
      this.impactDirection = impactDirection;
      this.damageDealer = damageDealer;
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
      if (damageDealer.hasAttribute("eligible_for_removal")) {
         context.getGameWorld().remove(damageDealer);
      }
      super.update(source, delta);
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      super.update(source, target, delta);
      if (damageDealer.collidesWith(target) && !target.getId().equals(source.getId())) {
         if (source.getId().equals(damageDealer.getId())) {
            dealDamage(target);
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

   private void dealDamage(final GameObject target) {
      if (target.equals(damageDealer.getAttribute("owner"))) {
         return;
      }
      GameObject owner = (GameObject) damageDealer.getAttribute("owner");
      if (owner.getType().equals(target.getType())) {
         return;
      }
      if (target.getType() instanceof Item) {
         // To not deal damage on items but ignore them
         return;
      }
      damageDealer.setAttribute("eligible_for_removal", true);
      if (damageDealer.getType() instanceof WeaponType) {
         WeaponType type = (WeaponType) damageDealer.getType();
         float impactX = damageDealer.getLeft() + damageDealer.getWidth() / 2f;
         float impactY = damageDealer.getTop() + damageDealer.getHeight() / 2f;
         context.getParticleManager().spawnEffect(type.getImpactParticleFx(), impactX, impactY);
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
            mover.move(impactDirection, 1900f);
         }
         if (damageDealer.getType() instanceof WeaponType) {
            healthData.reduceHealth(((WeaponType) damageDealer.getType()).getDamage().get());
            target.setColor(Color.RED);
            TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 0.5f);
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