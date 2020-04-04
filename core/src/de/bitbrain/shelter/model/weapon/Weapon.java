package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public class Weapon {

   private final WeaponType type;
   private final GameObject owner;

   public Weapon(WeaponType type, GameObject owner) {
      this.type = type;
      this.owner = owner;
   }

   public void fire(GameContext2D context) {
      type.getFireStrategy().fire(owner, context);
   }
}
