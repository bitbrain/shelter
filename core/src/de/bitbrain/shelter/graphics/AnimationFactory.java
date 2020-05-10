package de.bitbrain.shelter.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.shelter.animation.AnimationTypes;

public class AnimationFactory {

   public static AnimationSpriteSheet buildSheet(String texturePath, int tileSize) {
      final Texture playerTexture = Asset.get(texturePath, Texture.class);
      return new AnimationSpriteSheet(playerTexture, tileSize);
   }

   public static AnimationConfig buildAnimationConfig(float interval) {
      return AnimationConfig.builder()
            .registerFrames(AnimationTypes.STANDING_SOUTH, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 0)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_SOUTH_WEST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 1)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_WEST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 2)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_NORTH_WEST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 3)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_NORTH, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 4)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_NORTH_EAST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 5)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_EAST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 6)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .registerFrames(AnimationTypes.STANDING_SOUTH_EAST, AnimationFrames.builder()
                  .resetIndex(0)
                  .duration(interval)
                  .origin(0, 7)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .playMode(Animation.PlayMode.LOOP)
                  .frames(8)
                  .randomOffset()
                  .build())
            .build();
   }
}
