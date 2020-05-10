package de.bitbrain.shelter;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.assets.annotations.AssetSource;

public interface Assets {

   @AssetSource(directory = "maps", assetClass = TiledMap.class)
   interface TiledMaps {
      String FOREST = "forest.tmx";
      String FOREST_2 = "forest-2.tmx";
      String SHELTER = "shelter.tmx";
   }

   @AssetSource(directory = "textures", assetClass = Texture.class)
   interface Textures {
      String ICON = "icon.png";
      String LOGO = "bitbrain-logo.png";

      String PLAYER_SPRITESHEET = "player-spritesheet.png";
      String ZOMBIE_SPRITESHEET = "zombie-spritesheet.png";
      String ITEMS_SPRITESHEET = "items-spritesheet.png";
      String SHOOT_OVERLAY_SPRITESHEET = "shoot-overlay-spritesheet.png";
      String MISC_SPRITESHEET = "misc-spritesheet.png";
      String SHADOW = "shadow.png";

      String MUNITION_AK47 = "ak47-munition.png";
      String TILESET_WEAPON_AK47 = "weapon-ak47-spritesheet.png";

      String UI_EQUIP = "ui-equip.9.png";
      String UI_HEALTH = "ui-health.9.png";
      String UI_HEALTH_ACTIVE = "ui-health-active.9.png";
      String BARREL = "barrel.png";

      String CURSOR_CROSSHAIR = "cursor-crosshair.png";
      String CURSOR_FORBIDDEN = "cursor-forbidden.png";
      String CURSOR_CHEST = "cursor-chest.png";
   }

   @AssetSource(directory = "particles", assetClass = ParticleEffect.class)
   interface Particles {
      String BLOOD_EXPLOSION = "blood-explosion.p";
      String EXPLOSION = "explosion.p";
      String BLOOD_IMPACT = "blood-impact.p";
      String SHOT_IMPACT = "shot-impact.p";
      String CHEST = "chest-particles.p";
      String MEDIPACK = "medipack.p";
      String AMMO = "ammo.p";
      String HEAL = "heal.p";
      String RADIOACTIVE = "radioactive.p";
   }

   @AssetSource(directory = "fonts", assetClass = FreeTypeFontGenerator.class)
   interface Fonts {
      String ANGIESNEW = "angiesnewhouse.ttf";
      String VISITOR = "visitor.ttf";
   }

   @AssetSource(directory = "sound", assetClass = Sound.class)
   interface Sounds {
      String WEAPON_AK_47 = "ak-47.ogg";
      String GUN_RELOAD = "gun-reload.ogg";
      String GUN_EMPTY = "gun-empty.ogg";
      String DEATH = "death.ogg";
      String HEAL = "heal.ogg";

      String CROWBAR_DRAW = "crowbar-draw.ogg";

      String DOOR_OPEN = "door-open.ogg";
      String DOOR_SHUT = "door-shut.ogg";

      String IMPACT_01 = "impact-01.ogg";
      String IMPACT_02 = "impact-02.ogg";
      String IMPACT_03 = "impact-03.ogg";

      String ZOMBIE_HIT_01 = "zombie-hit-01.ogg";
      String ZOMBIE_HIT_02 = "zombie-hit-02.ogg";
      String ZOMBIE_HIT_03 = "zombie-hit-03.ogg";
      String ZOMBIE_HIT_04 = "zombie-hit-04.ogg";

      String ZOMBIE_DEATH_01 = "zombie-death-01.ogg";
      String ZOMBIE_DEATH_02 = "zombie-death-02.ogg";

      String WALK_01 = "walk-01.ogg";
      String WALK_02 = "walk-02.ogg";
      String WALK_03 = "walk-03.ogg";
      String WALK_04 = "walk-04.ogg";

      String ZOMBIE_NOISE_1 = "zombie-long-01.ogg";
      String ZOMBIE_NOISE_2 = "zombie-short-01.ogg";
      String ZOMBIE_NOISE_3 = "zombie-short-02.ogg";

      String EXPLOSION = "explosion.ogg";
   }

   @AssetSource(directory = "music", assetClass = Music.class)
   interface Musics {
      String SHELTER_THEME = "shelter-theme.ogg";
   }
}
