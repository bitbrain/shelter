package de.bitbrain.shelter.model.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.*;

public class LongRangeAttackStrategy implements AttackStrategy {

   private final DeltaTimer attackRateTimer = new DeltaTimer();

   public LongRangeAttackStrategy() {
      attackRateTimer.update(100f);
   }

   @Override
   public void attack(GameObject owner, final GameContext2D context, EntityFactory entityFactory) {
      if (owner.hasAttribute(HealthData.class) && owner.getAttribute(HealthData.class).isDead()) {
         return;
      }
      final WeaponType weaponType = owner.getAttribute(WeaponType.class);
      Ammo ammo = owner.getAttribute(Ammo.class);
      if (attackRateTimer.reached(weaponType.getSpeed())) {
         if (ammo == null || ammo.isMagazineEmpty()) {
            attackRateTimer.reset();
            Sound sound = SharedAssetManager.getInstance().get(Assets.Sounds.GUN_EMPTY, Sound.class);
            sound.play(0.3f, (float) (0.95f + Math.random() * 0.1), 0f);
            return;
         }
         ammo.reduceAmmo();
         Sound sound = SharedAssetManager.getInstance().get(weaponType.getAttackSoundFx(), Sound.class);
         sound.play(0.2f, (float) (0.95f + Math.random() * 0.1), 0f);
         final Vector2 direction = new Vector2();
         // compute the center point
         final float radius = 6f;
         final float centerX = owner.getLeft() + owner.getWidth() / 2f;
         final float centerY = owner.getTop() + owner.getHeight() / 2f;

         direction.set(1f, 0f);
         if (owner.hasAttribute(EntityMover.class)) {
            direction.setAngle(owner.getAttribute(EntityMover.class).getLookDirection().angle() - 180f);
         }
         direction.setLength(radius);

         final GameObject bullet = entityFactory.addBullet(weaponType, centerX + direction.x, centerY + direction.y, direction);
         owner.setAttribute("lastBullet", bullet);
         bullet.setAttribute("owner", owner);
         final EntityMover mover = new EntityMover(600, context.getGameCamera(), context.getAudioManager());
         context.getBehaviorManager().apply(new DamageBehavior(direction, bullet, context) {
            @Override
            public void onAttach(GameObject source) {
               mover.onAttach(source);
               super.onAttach(source);
            }

            @Override
            public void update(GameObject source, float delta) {
               mover.move(direction);
               mover.update(source, delta);
               super.update(source, delta);
            }
         }, bullet);
         attackRateTimer.reset();
      }
   }

   @Override
   public void update(float delta) {
      attackRateTimer.update(Gdx.graphics.getRawDeltaTime());
   }
}
