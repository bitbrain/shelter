package de.bitbrain.shelter.behavior;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.audio.JukeBox;
import de.bitbrain.shelter.core.entities.EntityFactory;
import de.bitbrain.shelter.core.entities.EntityMover;
import de.bitbrain.shelter.core.items.Item;
import de.bitbrain.shelter.core.items.LootTable;
import de.bitbrain.shelter.core.model.HealthData;

public class HealthCheckBehavior extends BehaviorAdapter {

   private final GameContext2D context;
   private final JukeBox zombieDeathSounds;
   private final EntityFactory entityFactory;

   public HealthCheckBehavior(GameContext2D context, EntityFactory entityFactory) {
      this.context = context;
      this.entityFactory = entityFactory;
      this.zombieDeathSounds = new JukeBox(context.getAudioManager(), 200,
            Assets.Sounds.ZOMBIE_DEATH_01,
            Assets.Sounds.ZOMBIE_DEATH_02);
   }

   @Override
   public void update(GameObject target, float delta) {
      HealthData healthData = target.getAttribute(HealthData.class);
      EntityMover entityMover = target.getAttribute(EntityMover.class);
      if (healthData != null && healthData.isDead() && entityMover != null && target.isActive()) {
         if ("PLAYER".equals(target.getType())) {
            SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(0.8f, 1f, 0f);
            target.setActive(false);
            return;
         }
         if ("ZOMBIE".equals(target.getType())) {
            zombieDeathSounds.playSound(target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
         }
         target.setActive(false);
         Color targetColor = Color.BLUE.cpy();
         targetColor.a = 0f;
         TweenUtils.toColor(target.getColor(), targetColor, 0.7f);
         float randomX = (float) (target.getWidth() * 2f * Math.random());
         float randomY = (float) (target.getHeight() * 2f * Math.random());
         context.getParticleManager().spawnEffect(Assets.Particles.BLOOD_EXPLOSION, target.getLeft() + randomX - target.getWidth() / 2f, target.getTop() + randomY + target.getHeight() / 2f);

         // Drop an item if we can
         if (target.hasAttribute(LootTable.class)) {
            LootTable lootTable = target.getAttribute(LootTable.class);
            Item item = lootTable.drop();
            if (item != null) {
               entityFactory.addItem(target.getLeft(), target.getTop(), item);
            }
         }
         context.getBehaviorManager().remove(target);
      }
   }


}
