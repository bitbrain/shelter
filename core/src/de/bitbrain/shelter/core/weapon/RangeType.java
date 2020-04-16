package de.bitbrain.shelter.core.weapon;

import de.bitbrain.braingdx.util.Factory;

public enum RangeType {

   CLOSE_RANGE(new Factory<AttackStrategy>() {
      @Override
      public CloseAttackStrategy create() {
         return new CloseAttackStrategy();
      }
   }),
   LONG_RANGE(new Factory<AttackStrategy>() {
      @Override
      public AttackStrategy create() {
         return new LongRangeAttackStrategy();
      }
   });

   private final Factory<AttackStrategy> attackStrategyFactory;

   RangeType(Factory<AttackStrategy> attackStrategyFactory) {
      this.attackStrategyFactory = attackStrategyFactory;
   }

   public Factory<AttackStrategy> getAttackStrategyFactory() {
      return attackStrategyFactory;
   }
}
