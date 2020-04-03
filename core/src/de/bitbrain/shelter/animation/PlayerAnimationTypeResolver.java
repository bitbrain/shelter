package de.bitbrain.shelter.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.Movement;

/**
 * counter-clockwise from 0-360 degrees (starting west)
 */
public class PlayerAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   @Override
   public Object getAnimationType(GameObject object) {
      if (object.hasAttribute(Movement.class)) {
         final Movement movement = (Movement)object.getAttribute(Movement.class);
         final float angle = movement.getLookDirection().angle();
         if (angle <= 22.5f || angle >= 337.5) {
            return AnimationTypes.STANDING_WEST;
         }
         if (angle > 22.5f && angle <= 67.5f) {
            return AnimationTypes.STANDING_SOUTH_WEST;
         }
         if (angle > 67.5f && angle <= 112.5f) {
            return AnimationTypes.STANDING_SOUTH;
         }
         if (angle > 112.5f && angle <= 157.5f) {
            return AnimationTypes.STANDING_SOUTH_EAST;
         }
         if (angle > 157.5f && angle <= 202.5f) {
            return AnimationTypes.STANDING_EAST;
         }
         if (angle > 202.5f && angle <= 247.5f) {
            return AnimationTypes.STANDING_NORTH_EAST;
         }
         if (angle > 247.5f && angle <= 292.5f) {
            return AnimationTypes.STANDING_NORTH;
         }
         if (angle > 292.5f && angle <= 337.5f) {
            return AnimationTypes.STANDING_NORTH_WEST;
         }
      }
      return AnimationTypes.STANDING_SOUTH;
   }
}
