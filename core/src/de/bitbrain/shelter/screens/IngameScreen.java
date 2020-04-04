package de.bitbrain.shelter.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingConfig;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.animation.EntityAnimationRenderer;
import de.bitbrain.shelter.graphics.RenderOrderComparator;
import de.bitbrain.shelter.input.ingame.IngameKeyboardAdapter;
import de.bitbrain.shelter.model.EntityFactory;
import de.bitbrain.shelter.model.EntityMover;
import de.bitbrain.shelter.model.spawn.Spawner;
import de.bitbrain.shelter.model.weapon.WeaponHandler;
import de.bitbrain.shelter.model.weapon.WeaponType;
import de.bitbrain.shelter.physics.BulletContactListener;

import java.util.ArrayList;
import java.util.List;

import static de.bitbrain.shelter.Assets.TiledMaps.FOREST;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class IngameScreen extends BrainGdxScreen2D<ShelterGame> {

   private EntityMover playerEntityMover;
   private EntityFactory entityFactory;
   private GameContext2D context;
   private WeaponHandler playerWeaponHandler;
   private List<Spawner> spawners = new ArrayList<Spawner>();
   private GameObject playerObject;

   public IngameScreen(ShelterGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      this.entityFactory = new EntityFactory(context);
      setupLighting(context);
      setupWorld(context);
      setupRenderer(context);
      setupInput(context);
      setupPhysics(context);

      for (Spawner spawner : spawners) {
         spawner.spawn(context, playerObject);
      }
   }

   private void setupWorld(GameContext2D context) {
      this.context = context;
      context.getTiledMapManager().load(FOREST, context.getGameCamera().getInternalCamera());
      for (GameObject object : context.getGameWorld().getObjects()) {
         if (object.getType().equals("PLAYER")) {
            this.playerObject = object;
            // We need to make the actual entity smaller than the sprite
            // sprite -> 32x32
            // entity -> 8x8 for collision purposes
            object.setDimensions(8f, 8f);
            object.setScaleX(4f);
            object.setScaleY(4f);
            object.setOffset(-12f, -4f);

            // Setup camera tracking
            context.getGameCamera().setTrackingTarget(object);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setTargetTrackingSpeed(0.01f);
            context.getGameCamera().setZoom(200, GameCamera.ZoomMode.TO_HEIGHT);
            playerEntityMover = new EntityMover(2150f, context.getGameCamera());
            object.setAttribute(EntityMover.class, playerEntityMover);
            context.getBehaviorManager().apply(playerEntityMover, object);

            // add physics
            BodyDef bodyDef = createBodyDef(object);
            FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 4f);
            context.getPhysicsManager().attachBody(bodyDef, fixtureDef, object);

            // Give it a nice weapon
            object.setAttribute(WeaponType.class, WeaponType.AK47);
            playerWeaponHandler = new WeaponHandler(object);
         } else if (object.getType().equals("SPAWNER")) {
            int capacity = object.getAttribute("capacity", 1);
            spawners.add(new Spawner(object.getLeft(), object.getTop(), object.getWidth(), object.getHeight(), entityFactory, capacity));

         }
      }
   }

   private void setupRenderer(GameContext2D context) {
      context.getRenderManager().setRenderOrderComparator(new RenderOrderComparator());
      context.getRenderManager().register("PLAYER", new EntityAnimationRenderer(Assets.Textures.PLAYER_SPRITESHEET, 0.6f));
      context.getRenderManager().register("ZOMBIE", new EntityAnimationRenderer(Assets.Textures.ZOMBIE_SPRITESHEET, 0.3f));
      for (WeaponType type : WeaponType.values()) {
         context.getRenderManager().register(type, new SpriteRenderer(SharedAssetManager.getInstance().get(type.getMunitionTexture(), Texture.class)));
      }
   }

   private void setupLighting(GameContext2D context) {
      LightingConfig config = new LightingConfig();
      config.blur(true);
      config.rays(500);
      context.getLightingManager().setConfig(config);
      context.getLightingManager().setAmbientLight(Color.valueOf("110022"));
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerEntityMover, playerWeaponHandler, context));
   }

   private void setupPhysics(GameContext2D context) {
      context.getPhysicsManager().setIterationCount(2);
      context.getPhysicsManager().getPhysicsWorld().setContactListener(new BulletContactListener(context));
   }
}
