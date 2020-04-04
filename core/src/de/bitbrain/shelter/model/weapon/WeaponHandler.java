package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityFactory;

public class WeaponHandler {

   private final GameObject owner;
   private final EntityFactory entityFactory;

   public WeaponHandler(GameObject owner, EntityFactory entityFactory) {
      this.owner = owner;
      this.entityFactory = entityFactory;
   }

   public void fire(GameContext2D context) {
      if (owner.hasAttribute(WeaponType.class)) {
         WeaponType type = owner.getAttribute(WeaponType.class);
         type.getFireStrategy().fire(owner, context, entityFactory);
      }
   }
}
