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
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.HealthData;

public class HealthBar extends Actor {

   private final HealthData healthData;
   private final NinePatch background;
   private final NinePatch foreground;
   private final GameCamera camera;
   private final Label description;

   public HealthBar(HealthData healthData, GameCamera camera) {
      this.healthData = healthData;
      this.camera = camera;
      this.background = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_HEALTH, Texture.class), 4);
      this.foreground = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_HEALTH_ACTIVE, Texture.class), 4);
      description = new Label("health", Styles.LABEL_DESCRIPTION);
      description.setFontScale(0.35f);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
      float x = camera.getPosition().x + getX() - camera.getScaledCameraWidth() / 2f;
      float y = camera.getPosition().y + getY() - camera.getScaledCameraHeight() / 2f;
      background.draw(batch, x, y, getWidth(), getHeight());
      if (healthData.getHealthPercentage() > 0.1) {
         foreground.draw(batch, x, y, getWidth(), getHeight() * healthData.getHealthPercentage());
      }
      description.setPosition(x - 9f, y - 14f);
      description.draw(batch, parentAlpha);
   }
}
