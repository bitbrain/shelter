package de.bitbrain.shelter.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.AnimationDrawable;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.i18n.Bundle;
import de.bitbrain.shelter.i18n.Messages;
import de.bitbrain.shelter.input.logo.LogoControllerInputAdapter;
import de.bitbrain.shelter.input.logo.LogoKeyboardInputAdapter;
import de.bitbrain.shelter.ui.GlitchLabel;
import de.bitbrain.shelter.ui.Styles;

public class LogoScreen extends BrainGdxScreen2D {

   private GameContext2D context;

   private boolean exiting = false;
   private GlitchLabel slogan;

   public LogoScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      Music music = Asset.get(Assets.Musics.SHELTER_THEME, Music.class);
      music.setLooping(true);
      music.setVolume(0.5f);
      music.play();
      this.context = context;
      context.getScreenTransitions().in(0.5f);
      final Texture logoTexture = Asset.get(Assets.Textures.LOGO, Texture.class);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(logoTexture, 16);
      AnimationDrawable drawable = new AnimationDrawable(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationDrawable.DEFAULT_FRAME_ID, AnimationFrames.builder()
                        .origin(0, 0)
                        .frames(16)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.NORMAL)
                        .duration(0.1f)
                        .build())
                  .build());

      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int i, BaseTween<?> baseTween) {
            if (!exiting) {
               context.getScreenTransitions().out(new TitleScreen(getGame()), 2.5f);
            }
         }
      }).delay(1.6f).start(SharedTweenManager.getInstance());

      Table layout = new Table();
      layout.setFillParent(true);

      Image image = new Image(drawable);
      layout.add(image).width(256).height(256).padBottom(65).row();

      slogan = new GlitchLabel(Bundle.get(Messages.BITBRAIN), Styles.LABEL_INTRO_BITBRAIN);
      slogan.setAlignment(Align.center);
      layout.add(slogan).width(Gdx.graphics.getWidth());

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setZoom(1500, GameCamera.ZoomMode.TO_HEIGHT);
      context.getGameCamera().getInternalCamera().update();

      setupInput(context);
   }

   public void exit() {
      if (!exiting) {
         context.getScreenTransitions().out(new TitleScreen(getGame()), 0.5f);
         exiting = true;
      }
   }

   @Override
   public void dispose() {
      super.dispose();
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new LogoKeyboardInputAdapter(this));
      context.getInputManager().register(new LogoControllerInputAdapter(this));
   }
}
