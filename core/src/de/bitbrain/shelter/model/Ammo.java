package de.bitbrain.shelter.model;

public class Ammo {

   int ammo;
   int maxAmmo;

   public Ammo(int maxAmmo) {
      this.maxAmmo = maxAmmo;
      this.ammo = maxAmmo;
   }

   public void addAmmo(int ammo) {
      this.ammo = Math.min(this.ammo + ammo, maxAmmo);
   }

   public void reduceAmmo() {
      this.ammo = Math.max(this.ammo - 1, 0);
   }

   public int getAmmo() {
      return ammo;
   }

   public boolean isMagazineEmpty() {
      return ammo == 0;
   }

   public int getMaxAmmo() {
      return maxAmmo;
   }
}
