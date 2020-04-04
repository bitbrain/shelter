package de.bitbrain.shelter.model.items;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.shelter.Assets;

public enum Item {

   AMMO("Ammo", new AmmoCollectEffect(), 1, Color.valueOf("ffcf66cc"), Assets.Particles.AMMO),
   MEDIKIT("Medikit", new MedikitCollectEffect(), 0, Color.valueOf("ff0011bb"), Assets.Particles.MEDIPACK);

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
