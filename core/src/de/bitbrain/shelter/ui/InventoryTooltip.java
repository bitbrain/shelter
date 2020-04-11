package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.items.InventoryItem;

public class InventoryTooltip extends Table {

   private final Label name;
   private final Label stats;
   private final Label description;
   private final NinePatch background;

   public InventoryTooltip() {
      this.background = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_EQUIP, Texture.class), 4);
      this.name = new Label("", Styles.LABEL_TOOLTIP_NAME);
      this.name.setFontScale(0.5f);
      this.name.setAlignment(Align.left);
      left().add(name).padLeft(6f).align(Align.left).row();
      this.stats = new Label("", Styles.LABEL_TOOLTIP_STATS);
      this.stats.setFontScale(0.4f);
      this.stats.setAlignment(Align.left);
      left().add(stats).padLeft(6f).padTop(2f).align(Align.left).row();
      this.description = new Label("", Styles.LABEL_DESCRIPTION);
      this.description.setFontScale(0.3f);
      this.description.setWrap(true);
      this.description.setAlignment(Align.left);
      left().add(description).padLeft(6f).padTop(2f).align(Align.left).row();
      setColor(1f, 1f, 1f, 0f);
   }

   public void setItem(InventoryItem inventoryItem) {
      this.name.setText(inventoryItem.getName());
      this.name.setColor(inventoryItem.getRarity().getColor());
      this.stats.setText(inventoryItem.getStats());
      this.description.setText(inventoryItem.getDescription());
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      batch.setColor(getColor());
      background.draw(batch, getX(),getY(), getWidth(), getHeight());
      batch.setColor(Color.WHITE.cpy());
      super.draw(batch, parentAlpha);
   }
}
