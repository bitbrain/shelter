package de.bitbrain.shelter.animation;

import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.HealthData;

public class AlwaysAnimationEnabler implements Enabler<GameObject> {

   @Override
   public boolean isEnabledFor(GameObject target) {
      if (target.hasAttribute(HealthData.class)) {
         return !target.getAttribute(HealthData.class).isDead();
      }
      return true;
   }
}
