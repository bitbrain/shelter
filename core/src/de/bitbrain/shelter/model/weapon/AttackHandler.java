package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityFactory;

public class AttackHandler {

   private final GameObject owner;
   private final EntityFactory entityFactory;

   public AttackHandler(GameObject owner, EntityFactory entityFactory) {
      this.owner = owner;
      this.entityFactory = entityFactory;
   }

   public void attack(GameContext2D context) {
      if (owner.hasAttribute(WeaponType.class)) {
         WeaponType type = owner.getAttribute(WeaponType.class);
         type.getAttackStrategy().attack(owner, context, entityFactory);
      }
   }
}
