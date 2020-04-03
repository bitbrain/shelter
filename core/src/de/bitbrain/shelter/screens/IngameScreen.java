package de.bitbrain.shelter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.animation.AlwaysAnimationEnabler;
import de.bitbrain.shelter.animation.AnimationTypes;
import de.bitbrain.shelter.animation.PlayerAnimationTypeResolver;
import de.bitbrain.shelter.input.ingame.IngameKeyboardAdapter;
import de.bitbrain.shelter.model.EntityMover;
import de.bitbrain.shelter.model.EntitySpawner;

import static de.bitbrain.shelter.Assets.TiledMaps.FOREST;

public class IngameScreen extends BrainGdxScreen2D<ShelterGame> {

   private EntityMover playerEntityMover;
   private EntitySpawner entitySpawner;
   private GameContext2D context;
   private final Vector3 tmp = new Vector3();
   private boolean touched;

   public IngameScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      this.entitySpawner = new EntitySpawner(context);
      setupWorld(context);
      setupInput(context);
      setupRenderer(context);
      setupLighting(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      playerEntityMover.lookAtScreen(Gdx.input.getX(), Gdx.input.getY());
      if (Gdx.input.isTouched() && !touched) {
         touched = true;
         tmp.x = Gdx.input.getX();
         tmp.y = Gdx.input.getY();
         context.getGameCamera().getInternalCamera().unproject(tmp);
         entitySpawner.spawnZombie(tmp.x, tmp.y);
      } else if (!Gdx.input.isTouched()) {
         touched = false;
      }
   }

   private void setupWorld(GameContext2D context) {
      this.context = context;
      context.getTiledMapManager().load(FOREST, context.getGameCamera().getInternalCamera());
      for (GameObject object : context.getGameWorld().getObjects()) {
         if (object.getType().equals("PLAYER")) {
            object.setDimensions(32f, 32f);
            context.getGameCamera().setTrackingTarget(object);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setTargetTrackingSpeed(0.01f);
            context.getGameCamera().setZoom(200, GameCamera.ZoomMode.TO_HEIGHT);
            playerEntityMover = new EntityMover(50f, context.getGameCamera());
            object.setAttribute(EntityMover.class, playerEntityMover);
            context.getBehaviorManager().apply(playerEntityMover, object);
         }
      }
   }

   private void setupRenderer(GameContext2D context) {
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER_SPRITESHEET);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 32);
      context.getRenderManager().register("PLAYER", new AnimationRenderer(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationTypes.STANDING_SOUTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_SOUTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 1)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 2)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 3)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 4)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 5)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 6)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_SOUTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.3f)
                        .origin(0, 7)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build(), new PlayerAnimationTypeResolver(), new AlwaysAnimationEnabler()
      ) {
         @Override
         public void render(GameObject object, Batch batch, float delta) {
            Texture shadow = SharedAssetManager.getInstance().get(Assets.Textures.SHADOW, Texture.class);
            batch.draw(shadow, object.getLeft(), object.getTop(), object.getWidth(), object.getHeight());
            super.render(object, batch, delta);
         }
      });
      final Texture zombieTexture = SharedAssetManager.getInstance().get(Assets.Textures.ZOMBIE_SPRITESHEET);
      AnimationSpriteSheet zombieSheet = new AnimationSpriteSheet(zombieTexture, 32);
      context.getRenderManager().register("ZOMBIE", new AnimationRenderer(zombieSheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationTypes.STANDING_SOUTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_SOUTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 1)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 2)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH_WEST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 3)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 4)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_NORTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 5)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 6)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.STANDING_SOUTH_EAST, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.6f)
                        .origin(0, 7)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build(), new PlayerAnimationTypeResolver(), new AlwaysAnimationEnabler()
      ) {
         @Override
         public void render(GameObject object, Batch batch, float delta) {
            Texture shadow = SharedAssetManager.getInstance().get(Assets.Textures.SHADOW, Texture.class);
            batch.draw(shadow, object.getLeft(), object.getTop(), object.getWidth(), object.getHeight());
            super.render(object, batch, delta);
         }
      });
   }

   private void setupLighting(GameContext2D context) {
      context.getLightingManager().setAmbientLight(Color.valueOf("110022"));
   }

   private void setupInput(GameContext2D context2D) {
      context2D.getInputManager().register(new IngameKeyboardAdapter(playerEntityMover));
   }
}
