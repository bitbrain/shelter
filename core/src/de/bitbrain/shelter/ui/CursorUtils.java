package de.bitbrain.shelter.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import de.bitbrain.braingdx.assets.Asset;

import java.util.HashMap;
import java.util.Map;

public class CursorUtils {

   private static Map<String, Cursor> cursors = new HashMap<>();

   public static void dispose() {
      for (Cursor cursor : cursors.values()) {
         cursor.dispose();
      }
      cursors.clear();
   }

   public static void setCursor(String cursorAssetId) {
      Cursor cursor;
      if (cursors.containsKey(cursorAssetId)) {
         cursor = cursors.get(cursorAssetId);
      } else {
         Texture texture = Asset.get(cursorAssetId, Texture.class);
         texture.getTextureData().prepare();
         cursor = Gdx.graphics.newCursor(texture.getTextureData().consumePixmap(), 32, 32);
         cursors.put(cursorAssetId, cursor);
      }
      Gdx.graphics.setCursor(cursor);
   }
}
