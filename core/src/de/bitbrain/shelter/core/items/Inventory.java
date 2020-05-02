package de.bitbrain.shelter.core.items;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.core.model.Ammo;
import de.bitbrain.shelter.core.weapon.RangeType;
import de.bitbrain.shelter.core.weapon.WeaponType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {

   private final Map<RangeType, List<WeaponType>> availableWeapons = new HashMap<RangeType, List<WeaponType>>();
   private final Map<RangeType, Integer> selectedIndices = new HashMap<RangeType, Integer>();

   private GameObject owner;
   private final Ammo ammo;
   private RangeType currentlyEquippedType;

   public Inventory() {
      this.ammo = new Ammo(200);
   }

   public RangeType getEquippedRangeType() {
      return owner.getAttribute(WeaponType.class) != null ? owner.getAttribute(WeaponType.class).getRangeType() : null;
   }

   public void setOwner(GameObject owner) {
      this.owner = owner;
      if (currentlyEquippedType != null) {
         equipSelected(currentlyEquippedType);
      }
   }

   public boolean addWeapon(WeaponType weapon) {
      List<WeaponType> weapons = availableWeapons.get(weapon.getRangeType());
      if (weapons == null) {
         weapons = new ArrayList<WeaponType>();
         availableWeapons.put(weapon.getRangeType(), weapons);
      }
      if (weapons.contains(weapon)) {
         return false;
      }
      weapons.add(weapon);
      selectNextWeapon(weapon.getRangeType());
      equipSelected(weapon.getRangeType());
      return true;
   }
   
   public Ammo getAmmo() {
      return ammo;
   }

   public void equipSelected(RangeType rangeType) {
      if (owner != null) {
         WeaponType type = getSelectedWeapon(rangeType);
         if (type != null) {
            owner.setAttribute(WeaponType.class, getSelectedWeapon(rangeType));
            currentlyEquippedType = rangeType;
         }
      }
   }

   public WeaponType getSelectedWeapon(RangeType rangeType) {
      if (availableWeapons.isEmpty()) {
         return null;
      }
      if (!availableWeapons.containsKey(rangeType)) {
         return null;
      }
      Integer index = selectedIndices.get(rangeType);
      if (index == null) {
         index = 0;
         selectedIndices.put(rangeType, index);
      }
      return availableWeapons.get(rangeType).get(index);
   }

   public WeaponType selectNextWeapon(RangeType rangeType) {
      List<WeaponType> weapons = availableWeapons.get(rangeType);
      Integer index = selectedIndices.get(rangeType);
      if (index == null) {
         index = 0;
      } else {
         index++;
      }
      if (index >= weapons.size()) {
         index = 0;
      }
      selectedIndices.put(rangeType, index);
      return weapons.get(index);
   }

   public WeaponType selectPreviousWeapon(RangeType rangeType) {
      List<WeaponType> weapons = availableWeapons.get(rangeType);
      Integer index = selectedIndices.get(rangeType);
      if (index == null) {
         index = weapons.size() - 1;
      } else {
         index--;
      }
      if (index < 0) {
         index = weapons.size() - 1;
      }
      selectedIndices.put(rangeType, index);
      return weapons.get(index);
   }
}
