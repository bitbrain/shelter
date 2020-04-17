package de.bitbrain.shelter.core;

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
import de.bitbrain.braingdx.tweens.PointLight2DTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.ai.ZombieBehavior;
import de.bitbrain.shelter.behavior.ExplosionBehavior;
import de.bitbrain.shelter.core.items.Item;
import de.bitbrain.shelter.core.items.LootTable;
import de.bitbrain.shelter.core.model.HealthData;
import de.bitbrain.shelter.core.model.MaterialType;
import de.bitbrain.shelter.core.weapon.WeaponType;
import de.bitbrain.shelter.util.Supplier;

import static de.bitbrain.shelter.Assets.Particles.RADIOACTIVE;
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

      // add lighting
      PointLight lightClose = context.getLightingManager().createPointLight(30f, Color.valueOf("11ff00"));
      context.getLightingManager().attach(lightClose, barrel, 10, 10);
      PointLight lightWide = context.getLightingManager().createPointLight(120f, Color.valueOf("00ff11"));
      context.getLightingManager().attach(lightWide, barrel, 10, 10);
      final float offset = (float) Math.random();
      Tween.to(lightWide, PointLight2DTween.COLOR_A, 8f)
            .delay(offset)
            .target(0.5f)
            .repeatYoyo(Tween.INFINITY, offset)
            .ease(TweenEquations.easeInOutElastic)
            .start(SharedTweenManager.getInstance());
      Tween.to(lightWide, PointLight2DTween.DISTANCE, 8f)
            .delay(offset)
            .target(117f)
            .repeatYoyo(Tween.INFINITY, offset)
            .ease(TweenEquations.easeInOutElastic)
            .start(SharedTweenManager.getInstance());

      context.getBehaviorManager().apply(new ExplosionBehavior(80f, 500, context), barrel);

      // add physics
      BodyDef bodyDef = createBodyDef(barrel);
      bodyDef.type = BodyDef.BodyType.StaticBody;
      FixtureDef fixtureDef = createBodyFixtureDef(0f, 0f, 10f);
      context.getPhysicsManager().attachBody(bodyDef, fixtureDef, barrel);

      context.getParticleManager().attachEffect(RADIOACTIVE, barrel, 8f, 8f);

      // TODO add sound effects
      // TODO implement radioactive effect (zombies are not immune but take less damage)
      return barrel;
   }

   public GameObject addItem(float x, float y, final Item item) {
      GameObject itemObject = context.getGameWorld().addObject("npcs", false);
      final String itemObjectId = itemObject.getId();
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
            boolean isItem = source.getId().equals(itemObjectId);
            boolean isPlayer = "PLAYER".equals(target.getType());
            if (source.collidesWith(target) && isItem && isPlayer) {
               GameObject player = "PLAYER".equals(source.getType()) ? source : target;
               if (item.getCollectEffect().onCollect(item, player, context)) {
                  context.getGameWorld().remove(itemObjectId);
               }
            }
         }
      }, itemObject);
      Tween.to(itemObject, GameObjectTween.OFFSET_Y, 1f)
            .delay((float) Math.random())
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
      GameObject zombie = context.getGameWorld().addObject("npcs", false);
      zombie.setAttribute(HealthData.class, new HealthData(35));
      zombie.setType("ZOMBIE");
      zombie.setPosition(x, y);
      zombie.setZIndex(99999f);
      zombie.setDimensions(8f, 8f);
      zombie.setScaleX(4f);
      zombie.setScaleY(4f);
      zombie.setOffset(-12f,-4f);
      zombie.setAttribute(MaterialType.class, MaterialType.FLESH);
      zombie.setAttribute(WeaponType.class, WeaponType.ZOMBIE_BITE);
      EntityMover entityMover = new EntityMover(717f, context.getGameCamera(), context.getAudioManager());
      zombie.setAttribute(EntityMover.class, entityMover);
      zombie.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);
      LootTable lootTable = new LootTable();
      lootTable.add(Item.AK47_ITEM, 0.01f);
      lootTable.add(Item.AMMO_ITEM, 0.1f);
      lootTable.add(Item.MEDIKIT_ITEM, 0.1f);
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
            target.setColor(1f, 1f, 1f, 0f);
            Tween.to(target, GameObjectTween.ALPHA, 0.15f)
                  .target(1f)
                  .start(SharedTweenManager.getInstance());
            target.setRotation(direction.angle() - 90f);
            PointLight light = context.getLightingManager().createPointLight(135f, Color.valueOf("ffaa8855"));
            light.setPosition(target.getLeft(), target.getTop());
            context.getLightingManager().attach(light, target, 1f, 1f);
         }
      }, false);
   }

   public GameObject addDamageTelegraph(final WeaponType type, final float centerX, final float centerY, final float width, final float height, final float rotation) {
      return context.getGameWorld().addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setType(type);
            target.setPosition(centerX, centerY);
            target.setRotation(rotation);
            target.setDimensions(width, height);
            target.setAttribute("tmx_layer_index", tmxContext.getTiledMap().getLayers().size() - 2);

         }
      }, false);
   }
}
