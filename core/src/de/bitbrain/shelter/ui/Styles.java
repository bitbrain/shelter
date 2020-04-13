package de.bitbrain.shelter.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.ThemeColors;

import static com.badlogic.gdx.graphics.Color.valueOf;

public class Styles {

   public static final Label.LabelStyle LABEL_DESCRIPTION = new Label.LabelStyle();
   public static final GlitchLabel.GlitchLabelStyle LABEL_INTRO_BITBRAIN = new GlitchLabel.GlitchLabelStyle();
   public static final Label.LabelStyle LABEL_DEBUG = new  Label.LabelStyle();
   public static final Label.LabelStyle LABEL_LOGO = new Label.LabelStyle();
   public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();
   public static final Label.LabelStyle STORY = new Label.LabelStyle();
   public static final Label.LabelStyle TIPS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_TOOLTIP_NAME = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_TOOLTIP_STATS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_TOOLTIP_DESCRIPTION = new Label.LabelStyle();
   public static final ImageButton.ImageButtonStyle INVENTORY_ICON = new ImageButton.ImageButtonStyle();

   public static void init() {
      LABEL_DESCRIPTION.font = bake(Assets.Fonts.ANGIESNEW, 24);

      LABEL_INTRO_BITBRAIN.font = bake(Assets.Fonts.VISITOR, 48);
      LABEL_INTRO_BITBRAIN.fontColor = valueOf("00d56e");
      LABEL_INTRO_BITBRAIN.glitchPool = "01";

      LABEL_DEBUG.font = bake(Assets.Fonts.VISITOR, 32);
      LABEL_DEBUG.fontColor = valueOf("00d56e");

      LABEL_LOGO.font = bake(Assets.Fonts.ANGIESNEW, 190, false);
      LABEL_LOGO.fontColor = ThemeColors.TEXT.cpy();
      DIALOG_TEXT.font = bake(Assets.Fonts.ANGIESNEW, 48, false);
      STORY.fontColor = ThemeColors.TEXT.cpy();
      STORY.font = bake(Assets.Fonts.ANGIESNEW, 48, false);
      TIPS.fontColor = ThemeColors.TEXT.cpy();
      TIPS.font = bake(Assets.Fonts.ANGIESNEW, 20, false);
      DIALOG_TEXT.fontColor = ThemeColors.BUTTON.cpy();

      LABEL_TOOLTIP_NAME.fontColor = Color.WHITE.cpy();
      LABEL_TOOLTIP_NAME.font = bake(Assets.Fonts.ANGIESNEW, 28);
      LABEL_TOOLTIP_STATS.fontColor = Color.WHITE.cpy();
      LABEL_TOOLTIP_STATS.font = bake(Assets.Fonts.ANGIESNEW, 26);
      LABEL_TOOLTIP_DESCRIPTION.fontColor = Color.WHITE.cpy();
      LABEL_TOOLTIP_DESCRIPTION.font = bake(Assets.Fonts.ANGIESNEW, 26);


   }

   public static BitmapFont bake(String fontPath, int size, boolean border) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = true;
      if (border) {
         param.borderColor = Color.BLACK;
         param.borderWidth = 2.4f;
         param.borderGamma = 3f;
      }
      BitmapFont font = generator.generateFont(param);
      // disable integer positions so UI aligns correctly
      font.setUseIntegerPositions(false);
      return font;
   }

   public static BitmapFont bake(String fontPath, int size) {
      return bake(fontPath, size, true);
   }
}
