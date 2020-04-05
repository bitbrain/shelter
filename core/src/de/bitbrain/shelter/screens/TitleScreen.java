package de.bitbrain.shelter.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
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
      Label logo = new Label(".shelter", Styles.LABEL_LOGO);
      layout.add(logo).row();

      Label pressAnyButton = new Label("press any key", Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(Gdx.graphics.getHeight() / 6f).row();

      context.getStage().addActor(layout);

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

      AutoReloadPostProcessorEffect<Bloom> bloom = context.getShaderManager().createBloomEffect();
      bloom.mutate(new Mutator<Bloom>() {
         @Override
         public void mutate(Bloom target) {
            target.setBlurPasses(20);
            target.setBloomIntesity(0.7f);
         }
      });


      context.getRenderPipeline().addEffects(RenderPipeIds.UI, bloom);
   }

   @Override
   protected void onUpdate(float delta) {
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(8f, 1f, 0f);
         context.getScreenTransitions().out(new IngameScreen(getGame(), FOREST), 1f);
      }
   }
}
