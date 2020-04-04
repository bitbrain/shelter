package de.bitbrain.shelter.model.items;

import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.Ammo;

public class AmmoCollectEffect implements CollectEffect {

   @Override
   public void onCollect(Item item, GameObject player, GameContext context) {
      Ammo ammo = player.getAttribute(Ammo.class);
      if (ammo != null) {
         ammo.addAmmo((int) (ammo.getMaxAmmo() * (0.1f + Math.random() * 0.1f)));
         context.getAudioManager().spawnSound(Assets.Sounds.GUN_RELOAD, player.getLeft(), player.getTop(), 1f, 0.6f, 300f);
      }
   }
}
