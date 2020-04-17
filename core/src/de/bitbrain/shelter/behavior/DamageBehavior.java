package de.bitbrain.shelter.behavior;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.core.entities.EntityMover;
import de.bitbrain.shelter.core.items.Item;
import de.bitbrain.shelter.core.model.HealthData;
import de.bitbrain.shelter.core.model.MaterialType;
import de.bitbrain.shelter.core.weapon.WeaponType;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 impactDirection;
   private final GameObject damageDealer;
   private final GameContext2D context;
   private final JukeBox zombieHitSounds;

   public DamageBehavior(Vector2 impactDirection, GameObject damageDealer, GameContext2D context) {
      this.impactDirection = impactDirection;
      this.damageDealer = damageDealer;
      this.context = context;
      this.zombieHitSounds = new JukeBox(context.getAudioManager(), 200,
            Assets.Sounds.ZOMBIE_HIT_01,
            Assets.Sounds.ZOMBIE_HIT_02,
            Assets.Sounds.ZOMBIE_HIT_03,
            Assets.Sounds.ZOMBIE_HIT_04);
   }

   @Override
   public void update(GameObject source, float delta) {
      if (damageDealer.hasAttribute("eligible_for_removal")) {
         context.getGameWorld().remove(damageDealer.getId());
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
   }

   private void dealDamage(final GameObject target) {
      if (!target.hasAttribute(HealthData.class) && !target.hasAttribute(MaterialType.class)) {
         return;
      }
      if (target.equals(damageDealer.getAttribute("owner"))) {
         return;
      }
      GameObject owner = (GameObject) damageDealer.getAttribute("owner");
      if (owner.getType() == null || owner.getType().equals(target.getType())) {
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