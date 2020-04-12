package de.bitbrain.shelter.model.weapon;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.DamageBehavior;
import de.bitbrain.shelter.model.EntityFactory;
import de.bitbrain.shelter.model.EntityMover;
import de.bitbrain.shelter.model.HealthData;

public class CloseAttackStrategy implements AttackStrategy {

   private final DeltaTimer attackRateTimer = new DeltaTimer();

   @Override
   public void attack(GameObject owner, final GameContext2D context, EntityFactory entityFactory) {
      if (owner.hasAttribute(HealthData.class) && owner.getAttribute(HealthData.class).isDead()) {
         return;
      }
      final WeaponType weaponType = owner.getAttribute(WeaponType.class);
      if (attackRateTimer.reached(weaponType.getSpeed())) {
         final Vector2 direction = new Vector2();
         attackRateTimer.reset();
         float centerX = owner.getLeft() + owner.getWidth() / 2f;
         float centerY = owner.getTop() + owner.getHeight() / 2f;
         final float radius = 8f;

         direction.set(1f, 0f);
         if (owner.hasAttribute(EntityMover.class)) {
            direction.setAngle(owner.getAttribute(EntityMover.class).getLookDirection().angle() - 180f);
         }
         direction.setLength(radius);
         final GameObject telegraph = entityFactory.addDamageTelegraph(
               weaponType,
               centerX + direction.x,
               centerY + direction.y,
               weaponType.getDamage().getImpactWidth(),
               weaponType.getDamage().getImpactHeight(),
               owner.getRotation()
         );
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               context.getGameWorld().remove(telegraph);
            }
         }).delay(0.1f)
           .start(SharedTweenManager.getInstance());
         context.getBehaviorManager().apply(new DamageBehavior(direction, telegraph, context), telegraph);
         telegraph.setAttribute("owner", owner);
      }
   }

   @Override
   public void update(float delta) {
      attackRateTimer.update(delta);
   }
}
