package de.bitbrain.shelter.model.items;

import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.HealthData;

public class MedikitCollectEffect implements CollectEffect {

   @Override
   public void onCollect(Item item, GameObject player, GameContext context) {
      HealthData data = player.getAttribute(HealthData.class);
      if (data != null) {
         data.addHealth((int) (data.getTotalHealth() * (0.1f + Math.random() * 0.2f)));
      }
   }
}
