package de.bitbrain.shelter.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.Ammo;
import de.bitbrain.shelter.model.weapon.WeaponType;

public class BulletRenderer extends SpriteRenderer {

   private final AnimationRenderer shootOverlay;

   public BulletRenderer(WeaponType type) {
      super(SharedAssetManager.getInstance().get(type.getWeaponTexture(), Texture.class));
      Texture overlayTexture = SharedAssetManager.getInstance().get(Assets.Textures.SHOOT_OVERLAY_SPRITESHEET, Texture.class);
      AnimationSpriteSheet overlaySheet = new AnimationSpriteSheet(overlayTexture, 16);
      this.shootOverlay = new AnimationRenderer(overlaySheet, AnimationConfig.builder()
            .registerFrames(AnimationFrames.builder()
                  .playMode(Animation.PlayMode.LOOP_RANDOM)
                  .origin(0, 0)
                  .frames(8)
                  .resetIndex(0)
                  .duration(0.1f)
                  .direction(AnimationFrames.Direction.HORIZONTAL)
                  .build())
            .build())
            .size(16, 16)
            .offset(-6, -6);
      size(1, 14);
      origin(0, 0);
      rotationalOffset(0f, -14);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      super.render(object, batch, delta);
      if (object.hasAttribute("owner")) {
         GameObject owner = (GameObject) object.getAttribute("owner");
         if (!owner.hasAttribute("lastBullet")) {
            return;
         }
         if (!owner.getAttribute("lastBullet").equals(object)) {
            return;
         }
         Ammo ammo = owner.getAttribute(Ammo.class);
         shootOverlay.enabled(ammo.getAmmo() < owner.getAttribute("previousMunitionCount", 0));
         Color color = object.getColor();
         object.setColor(1f, 1f, 1f, 1f);
         shootOverlay.render(object, batch, delta);
         object.setColor(color);
         owner.setAttribute("previousMunitionCount", ammo.getAmmo());
      }
   }
}
