package de.bitbrain.shelter.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.animation.AlwaysAnimationEnabler;
import de.bitbrain.shelter.animation.PlayerAnimationTypeResolver;
import de.bitbrain.shelter.model.Ammo;
import de.bitbrain.shelter.model.HealthData;
import de.bitbrain.shelter.model.weapon.WeaponType;

import java.util.HashMap;
import java.util.Map;

import static de.bitbrain.shelter.graphics.AnimationFactory.buildAnimationConfig;
import static de.bitbrain.shelter.graphics.AnimationFactory.buildSheet;

public class EntityAnimationRenderer extends AnimationRenderer {

   private Map<WeaponType, AnimationRenderer> currentlyEquippedItemRenderer = new HashMap<WeaponType, AnimationRenderer>();
   private final float interval;

   public EntityAnimationRenderer(String sheetPath, float interval) {
      super(buildSheet(sheetPath, 32), buildAnimationConfig(interval), new PlayerAnimationTypeResolver(), new AlwaysAnimationEnabler());
      this.interval = interval;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      Texture shadow = SharedAssetManager.getInstance().get(Assets.Textures.SHADOW, Texture.class);
      if (!object.hasAttribute(HealthData.class) || !object.getAttribute(HealthData.class).isDead()) {
         batch.draw(shadow, object.getLeft() + object.getOffsetX(), object.getTop() + object.getOffsetY(),
               object.getWidth() * object.getScaleX(), object.getHeight() * object.getScaleY());
      }
      super.render(object, batch, delta);
      if (object.hasAttribute(WeaponType.class) && object.getAttribute(WeaponType.class).getTileset() != null) {
         WeaponType type = object.getAttribute(WeaponType.class);
         AnimationRenderer renderer = currentlyEquippedItemRenderer.get(type);
         if (renderer == null) {
            AnimationSpriteSheet sheet = buildSheet(type.getTileset(), 32);
            renderer = new AnimationRenderer(sheet, buildAnimationConfig(interval), new PlayerAnimationTypeResolver(), new AlwaysAnimationEnabler());
            currentlyEquippedItemRenderer.put(type, renderer);
         }
         renderer.render(object, batch, delta);
      }
   }
}
