package de.bitbrain.shelter.model.weapon;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.shelter.model.EntityFactory;

public interface AttackStrategy extends Updateable {

   void attack(GameObject owner, GameContext2D context, EntityFactory entityFactory);
}
