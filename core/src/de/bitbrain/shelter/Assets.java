package de.bitbrain.shelter;

public interface Assets {

   interface TiledMaps {
      String FOREST = "maps/forest.tmx";
      String FOREST_2 = "maps/forest-2.tmx";
      String SHELTER = "maps/shelter.tmx";
   }

   interface Textures {
      String ICON = "textures/icon.png";
      String LOGO = "textures/bitbrain-logo.png";

      String CROSSHAIR = "textures/crosshair.png";

      String PLAYER_SPRITESHEET = "textures/player-spritesheet.png";
      String ZOMBIE_SPRITESHEET = "textures/zombie-spritesheet.png";
      String ITEMS_SPRITESHEET = "textures/items-spritesheet.png";
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
      String MEDIPACK = "particles/medipack.p";
      String AMMO = "particles/ammo.p";
      String HEAL = "particles/heal.p";
   }

   interface Fonts {
      String ANGIESNEW = "fonts/angiesnewhouse.ttf";
      String VISITOR = "fonts/visitor.ttf";
   }

   interface Sounds {
      String WEAPON_AK_47 = "sound/ak-47.ogg";
      String GUN_RELOAD = "sound/gun-reload.ogg";
      String GUN_EMPTY = "sound/gun-empty.ogg";
      String DEATH = "sound/death.ogg";
      String HEAL = "sound/heal.ogg";

      String IMPACT_01 = "sound/impact-01.ogg";
      String IMPACT_02 = "sound/impact-02.ogg";
      String IMPACT_03 = "sound/impact-03.ogg";

      String ZOMBIE_HIT_01 = "sound/zombie-hit-01.ogg";
      String ZOMBIE_HIT_02 = "sound/zombie-hit-02.ogg";
      String ZOMBIE_HIT_03 = "sound/zombie-hit-03.ogg";
      String ZOMBIE_HIT_04 = "sound/zombie-hit-04.ogg";

      String ZOMBIE_DEATH_01 = "sound/zombie-death-01.ogg";
      String ZOMBIE_DEATH_02 = "sound/zombie-death-02.ogg";

      String WALK_01 = "sound/walk-01.ogg";
      String WALK_02 = "sound/walk-02.ogg";
      String WALK_03 = "sound/walk-03.ogg";
      String WALK_04 = "sound/walk-04.ogg";

      String ZOMBIE_NOISE_1 = "sound/zombie-long-01.ogg";
      String ZOMBIE_NOISE_2 = "sound/zombie-short-01.ogg";
      String ZOMBIE_NOISE_3 = "sound/zombie-short-02.ogg";
   }

   interface Musics {
      String SHELTER_THEME = "music/shelter-theme.ogg";
   }
}
