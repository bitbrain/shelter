package de.bitbrain.shelter.model.items;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.HealthData;

public class MedikitCollectEffect implements CollectEffect {

   @Override
   public void onCollect(Item item, GameObject player, GameContext2D context) {
      HealthData data = player.getAttribute(HealthData.class);
      if (data != null) {
         data.addHealth((int) (data.getTotalHealth() * (0.1f + Math.random() * 0.2f)));
         player.setColor(Color.LIME);
         TweenUtils.toColor(player.getColor(), Color.WHITE.cpy(), 2f);
         context.getAudioManager().spawnSound(Assets.Sounds.HEAL, player.getLeft(), player.getTop(), 1f, 0.6f, 300f);
         context.getParticleManager().attachEffect(Assets.Particles.HEAL, player, 4f, 4f);
      }
   }
}
