package de.bitbrain.shelter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.animation.AlwaysAnimationEnabler;
import de.bitbrain.shelter.animation.AnimationTypes;
import de.bitbrain.shelter.animation.PlayerAnimationTypeResolver;
import de.bitbrain.shelter.input.ingame.IngameKeyboardAdapter;
import de.bitbrain.shelter.model.Player;

import static de.bitbrain.shelter.Assets.TiledMaps.FOREST;

public class IngameScreen extends BrainGdxScreen2D<ShelterGame> {

   private Player player;

   public IngameScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      setupInput(context);
      setupWorld(context);
      setupRenderer(context);
      setupLighting(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      player.lookAtScreen(Gdx.input.getX(), Gdx.input.getY());
   }

   private void setupWorld(GameContext2D context) {
      TiledMapContext tmxContext = context.getTiledMapManager().load(FOREST, context.getGameCamera().getInternalCamera());
      for (GameObject object : context.getGameWorld().getObjects()) {
         if (object.getType().equals("PLAYER")) {
            object.setDimensions(32f, 32f);
            context.getGameCamera().setTrackingTarget(object);
            context.getGameCamera().setStickToWorldBounds(false);
            context.getGameCamera().setZoom(200, GameCamera.ZoomMode.TO_HEIGHT);
            player = new Player(object, context.getGameCamera());
         }
      }
   }

   private void setupRenderer(GameContext2D context) {
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER_SPRITESHEET);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 32);
      context.getRenderManager().register("PLAYER", new AnimationRenderer(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationTypes.PLAYER_STANDING_SOUTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_SOUTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 1)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 2)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_NORTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 3)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_NORTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 4)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_NORTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 5)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 6)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_STANDING_SOUTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 7)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build(), new PlayerAnimationTypeResolver(player), new AlwaysAnimationEnabler()
      ));
   }

   private void setupLighting(GameContext2D context) {
      context.getLightingManager().setAmbientLight(Color.valueOf("110022"));
   }

   private void setupInput(GameContext2D context2D) {
      context2D.getInputManager().register(new IngameKeyboardAdapter());
   }
}
