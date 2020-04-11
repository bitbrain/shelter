package de.bitbrain.shelter.model.weapon;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.*;

import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class DefaultWeaponFireStrategy implements FireStrategy {

   private final DeltaTimer fireRateTimer = new DeltaTimer();

   private Vector3 target = new Vector3();

   public DefaultWeaponFireStrategy() {
      fireRateTimer.update(0.1f);
   }

   @Override
   public void fire(GameObject owner, final GameContext2D context, EntityFactory entityFactory) {
      if (owner.hasAttribute(HealthData.class) && owner.getAttribute(HealthData.class).isDead()) {
         return;
      }
      final WeaponType weaponType = owner.getAttribute(WeaponType.class);
      fireRateTimer.update(Gdx.graphics.getRawDeltaTime());
      Ammo ammo = owner.getAttribute(Ammo.class);
      if (fireRateTimer.reached(weaponType.getSpeed())) {
         if (ammo == null || ammo.isMagazineEmpty()) {
            fireRateTimer.reset();
            Sound sound = SharedAssetManager.getInstance().get(Assets.Sounds.GUN_EMPTY, Sound.class);
            sound.play(0.3f, (float) (0.95f + Math.random() * 0.1), 0f);
            return;
         }
         ammo.reduceAmmo();
         Sound sound = SharedAssetManager.getInstance().get(weaponType.getShootSoundFx(), Sound.class);
         sound.play(0.2f, (float) (0.95f + Math.random() * 0.1), 0f);
         final Vector2 direction = new Vector2();
         // compute the center point
         final float radius = 6f;
         final float centerX = owner.getLeft() + owner.getWidth() / 2f;
         final float centerY = owner.getTop() + owner.getHeight() / 2f;

         // compute the target point (world coordinates)
         target.x = Gdx.input.getX();
         target.y = Gdx.input.getY();

         // Apply error rate
         target.x += -4f + Math.random() * 8f;
         target.y += -4f + Math.random() * 8f;

         context.getGameCamera().getInternalCamera().unproject(target);

         direction.x = target.x - centerX;
         direction.y = target.y - centerY;
         direction.setLength(radius);

         final GameObject bullet = entityFactory.addBullet(weaponType, centerX + direction.x, centerY + direction.y, direction);
         owner.setAttribute("lastBullet", bullet);
         bullet.setAttribute("owner", owner);
         final EntityMover mover = new EntityMover(600, context.getGameCamera(), context.getAudioManager());
         context.getBehaviorManager().apply(new DamageBehavior(direction, bullet, context, entityFactory) {
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
         fireRateTimer.reset();
      }
   }
}
