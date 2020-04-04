package de.bitbrain.shelter.model.weapon;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.EntityMover;

import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class Ak47Strategy implements FireStrategy {

   private final DeltaTimer fireRateTimer = new DeltaTimer();

   private Vector3 target = new Vector3();

   public  Ak47Strategy() {
      fireRateTimer.update(0.2f);
   }

   @Override
   public void fire(GameObject owner, final GameContext2D context) {
      fireRateTimer.update(Gdx.graphics.getRawDeltaTime());
      if (fireRateTimer.reached(0.2f)) {

         final Vector2 direction = new Vector2();
         // compute the center point
         final float radius = 14;
         final float centerX = owner.getLeft() + owner.getWidth() / 2f;
         final float centerY = owner.getTop() + owner.getHeight() / 2f;

         // compute the target point (world coordinates)
         target.x = Gdx.input.getX();
         target.y = Gdx.input.getY();
         context.getGameCamera().getInternalCamera().unproject(target);

         direction.x = target.x - centerX;
         direction.y = target.y - centerY;
         direction.setLength(radius);

         final WeaponType ak74Type = WeaponType.AK47;
         final GameObject bullet = context.getGameWorld().addObject(new Mutator<GameObject>() {
            @Override
            public void mutate(GameObject target) {
               target.setType(ak74Type);
               target.setPosition(centerX + direction.x, centerY + direction.y);
               target.setRotation(direction.angle());
               target.setDimensions(2, 4);
               target.setAttribute("tmx_layer_index", 0);
            }
         });
         BodyDef bodyDef = createBodyDef(bullet);
         bodyDef.active = false;
         FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 4f, 2f);
         Body body = context.getPhysicsManager().attachBody(bodyDef, fixtureDef, bullet);
         body.setTransform(centerX + direction.x, centerY + direction.y, direction.angleRad() + (90f * MathUtils.radiansToDegrees));
         final EntityMover mover = new EntityMover(400, context.getGameCamera());
         context.getBehaviorManager().apply(new BehaviorAdapter() {

            @Override
            public void onAttach(GameObject source) {
               super.onAttach(source);
               mover.onAttach(source);
            }

            @Override
            public void update(GameObject source, float delta) {
               mover.move(direction);
               mover.update(source, delta);
            }

            @Override
            public void update(GameObject source, GameObject target, float delta) {
               super.update(source, target, delta);
               if (source.collidesWith(target) && !target.getType().equals(source.getType())) {
                  if (source.getId().equals(bullet.getId())) {
                     context.getGameWorld().remove(source);
                     context.getGameWorld().remove(target);
                     context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_EXPLOSION, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
                  }
                  if (target.getId().equals(bullet.getId())) {
                     context.getGameWorld().remove(source);
                     context.getGameWorld().remove(target);
                     context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_EXPLOSION, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
                  }
               }
            }
         }, bullet);
         bullet.setRotation(direction.angle() - 90f);
         PointLight light = context.getLightingManager().createPointLight(135f, Color.valueOf("ffaa8855"));
         light.setPosition(bullet.getLeft(), bullet.getTop());
         context.getLightingManager().attach(light, bullet, 1f, 1f);
         fireRateTimer.reset();
      }
   }
}
