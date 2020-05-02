package de.bitbrain.shelter.behavior;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.core.model.HealthData;

public class RadioactivityBehavior extends BehaviorAdapter {

   private final float TICK_INTERVAL = 0.25f;
   private final Vector2 distance = new Vector2();

   private final DeltaTimer attackRateTimer = new DeltaTimer();

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      attackRateTimer.update(delta);
   }

   @Override
   public void onDetach(GameObject source) {
      super.onDetach(source);
   }

   @Override
   public void onStatusChange(GameObject source, boolean updateable) {
      super.onStatusChange(source, updateable);
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      super.update(source, target, delta);
      final float minRange = 160f;
      if (attackRateTimer.reached(TICK_INTERVAL)) {
         attackRateTimer.reset();
         if ("PLAYER".equals(target.getType())) {
            distance.x = (target.getLeft() + target.getWidth() / 2f) - (source.getLeft() + source.getWidth() / 2f);
            distance.y = (target.getTop() + target.getHeight() / 2f) - (source.getTop() + source.getHeight() / 2f);
            if (distance.len() <= minRange) {
               HealthData data = target.getAttribute(HealthData.class);
               final int damage = (int) Math.round((float) data.getTotalHealth() * (50f / Math.pow(distance.len(), 2f)));
               data.reduceHealth(damage);
            }
         }
      }
   }
}
