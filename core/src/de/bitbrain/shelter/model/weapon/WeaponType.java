package de.bitbrain.shelter.model.weapon;

import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.Assets.Sounds;
import de.bitbrain.shelter.Assets.Textures;

public enum WeaponType {

   AK47("AK 47",
         "A fast weapon",
         Textures.ICON_WEAPON_AK47,
         Textures.TILESET_WEAPON_AK47,
         Textures.MUNITION_AK47,
         Sounds.WEAPON_AK_47,
         Assets.Particles.SHOT_IMPACT,
         new DefaultWeaponFireStrategy()
   );

   private final String name;
   private final String description;
   private final String icon;
   private final String tileset;
   private final String munitionTexture;
   private final String shootSoundFx;
   private final String impactParticleFx;
   private final FireStrategy fireStrategy;

   WeaponType(String name, String description, String icon, String tileset, String munitionTexture, String shootSoundFx, String impactParticleFx, FireStrategy fireStrategy) {
      this.name = name;
      this.description = description;
      this.icon = icon;
      this.tileset = tileset;
      this.munitionTexture = munitionTexture;
      this.shootSoundFx = shootSoundFx;
      this.impactParticleFx = impactParticleFx;
      this.fireStrategy = fireStrategy;
   }

   public String getMunitionTexture() {
      return munitionTexture;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public String getShootSoundFx() {
      return shootSoundFx;
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

   public String getImpactParticleFx() {
      return impactParticleFx;
   }
}
