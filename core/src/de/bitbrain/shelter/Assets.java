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
   }

   interface Particles {
      String BLOOD_EXPLOSION = "particles/blood-explosion.p";
   }
}
