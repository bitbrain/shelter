package de.bitbrain.shelter.core.items;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.core.weapon.WeaponType;

public class WeaponCollectEffect implements CollectEffect {

   private final WeaponType weaponType;

   public WeaponCollectEffect(WeaponType weaponType) {
      this.weaponType = weaponType;
   }

   @Override
   public boolean onCollect(Item item, GameObject player, GameContext2D context) {
      Inventory data = player.getAttribute(Inventory.class);
      if (data != null) {
         boolean collected = data.addWeapon(weaponType);
         if (weaponType.getReloadSoundFx() != null && collected) {
            context.getAudioManager().spawnSound(weaponType.getReloadSoundFx(), player.getLeft(), player.getTop(), 1f, 0.6f, 300f);
         }
         return collected;
      }
      return false;
   }
}
