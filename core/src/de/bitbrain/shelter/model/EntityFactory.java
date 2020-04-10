package de.bitbrain.shelter.model;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.ai.ZombieBehavior;
import de.bitbrain.shelter.model.items.Item;
import de.bitbrain.shelter.model.items.LootTable;
import de.bitbrain.shelter.model.weapon.WeaponType;
import de.bitbrain.shelter.util.Supplier;

import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyDef;
import static de.bitbrain.shelter.physics.PhysicsFactory.createBodyFixtureDef;

public class EntityFactory {

   private final GameContext2D context;
   private final Supplier<GameObject> playerObjectSupplier;
   private final TiledMapContext tmxContext;

   public EntityFactory(GameContext2D context, TiledMapContext tmxContext, Supplier<GameObject> playerObjectSupplier) {
      this.context = context;
      this.playerObjectSupplier = playerObjectSupplier;
      this.tmxContext = tmxContext;
   }

   public GameObject addBarrel(GameObject originalBarrel) {
      GameObject barrel = context.getGameWorld().addObject("npcs");
      barrel.setType(originalBarrel.getType());
      barrel.setPosition(originalBarrel.getLeft(), originalBarrel.getTop());
      barrel.setZIndex(99999f);
      barrel.setDimensions(22f, 22f);
      barrel.setAttribute(HealthData.class, new HealthData(45));
      barrel.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);
      PointLight light = context.getLightingManager().createPointLight(100f, Color.RED);
      context.getLightingManager().attach(light, barrel, 10, 10);
      context.getBehaviorManager().apply(new ExplosionBehavior(80f, 500, context), barrel);

      // add physics
      BodyDef bodyDef = createBodyDef(barrel);
      bodyDef.type = BodyDef.BodyType.StaticBody;
      FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 10f);
      context.getPhysicsManager().attachBody(bodyDef, fixtureDef, barrel);
      return barrel;
   }

   public GameObject addItem(float x, float y, final Item item) {
      GameObject itemObject = context.getGameWorld().addObject("npcs");
      itemObject.setType(item);
      itemObject.setPosition(x, y);
      itemObject.setZIndex(99999f);
      itemObject.setDimensions(9f, 9f);
      itemObject.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);
      PointLight light = context.getLightingManager().createPointLight(100f, item.getLightColor());
      context.getLightingManager().attach(light, itemObject, 4, 4);
      context.getBehaviorManager().apply(new BehaviorAdapter() {
         @Override
         public void update(GameObject source, GameObject target, float delta) {
            boolean isItem = item.equals(source.getType()) || item.equals(target.getType());
            boolean isPlayer = "PLAYER".equals(source.getType()) || "PLAYER".equals(target.getType());
            if (source.collidesWith(target) && isItem && isPlayer) {
               GameObject player = "PLAYER".equals(source.getType()) ? source : target;
               GameObject itemObject = "PLAYER".equals(source.getType()) ? target : source;
               item.getCollectEffect().onCollect(item, player, context);
               context.getGameWorld().remove(itemObject);
            }
         }
      }, itemObject);
      Tween.to(itemObject, GameObjectTween.OFFSET_Y, 1f)
            .target(5f)
            .ease(TweenEquations.easeInBounce)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());
      itemObject.getColor().a = 0f;
      Tween.to(itemObject.getColor(), GameObjectTween.ALPHA, 0.5f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      context.getParticleManager().attachEffect(item.getParticleEffectPath(), itemObject, 4, 4);
      return itemObject;
   }

   public GameObject addZombie(float x, float y) {
      GameObject zombie = context.getGameWorld().addObject("npcs");
      zombie.setAttribute(HealthData.class, new HealthData(45));
      zombie.setType("ZOMBIE");
      zombie.setPosition(x, y);
      zombie.setZIndex(99999f);
      zombie.setDimensions(8f, 8f);
      zombie.setScaleX(4f);
      zombie.setScaleY(4f);
      zombie.setOffset(-12f,-4f);
      EntityMover entityMover = new EntityMover(717f, context.getGameCamera(), context.getAudioManager());
      zombie.setAttribute(EntityMover.class, entityMover);
      zombie.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);
      LootTable lootTable = new LootTable();
      lootTable.add(Item.AMMO, 0.1f);
      lootTable.add(Item.MEDIKIT, 0.1f);
      zombie.setAttribute(LootTable.class, lootTable);
      context.getBehaviorManager().apply(entityMover, zombie);
      context.getBehaviorManager().apply(new ZombieBehavior(playerObjectSupplier.supply(), context, entityMover, this), zombie);
      Color color = new Color(1f, 0, 0, 0.15f);
      PointLight light = context.getLightingManager().createPointLight(10f, color);
      context.getLightingManager().attach(light, zombie, 16f, 17f);
      // add physics
      BodyDef bodyDef = createBodyDef(zombie);
      FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 4f);
      context.getPhysicsManager().attachBody(bodyDef, fixtureDef, zombie);
      zombie.getColor().a = 0f;
      Tween.to(zombie.getColor(), GameObjectTween.ALPHA, 0.7f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      return zombie;
   }

   public GameObject addBullet(final WeaponType type, final float centerX, final float centerY,  final Vector2 direction) {
      return context.getGameWorld().addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setType(type);
            target.setPosition(centerX + direction.x, centerY + direction.y);
            target.setRotation(direction.angle());
            target.setDimensions(3, 3);
            target.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);
         }
      });
   }
}
