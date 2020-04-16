package de.bitbrain.shelter.core.items;

import com.badlogic.gdx.graphics.Color;

public enum Rarity {
   COMMON(Color.valueOf("ffffff")),
   UNCOMMON(Color.valueOf("72ff00")),
   RARE(Color.valueOf("0025ff")),
   EPIC(Color.valueOf("6200ff")),
   LEGENDARY(Color.valueOf("ff7f00")),
   ARTIFACT(Color.valueOf("ff008f"));

   private final Color color;

   Rarity(Color color) {
      this.color = color;
   }

   public Color getColor() {
      return color.cpy();
   }
}
