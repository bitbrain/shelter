package de.bitbrain.shelter.graphics;

import de.bitbrain.braingdx.world.GameObject;

import java.util.Comparator;

public class RenderOrderComparator implements Comparator<GameObject> {

   @Override
   public int compare(GameObject o1, GameObject o2) {
      if (o1.getZIndex() > o2.getZIndex())
         return 1;
      else if (o1.getZIndex() < o2.getZIndex())
         return -1;
      else
         return (int) (o2.getTop() - o1.getTop());
   }
}
