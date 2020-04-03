package de.bitbrain.shelter.animation;

import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;

public class AlwaysAnimationEnabler implements Enabler<GameObject> {

   @Override
   public boolean isEnabledFor(GameObject target) {
      return true;
   }
}
