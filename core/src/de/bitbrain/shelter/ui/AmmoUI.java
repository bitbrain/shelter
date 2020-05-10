package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.util.Colors;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.core.model.Ammo;
import de.bitbrain.shelter.core.weapon.WeaponType;

public class AmmoUI extends Actor {

   private final GameObject playerGameObject;
   private final NinePatch background;
   private final GameCamera camera;
   private final Label description;
   private final TextureRegion iconTexture;

   public AmmoUI(GameObject playerGameObject, GameCamera camera) {
      this.playerGameObject = playerGameObject;
      this.camera = camera;
      this.background = GraphicsFactory.createNinePatch(Asset.get(Assets.Textures.UI_EQUIP, Texture.class), 4);
      this.description = new Label("", Styles.LABEL_DESCRIPTION);
      this.description.setFontScale(0.4f);
      setZIndex(1);
      this.iconTexture = new TextureRegion(Asset.get(Assets.Textures.ITEMS_SPRITESHEET, Texture.class), 0, 9, 9, 9);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
      float x = camera.getPosition().x + getX() - camera.getScaledCameraWidth() / 2f;
      float y = camera.getPosition().y + getY() - camera.getScaledCameraHeight() / 2f;
      background.draw(batch, x, y, getWidth(), getHeight());

      if (playerGameObject.hasAttribute(WeaponType.class)) {
         Ammo ammo = playerGameObject.getAttribute(Ammo.class);
         batch.draw(iconTexture, x + 3.5f, y + 3.5f, 9f, 9f);
         int remainingAmmo = ammo != null ? ammo.getAmmo() : 0;
         if (ammo != null && !ammo.isMagazineEmpty()) {
            description.setColor(Color.WHITE);
         } else {
            description.setColor(Colors.darken(Color.RED.cpy(), 0.2f));
         }
         description.setText(remainingAmmo);
         description.setPosition(x + getWidth() + 2f, y + (getHeight() / 2f - description.getPrefHeight() / 2f) + 6.4f);
         description.draw(batch, parentAlpha);
      }
   }
}
