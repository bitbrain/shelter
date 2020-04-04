package de.bitbrain.shelter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.shelter.i18n.Bundle;
import de.bitbrain.shelter.screens.IngameScreen;
import de.bitbrain.shelter.ui.Styles;
import org.apache.commons.lang.SystemUtils;

import static de.bitbrain.shelter.Assets.Textures.CROSSHAIR;

public class ShelterGame extends BrainGdxGame {

   private enum ScreenMode {
      SCREENSHOT,
      GIF,
      FULLSCREEN
   }

   private ScreenMode screenMode;

   public ShelterGame(String[] args) {
      screenMode = ScreenMode.FULLSCREEN;
      for (String arg : args) {
         if (arg.equals("screenshot")) {
            screenMode = ScreenMode.SCREENSHOT;
         }
         if (arg.equals("gif")) {
            screenMode = ScreenMode.GIF;
         }
      }
   }

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?, ?> getInitialScreen() {
      Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(CROSSHAIR)), 32, 32));
      configureSettings();
      Bundle.load();
      Styles.init();
      return new IngameScreen(this);
   }

   private void configureSettings() {
      GameSettings settings = new GameSettings(new GameEventManagerImpl());
      GraphicsSettings graphicsSettings = settings.getGraphics();
      if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
         if (screenMode == ScreenMode.SCREENSHOT) {
            Gdx.graphics.setWindowedMode(3840, 2160);
         } else if (screenMode == ScreenMode.GIF) {
            Gdx.graphics.setWindowedMode(1248, 770);
         } else if (SystemUtils.IS_OS_WINDOWS) {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
         } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
         }
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.High);
         graphicsSettings.setRenderScale(0.3f);
         graphicsSettings.setParticleMultiplier(0.5f);
         graphicsSettings.save();
      }
   }
}
