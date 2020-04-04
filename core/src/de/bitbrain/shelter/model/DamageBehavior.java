package de.bitbrain.shelter.model;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;

public class DamageBehavior extends BehaviorAdapter {

   private final Vector2 bulletDirection;
   private final GameObject bullet;
   private final GameContext2D context;

   public DamageBehavior(Vector2 bulletDirection, GameObject bullet, GameContext2D context) {
      this.bulletDirection = bulletDirection;
      this.bullet = bullet;
      this.context = context;
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      super.update(source, target, delta);
      if ("PLAYER".equals(source.getType()) || "PLAYER".equals(target.getType())) {
         return;
      }
      if (source.collidesWith(target) && !target.getType().equals(source.getType())) {
         if (source.getId().equals(bullet.getId())) {
            dealDamage(source, target);
         }
         if (target.getId().equals(bullet.getId())) {
            dealDamage(target, source);
         }
      }
   }

   private void dealDamage(GameObject damageDealer, GameObject target) {
      EntityMover mover = target.getAttribute(EntityMover.class);
      mover.move(bulletDirection, 1900f);
      context.getGameWorld().remove(damageDealer);
      context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_IMPACT, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
      target.setColor(Color.RED);
      TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 0.5f);
   }
}