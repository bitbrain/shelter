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
         new RangeAttackStrategy()
   ),
   ZOMBIE_BITE("Zombie Bite",
         "A sharp bite with long, moldy teeth. This hurts!",
         new Damage(5, 30),
         0.4f,
         Rarity.COMMON,
         null,
         null,
         null,
         null,
         Assets.Particles.BLOOD_IMPACT,
         new CloseAttackStrategy()),
   RUSTY_CROWBAR("Rusty Crowbar",
         "A rusty but intact crowbar. Made from cast iron.",
         new Damage(8, 14),
         0.9f,
         Rarity.COMMON,
         null,
         null,
         null,
         null,
         Assets.Particles.BLOOD_IMPACT,
         new CloseAttackStrategy());

   private final String name;
   private final String description;
   private final String icon;
   private final String tileset;
   private final String attackTexture;
   private final String attackSoundFx;
   private final String impactParticleFx;
   private final AttackStrategy attackStrategy;
   private final Rarity rarity;
   private final Damage damage;
   private final float speed;

   WeaponType(String name, String description, Damage damage, float speed, Rarity rarity, String icon, String tileset, String attackTexture, String attackSoundFx, String impactParticleFx, AttackStrategy attackStrategy) {
      this.name = name;
      this.description = description;
      this.icon = icon;
      this.tileset = tileset;
      this.attackTexture = attackTexture;
      this.attackSoundFx = attackSoundFx;
      this.impactParticleFx = impactParticleFx;
      this.attackStrategy = attackStrategy;
      this.damage = damage;
      this.rarity = rarity;
      this.speed = speed;
   }

   public String getWeaponTexture() {
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

   public String getIcon() {
      return icon;
   }

   public String getTileset() {
      return tileset;
   }

   public AttackStrategy getAttackStrategy() {
      return attackStrategy;
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
