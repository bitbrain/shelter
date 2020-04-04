package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public interface FireStrategy {

   void fire(GameObject owner, GameContext2D context);
}
