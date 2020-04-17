package de.bitbrain.shelter.behavior;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.core.entities.EntityFactory;
import de.bitbrain.shelter.core.items.Item;
import de.bitbrain.shelter.core.model.ChestStatus;
import de.bitbrain.shelter.ui.CursorUtils;

public class ChestBehavior extends BehaviorAdapter {

   private final Vector2 distanceVector = new Vector2();
   private final Vector3 mousePosition = new Vector3();
   private final Rectangle rectangle = new Rectangle();
   private final Camera camera;
   private final EntityFactory entityFactory;

   private boolean opened;

   public ChestBehavior(Camera camera, EntityFactory entityFactory) {
      this.camera = camera;
      this.entityFactory = entityFactory;
   }

   @Override
   public void update(final GameObject source, GameObject target, float delta) {
      if ("CHEST".equals(source.getType()) && "PLAYER".equals(target.getType())) {
         distanceVector.x = target.getLeft() - source.getLeft();
         distanceVector.y = target.getTop() - source.getTop();
         rectangle.set(source.getLeft() - 14f, source.getTop() + 3f, 28, 20);
         mousePosition.x = Gdx.input.getX();
         mousePosition.y = Gdx.input.getY();
         camera.unproject(mousePosition);
         if (rectangle.contains(mousePosition.x, mousePosition.y) && distanceVector.len() < 30f) {
            CursorUtils.setCursor(Assets.Textures.CURSOR_CHEST);
            ChestStatus chestStatus = source.getAttribute(ChestStatus.class);
            if (!opened && (chestStatus == ChestStatus.CLOSED || chestStatus == ChestStatus.CLOSING) && Gdx.input.isTouched()) {
               source.setAttribute(ChestStatus.class, ChestStatus.OPENING);
               opened = true;
               // TODO add opening sound
               Tween.call(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> tween) {
                     if (source.hasAttribute("item")) {
                        // spawn the items now
                        String[] identifiers = source.getAttribute("item", String.class).split(",");
                        for (String identifier : identifiers) {
                           identifier = identifier.trim();
                           Item item = Item.valueOf(identifier + "_ITEM");
                           GameObject itemObject = entityFactory.addItem((float) (source.getLeft() - 14f + Math.random() * 28f), source.getTop() - 5f, item);
                        }
                        source.removeAttribute("item");
                     }
                  }
               }).delay(0.6f).start(SharedTweenManager.getInstance());
               Tween.call(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> tween) {
                     opened = false;
                     // TODO add closing sound
                     source.setAttribute(ChestStatus.class, ChestStatus.CLOSING);
                  }
               }).delay(1f).start(SharedTweenManager.getInstance());
            }
         } else {
            CursorUtils.setCursor(Assets.Textures.CURSOR_CROSSHAIR);
         }
      }
   }
}
