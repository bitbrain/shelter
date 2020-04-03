package de.bitbrain.shelter.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.Player;

/**
 * counter-clockwise from 0-360 degrees (starting west)
 */
public class PlayerAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   private final Player player;

   public PlayerAnimationTypeResolver(Player player) {
      this.player = player;
   }
   @Override
   public Object getAnimationType(GameObject object) {
      final float angle = player.getDirection().angle();
      if (angle <= 22.5f || angle >= 337.5) {
         return AnimationTypes.PLAYER_STANDING_WEST;
      }
      if (angle > 22.5f && angle <= 67.5f) {
         return AnimationTypes.PLAYER_STANDING_SOUTH_WEST;
      }
      if (angle > 67.5f && angle <= 112.5f) {
         return AnimationTypes.PLAYER_STANDING_SOUTH;
      }
      if (angle > 112.5f && angle <= 157.5f) {
         return AnimationTypes.PLAYER_STANDING_SOUTH_EAST;
      }
      if (angle > 157.5f && angle <= 202.5f) {
         return AnimationTypes.PLAYER_STANDING_EAST;
      }
      if (angle > 202.5f && angle <= 247.5f) {
         return AnimationTypes.PLAYER_STANDING_NORTH_EAST;
      }
      if (angle > 247.5f && angle <= 292.5f) {
         return AnimationTypes.PLAYER_STANDING_NORTH;
      }
      if (angle > 292.5f && angle <= 337.5f) {
         return AnimationTypes.PLAYER_STANDING_NORTH_WEST;
      }
      return AnimationTypes.PLAYER_STANDING_SOUTH;
   }
}
