package de.bitbrain.shelter.model.weapon;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityMover;

import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class Ak47Strategy implements FireStrategy {

   private final DeltaTimer fireRateTimer = new DeltaTimer();

   private Vector3 target = new Vector3();
   private Vector2 direction = new Vector2();

   @Override
   public void fire(GameObject owner, GameContext2D context) {
      fireRateTimer.update(Gdx.graphics.getRawDeltaTime());
      if (fireRateTimer.reached(0.5f)) {
         // compute the center point
         final float radius = 4;
         final float centerX = owner.getLeft() + owner.getWidth() / 2f;
         final float centerY = owner.getTop() + owner.getHeight() / 2f;

         // compute the target point (world coordinates)
         target.x = Gdx.input.getX();
         target.y = Gdx.input.getY();
         context.getGameCamera().getInternalCamera().unproject(target);

         direction.x = target.x - centerX;
         direction.y = target.y - centerY;
         direction.setLength(radius);

         WeaponType ak74Type = WeaponType.AK47;
         GameObject bullet = context.getGameWorld().addObject();
         bullet.setType(ak74Type);
         bullet.setDimensions(1, 2);
         bullet.setAttribute("tmx_layer_index", 0);
         BodyDef bodyDef = createBodyDef(bullet);
         bodyDef.active = false;
         FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 1f, 2f);
         Body body = context.getPhysicsManager().attachBody(bodyDef, fixtureDef, bullet);
         body.setTransform(centerX + direction.x, centerY + direction.y, direction.angleRad() + (90f * MathUtils.radiansToDegrees));
         final EntityMover mover = new EntityMover(9000, context.getGameCamera());
         context.getBehaviorManager().apply(new BehaviorAdapter() {
            @Override
            public void update(GameObject source, float delta) {
               mover.move(direction);
            }
         }, bullet);
         bullet.setRotation(direction.angle());
         PointLight light = context.getLightingManager().createPointLight(64f, Color.valueOf("ffaa88"));
         context.getLightingManager().attach(light, bullet, 0f, 0f);
         fireRateTimer.reset();
      }
   }
}
