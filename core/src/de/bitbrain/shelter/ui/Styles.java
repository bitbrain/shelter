package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.shelter.Assets;

import static com.badlogic.gdx.graphics.Color.valueOf;

public class Styles {

   public static final Label.LabelStyle LABEL_DESCRIPTION = new Label.LabelStyle();
   public static final GlitchLabel.GlitchLabelStyle LABEL_INTRO_BITBRAIN = new GlitchLabel.GlitchLabelStyle();
   public static final Label.LabelStyle LABEL_LOGO = new Label.LabelStyle();
   public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();


   public static void init() {
      LABEL_DESCRIPTION.font = bake(Assets.Fonts.EIGHTBIT_WONDER, 18);

      LABEL_INTRO_BITBRAIN.font = bake(Assets.Fonts.VISITOR, 48);
      LABEL_INTRO_BITBRAIN.fontColor = valueOf("00d56e");
      LABEL_INTRO_BITBRAIN.glitchPool = "01";
      LABEL_LOGO.font = bake(Assets.Fonts.VISITOR, 90);
      LABEL_LOGO.fontColor = Color.RED;
      DIALOG_TEXT.font = bake(Assets.Fonts.VISITOR, 48);
      DIALOG_TEXT.fontColor = Color.RED;

   }



   public static BitmapFont bake(String fontPath, int size) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = true;
      param.borderColor = Color.BLACK;
      param.borderWidth = 2.4f;
      param.borderGamma = 3f;
      BitmapFont font = generator.generateFont(param);
      // disable integer positions so UI aligns correctly
      font.setUseIntegerPositions(false);
      return font;
   }
}
