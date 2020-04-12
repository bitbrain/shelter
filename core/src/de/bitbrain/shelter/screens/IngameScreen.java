package de.bitbrain.shelter.screens;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.*;
import de.bitbrain.braingdx.graphics.lighting.LightingConfig;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ShelterGame;
import de.bitbrain.shelter.ThemeColors;
import de.bitbrain.shelter.animation.AlwaysAnimationEnabler;
import de.bitbrain.shelter.graphics.BulletRenderer;
import de.bitbrain.shelter.graphics.EntityAnimationRenderer;
import de.bitbrain.shelter.graphics.RenderOrderComparator;
import de.bitbrain.shelter.i18n.Messages;
import de.bitbrain.shelter.input.ingame.IngameKeyboardAdapter;
import de.bitbrain.shelter.model.*;
import de.bitbrain.shelter.model.items.Inventory;
import de.bitbrain.shelter.model.items.Item;
import de.bitbrain.shelter.model.spawn.Spawner;
import de.bitbrain.shelter.model.weapon.AttackHandler;
import de.bitbrain.shelter.model.weapon.WeaponType;
import de.bitbrain.shelter.ui.AmmoUI;
import de.bitbrain.shelter.ui.HealthUI;
import de.bitbrain.shelter.ui.InventoryTooltip;
import de.bitbrain.shelter.ui.InventoryUI;
import de.bitbrain.shelter.util.Supplier;

import java.util.ArrayList;
import java.util.List;

import static de.bitbrain.braingdx.graphics.GraphicsFactory.createTexture;
import static de.bitbrain.shelter.ThemeColors.AMBIENT;
import static de.bitbrain.shelter.animation.AnimationTypes.STANDING_SOUTH;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class IngameScreen extends BrainGdxScreen2D<ShelterGame> implements Supplier<GameObject> {

   private EntityMover playerEntityMover;
   private EntityFactory entityFactory;
   private GameContext2D context;
   private AttackHandler playerAttackHandler;
   private List<Spawner> spawners = new ArrayList<Spawner>();
   private GameObject playerObject;
   private final Vector2 spawnPoint = new Vector2();
   private final List<GameObject> originalBarrels = new ArrayList<>();
   private final String tiledMapPath;
   private final String alternativeMapPath;
   private boolean saveRoom;
   private boolean entered;

   public IngameScreen(ShelterGame game, String tiledMapPath) {
      this(game, tiledMapPath, null);
   }


   public IngameScreen(ShelterGame game, String tiledMapPath, String alternativeMapPath) {
      super(game);
      this.tiledMapPath = tiledMapPath;
      this.alternativeMapPath = alternativeMapPath;
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      playerAttackHandler.update(delta);
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
         for (Spawner spawner : spawners) {
            spawner.spawn(context, playerObject);
         }
      }
      if (playerObject.getAttribute(HealthData.class).isDead()) {
         for (GameObject o : context.getGameWorld().getGroup("npcs")) {
            context.getGameWorld().remove(o);
         }
         for (Spawner spawner : spawners) {
            spawner.spawn(context, playerObject);
         }
         for (GameObject barrel : originalBarrels) {
            entityFactory.addBarrel(barrel);
         }
         entered = false;
         playerObject.setActive(true);
         playerObject.getAttribute(Ammo.class).reset();
         playerObject.getAttribute(HealthData.class).reset();
         playerObject.getAttribute(Body.class).setTransform(spawnPoint.x, spawnPoint.y, 0f);
         context.getScreenTransitions().in(0.5f);
      }
   }

   @Override
   protected void onCreate(GameContext2D context) {
      context.setBackgroundColor(ThemeColors.BACKGROUND);
      context.getScreenTransitions().in(2.5f);
      setupLighting(context);
      setupWorld(context);
      setupRenderer(context);
      setupInput(context);
      setupPhysics(context);
      setupUI(context);

      for (Spawner spawner : spawners) {
         spawner.spawn(context, playerObject);
      }
   }

   private void setupWorld(final GameContext2D context) {
      this.context = context;
      TiledMapContext tmxContext = context.getTiledMapManager().load(tiledMapPath, context.getGameCamera().getInternalCamera());

      this.entityFactory = new EntityFactory(context, tmxContext, this);
      if (alternativeMapPath == null && "shelter".equals(tmxContext.getTiledMap().getProperties().get("name", "", String.class))) {
         // SAVE ROOM! GAME SUCCESS!
         saveRoom = true;
         context.getScreenTransitions().out(new StoryScreen(getGame(), new TitleScreen(getGame()),
               Messages.STORY_OUTRO_1,
               Messages.STORY_OUTRO_2,
               Messages.STORY_OUTRO_3
         ), 3f);
      }
      for (final GameObject object : context.getGameWorld().getObjects()) {
         if ("PLAYER".equals(object.getType())) {
            this.playerObject = object;
            playerObject.setAttribute(MaterialType.class, MaterialType.FLESH);
            // We need to make the actual entity smaller than the sprite
            // sprite -> 32x32
            // entity -> 8x8 for collision purposes
            spawnPoint.x = object.getLeft();
            spawnPoint.y = object.getTop();
            object.setDimensions(8f, 8f);
            object.setScaleX(4f);
            object.setScaleY(4f);
            object.setOffset(-12f, -4f);
            object.setAttribute(HealthData.class, new HealthData(500));
            object.setAttribute(Ammo.class, new Ammo(200));
            object.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);

            // Setup camera tracking
            context.getGameCamera().setTrackingTarget(object);
            context.getGameCamera().setStickToWorldBounds(false);
            context.getGameCamera().setTargetTrackingSpeed(0.01f);
            context.getGameCamera().setZoomScalingFactor(0f);
            context.getGameCamera().setZoom(250, GameCamera.ZoomMode.TO_HEIGHT);
            playerEntityMover = new EntityMover(2150f, context.getGameCamera(), context.getAudioManager());
            object.setAttribute(EntityMover.class, playerEntityMover);
            context.getBehaviorManager().apply(playerEntityMover, object);

            // add physics
            BodyDef bodyDef = createBodyDef(object);
            FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 4f);
            context.getPhysicsManager().attachBody(bodyDef, fixtureDef, object);

            // Initialise inventory
            Inventory inventory = new Inventory(object);
            inventory.addWeapon(WeaponType.RUSTY_CROWBAR);
            playerAttackHandler = new AttackHandler(object, entityFactory);
         } else if (object.getType().equals("SPAWNER") && !saveRoom) {
            int capacity = object.getAttribute("capacity", 1);
            context.getGameWorld().remove(object);
            spawners.add(new Spawner(object.getLeft(), object.getTop(), object.getWidth(), object.getHeight(), entityFactory, capacity));
         } else if (object.getType().equals("SHELTER")) {
            PointLight light = context.getLightingManager().createPointLight(250, Color.RED);
            context.getLightingManager().attach(light, object, object.getWidth() / 2f, object.getHeight() / 2f);
            context.getBehaviorManager().apply(new BehaviorAdapter() {
               @Override
               public void update(GameObject source, GameObject target, float delta) {
                  if (source.collidesWith(target)) {
                     if ("PLAYER".equals(source.getType()) || "PLAYER".equals(target.getType())) {
                        context.getBehaviorManager().clear();
                        playerObject.setActive(false);
                        String next = object.getAttribute("next", String.class);
                        context.getAudioManager().spawnSound(Assets.Sounds.DOOR_OPEN, source.getLeft(), source.getTop(), 1f, 0.5f, 1200);
                        context.getScreenTransitions().out(new IngameScreen(getGame(), Assets.TiledMaps.SHELTER, next), 0.3f);
                     }
                  }
               }
            }, object);
         } else if (object.getType().equals("DOOR")) {
            if (alternativeMapPath != null) {
               context.getBehaviorManager().apply(new BehaviorAdapter() {

                  @Override
                  public void update(GameObject source, GameObject target, float delta) {
                     if (source.collidesWith(target) && !entered) {
                        if ("PLAYER".equals(source.getType()) || "PLAYER".equals(target.getType())) {
                           entered = true;
                           context.getBehaviorManager().clear();
                           playerObject.setActive(false);
                           context.getAudioManager().spawnSound(Assets.Sounds.DOOR_SHUT, source.getLeft(), source.getTop(), 1f, 0.5f, 1200);
                           context.getScreenTransitions().out(new IngameScreen(getGame(), alternativeMapPath), 0.3f);
                        }
                     }
                  }
               }, object);
            }
         } else if (object.getType().equals("BARREL")) {
            playerObject.setAttribute(MaterialType.class, MaterialType.STEEL);
            originalBarrels.add(object.copy());
            entityFactory.addBarrel(object);
            context.getGameWorld().remove(object);
         }
      }
   }

   private void setupRenderer(GameContext2D context) {
      context.getRenderManager().setRenderOrderComparator(new RenderOrderComparator());
      context.getRenderManager().register("PLAYER", new EntityAnimationRenderer(Assets.Textures.PLAYER_SPRITESHEET, 0.6f));
      context.getRenderManager().register("ZOMBIE", new EntityAnimationRenderer(Assets.Textures.ZOMBIE_SPRITESHEET, 0.3f));
      context.getRenderManager().register("BARREL", new SpriteRenderer(Assets.Textures.BARREL));
      Texture itemTexture = SharedAssetManager.getInstance().get(Assets.Textures.ITEMS_SPRITESHEET, Texture.class);
      AnimationSpriteSheet itemSpriteSheet = new AnimationSpriteSheet(itemTexture, 9);
      for (WeaponType type : WeaponType.values()) {
         if (type.getAttackTexture() != null) {
            context.getRenderManager().register(type, new BulletRenderer(type));
         }// else {
        //    context.getRenderManager().register(type, new SpriteRenderer(createTexture(2, 2, Color.RED)));
        // }
      }
      for (Item type : Item.values()) {
         context.getRenderManager().register(type, new AnimationRenderer(itemSpriteSheet, AnimationConfig.builder()
               .registerFrames(STANDING_SOUTH, AnimationFrames.builder()
                     .direction(AnimationFrames.Direction.HORIZONTAL)
                     .frames(4)
                     .origin(0, type.getAnimationIndex())
                     .playMode(Animation.PlayMode.LOOP)
                     .duration(0.3f)
                     .build())
               .build(), new AnimationTypeResolver<GameObject>() {
            @Override
            public Object getAnimationType(GameObject object) {
               return STANDING_SOUTH;
            }
         }, new AlwaysAnimationEnabler()) {
            @Override
            public void render(GameObject object, Batch batch, float delta) {
               Texture shadow = SharedAssetManager.getInstance().get(Assets.Textures.SHADOW, Texture.class);
               float scale = 6f * (object.getOffsetY() / 8);
               batch.draw(shadow, object.getLeft() - scale * 2f, object.getTop() - scale - 2f,
                     object.getWidth() + scale * 4f, object.getHeight() + scale * 2f + 3f);
               super.render(object, batch, delta);
            }
         });
      }
      context.getRenderPipeline().moveAfter(RenderPipeIds.LIGHTING, RenderPipeIds.PARTICLES);
   }

   private void setupLighting(GameContext2D context) {
      LightingConfig config = new LightingConfig();
      config.blur(true);
      config.rays(500);
      context.getLightingManager().setConfig(config);
      context.getLightingManager().setAmbientLight(AMBIENT);
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerEntityMover, playerAttackHandler, context));
   }

   private void setupPhysics(GameContext2D context) {
      context.getPhysicsManager().setIterationCount(2);
   }

   private void setupUI(GameContext2D context) {
      HealthUI healthUI = new HealthUI(playerObject.getAttribute(HealthData.class), context.getGameCamera());
      healthUI.setWidth(16f);
      healthUI.setHeight(64f);
      healthUI.setPosition(16f, 16f);
      context.getWorldStage().addActor(healthUI);

      AmmoUI ammoUI = new AmmoUI(playerObject, context.getGameCamera());
      ammoUI.setWidth(16f);
      ammoUI.setHeight(16f);
      ammoUI.setPosition(32f, 32f);
      context.getWorldStage().addActor(ammoUI);

      final InventoryTooltip inventoryTooltip = new InventoryTooltip();
      inventoryTooltip.setSize(170, 60f);
      inventoryTooltip.setColor(1f, 1f, 1f, 0f);

      InventoryUI inventoryUI = new InventoryUI(playerObject, context.getGameCamera());
      inventoryUI.setDebug(true);
      inventoryUI.setWidth(16f);
      inventoryUI.setHeight(16f);
      inventoryUI.setPosition(32f, 16f);

      context.getWorldStage().addActor(inventoryUI);

      context.getWorldStage().addActor(inventoryTooltip);

   }

   @Override
   public GameObject supply() {
      return playerObject;
   }
}
