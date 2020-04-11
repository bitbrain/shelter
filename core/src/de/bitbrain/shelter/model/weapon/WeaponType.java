package de.bitbrain.shelter.model.weapon;

import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.Assets.Sounds;
import de.bitbrain.shelter.Assets.Textures;
import de.bitbrain.shelter.model.Damage;
import de.bitbrain.shelter.model.items.InventoryItem;
import de.bitbrain.shelter.model.items.Rarity;

public enum WeaponType implements InventoryItem {

   AK47("AK 47",
         "A powerful distance weapon. Made in Russia.",
         new Damage(5, 10),
         0.15f,
         Rarity.RARE,
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
   private final Rarity rarity;
   private final Damage damage;
   private final float speed;

   WeaponType(String name, String description, Damage damage, float speed, Rarity rarity, String icon, String tileset, String munitionTexture, String shootSoundFx, String impactParticleFx, FireStrategy fireStrategy) {
      this.name = name;
      this.description = description;
      this.icon = icon;
      this.tileset = tileset;
      this.munitionTexture = munitionTexture;
      this.shootSoundFx = shootSoundFx;
      this.impactParticleFx = impactParticleFx;
      this.fireStrategy = fireStrategy;
      this.damage = damage;
      this.rarity = rarity;
      this.speed = speed;
   }

   public String getMunitionTexture() {
      return munitionTexture;
   }

   @Override
   public Rarity getRarity() {
      return rarity;
   }

   public String getName() {
      return name;
   }

   @Override
   public String getStats() {
      return damage.getMin() + "-" + damage.getMax() + " damage (" + computeDps() + "dps)";
   }

   public Damage getDamage() {
      return damage;
   }

   public float getSpeed() {
      return speed;
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

   private float computeDps() {
      final float damagePerHit = damage.getMin() + (damage.getMax() - damage.getMin()) / 2f;
      final float hitsPerSecond = 1f / getSpeed();
      return damagePerHit * hitsPerSecond;
   }
}
