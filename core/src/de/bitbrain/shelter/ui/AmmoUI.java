package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.util.Colors;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.Ammo;
import de.bitbrain.shelter.model.weapon.WeaponType;

public class AmmoUI extends Actor {

   private final GameObject playerGameObject;
   private final NinePatch background;
   private final GameCamera camera;
   private final Label description;

   public AmmoUI(GameObject playerGameObject, GameCamera camera) {
      this.playerGameObject = playerGameObject;
      this.camera = camera;
      this.background = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_EQUIP, Texture.class), 4);
      this.description = new Label("", Styles.LABEL_DESCRIPTION);
      this.description.setFontScale(0.4f);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
      float x = camera.getPosition().x + getX() - camera.getScaledCameraWidth() / 2f;
      float y = camera.getPosition().y + getY() - camera.getScaledCameraHeight() / 2f;
      background.draw(batch, x, y, getWidth(), getHeight());

      if (playerGameObject.hasAttribute(WeaponType.class)) {
         WeaponType weapon = playerGameObject.getAttribute(WeaponType.class);
         Ammo ammo = playerGameObject.getAttribute(Ammo.class);
         Texture texture = SharedAssetManager.getInstance().get(weapon.getMunitionTexture(), Texture.class);
         batch.draw(texture, x + getWidth() / 3f, y + getHeight() / 3f, getWidth() / 3f, getHeight() / 3f);
         int remainingAmmo = ammo != null ? ammo.getAmmo() : 0;
         if (ammo != null && !ammo.isMagazineEmpty()) {
            description.setColor(Color.WHITE);
         } else {
            description.setColor(Colors.darken(Color.RED.cpy(), 0.2f));
         }
         description.setText(remainingAmmo);
         description.setPosition(x + getWidth() + 3f, y + (getHeight() / 2f - description.getPrefHeight() / 2f) + 3f);
         description.draw(batch, parentAlpha);
      }
   }
}
