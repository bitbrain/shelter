package de.bitbrain.shelter.core.items;

import java.util.HashMap;
import java.util.Map;

public class LootTable {

   private Map<Item, Float> items = new HashMap<>();

   public void add(Item item, float dropRate) {
      items.put(item, dropRate);
   }

   public Item drop() {
      for (Map.Entry<Item, Float> entry : items.entrySet()) {
         if (Math.random() < entry.getValue()) {
            return entry.getKey();
         }
      }
      return null;
   }
}
