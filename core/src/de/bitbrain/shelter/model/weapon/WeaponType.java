package de.bitbrain.shelter.model.weapon;

import de.bitbrain.shelter.Assets;

public enum WeaponType {

   AK47("AK-47",
         "A fast weapon",
         Assets.Textures.ICON_WEAPON_AK47,
         Assets.Textures.TILESET_WEAPON_AK47,
         new Ak47Strategy()
   );

   private final String name;
   private final String description;
   private final String icon;
   private final String tileset;
   private final FireStrategy fireStrategy;

   WeaponType(String name, String description, String icon, String tileset, FireStrategy fireStrategy) {
      this.name = name;
      this.description = description;
      this.icon = icon;
      this.tileset = tileset;
      this.fireStrategy = fireStrategy;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public String getIcon() {
      return icon;
   }

   public String getTileset() {
      return tileset;
   }

   public FireStrategy getFireStrategy() {
      return fireStrategy;
   }
}
