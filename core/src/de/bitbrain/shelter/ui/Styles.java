package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.shelter.Assets;

public class Styles {

   public static final Label.LabelStyle LABEL_DESCRIPTION = new Label.LabelStyle();


   public static void init() {
      LABEL_DESCRIPTION.font = bake(Assets.Fonts.EIGHTBIT_WONDER, 18);
   }



   public static BitmapFont bake(String fontPath, int size) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = true;
      BitmapFont font = generator.generateFont(param);
      // disable integer positions so UI aligns correctly
      font.setUseIntegerPositions(false);
      return font;
   }
}
