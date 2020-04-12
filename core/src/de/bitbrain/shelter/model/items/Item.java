package de.bitbrain.shelter.model.items;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.shelter.Assets;
import de.bitbrain.shelter.model.weapon.WeaponType;

public enum Item {

   AMMO_ITEM("Ammo", new AmmoCollectEffect(), 1, Color.valueOf("ffcf66cc"), Assets.Particles.AMMO),
   MEDIKIT_ITEM("Medikit", new MedikitCollectEffect(), 0, Color.valueOf("ff0011bb"), Assets.Particles.MEDIPACK),
   RUSTY_CROWBAR_ITEM(WeaponType.RUSTY_CROWBAR.getName(), new WeaponCollectEffect(WeaponType.RUSTY_CROWBAR), 2, Color.valueOf("ff0011bb"), Assets.Particles.MEDIPACK),
   AK47_ITEM(WeaponType.AK47.getName(), new WeaponCollectEffect(WeaponType.AK47), 3, Color.valueOf("ff0011bb"), Assets.Particles.MEDIPACK);

   private final String name;
   private final CollectEffect collectEffect;
   private final int animationIndex;
   private final Color lightColor;
   private final String particleEffectPath;

   Item(String name, CollectEffect collectEffect, int animationIndex, Color lightColor, String particleEffectPath) {
      this.name = name;
      this.collectEffect = collectEffect;
      this.animationIndex = animationIndex;
      this.lightColor = lightColor;
      this.particleEffectPath = particleEffectPath;
   }

   public String getName() {
      return name;
   }

   public CollectEffect getCollectEffect() {
      return collectEffect;
   }

   public int getAnimationIndex() {
      return animationIndex;
   }

   public Color getLightColor() {
      return lightColor.cpy();
   }

   public String getParticleEffectPath() {
      return particleEffectPath;
   }
}
