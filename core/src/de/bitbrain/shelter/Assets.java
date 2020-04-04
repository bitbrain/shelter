package de.bitbrain.shelter;

public interface Assets {

   interface TiledMaps {
      String FOREST = "maps/forest.tmx";
   }

   interface Textures {
      String CROSSHAIR = "textures/crosshair.png";

      String PLAYER_SPRITESHEET = "textures/player-spritesheet.png";
      String ZOMBIE_SPRITESHEET = "textures/zombie-spritesheet.png";
      String SHADOW = "textures/shadow.png";

      String MUNITION_AK47 = "textures/ak47-munition.png";
      String ICON_WEAPON_AK47 = "textures/icon-ak47.png";
      String TILESET_WEAPON_AK47 = "textures/weapon-ak47-spritesheet.png";

      String UI_EQUIP = "textures/ui-equip.9.png";
      String UI_HEALTH = "textures/ui-health.9.png";
      String UI_HEALTH_ACTIVE = "textures/ui-health-active.9.png";
   }

   interface Particles {
      String BLOOD_EXPLOSION = "particles/blood-explosion.p";
      String BLOOD_IMPACT = "particles/blood-impact.p";
   }

   interface Fonts {
      String EIGHTBIT_WONDER = "fonts/8bitwonder.ttf";
   }

   interface Sounds {
      String WEAPON_AK_47 = "sound/ak-47.ogg";
      String GUN_RELOAD = "sound/gun-reload.ogg";
      String GUN_EMPTY = "sound/gun-empty.ogg";
   }
}
