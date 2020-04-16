package de.bitbrain.shelter.core.weapon;

import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.Assets.Sounds;
import de.bitbrain.shelter.Assets.Textures;
import de.bitbrain.shelter.core.model.Damage;
import de.bitbrain.shelter.core.items.InventoryItem;
import de.bitbrain.shelter.core.items.Rarity;

public enum WeaponType implements InventoryItem {

   AK47("AK 47",
         "A powerful distance weapon. Made in Russia.",
         new Damage(5, 10, 3, 3, 0f),
         0.15f,
         Rarity.RARE,
         0,
         3,
         Textures.TILESET_WEAPON_AK47,
         Textures.MUNITION_AK47,
         Sounds.WEAPON_AK_47,
         Sounds.GUN_RELOAD,
         Assets.Particles.SHOT_IMPACT,
         RangeType.LONG_RANGE
   ),
   ZOMBIE_BITE("Zombie Bite",
         "A sharp bite with long, moldy teeth. This hurts!",
         new Damage(5, 30, 3, 6, 0f),
         0.7f,
         Rarity.COMMON,
         null,
         null,
         null,
         null,
         null,
         null,
         Assets.Particles.BLOOD_IMPACT,
         RangeType.CLOSE_RANGE),
   RUSTY_CROWBAR("Rusty Crowbar",
         "A rusty but intact crowbar. Made from cast iron.",
         new Damage(8, 14, 14, 8, 0.5f),
         1f,
         Rarity.COMMON,
         0,
         2,
         null,
         null,
         null,
         Sounds.CROWBAR_DRAW,
         Assets.Particles.BLOOD_IMPACT,
         RangeType.CLOSE_RANGE);

   private final String name;
   private final String description;
   private final Integer iconIndexX;
   private final Integer iconIndexY;
   private final String tileset;
   private final String attackTexture;
   private final String attackSoundFx;
   private final String reloadSoundFx;
   private final String impactParticleFx;
   private final RangeType rangeType;
   private final Rarity rarity;
   private final Damage damage;
   private final float speed;

   WeaponType(String name, String description, Damage damage, float speed, Rarity rarity, Integer iconIndexX, Integer iconIndexY, String tileset, String attackTexture, String attackSoundFx, String reloadSoundFx, String impactParticleFx, RangeType rangeType) {
      this.name = name;
      this.description = description;
      this.iconIndexX = iconIndexX;
      this.iconIndexY = iconIndexY;
      this.tileset = tileset;
      this.attackTexture = attackTexture;
      this.attackSoundFx = attackSoundFx;
      this.reloadSoundFx = reloadSoundFx;
      this.impactParticleFx = impactParticleFx;
      this.rangeType = rangeType;
      this.damage = damage;
      this.rarity = rarity;
      this.speed = speed;
   }

   public String getReloadSoundFx() {
      return reloadSoundFx;
   }

   public String getAttackTexture() {
      return attackTexture;
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

   public String getAttackSoundFx() {
      return attackSoundFx;
   }

   public Integer getIconIndexX() {
      return iconIndexX;
   }

   public Integer getIconIndexY() {
      return iconIndexY;
   }

   public String getTileset() {
      return tileset;
   }

   public Factory<AttackStrategy> getAttackStrategyFactory() {
      return rangeType.getAttackStrategyFactory();
   }

   public String getImpactParticleFx() {
      return impactParticleFx;
   }

   public RangeType getRangeType() {
      return rangeType;
   }

   private float computeDps() {
      final float damagePerHit = damage.getMin() + (damage.getMax() - damage.getMin()) / 2f;
      final float hitsPerSecond = 1f / getSpeed();
      return damagePerHit * hitsPerSecond;
   }
}
