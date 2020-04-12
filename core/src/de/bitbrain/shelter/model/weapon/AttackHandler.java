package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityFactory;

public class AttackHandler implements Updateable {

   private final GameObject owner;
   private final EntityFactory entityFactory;
   private AttackStrategy attackStrategy;
   private WeaponType weaponType;

   public AttackHandler(GameObject owner, EntityFactory entityFactory) {
      this.owner = owner;
      this.entityFactory = entityFactory;
      weaponType = owner.getAttribute(WeaponType.class);
      if (weaponType != null) {
         attackStrategy = weaponType.getAttackStrategyFactory().create();
      }
   }

   public void attack(GameContext2D context) {
      if (attackStrategy != null) {
         attackStrategy.attack(owner, context, entityFactory);
      }
   }

   @Override
   public void update(float delta) {
      if (weaponType != owner.getAttribute(WeaponType.class)) {
         weaponType = owner.getAttribute(WeaponType.class);
         if (weaponType != null) {
            attackStrategy = weaponType.getAttackStrategyFactory().create();
         }
      }
      if (attackStrategy != null) {
         attackStrategy.update(delta);
      }
   }
}
