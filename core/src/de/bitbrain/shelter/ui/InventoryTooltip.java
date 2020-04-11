package de.bitbrain.shelter.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.shelter.model.items.InventoryItem;

public class InventoryTooltip extends Table {

   private final InventoryItem inventoryItem;

   public InventoryTooltip(InventoryItem inventoryItem) {
      this.inventoryItem = inventoryItem;
   }
}
