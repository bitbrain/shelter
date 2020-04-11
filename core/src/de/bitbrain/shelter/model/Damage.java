package de.bitbrain.shelter.model;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class Damage {

   private final int min;
   private final int max;

   public Damage(int min, int max) {
      if (min >= max) {
         throw new GdxRuntimeException("Invalid damage min/max value! min must be smaller than max");
      }
      this.min = min;
      this.max = max;
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
}
