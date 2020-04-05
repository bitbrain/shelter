package de.bitbrain.shelter.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.ThemeColors;
import de.bitbrain.shelter.i18n.Bundle;
import de.bitbrain.shelter.i18n.Messages;
import de.bitbrain.shelter.model.story.StoryTeller;
import de.bitbrain.shelter.ui.Styles;

import static de.bitbrain.shelter.Assets.TiledMaps.FOREST;

public class TitleScreen extends BrainGdxScreen2D<ShelterGame> {

   private boolean exiting;

   private GameContext2D context;

   public TitleScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      context.setBackgroundColor(ThemeColors.BACKGROUND);
      this.context = context;
      context.getScreenTransitions().in(1.5f);

      AutoReloadPostProcessorEffect<Vignette> vignette = context.getShaderManager().createVignetteEffect();
      vignette.mutate(new Mutator<Vignette>() {
         @Override
         public void mutate(Vignette target) {
            target.setLutIntensity(0.7f);
            target.setIntensity(0.9f);
            target.setSaturationMul(1.1f);
         }
      });
      context.getRenderPipeline().addEffects(RenderPipeIds.WORLD, vignette);

      Table layout = new Table();
      layout.setFillParent(true);
      Sprite sprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.ICON, Texture.class));
      sprite.setSize(200, 200);
      Image icon = new Image(new SpriteDrawable(sprite));
      layout.add(icon).row();
      Label logo = new Label("shelter", Styles.LABEL_LOGO);
      layout.add(logo).row();

      Label pressAnyButton = new Label(Bundle.get(Messages.PLAY_GAME), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      Label tips = new Label(Bundle.get(Messages.TIPS), Styles.TIPS);
      layout.add(tips).padTop(170f);

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setZoom(1000, GameCamera.ZoomMode.TO_HEIGHT);

      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(2f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(3f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());

      logo.getColor().a = 0f;
      Tween.to(logo, ActorTween.ALPHA, 3f).target(1f).delay(0f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());
   }

   @Override
   protected void onUpdate(float delta) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         final IngameScreen initialScreen = new IngameScreen(getGame(), FOREST);
         SharedAssetManager.getInstance().get(Assets.Sounds.GUN_RELOAD, Sound.class).play(0.5f, 1f, 0f);
         context.getScreenTransitions().out(new StoryScreen(getGame(), initialScreen,
               Messages.STORY_INTRO_1,
               Messages.STORY_INTRO_2,
               Messages.STORY_INTRO_3,
               Messages.STORY_INTRO_4
               ), 1f);
      }
   }
}
