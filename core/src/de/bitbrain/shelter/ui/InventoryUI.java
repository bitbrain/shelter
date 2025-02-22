package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.weapon.WeaponType;

import javax.management.ObjectName;

public class InventoryUI extends Actor {

   private final GameObject playerGameObject;
   private final NinePatch background;
   private final GameCamera camera;
   private final Label description;

   public InventoryUI(GameObject playerGameObject, GameCamera camera) {
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
         Texture texture = SharedAssetManager.getInstance().get(weapon.getIcon(), Texture.class);
         batch.draw(texture, x, y, getWidth(), getHeight());
         description.setText(weapon.getName());
         description.setPosition(x + getWidth() + 2f, y + (getHeight() / 2f - description.getPrefHeight() / 2f) + 6.4f);
         description.draw(batch, parentAlpha);
      }
   }
}
