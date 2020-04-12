package de.bitbrain.shelter.model;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class Damage {

   private final int min;
   private final int max;
   private final float impactWidth;
   private final float impactHeight;
   private final float damageDelay;

   public Damage(int min, int max, float impactWidth, float impactHeight, float damageDelay) {
      if (min >= max) {
         throw new GdxRuntimeException("Invalid damage min/max value! min must be smaller than max");
      }
      this.min = min;
      this.max = max;
      this.impactWidth = impactWidth;
      this.impactHeight = impactHeight;
      this.damageDelay = damageDelay;
   }

   public float getDamageDelay() {
      return damageDelay;
   }

   public int get() {
      return (int) (min + Math.random() * (max - min));
   }

   public int getMin() {
      return min;
   }

   public int getMax() {
      return max;
   }

   public float getImpactWidth() {
      return impactWidth;
   }

   public float getImpactHeight() {
      return impactHeight;
   }
}
