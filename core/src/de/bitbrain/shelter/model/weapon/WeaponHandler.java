package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public class WeaponHandler {

   private final GameObject owner;

   public WeaponHandler(GameObject owner) {
      this.owner = owner;
   }

   public void fire(GameContext2D context) {
      if (owner.hasAttribute(WeaponType.class)) {
         WeaponType type = owner.getAttribute(WeaponType.class);
         type.getFireStrategy().fire(owner, context);
      }
   }
}
